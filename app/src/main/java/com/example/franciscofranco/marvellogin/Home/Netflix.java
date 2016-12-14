package com.example.franciscofranco.marvellogin.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.franciscofranco.marvellogin.R;

import java.util.HashMap;

public class Netflix extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netflix);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        instantianteSlider();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.netflix).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_logout:
                HomeActivity.logoutUser();
                return true;

            case R.id.netflix:
                return true;

            case R.id.search:
                HomeActivity.goToSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void instantianteSlider() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Luke Cage", "http://cdn3-www.comingsoon.net/assets/uploads/gallery/luke-cage-set/cpvqrbzusaa3s3v.jpg");
        url_maps.put("DareDevil", "http://nerdist.com/wp-content/uploads/2016/03/Daredevil-Season-2-Trio-Poster.jpg");
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
}
