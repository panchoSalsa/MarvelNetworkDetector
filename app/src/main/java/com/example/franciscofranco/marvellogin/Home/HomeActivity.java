package com.example.franciscofranco.marvellogin.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.franciscofranco.marvellogin.ConnectivityReceiver;
import com.example.franciscofranco.marvellogin.MainActivity;
import com.example.franciscofranco.marvellogin.MyApplication;
import com.example.franciscofranco.marvellogin.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private SliderLayout mDemoSlider;
    private CallbackManager callbackManager;
    private TextView info;
    private static Context mContext;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_home);

        info = (TextView) findViewById(R.id.info);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        instantianteSlider();

        mContext = activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);

        greet();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    public static void gotoMain() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);

        activity.finish();
    }

    public static void logoutUser() {
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut();
            gotoMain();
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

            case R.id.netflix:
                goToNetflixSlider();
                return true;

            case R.id.search:
                goToSearch();
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

    public void onNetworkConnectionChanged(boolean isConnected) {
        if (! ConnectivityReceiver.isConnected()) {
            gotoMain();
        }
    }

    private void instantianteSlider() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        //url_maps.put("Doctor Strange", "http://static.srcdn.com/wp-content/uploads/Doctor-Strange-Poster.jpg");
        url_maps.put("Luke Cage", "http://cdn3-www.comingsoon.net/assets/uploads/gallery/luke-cage-set/cpvqrbzusaa3s3v.jpg");
        url_maps.put("DareDevil", "http://nerdist.com/wp-content/uploads/2016/03/Daredevil-Season-2-Trio-Poster.jpg");
        //url_maps.put("Guardians of the Galaxy 2", "https://i0.wp.com/media2.slashfilm.com/slashfilm/wp/wp-content/images/guardiansofthegalaxy2-teaserposter-full-highquality.jpg");
        url_maps.put("Jessica Jones", "http://cdn1-www.comingsoon.net/assets/uploads/gallery/marvels-jessica-jones/jessicajonesposter.jpg");


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
        //Log.d("FRANCO_DEBUG", "onSliderClicked");

        String netFlixId = getNetFlixId((String) slider.getBundle().get("extra"));

        netflixIntent(netFlixId);
    }

    public String getNetFlixId(String title) {

        String netFlixId = null;

        switch (title) {
            case "Luke Cage":
                netFlixId = "80002537";
                break;
            case "Jessica Jones":
                netFlixId = "80002311";
                break;
            case "DareDevil":
                netFlixId = "80018294";
                break;
        }
        return netFlixId;
    }

    public void netflixIntent(String netFlixId ) {
        String watchUrl = "http://www.netflix.com/title/" + netFlixId;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setClassName("com.netflix.mediaclient", "com.netflix.mediaclient.ui.launch.UIWebViewActivity");
        intent.setData(Uri.parse(watchUrl));
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public static void goToSearch() {
        Intent intent = new Intent(mContext, HeroByName.class);
        mContext.startActivity(intent);
    }

    public static void goToNetflixSlider() {
        Intent intent = new Intent(mContext, Netflix.class);
        mContext.startActivity(intent);
    }

    public void goToStorage(View view) {
        Intent intent = new Intent(this, Storage.class);
        this.startActivity(intent);
    }
}