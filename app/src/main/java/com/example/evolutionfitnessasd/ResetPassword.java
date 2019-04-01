package com.example.evolutionfitnessasd;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "Reset Password";
    private EditText memailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        memailField = findViewById(R.id.emailF);
        findViewById(R.id.reset).setOnClickListener(this);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = memailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            memailField.setError("Required.");
            valid = false;
        } else {
            memailField.setError(null);
        }
        return valid;
    }

    private void resetPassword() {
        String email = memailField.getText().toString();
        Log.d(TAG, "resetPassword:" + email);
        if (!validateForm()) {
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseAuth.getInstance().sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.reset) {
            resetPassword();
        }
    }
}
