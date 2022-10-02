package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parstagram.GridAdapter;
import com.example.parstagram.LoginActivity;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    Button btnLogOut;
    public static final String TAG = "ProfileFragment";
   // protected PostsAdapter adapter;
    protected ArrayList<Post> allposts;
    GridAdapter gridAdapter;
    GridView gridView;
    TextView tvUname;
    ImageView ivProfile;
    ParseUser currentUser;

    public static ProfileFragment newInstance(String title) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("title", title);

        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profile_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        gridView = view.findViewById(R.id.gvPictures);
        tvUname = view.findViewById(R.id.tvUname);
        ivProfile = view.findViewById(R.id.ivProfile);
        Bundle bundle = getArguments();
        if(bundle == null){
            currentUser = ParseUser.getCurrentUser();
        }else {
            Post post = Parcels.unwrap(bundle.getParcelable("post"));
            currentUser = post.getUser();
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        allposts = new ArrayList<>();
        gridAdapter = new GridAdapter(getContext(),allposts);
        gridView.setAdapter(gridAdapter);
        tvUname.setText(currentUser.getUsername());

        Glide.with(getContext())
                .load(currentUser.getParseFile("profile").getUrl())
                .transform(new RoundedCorners(100))
                .into(ivProfile);

        queryPosts();
    }

    //@Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                allposts.addAll(posts);
                gridAdapter.notifyDataSetChanged();
            }
        });
    }
}
