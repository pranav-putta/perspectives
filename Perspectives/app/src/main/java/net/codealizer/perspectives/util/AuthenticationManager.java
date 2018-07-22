package net.codealizer.perspectives.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager {

    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    private static final String KEY_PREFS = "auth_prefs";
    private static final String KEY_SIGNED_IN = "key_signed_in";
    private static final String KEY_UID = "key_uid";

    private AuthenticationManager(Context ctx) {
        prefs = ctx.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public static AuthenticationManager createInstance(Context ctx) {
        return new AuthenticationManager(ctx);
    }



    /**
     * Check shared prefs if user is logged in
     * @return bool
     */
    public boolean isUserSignedIn() {
        return prefs.getBoolean(KEY_SIGNED_IN, false);
    }

    /**
     * update the shared preference
     */
    public void signInUser(FirebaseUser user) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_SIGNED_IN, true);
        editor.putString(KEY_UID, user.getUid());
        editor.apply();
    }

    public String getUserUID() {
        return prefs.getString(KEY_UID, "");
    }

    public FirebaseAuth firebase() {
        return mAuth;
    }


}
