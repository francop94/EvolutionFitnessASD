package com.example.evolutionfitnessasd;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class shared {
    private static String NOME;
    private static String COGNOME;
    private static String UID;
    private static String UIDM;

    public static String getNOME() {
        return NOME;
    }

    public static void setNOME(String nome) {
        NOME = nome;
    }

    public static String getCOGNOME() {
        return COGNOME;
    }

    public static void setCOGNOME(String cognome) {
        COGNOME = cognome;
    }

    public static String getUID() {
        return UID;
    }

    public static void setUID(String uid) {
        UID = uid;
    }
    public static String getUIDMan() {
        return UIDM;
    }

    public static void setUIDMan(String uid) {
        UIDM = uid;
    }

}
