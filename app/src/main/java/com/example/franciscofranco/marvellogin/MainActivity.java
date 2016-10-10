package com.example.franciscofranco.marvellogin;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ImageView image;
    private Button btn;

    // i need to store my buttton or else it will get destroyed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.img);
        btn = (Button) findViewById(R.id.button1);

        checkConnection();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

        btn.setVisibility(View.VISIBLE);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            value = R.mipmap.login_landscape;
            btn.setY(100);

        } else {
            value = R.mipmap.login;
            btn.setY(1000);

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
