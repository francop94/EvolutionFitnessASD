package com.example.evolutionfitnessasd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MuscleViewHolder> {

    private Context mContext;
    private List< MuscleData > mMuscleList;

    MyAdapter(Context mContext, List< MuscleData > mMuscleList) {
        this.mContext = mContext;
        this.mMuscleList = mMuscleList;
    }

    @Override
    public MuscleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row, parent, false);
        return new MuscleViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MuscleViewHolder holder, int position) {

        holder.mImage.setImageResource(mMuscleList.get(position).getMuscleImage());
        holder.mTitle.setText(mMuscleList.get(position).getMuscleName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String muscle = mMuscleList.get(holder.getAdapterPosition()).getMuscleName();
                Intent mIntent = new Intent(mContext, TrainingVideos.class);
                mIntent.putExtra("Title",muscle);
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMuscleList.size();
    }
}

class MuscleViewHolder extends RecyclerView.ViewHolder {

    CardView mCardView;
    ImageView mImage;
    TextView mTitle;

    MuscleViewHolder(View itemView) {
        super(itemView);

        mCardView = itemView.findViewById(R.id.cardview);
        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
    }
}
