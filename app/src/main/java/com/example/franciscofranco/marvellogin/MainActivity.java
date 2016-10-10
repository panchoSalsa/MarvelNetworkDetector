package com.example.franciscofranco.marvellogin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ImageView image;
    private Button btn;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    // i need to store my buttton or else it will get destroyed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.img);
//        btn = (Button) findViewById(R.id.button1);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

        finish();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        myFunc(isConnected);
    }

    private void myFunc(boolean isConnected) {

        if (isConnected) {
            displayLogin();
        } else {
            displayNetworkError();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        myFunc(isConnected);
    }

    private void displayLogin() {
        int value = 0;

        loginButton.setVisibility(View.VISIBLE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            value = R.mipmap.login_landscape;
            loginButton.setY(650);

        } else {
            value = R.mipmap.login;
            loginButton.setY(1000);
        }

        Picasso.with(this)
                .load(value)
                .fit()
                .into(image);
    }

    private void displayNetworkError() {
        int value = 0;

        btn.setVisibility(View.GONE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            value = R.mipmap.network_error_landscape;
        } else {
            value = R.mipmap.network_error_portrait;
        }

        Picasso.with(this)
                .load(value)
                .fit()
                .into(image);
    }
}
