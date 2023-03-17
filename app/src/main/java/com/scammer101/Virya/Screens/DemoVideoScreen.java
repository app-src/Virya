package com.scammer101.Virya.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.scammer101.Virya.databinding.ActivityDemoVideoScreenBinding;

public class DemoVideoScreen extends AppCompatActivity {

    private ActivityDemoVideoScreenBinding binding;
    private ExoPlayer exoPlayer;
    private String url;
    private String yogaPose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoVideoScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        yogaPose = i.getStringExtra("yoga");
        init();
        setListener();



    }

    private void init()
    {
        exoPlayer = new ExoPlayer.Builder(this).build();
        if(yogaPose.equals("treepose"))
        {
            url = "https://firebasestorage.googleapis.com/v0/b/virya-50134.appspot.com/o/Yoga%2Ftreepose.mp4?alt=media&token=a9059645-257c-4b79-b332-23ff7510038a";
        }
        else if(yogaPose.equals("warrior2pose"))
        {
            url = "https://firebasestorage.googleapis.com/v0/b/virya-50134.appspot.com/o/Yoga%2Fwarrior.mp4?alt=media&token=390a021f-4db4-4e93-8325-48eb0a6dab5e";
        }
        else
        {
            url = "https://firebasestorage.googleapis.com/v0/b/virya-50134.appspot.com/o/Yoga%2Ftreepose.mp4?alt=media&token=a9059645-257c-4b79-b332-23ff7510038a";
        }

    }

    private void setListener()
    {
        binding.demoExoPlayer.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        binding.demoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoVideoScreen.this, PoseDetectorActivity.class);
                intent.putExtra("yoga", yogaPose);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();
    }
}