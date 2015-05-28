package ie.gmit.glen.placed;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    double lat = Double.parseDouble(DisplayActivity.singlePlace.getLat());
    double lng = Double.parseDouble(DisplayActivity.singlePlace.getLng());
    final  LatLng NEWPIN = new LatLng(lat,lng);

    private LocationManager locationManager;
    private Location myLocation;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
//        https://code.google.com/p/android/issues/detail?id=82997
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWPIN, 13.0f));
        mMap.setMyLocationEnabled(true);
       // mMap.getUiSettings().setMyLocationButtonEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();

    }




    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

        }
    }


    private void setUpMap()
    {


        String label = DisplayActivity.singlePlace.getPlace();

        mMap.addMarker(new MarkerOptions().position(NEWPIN).title(label));


    }


}
