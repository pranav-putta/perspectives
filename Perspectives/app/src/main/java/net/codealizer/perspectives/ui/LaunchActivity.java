package net.codealizer.perspectives.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import net.codealizer.perspectives.R;
import net.codealizer.perspectives.util.AuthenticationManager;
import net.codealizer.perspectives.util.Global;

public class LaunchActivity extends AppCompatActivity {

    private static final int DELAY = 1000;
    private Runnable transition = new Runnable() {
        @Override
        public void run() {
            Intent intent;
            if (!Global.authManager.isUserSignedIn()) {
                intent = new Intent(LaunchActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(LaunchActivity.this, MainActivity.class);
            }

            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        initializeApp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(transition, DELAY);
    }

    private void initializeApp() {
        // init global variables
        Global.authManager = AuthenticationManager.createInstance(this.getBaseContext());
        Global.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }
}
