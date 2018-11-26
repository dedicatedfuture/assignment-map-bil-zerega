package com.billzerega.android.assignment_maps_bil_zerega;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {
    private GoogleMap mMap;
    private ArrayList<MapLocation> mMapLocations = new ArrayList<MapLocation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);
        firebaseLoadData();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("BATMAN onMapReady firing");
        mMap = googleMap;

        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);

        LatLng sydney = new LatLng(-33.868,151.2093 );
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }

    @Override
    public void onCameraIdle() {
        Toast.makeText(this, "The camera has stopped moving.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveStarted(int reason){
        if(reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE){
            Toast.makeText(this, "User is gestering on the map", Toast.LENGTH_SHORT).show();
        }else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            Toast.makeText(this, "The user tapped something on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Toast.makeText(this, "The app moved the camera.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseLoadData(){
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot locationSnapshot : dataSnapshot.getChildren()){
                    String location = locationSnapshot.child("location").getValue(String.class);
                    Double lattitude = locationSnapshot.child("lattitude").getValue(Double.class);
                    Double longitude = locationSnapshot.child("longitude").getValue(Double.class);
                    Log.d("FirebaseLoadData", "location: " + location + " lattitude: " +
                            lattitude + " longitude: " + longitude);

                    mMapLocations.add(new MapLocation(lattitude, location, longitude));


                }
                createMarkersFromFirebase(mMapLocations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createMarkersFromFirebase(ArrayList<MapLocation> locations){

        for(MapLocation location : locations){
            LatLng newLocation = new LatLng(location.getLattitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(newLocation).title(location.getLocation()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
        }


    }
}

