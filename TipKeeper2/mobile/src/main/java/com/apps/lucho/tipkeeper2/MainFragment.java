package com.apps.lucho.tipkeeper2;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int WRITE_EXTERNAL_PERMISSION_REQUEST_CODE = 11;

    final double regularHourlyWage = 7.50;
    final double runnerPerc = .18;
    final double gauchoPerc = .38;
    final double gauchosPerc = .19;
    final double barbackWage = 10.00;
    final double grillerWage = 15.00;
    final double trainingWage = 10.00;

    private SharedPreferences pref;

    ArrayList<View> al;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //add day and date to bottom of app
        TextView textView = (TextView) getActivity().findViewById(R.id.tvDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd/yyyy");
        Date d = new Date();
        String date = sdf.format(d);
        textView.setText(date);


        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/TitilliumWeb-Regular.ttf");
        Typeface typefaceBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/TitilliumWeb-SemiBold.ttf");

        al = getAllChildren(getActivity().findViewById(R.id.fragment_main));

        //remove duplicates
        al = new ArrayList<View>(new LinkedHashSet<View>(al));
        //if textview or edit text, then change typeface
        for(int i=0;i<al.size();i++){
            View child = al.get(i);
            //Log.d(TAG + "++++++++++",child.toString());
            if (child instanceof TextView || child instanceof EditText){
                if(child instanceof EditText){
                    EditText et = (EditText) child;
                    et.setTypeface(typeface);
                }
                else{
                    TextView tv = (TextView) child;
                    tv.setTypeface(typefaceBold);
                }

            }
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_tip_keeper, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            clear();
            Toast.makeText(getContext(), "Clicked Clear", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_save){
            saveWeek();
            //Toast.makeText(getContext(), "Clicked Save", Toast.LENGTH_SHORT).show();
            return true;




//            Toast.makeText(getContext(), "Clicked Save", Toast.LENGTH_SHORT).show();
//            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
//                if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveWeek();
//                } else {
//                    String[] permissionRequest = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                    requestPermissions(permissionRequest, WRITE_EXTERNAL_PERMISSION_REQUEST_CODE);
//                }
//            }
//            else{
//                saveWeek();
//            }
//            return true;





        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == WRITE_EXTERNAL_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveWeek();
            }
            else{
                Toast.makeText(getContext(),"PERMISSION DENIED: Can't access", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void calculateTotal(View view) {
        double hourWageTotal = 0;
        double tipWageTotal = 0;
        double total = 0;

        TextView tv = (TextView) getActivity().findViewById(R.id.tvTotal);

        if(((CheckBox)getActivity().findViewById(R.id.checkboxFri)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndFri)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartFri)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerFri)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextFriTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerFri)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxSat)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndSat)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartSat)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerSat)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextSatTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerSat)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxSun)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndSun)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartSun)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerSun)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextSunTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerSun)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxMon)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndMon)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartMon)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerMon)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextMonTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerMon)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxTue)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndTue)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartTue)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerTue)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextTueTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerTue)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxWed)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndWed)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartWed)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerWed)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextWedTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerWed)));
        }
        if(((CheckBox)getActivity().findViewById(R.id.checkboxThu)).isChecked()){
            hourWageTotal = hourWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextEndThu)).getText().toString()) - Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextStartThu)).getText().toString())) * getWage((Spinner)getActivity().findViewById(R.id.spinnerThu)));
            tipWageTotal = tipWageTotal + ((Double.parseDouble(((EditText)getActivity().findViewById(R.id.editTextThuTip)).getText().toString())) * getPercentage((Spinner)getActivity().findViewById(R.id.spinnerThu)));
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


    private ArrayList<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }


    //to also toggle boxes if textview day is touched
    public void toggleBox(View view) {
        switch (view.getId()) {
            case R.id.textViewFri:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxFri)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxFri)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxFri)).setChecked(true);
                break;
            case R.id.textViewSat:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxSat)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxSat)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxSat)).setChecked(true);
                break;
            case R.id.textViewSun:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxSun)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxSun)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxSun)).setChecked(true);
                break;
            case R.id.textViewMon:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxMon)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxMon)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxMon)).setChecked(true);
                break;
            case R.id.textViewTue:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxTue)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxTue)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxTue)).setChecked(true);
                break;
            case R.id.textViewWed:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxWed)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxWed)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxWed)).setChecked(true);
                break;
            case R.id.textViewThu:
                if (((CheckBox) getActivity().findViewById(R.id.checkboxThu)).isChecked()) ((CheckBox) getActivity().findViewById(R.id.checkboxThu)).setChecked(false);
                else ((CheckBox) getActivity().findViewById(R.id.checkboxThu)).setChecked(true);
                break;
        }
    }

    public void clear(){
        for(int i=0;i<al.size();i++){
            View child = al.get(i);
            if (child instanceof CheckBox || child instanceof EditText || child instanceof Spinner){
                //Log.d(TAG + "++++++++++",child.toString());
                if(child instanceof EditText){
                    EditText et = (EditText) child;
                    et.setText("");
                }
                else if (child instanceof CheckBox) {
                    CheckBox cb = (CheckBox) child;
                    cb.setChecked(false);
                }
                else {
                    Spinner spin = (Spinner) child;
                    spin.setSelection(0);
                }
            }
        }
        ((Spinner) getActivity().findViewById(R.id.spinnerFri)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerSat)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerSun)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerMon)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerTue)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerWed)).setSelection(0);
        ((Spinner) getActivity().findViewById(R.id.spinnerThu)).setSelection(0);

    }

    public void saveWeek(){

        Date date = new Date() ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd__HH-mm-ss");
        String dateStr = sdf.format(date);

        Calendar calendar;
        calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = "", endDate = "";
        startDate = df.format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        endDate = df.format(calendar.getTime());


        String filename = dateStr + ".txt";
        String stringTxt = "";
        stringTxt += startDate + "-----" + endDate + "\n \n";
        stringTxt += "Total: $" + ((TextView) getActivity().findViewById(R.id.tvTotal)).getText() + "\n";


        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"Weeks");
        if(!directory.exists()) {
            directory.mkdirs();
        }

        //Toast.makeText(getActivity().getBaseContext(), directory.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity().getBaseContext(),Environment.getExternalStorageDirectory()+File.separator+"Weeks"+File.separator+filename,Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity().getBaseContext(),Environment.getExternalStorageDirectory().getAbsolutePath().toString()+File.separator+"Weeks"+File.separator+filename,Toast.LENGTH_LONG).show();

        FileOutputStream outputStream;

        try {
            //outputStream = getContext().openFileOutput(Environment.getExternalStorageDirectory()+File.separator+"Weeks"+File.separator+filename, getContext().MODE_PRIVATE);
            outputStream = new FileOutputStream (new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Weeks"+File.separator+filename), true);
            outputStream.write(stringTxt.getBytes());
            outputStream.close();

            //display file saved message
            Toast.makeText(getActivity().getBaseContext(), "Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs = MainFragment.this.getContext().getSharedPreferences("data",getContext().MODE_PRIVATE);

        ((CheckBox) getActivity().findViewById(R.id.checkboxFri)).setChecked(prefs.getBoolean("friBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartFri)).setText(prefs.getString("friStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndFri)).setText(prefs.getString("friEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextFriTip)).setText(prefs.getString("friTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerFri)).setSelection(prefs.getInt("friSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxSat)).setChecked(prefs.getBoolean("satBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartSat)).setText(prefs.getString("satStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndSat)).setText(prefs.getString("satEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextSatTip)).setText(prefs.getString("satTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerSat)).setSelection(prefs.getInt("satSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxSun)).setChecked(prefs.getBoolean("sunBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartSun)).setText(prefs.getString("sunStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndSun)).setText(prefs.getString("sunEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextSunTip)).setText(prefs.getString("sunTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerSun)).setSelection(prefs.getInt("sunSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxMon)).setChecked(prefs.getBoolean("monBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartMon)).setText(prefs.getString("monStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndMon)).setText(prefs.getString("monEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextMonTip)).setText(prefs.getString("monTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerMon)).setSelection(prefs.getInt("monSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxTue)).setChecked(prefs.getBoolean("tueBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartTue)).setText(prefs.getString("tueStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndTue)).setText(prefs.getString("tueEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextTueTip)).setText(prefs.getString("tueTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerTue)).setSelection(prefs.getInt("tueSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxWed)).setChecked(prefs.getBoolean("wedBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartWed)).setText(prefs.getString("wedStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndWed)).setText(prefs.getString("wedEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextWedTip)).setText(prefs.getString("wedTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerWed)).setSelection(prefs.getInt("wedSpinner", 0));

        ((CheckBox) getActivity().findViewById(R.id.checkboxThu)).setChecked(prefs.getBoolean("thuBox",false));
        ((EditText)getActivity().findViewById(R.id.editTextStartThu)).setText(prefs.getString("thuStart", ""));
        ((EditText)getActivity().findViewById(R.id.editTextEndThu)).setText(prefs.getString("thuEnd", ""));
        ((EditText)getActivity().findViewById(R.id.editTextThuTip)).setText(prefs.getString("thuTip", ""));
        ((Spinner)getActivity().findViewById(R.id.spinnerThu)).setSelection(prefs.getInt("thuSpinner", 0));
    }

    @Override
    public void onPause() {
        super.onPause();

        pref = getContext().getSharedPreferences("data", getContext().MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("friBox", ((CheckBox)getActivity().findViewById(R.id.checkboxFri)).isChecked());
        editor.putString("friStart",((EditText)getActivity().findViewById(R.id.editTextStartFri)).getText().toString());
        editor.putString("friEnd",((EditText)getActivity().findViewById(R.id.editTextEndFri)).getText().toString());
        editor.putString("friTip",((EditText)getActivity().findViewById(R.id.editTextFriTip)).getText().toString());
        editor.putInt("friSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerFri)).getSelectedItemPosition());

        editor.putBoolean("satBox", ((CheckBox)getActivity().findViewById(R.id.checkboxSat)).isChecked());
        editor.putString("satStart",((EditText)getActivity().findViewById(R.id.editTextStartSat)).getText().toString());
        editor.putString("satEnd",((EditText)getActivity().findViewById(R.id.editTextEndSat)).getText().toString());
        editor.putString("satTip",((EditText)getActivity().findViewById(R.id.editTextSatTip)).getText().toString());
        editor.putInt("satSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerSat)).getSelectedItemPosition());

        editor.putBoolean("sunBox", ((CheckBox)getActivity().findViewById(R.id.checkboxSun)).isChecked());
        editor.putString("sunStart",((EditText)getActivity().findViewById(R.id.editTextStartSun)).getText().toString());
        editor.putString("sunEnd",((EditText)getActivity().findViewById(R.id.editTextEndSun)).getText().toString());
        editor.putString("sunTip",((EditText)getActivity().findViewById(R.id.editTextSunTip)).getText().toString());
        editor.putInt("sunSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerSun)).getSelectedItemPosition());

        editor.putBoolean("monBox", ((CheckBox)getActivity().findViewById(R.id.checkboxMon)).isChecked());
        editor.putString("monStart",((EditText)getActivity().findViewById(R.id.editTextStartMon)).getText().toString());
        editor.putString("monEnd",((EditText)getActivity().findViewById(R.id.editTextEndMon)).getText().toString());
        editor.putString("monTip",((EditText)getActivity().findViewById(R.id.editTextMonTip)).getText().toString());
        editor.putInt("monSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerMon)).getSelectedItemPosition());

        editor.putBoolean("tueBox", ((CheckBox)getActivity().findViewById(R.id.checkboxTue)).isChecked());
        editor.putString("tueStart",((EditText)getActivity().findViewById(R.id.editTextStartTue)).getText().toString());
        editor.putString("tueEnd",((EditText)getActivity().findViewById(R.id.editTextEndTue)).getText().toString());
        editor.putString("tueTip",((EditText)getActivity().findViewById(R.id.editTextTueTip)).getText().toString());
        editor.putInt("tueSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerTue)).getSelectedItemPosition());

        editor.putBoolean("wedBox", ((CheckBox)getActivity().findViewById(R.id.checkboxWed)).isChecked());
        editor.putString("wedStart",((EditText)getActivity().findViewById(R.id.editTextStartWed)).getText().toString());
        editor.putString("wedEnd",((EditText)getActivity().findViewById(R.id.editTextEndWed)).getText().toString());
        editor.putString("wedTip",((EditText)getActivity().findViewById(R.id.editTextWedTip)).getText().toString());
        editor.putInt("wedSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerWed)).getSelectedItemPosition());

        editor.putBoolean("thuBox", ((CheckBox)getActivity().findViewById(R.id.checkboxThu)).isChecked());
        editor.putString("thuStart",((EditText)getActivity().findViewById(R.id.editTextStartThu)).getText().toString());
        editor.putString("thuEnd",((EditText)getActivity().findViewById(R.id.editTextEndThu)).getText().toString());
        editor.putString("thuTip",((EditText)getActivity().findViewById(R.id.editTextThuTip)).getText().toString());
        editor.putInt("thuSpinner", ((Spinner)getActivity().findViewById(R.id.spinnerThu)).getSelectedItemPosition());

        editor.apply();

    }


}
