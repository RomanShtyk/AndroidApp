package com.example.rdsh.intership1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {

    public static List<User> users = new ArrayList<>();

    public static final String EMAIL = "email";
    public static final String PUBLIC_PROFILE = "public_profile";

    CallbackManager callbackManager;

    LoginButton loginButton;
    Button button, registerButton;
    TextView errorLabel, email, password, passwordForgotten;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hardcoded list of Users:
        // use this obects email and password to log in app
        User admin = new User("admin", "admin", "Roman", "Shtykalo", "1234567890");
        User notAdmin = new User("user", "user", "User", "User", "123");
        users.add(admin);
        users.add(notAdmin);

        //Facebook api logger
        AppEventsLogger.activateApp(getApplication());
        button = findViewById(R.id.btnLogin);

        //forgot password button realisation
        passwordForgotten = findViewById(R.id.editText2);
        passwordForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Ooops...")
                        .setMessage("This service is temporary unavailable")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Sorry:)", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //this is error label, which will show us the login errors
                errorLabel = findViewById(R.id.errorLabel);
                errorLabel.setVisibility(View.INVISIBLE);
                registerButton = findViewById(R.id.register_button);
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                        builder.setTitle("Ooops...")
                                .setMessage("This service is temporary unavailable")
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(), "Sorry:)", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                //This is login button, used hardcoded list of users
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean trigger = false;
                        for (User user : users) {
                            if (email.getText().toString().equals(user.getEmail()) && password.getText().toString().equals(user.getPassword())) {
                                Profile.setCurrentProfile(new Profile("id", "firstName", "", "lastName", "", null));
                                AccessToken.setCurrentAccessToken(new AccessToken("custom", "2054277547993040", "id", null, null, null, null, null, null));
                                Intent intent = new Intent(LoginActivity.this, ClubCardActivity.class);
                                intent.putExtra("name", user.getFirst_name());
                                intent.putExtra("lastName", user.getLast_name());
                                intent.putExtra("id", user.getId());
                                startActivity(intent);
                                //"notification" for login
                                Toast.makeText(getApplicationContext(), "Welcome back, " + user.getFirst_name(), Toast.LENGTH_LONG).show();
                            }
                        }
                        for (User user : users) {
                                if (email.getText().toString().equals(user.getEmail()) && !password.getText().toString().equals(user.getPassword())) {
                                trigger = true;
                                errorLabel.setText("        Wrong Password!");
                                errorLabel.setVisibility(View.VISIBLE);
                                }
                        }
                        for (User user : users){
                            if (!trigger){
                                errorLabel.setText("    Your Email is not registred");
                                errorLabel.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });

                //code for Facebook API
                loginButton = findViewById(R.id.login_button);
                loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
                callbackManager = CallbackManager.Factory.create();

                // realisation of validation for Email, Password, which can not be empty
                email = findViewById(R.id.txtEmail);
                password = findViewById(R.id.txtPassword);
                button.setEnabled(false);
                email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        button.setEnabled(s.toString().trim().length() != 0 && password.getText().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        button.setEnabled(s.toString().trim().length() != 0 && email.getText().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                //Facebook API
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("YES!!");
                        String accessToken = loginResult.getAccessToken().getToken();
                        getUserProfile(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("CANCEL");
                        errorLabel.setText("Login was cancelled");
                        errorLabel.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        System.out.println("ERROR");
                        errorLabel.setText("Login Error");
                        errorLabel.setVisibility(View.VISIBLE);
                    }
                });
            }

            //getting of user's data, and transition to Club_Card_Activity
            private void getUserProfile(AccessToken currentAccessToken) {
                GraphRequest request = GraphRequest.newMeRequest(
                        currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.d("TAG", object.toString());
                                try {
                                    String first_name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String userId = object.getString("id");

                                    Intent intent = new Intent(LoginActivity.this, ClubCardActivity.class);
                                    intent.putExtra("id", userId);
                                    intent.putExtra("name", first_name);
                                    intent.putExtra("lastName", last_name);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), "Welcome back, " + first_name, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }

}