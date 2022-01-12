package petrik.hu.gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private TextView textViewGps;
    private Timer myTimer;
    private double longitude, latitude;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean writePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void init() {
        textViewGps = findViewById(R.id.textViewGps);

        //LocationManager
        //Ez felelős a hely lekérdezéséért
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //LocationListener, ez lesz a LocationManager eseménykezelője
        locationListener = location -> {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        };

        //Van e engedélyünk a fájl olvasáshoz és a fájl íráshoz
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            writePermission = false;

            //Engedély kérés ablak megnyitása
            String[] permission = new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                };
            ActivityCompat.requestPermissions(this, permission, 1);
        } else {
            writePermission = true;
        }
    }
}