package com.example.evolutionfitnessasd;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

    ImageView mContact;
    TextView mDescription;
    TextView mContactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContact = findViewById(R.id.ivImage);
        mDescription = findViewById(R.id.tvDescription);
        mContactName = (TextView) findViewById(R.id.contactName);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mContactName.setText(mBundle.getString("Title"));
            mContact.setImageResource(mBundle.getInt("Image"));
            mDescription.setText(mBundle.getString("Description"));
        }
    }
}