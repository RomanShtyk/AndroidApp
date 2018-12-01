package com.example.rdsh.intership1;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends Activity {

    boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        System.out.println(":::::" + loggedOut);
        Intent intent = new Intent(MainActivity.this, loggedOut ? LoginActivity.class : ClubCardActivity.class);
        startActivity(intent);

    }
}
