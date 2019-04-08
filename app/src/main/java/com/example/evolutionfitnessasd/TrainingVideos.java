package com.example.evolutionfitnessasd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Vector;

public class TrainingVideos extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<YouTubeVideos> youTubeVideos = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_videos);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"http://www.youtube.com/embed/bSMZknDI6bg\" frameborder=\"0\" allowfullscreen ng-show=\"showvideo\"></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"http://www.youtube.com/embed/ifXalt3MJtM\" frameborder=\"0\" allowfullscreen ng-show=\"showvideo\"></iframe>"));
        VideoAdapter videoAdapter = new VideoAdapter(youTubeVideos);
        recyclerView.setAdapter(videoAdapter);



    }
}
