package net.codealizer.perspectives.util;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.HashMap;

public class Global {

    // Holds instance variables
    public static AuthenticationManager authManager;
    public static GoogleSignInOptions gso;
    public static HashMap<String, Integer> emotionMap;

}
