package com.apps.lucho.tipkeeper2;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityTipKeeper extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    final double regularHourlyWage = 7.50;
    final double runnerPerc = .18;
    final double gauchoPerc = .38;
    final double gauchosPerc = .19;
    final double barbackWage = 10.00;
    final double grillerWage = 15.00;
    final double trainingWage = 10.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tip_keeper);

        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.fragment_container,new MainFragment(),"Main_Fragment");
//        ft.commit();



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_past_weeks) {
            PastWeeksFragment fragment = new PastWeeksFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_about) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void calculateTotal(View view) {
        //Toast.makeText(this, "Clicked Total", Toast.LENGTH_SHORT).show();
        double hourWageTotal = 0;
        double tipWageTotal = 0;
        double total = 0;

        TextView tv = (TextView) findViewById(R.id.tvTotal);

        if(((CheckBox)findViewById(R.id.checkboxFri)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndFri)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartFri)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerFri)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextFriTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerFri)));
        }
        if(((CheckBox)findViewById(R.id.checkboxSat)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndSat)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartSat)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerSat)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextSatTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerSat)));
        }
        if(((CheckBox)findViewById(R.id.checkboxSun)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndSun)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartSun)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerSun)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextSunTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerSun)));
        }
        if(((CheckBox)findViewById(R.id.checkboxMon)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndMon)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartMon)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerMon)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextMonTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerMon)));
        }
        if(((CheckBox)findViewById(R.id.checkboxTue)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndTue)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartTue)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerTue)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextTueTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerTue)));
        }
        if(((CheckBox)findViewById(R.id.checkboxWed)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndWed)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartWed)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerWed)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextWedTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerWed)));
        }
        if(((CheckBox)findViewById(R.id.checkboxThu)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextEndThu)).getText().toString()) - Double.parseDouble(((EditText)findViewById(R.id.editTextStartThu)).getText().toString())) * getWage((Spinner)findViewById(R.id.spinnerThu)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)findViewById(R.id.editTextThuTip)).getText().toString())) * getPercentage((Spinner)findViewById(R.id.spinnerThu)));
        }

        total = hourWageTotal + tipWageTotal;
        tv.setText(Double.toString(total));
    }

    private double getPercentage(Spinner day){
        if(day.getSelectedItem().toString().equals("Runner")) return runnerPerc;
        else if(day.getSelectedItem().toString().equals("Gaucho")) return gauchoPerc;
        else if(day.getSelectedItem().toString().equals("Gauchos")) return gauchosPerc;
        return -1;
    }

    private double getWage(Spinner day){
        if(day.getSelectedItem().toString().equals("Runner")) return regularHourlyWage;
        else if(day.getSelectedItem().toString().equals("Gaucho")) return regularHourlyWage;
        else if(day.getSelectedItem().toString().equals("Gauchos")) return regularHourlyWage;
        else if(day.getSelectedItem().toString().equals("Griller")) return grillerWage;
        else if(day.getSelectedItem().toString().equals("Barback")) return barbackWage;
        else if(day.getSelectedItem().toString().equals("Training")) return trainingWage;
        return -1;
    }

    public void toggleBox(View view) {
        //Toast.makeText(this, "Clicked Box", Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.textViewFri:
                if (((CheckBox) findViewById(R.id.checkboxFri)).isChecked()) ((CheckBox) findViewById(R.id.checkboxFri)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxFri)).setChecked(true);
                break;
            case R.id.textViewSat:
                if (((CheckBox) findViewById(R.id.checkboxSat)).isChecked()) ((CheckBox) findViewById(R.id.checkboxSat)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxSat)).setChecked(true);
                break;
            case R.id.textViewSun:
                if (((CheckBox) findViewById(R.id.checkboxSun)).isChecked()) ((CheckBox) findViewById(R.id.checkboxSun)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxSun)).setChecked(true);
                break;
            case R.id.textViewMon:
                if (((CheckBox) findViewById(R.id.checkboxMon)).isChecked()) ((CheckBox) findViewById(R.id.checkboxMon)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxMon)).setChecked(true);
                break;
            case R.id.textViewTue:
                if (((CheckBox) findViewById(R.id.checkboxTue)).isChecked()) ((CheckBox) findViewById(R.id.checkboxTue)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxTue)).setChecked(true);
                break;
            case R.id.textViewWed:
                if (((CheckBox) findViewById(R.id.checkboxWed)).isChecked()) ((CheckBox) findViewById(R.id.checkboxWed)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxWed)).setChecked(true);
                break;
            case R.id.textViewThu:
                if (((CheckBox) findViewById(R.id.checkboxThu)).isChecked()) ((CheckBox) findViewById(R.id.checkboxThu)).setChecked(false);
                else ((CheckBox) findViewById(R.id.checkboxThu)).setChecked(true);
                break;
        }
    }


}