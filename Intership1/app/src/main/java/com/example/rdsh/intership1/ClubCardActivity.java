package com.example.rdsh.intership1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.Profile;

public class ClubCardActivity extends Activity{

    TextView name, surname, clubId;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_card);

        // transfer of user data from LoginActivity to ClubCardActivity
        name = findViewById(R.id.nameLabel);
        name.setText(getIntent().getStringExtra("name"));

        surname = findViewById(R.id.surnameLabel);
        surname.setText(getIntent().getStringExtra("lastName"));

        clubId = findViewById(R.id.clubIdLabel);
        clubId.setText(getIntent().getStringExtra("id"));

        //realisation of logout button
        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile.setCurrentProfile(null);
                AccessToken.setCurrentAccessToken(null);
                Intent intent = new Intent(ClubCardActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //logout by press back button
    @Override
    public void onBackPressed() {
        Profile.setCurrentProfile(null);
        AccessToken.setCurrentAccessToken(null);
        Intent intent = new Intent(ClubCardActivity.this, LoginActivity.class);
        startActivity(intent);
    }




}