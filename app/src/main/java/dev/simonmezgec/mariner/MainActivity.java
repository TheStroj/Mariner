package dev.simonmezgec.mariner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import dev.simonmezgec.mariner.ui.main.SectionsPagerAdapter;

/** Main Activity with the search and favorite functionality. */
@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter
                = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                AppBarLayout appbar = findViewById(R.id.app_bar_layout);
                appbar.setExpanded(true,true);
                if (position == 0) {
                    SharedPreferences prefs
                            = getSharedPreferences(Constant.SHARED_PREFERENCES, MODE_PRIVATE);
                    boolean readyToSearch
                            = prefs.getBoolean(Constant.PREFERENCES_READY_TO_SEARCH, true);
                    if (!readyToSearch) fab.show();
                    else fab.hide();
                } else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /** Launches the Info Activity. */
    public void launchAppInfo(View v) {
        Class destinationClass = InfoActivity.class;
        Intent intentToStartActivity = new Intent(this, destinationClass);
        startActivity(intentToStartActivity);
    }
}