package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class TrainingVideos extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView mMuscleType;
    private boolean pectoral, biceps, triceps, quadriceps, shoulders, abs, dorsal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_videos);
        Bundle mBundle = getIntent().getExtras();
        mMuscleType = (TextView) findViewById(R.id.muscleType);
            if (mBundle != null) {
                mMuscleType.setText(mBundle.getString("Title"));
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Pettorali")){ pectoral=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Bicipiti")){ biceps=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Tricipiti")){ triceps=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Quadricipiti")){ quadriceps=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Addominali")){ abs=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Spalle")){ shoulders=true;}
                if(Objects.requireNonNull(mBundle.getString("Title")).equals("Dorsali")){ dorsal=true;}
            }

        setUpRecyclerView();
        populateRecyclerView();

    }

    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateDummyVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoModelArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //start youtube player activity by passing selected video id via intent
                startActivity(new Intent(TrainingVideos.this, YoutubePlayerActivity.class)
                        .putExtra("video_id", youtubeVideoModelArrayList.get(position).getVideoId()));

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }


    /**
     * method to generate dummy array list of videos
     *
     * @return
     */
    private ArrayList<YoutubeVideoModel> generateDummyVideoList() {
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
        if(pectoral) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_pectoral);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_pectoral);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_pectoral);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(biceps) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_biceps);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_biceps);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_biceps);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(triceps) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_triceps);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_triceps);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_triceps);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(quadriceps) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_quadriceps);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_quadriceps);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_quadriceps);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(abs) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_abs);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_abs);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_abs);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(dorsal) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_dorsal);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_dorsal);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_dorsal);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }
        if(shoulders) {
            //get the video id array, title array and duration array from strings.xml
            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array_shoulders);
            String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array_shoulders);
            String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array_shoulders);

            //loop through all items and add them to arraylist
            for (int i = 0; i < videoIDArray.length; i++) {

                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
                youtubeVideoModel.setVideoId(videoIDArray[i]);
                youtubeVideoModel.setTitle(videoTitleArray[i]);
                youtubeVideoModel.setDuration(videoDurationArray[i]);

                youtubeVideoModelArrayList.add(youtubeVideoModel);

            }
        }

        return youtubeVideoModelArrayList;
    }
}