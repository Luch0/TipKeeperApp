package com.apps.lucho.tipkeeper2;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PastWeeksFragment extends Fragment {

    private static final int CHOOSER_REQUEST_CODE = 6384; // onActivityResult request
    private static final int READ_EXTERNAL_PERMISSION_REQUEST_CODE = 10;


    public PastWeeksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_weeks, container, false);
    }



    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, CHOOSER_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_EXTERNAL_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showChooser();
            }
            else{
                Toast.makeText(getContext(),"PERMISSION DENIED: Can't access", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSER_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(getContext(), uri);
                            //Toast.makeText(getContext(), "File Selected: " + path, Toast.LENGTH_LONG).show();
                            showWeek(path);
                        } catch (Exception e) {
                            Log.e("FSTA", "FileSelEx", e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showWeek(String path){

        //File sdcard = Environment.getExternalStorageDirectory();
        //File file = new File(sdcard,"/TestAppTXT/test.txt"); //my phone
        //File file = new File(sdcard,"/Download/test.txt"); //emulator
        //Toast.makeText(getContext(), file.getAbsolutePath(), Toast.LENGTH_LONG).show();


        //File fileDirectory = new File(Environment.getDataDirectory()+"/YourDirectory/");
        //File file = new File(fileDirectory,path);
        File file = new File(path);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            //You'll need to add proper error handling here
        }

        TextView tv = (TextView)getActivity().findViewById(R.id.tvWeekData);
        tv.setText(text);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.past_weeks_activity_tip_keeper, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_open) {
            Toast.makeText(getContext(), "Clicked Open", Toast.LENGTH_SHORT).show();
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                if (getContext().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showChooser();
                } else {
                    String[] permissionRequest = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, READ_EXTERNAL_PERMISSION_REQUEST_CODE);
                }
            }
            else{
                showChooser();
            }
            return true;
        }
        else if (id == R.id.action_show){
            Toast.makeText(getContext(), "Clicked Show", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
