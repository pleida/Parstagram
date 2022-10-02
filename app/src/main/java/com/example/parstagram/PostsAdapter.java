package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import fragments.ProfileFragment;

public class PostsAdapter  extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    private static ArrayList<String> likeList;
    public int like;

    ParseUser currentUser;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> postList){
        posts.addAll(postList);
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private TextView tvDescription;
        private ImageView ivImage;
        private  ImageView ivProfile;
        private ImageButton ibLike;
        private TextView tvNberLike;
        private TextView tvDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ibLike = itemView.findViewById(R.id.ibLike);
            tvNberLike = itemView.findViewById(R.id.tvNberLike);
            tvDate = itemView.findViewById(R.id.tvDate);


        }

        public void bind(Post post) {
            // Bind the post data to the view element
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());

            tvDate.setText(TimeFormatter.getTimeStamp(post.getCreatedAt().toString()));

            currentUser = ParseUser.getCurrentUser();
            try {
                likeList =Post.fromJsonArray(post.getListLike());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //  change heart's color once clicked
            try{
                if (likeList.contains(currentUser.getObjectId())) {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_red_heart);
                    ibLike.setImageDrawable(drawable);
                }else {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.like);
                    ibLike.setImageDrawable(drawable);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
                // Make Like button clickable
            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like = post.getLIkeNumber();
                    int index;

                    if (!likeList.contains(currentUser.getObjectId())){
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_red_heart);
                        ibLike.setImageDrawable(drawable);
                        like++;
                        index = -1;

                    }else {
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.like);
                        ibLike.setImageDrawable(drawable);
                        like--;
                        index = likeList.indexOf(currentUser.getObjectId());
                    }


                    tvNberLike.setText(String.valueOf(like) + " like");
                    saveLike(post, like, index, currentUser);
                }
            });

            // Inflate picture into feed
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            Glide.with(context)
                    .load(post.getUser().getParseFile("profile").getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfile);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            // Make profile clickable
            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((FragmentActivity) context ).getSupportFragmentManager();
                    ProfileFragment profileFragment = ProfileFragment.newInstance("title");
                    Bundle bundle= new Bundle();
                    bundle.putParcelable("post",Parcels.wrap(post));
                    profileFragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();
                }
            });
        }

        // Method to save user's like
        private void saveLike(Post post, int like, int index, ParseUser currentUser) {
            post.setLikeNumber(like);

            if (index == -1){
                post.setListLike(currentUser);
                likeList.add(currentUser.getObjectId());
            }else {
                likeList.remove(index);
                post.removeListLike(likeList);
            }
        }
    }
}
