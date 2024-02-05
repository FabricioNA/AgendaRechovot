package com.example.agendarechovot.ModelsAdapter.Util;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigBD {
    private static FirebaseAuth auth;

    public static FirebaseAuth FirebaseAuthentication() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
