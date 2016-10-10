package com.example.franciscofranco.marvellogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_home);

        info = (TextView) findViewById(R.id.info);

        greet();
    }

    private void gotoLogin(){
        android.content.Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void logoutUser() {
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut();
            gotoLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void greet() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        parseJSON(object);
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void parseJSON(JSONObject obj) {
        String name = "";

        try {
            name = obj.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome back " + name + "!\n");

        info.setText(sb.toString());
    }
}
