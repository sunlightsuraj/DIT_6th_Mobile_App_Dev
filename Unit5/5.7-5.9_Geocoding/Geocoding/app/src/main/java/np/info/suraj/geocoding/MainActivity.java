package np.info.suraj.geocoding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String provider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            }
                        });
            }
        } else {
            Log.i("location", "got from getLastKnownLocation");
            updateWithNewLocation(location);
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void updateWithNewLocation(Location location) {
        TextView textView = findViewById(R.id.textView);
        String locationString = "No location found!";

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            locationString = "Your Location:\n lat: " + lat + " lng: " + lng;

            geocodeCoordinate(lat, lng);
        }

        textView.setText(locationString);
    }

    private void geocodeCoordinate(double lat, double lng) {
        TextView addressText = findViewById(R.id.addressText);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        String addressString;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 5);

            StringBuilder sb = new StringBuilder();
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    sb.append(address.getAddressLine(i)).append("\n");
                sb.append(address.getLocality()).append(",");
                sb.append(address.getPostalCode()).append(",");
                sb.append(address.getCountryName());
            }
            addressString = sb.toString();

            addressText.setText(addressString);
        } catch (IOException e) {
            Log.e("geocoder", "IO Exception", e);
        }
    }

    public void onClickButton(View v) {
        Intent intent = new Intent(this, SearchLocation.class);
        startActivity(intent);
    }
}