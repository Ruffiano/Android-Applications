package com.example.user.demo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.example.user.demo.R;
import com.example.user.demo.ServiceActivity;
import com.example.user.demo.SettingsActivity;
import com.example.user.demo.adapter.MainPagerAdapter;
import com.example.user.demo.service.TaskButlerService;
import com.example.user.demo.service.WakefulIntentService;
import com.example.user.demo.utils.Settings;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


/**
 * Created by User on 01.02.2016.
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{

    @Inject
    Settings settings;
    private DrawerLayout mDrawerLayout;

    EventBus eventBus;

    /*********2016.04.28**********/


//    boolean isTurnOnFireSensor = true;
//    boolean isTurnOnGasSensor = true;
//    boolean isTurnOnAutoSwing = true;
//    boolean isTurnOnNotice = true;

//    Switch switch_sensor_fire = null;
//    Switch switch_sensor_gas = null;
//    Switch switch_auto_swing = null;
//    Switch switch_notice = null;
//    SeekBar speed_controller = null;

    /******************************/


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!settings.getAuth()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setHomeAsUp();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_settings);


        // ------------------------- 서비스 ---------------------------- //
        Intent Service = new Intent(this, ServiceActivity.class);
        startService(Service);
        // ------------------------------------------------------------ //

        /********************2016.04.27**************************/

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//        switch_sensor_fire = (Switch)findViewById(R.id.switch_sensor_fire1);
//        switch_sensor_gas = (Switch)findViewById(R.id.switch_sensor_gas1);
//        switch_auto_swing = (Switch)findViewById(R.id.switch_auto_swing1);
//        switch_notice = (Switch)findViewById(R.id.switch_notice1);
//        speed_controller = (SeekBar)findViewById(R.id.speed_controller);


//        switch_sensor_fire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                Toast.makeText(getApplication(), "fire sensor : " + isChecked, Toast.LENGTH_SHORT).show();
//            }
//        });
//        switch_sensor_gas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                Toast.makeText(getApplication(), "gas sensor : " + isChecked, Toast.LENGTH_SHORT).show();
//            }
//        });
//        switch_auto_swing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                Toast.makeText(getApplication(), "auto swing : " + isChecked, Toast.LENGTH_SHORT).show();
//            }
//        });
//        switch_notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//            {
//                Toast.makeText(getApplication(), "notice : " + isChecked, Toast.LENGTH_SHORT).show();
//            }
//        });
//        speed_controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
//        {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
//            {
//                Toast.makeText(getApplicationContext(), "" + progress, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar)
//            {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar)
//            {
//
//            }
//        });

        /**************************************************************/


        setupTabs();

        WakefulIntentService.acquireStaticLock(this);
        this.startService(new Intent(this, TaskButlerService.class));

    }


    private void setupTabs() {
        String mac_address = getIntent().getStringExtra("address");
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager(), mac_address));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {

            }

            @Override
            public void onPageSelected(int i)
            {
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        switch(id)
        {
            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                String mac_address = getIntent().getStringExtra("address");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("mac_address",mac_address);
                startActivity(intent);
                return true;
            case R.id.action_settings:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void onBackPressed()
//    {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if(drawer.isDrawerOpen(GravityCompat.START))
//        {
//            drawer.closeDrawer(GravityCompat.START);
//        }
//        else
//        {
//            super.onBackPressed();
//        }
//    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if(id == R.id.switch_sensor_fire)
//        {
//            isTurnOnFireSensor = !isTurnOnFireSensor;
//
//            Toast.makeText(getApplicationContext(), "fire sensor : " + isTurnOnFireSensor, Toast.LENGTH_SHORT).show();
//        }
//        else if(id == R.id.switch_sensor_gas)
//        {
//            isTurnOnGasSensor = !isTurnOnGasSensor;
//
//            Toast.makeText(getApplicationContext(), "gas sensor : " + isTurnOnFireSensor, Toast.LENGTH_SHORT).show();
//        }
//        else if(id == R.id.switch_auto_swing)
//        {
//            isTurnOnAutoSwing = !isTurnOnAutoSwing;
//
//            Toast.makeText(getApplicationContext(), "auto swing : " + isTurnOnFireSensor, Toast.LENGTH_SHORT).show();
//        }
//        else if(id == R.id.switch_notice)
//        {
//            isTurnOnNotice = !isTurnOnNotice;
//
//            Toast.makeText(getApplicationContext(), "notice : " + isTurnOnFireSensor, Toast.LENGTH_SHORT).show();
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
