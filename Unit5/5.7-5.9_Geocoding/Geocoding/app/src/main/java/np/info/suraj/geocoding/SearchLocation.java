package np.info.suraj.geocoding;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
    }

    public void onClickSearchButton(View v) {
        EditText searchInput = findViewById(R.id.searchInput);
        TextView resultText = findViewById(R.id.resultText);

        String searchText = searchInput.getText().toString();
        String resultString = geocodeLocation(searchText);
    Log.i("locationString", "Final: " + resultString);
        resultText.setText(resultString);
    }

    private String geocodeLocation(String searchText) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        StringBuilder addressString = new StringBuilder();

        try {
            addresses = geocoder.getFromLocationName(searchText, 5);

            if (addresses.size() > 0) {
                for (int k = 0; k < addresses.size(); ++k) {
                    Address address = addresses.get(k);
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");
                    sb.append(address.getLocality()).append(",");
                    sb.append(address.getPostalCode()).append(",");
                    sb.append(address.getCountryName()).append(",");
                    sb.append(address.getLatitude()).append("/").append(address.getLongitude());

                    Log.i("locationString", sb.toString());

                    addressString.append(sb).append("\n");
                }
            }
        } catch (IOException e) {
            Log.e("geocode", "IOException", e);
        }

        return "Result:\n" + addressString.toString();
    }
}