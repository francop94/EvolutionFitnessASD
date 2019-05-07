package com.example.evolutionfitnessasd;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity implements
        View.OnClickListener {
    private static final String TAG = "Registration";
    private EditText memailField;
    private EditText mpasswordField;
    private EditText musernameField;
    private EditText msurnameField;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Views
        memailField = findViewById(R.id.emailF);
        musernameField = findViewById(R.id.usernameF);
        msurnameField = findViewById(R.id.surnameF);
        mpasswordField = findViewById(R.id.passwordF);

        // Buttons
        findViewById(R.id.link_login).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        //findViewById(R.id.verifyEmail).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            updateUI(currentUser);
        }
        updateUI(null);

    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            sendEmailVerification();
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            memailField.getText().clear();
                            msurnameField.getText().clear();
                            mpasswordField.getText().clear();
                            musernameField.getText().clear();
                            mpasswordField.clearFocus();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Questa email è già registrata oppure il formato dell'email non è valido!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void sendEmailVerification() {
        // Disable button
        //findViewById(R.id.verifyEmail).setEnabled(false);
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        //findViewById(R.id.verifyEmail).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Email di verifica mandata a " + user.getEmail()+"\n"+"Controlla la tua cartella di spam se non ricevi l'email!",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            revokeAccess();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Errore nell'inviare l'email di verifica.",
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void revokeAccess() {
        // Firebase sign out
        if (mAuth != null) {
            mAuth.signOut();
        }
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            findViewById(R.id.register).setVisibility(View.GONE);
            findViewById(R.id.link_login).setVisibility(View.VISIBLE);
            /*findViewById(R.id.verifyEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.verifyEmail).setEnabled(!user.isEmailVerified());*/

        } else {
            findViewById(R.id.register).setVisibility(View.VISIBLE);
            //findViewById(R.id.verifyEmail).setVisibility(View.GONE);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = memailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            memailField.setError("Richiesta.");
            valid = false;
        } else {
            memailField.setError(null);
        }

        String username = musernameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            musernameField.setError("Richiesto.");
            valid = false;
        } else {
            shared.setNOME(username);
            musernameField.setError(null);
        }

        String surname = msurnameField.getText().toString();
        if (TextUtils.isEmpty(surname)) {
            msurnameField.setError("Richiesto.");
            valid = false;
        } else {
            shared.setCOGNOME(surname);
            msurnameField.setError(null);
        }

        String password = mpasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mpasswordField.setError("Richiesta.");
            valid = false;
        }
        else if(password.length()<6){
            mpasswordField.setError("La password deve essere lunga almeno 6 caratteri!");
            valid = false;
        } else{
            mpasswordField.setError(null);
        }

        return valid;
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register) {
            createAccount(memailField.getText().toString(), mpasswordField.getText().toString());
        }
        else if(i == R.id.link_login){
            //finish();
            Intent signIn = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(signIn);
        }
        /*else if(i == R.id.verifyEmail){
            sendEmailVerification();
        }*/
    }
}

