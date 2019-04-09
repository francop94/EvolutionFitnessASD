package com.example.evolutionfitnessasd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MuscleGroups extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List< MuscleData > mMuscleList;
    MuscleData mMuscleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_groups);
        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MuscleGroups.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Pectoral", R.drawable.pectoral);
        mMuscleList.add(mMuscleData);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Biceps", R.drawable.biceps);
        mMuscleList.add(mMuscleData);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Triceps", R.drawable.triceps);
        mMuscleList.add(mMuscleData);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Quadriceps", R.drawable.quadriceps);
        mMuscleList.add(mMuscleData);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Abs", R.drawable.abs);
        mMuscleList.add(mMuscleData);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Shoulders", R.drawable.shoulders);

        mMuscleList = new ArrayList<>();
        mMuscleData = new MuscleData("Dorsal", R.drawable.dorsal);

        mMuscleList.add(mMuscleData);
        mMuscleList.add(mMuscleData);

        MyAdapter myAdapter = new MyAdapter(MuscleGroups.this, mMuscleList);
        mRecyclerView.setAdapter(myAdapter);
    }
}

