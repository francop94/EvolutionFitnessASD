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

public class MyAdapterContacts extends RecyclerView.Adapter<ContactViewHolder> {

    private Context mContext;
    private List< ContactsData > mContactList;

    MyAdapterContacts(Context mContext, List< ContactsData > mContactList) {
        this.mContext = mContext;
        this.mContactList = mContactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewcontacts_item_row, parent, false);
        return new ContactViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {

        holder.mImage.setImageResource(mContactList.get(position).getContactImage());
        holder.mTitle.setText(mContactList.get(position).getContactName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = mContactList.get(holder.getAdapterPosition()).getContactName();
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("Title",contact);
                mIntent.putExtra("Description", mContactList.get(holder.getAdapterPosition()).getContactDescription());
                mIntent.putExtra("Image", mContactList.get(holder.getAdapterPosition()).getContactImage());
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}

class ContactViewHolder extends RecyclerView.ViewHolder {

    CardView mCardView;
    ImageView mImage;
    TextView mTitle;

    ContactViewHolder(View itemView) {
        super(itemView);

        mCardView = itemView.findViewById(R.id.cardview);
        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
    }
}
