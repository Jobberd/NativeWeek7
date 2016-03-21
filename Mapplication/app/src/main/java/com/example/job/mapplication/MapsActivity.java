package com.example.job.mapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Hashtable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    EditText latInput;
    EditText lngInput;
    Button findButton;
    String newLocationTitle;
    Boolean newInput;
    Hashtable<String, LatLng> myMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // Sets up the location find Button with the EditTexts
        latInput = (EditText) findViewById(R.id.latInput);
        lngInput = (EditText) findViewById(R.id.lngInput);
        findButton = (Button) findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double latString = Double.parseDouble(latInput.getText().toString());
                    Double lngString = Double.parseDouble(lngInput.getText().toString());
                    LatLng newLocation = new LatLng(latString, lngString);
                    if (promptUserInput()) {
                        changeLocation(newLocation, "newMarker");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No location input", Toast.LENGTH_LONG).show();
                    }
                    latInput.setText("");
                    lngInput.setText("");
                } catch (final NumberFormatException e) {
                    promptUser();
                }
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);



    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng amsterdam = new LatLng(52, 4);
        mMap.addMarker(new MarkerOptions().position(amsterdam).title("Marker in Amsterdam"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(amsterdam));
    }

    public void changeLocation(LatLng newMarker, String title) {
        mMap.addMarker(new MarkerOptions().position(newMarker).title(title));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(newMarker, 12);
        mMap.animateCamera(yourLocation);
    }

    private void promptUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wrong location input!");

        // Set up the positivebutton, canceling the dialog window
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private Boolean promptUserInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name your list:");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the positivebutton, creating a new menu item and refreshing all adapters when selected
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newLocationTitle = input.getText().toString();
                newInput = true;
            }
        });


        // Set up the negativebutton
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newInput = false;
                dialog.cancel();
            }
        });
        builder.show();
        return newInput;
    }
}
