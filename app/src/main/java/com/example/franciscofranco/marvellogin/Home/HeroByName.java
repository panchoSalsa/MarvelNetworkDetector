package com.example.franciscofranco.marvellogin.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.franciscofranco.marvellogin.MarvelAPI.HeroRequestTask;
import com.example.franciscofranco.marvellogin.R;
import com.example.franciscofranco.marvellogin.RenderingLogic.HeroJSONAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class HeroByName extends AppCompatActivity {


    //private EditText editText;

    public static ListView listView;

    private HeroJSONAdapter heroJSONAdapter;

    public static final String NAME = "name";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String DESCRIPTION = "description";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("FRANCO_DEBUG", "HeroByName onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_by_name);

        listView = (ListView) findViewById(R.id.heroList);

        heroJSONAdapter = new HeroJSONAdapter(this, getLayoutInflater());

        listView.setAdapter(heroJSONAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                triggerIntent(position);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        promptForInput();
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
                HomeActivity.logoutUser();
                return true;

            case R.id.search:
                promptForInput();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void triggerIntent(int position) {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        String name = null;
        String thumbnailUrl = null;
        String description = null;

        JSONObject obj = (JSONObject) heroJSONAdapter.getItem(position);

        try {

            name = obj.getString("name");

            JSONObject thumbnail = obj.getJSONObject("thumbnail");

            thumbnailUrl = thumbnail.getString("path")
                    + "."
                    + thumbnail.getString("extension") ;

            Log.d("FRANCO_DEBUG", "url: " + thumbnailUrl);

            description = obj.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra(NAME, name);
        intent.putExtra(THUMBNAIL_URL, thumbnailUrl);
        intent.putExtra(DESCRIPTION, description);
        startActivity(intent);

    }

    public void fetchData(String hero) {

        HeroRequestTask myTask = new HeroRequestTask(this, heroJSONAdapter);
        myTask.execute("timestamp", hero);

    }

    private void promptForInput() {
        final EditText txtUrl = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Search For Your Hero")
                .setView(txtUrl)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String hero = txtUrl.getText().toString();
                        Search(hero);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                // .show().getWindow..... show alertDialog with keyboard ready to type
                .show().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void Search(String hero) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        if (hero.isEmpty()) {
            return;
        } else {
            fetchData(hero);
        }
    }
}