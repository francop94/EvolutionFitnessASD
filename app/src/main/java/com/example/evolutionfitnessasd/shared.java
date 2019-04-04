package com.example.evolutionfitnessasd;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class shared {
    private static String NOME;

    public static String getNOME() {
        return NOME;
    }

    public static void setNOME(String nome) {
        NOME = nome;
    }

    private static boolean isADMIN;

    public static boolean getisADMIN() {
        return isADMIN;
    }

    public static void setIsADMIN() {
        isADMIN = true;
    }


}
