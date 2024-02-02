package np.info.suraj.locationservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String provider = LocationManager.GPS_PROVIDER;
    private static final String TREASURE_PROXIMITY_ALERT = "np.info.suraj.locationservice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Broadcase receiver for Promixity Alter Intent
        IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
        registerReceiver(new ProximityIntentReceiver(), filter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkPermission();

        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(
                        provider,
                        null,
                        getApplication().getMainExecutor(),
                        new Consumer<Location>() {
                            @Override
                            public void accept(Location location) {
                                Log.i("location", "got from getCurrentLocation");
                                updateWithNewLocation(location);
                                setProximityAlert(locationManager, location);
                            }
                        });
            }
        } else {
            Log.i("location", "got from getLastKnownLocation");
            updateWithNewLocation(location);
            setProximityAlert(locationManager, location);
        }


        setupLocationRefresh(locationManager);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void setupLocationRefresh(LocationManager locationManager) {
        String provider = LocationManager.GPS_PROVIDER;
        int t = 5000; // milliseconds
        int distance = 1; // meters
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateWithNewLocation(location);
            }
        };

        checkPermission();

        locationManager.requestLocationUpdates(provider, t, distance, locationListener);
    }

    private void updateWithNewLocation(Location location) {
        TextView textView = findViewById(R.id.textView);
        String locationString = "No location found!";

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            locationString = "Your Location:\n lat: " + lat + " lng: " + lng;
        }

        textView.setText(locationString);
    }

    private void setProximityAlert(LocationManager locationManager, Location location) {
        String locService = Context.LOCATION_SERVICE;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            float radius = 0.1f; // meters
            long expiration = -1; // do not expire

            Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, -1, intent, PendingIntent.FLAG_MUTABLE);

            checkPermission();

            locationManager.addProximityAlert(lat, lng, radius, expiration, pendingIntent);
        }
    }
}