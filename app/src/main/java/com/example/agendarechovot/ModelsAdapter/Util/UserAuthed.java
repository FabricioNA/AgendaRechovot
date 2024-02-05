package com.example.agendarechovot.ModelsAdapter.Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuthed {
    public static FirebaseUser usuarioLogado(){
        FirebaseAuth usuario = ConfigBD.FirebaseAuthentication();
        return usuario.getCurrentUser();
    }
}
