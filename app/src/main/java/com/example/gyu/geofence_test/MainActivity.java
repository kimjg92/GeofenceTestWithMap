package com.example.gyu.geofence_test;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

//참조 사이트
//https://tsicilian.wordpress.com/2013/06/25/android-geofencing-with-google-maps/
public class MainActivity extends FragmentActivity implements Serializable {


    GoogleMap map;
    GPS gps;

    ///////////////
    ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;
    /////////////
    private static final long SECONDS_PER_HOUR = 60;
    private static final long MILLISECONDS_PER_SECOND = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME =
            GEOFENCE_EXPIRATION_IN_HOURS *
                    SECONDS_PER_HOUR *
                    MILLISECONDS_PER_SECOND;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        setContentView(R.layout.activity_main);

        mGeofencePendingIntent = null;

        //위치정보 계속 받아오고 일단은 sleep를 이용해서 사용자가 원하는 시간단위로 갱신하고 그차이가 범위내이면 noti띄우는 service만ㄷ르어서 실행
        gps = new GPS(this);
        map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap(); // <- onCreate안에, setContentView 다음에 넣어야 nullpointer오류 안남
        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(new LatLng(37.5545, 126.971))
                        .zoom( 17.5F )
                        .bearing( 300F) // orientation
                                //.tilt( 50F) // viewing angle z축 회전
                        .build();
        // use GooggleMap mMap to move camera into position

        BitmapDrawable icon_drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.remindme);
        Bitmap icon = icon_drawable.getBitmap();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));
        GeofenceManager geofenceManager = new GeofenceManager(map, icon);
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.55376396, 126.97478771, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.556945, 126.97375774, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.55808469, 126.96955204, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.55281133, 126.96656942, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.55107614, 126.97341442, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("편의점", 37.55053175, 126.97493792, GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addGeofenceList(geofenceManager.CreateGeofence("내위치", gps.getLatitude(), gps.getLongitude(), GEOFENCE_EXPIRATION_TIME, Geofence.GEOFENCE_TRANSITION_ENTER));
        geofenceManager.addMarkerForFence();

        LatLng lat = new LatLng(37.5545, 126.971);

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("sample1")

                .setCircularRegion(
                        37.5545,
                        126.971,
                        20
                )
                .setExpirationDuration(60 * 60 * 1000 * 12)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        /*mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("sample1")

                .setCircularRegion(37.5545, 126.971, 20.0f)
                .setExpirationDuration(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());*/

    }




    public void scroll() {
        // we don't want to scroll too fast since
        // loading new areas in map takes time
        map.animateCamera( CameraUpdateFactory.scrollBy(10, -10),
                callback ); // 10 pix
    }

    private GoogleMap.CancelableCallback callback = new GoogleMap.CancelableCallback() {
        @Override
        public void onFinish() {
            scroll();
        }
        @Override
        public void onCancel() {}
    };

    public static void toggleView(GoogleMap map){
        map.setMapType(map.getMapType() ==
                GoogleMap.MAP_TYPE_NORMAL ?
                GoogleMap.MAP_TYPE_SATELLITE :
                GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}