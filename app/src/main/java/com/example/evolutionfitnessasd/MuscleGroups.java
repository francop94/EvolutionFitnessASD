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
        mMuscleData = new MuscleData("Pettorali", R.drawable.pectoral);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Bicipiti", R.drawable.biceps);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Tricipiti", R.drawable.triceps);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Quadricipiti", R.drawable.quadriceps);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Spalle", R.drawable.shoulders);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Dorsali", R.drawable.dorsal);
        mMuscleList.add(mMuscleData);
        mMuscleData = new MuscleData("Addominali", R.drawable.abs);
        mMuscleList.add(mMuscleData);

        MyAdapter myAdapter = new MyAdapter(MuscleGroups.this, mMuscleList);
        mRecyclerView.setAdapter(myAdapter);
    }
}

