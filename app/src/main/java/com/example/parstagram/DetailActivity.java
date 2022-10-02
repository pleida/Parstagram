package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    private TextView tvUsername;
    private ImageView ivProfile;
    private  ImageView ivImage;
    private  TextView tvDescription;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvUsername = findViewById(R.id.tvUsername);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfile = findViewById(R.id.ivProfile);
        ivImage = findViewById(R.id.ivImage);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        ParseFile picture = post.getImage();
        if(picture != null){
            Glide.with(DetailActivity.this).load(post.getImage().getUrl()).into(ivImage);
        }
        Glide.with(DetailActivity.this)
                .load(post.getUser().getParseFile("profile").getUrl())
                .transform(new RoundedCorners(100))
                .into(ivProfile);



    }
}