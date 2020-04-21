package kr.ac.skuniv.loadsearchalarm;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainTabsAdapter mainTabsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainTabsAdapter=new MainTabsAdapter(this,(TabHost)findViewById(android.R.id.tabhost),(UnTouchableViewPager)findViewById(R.id.pager));
        mainTabsAdapter.selectTab(MainTabsConfig.TABINDEX.FIRST);
    }


}


