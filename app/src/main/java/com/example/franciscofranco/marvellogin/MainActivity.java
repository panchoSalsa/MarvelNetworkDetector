package com.example.franciscofranco.marvellogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.img);

        checkConnection();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
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
        Picasso.with(this)
                .load(R.mipmap.login)
                .fit()
                .into(image);
    }

    private void displayNetworkError() {
        Picasso.with(this)
                .load(R.mipmap.network_error_portrait)
                .fit()
                .into(image);
    }
}
