package petrik.hu.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PerformanceHintManager;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

        init();

        //Helymeghatározás ellenőrzése
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            textViewGps.setText("Nincs helymeghatározás engedélyezve");

            //Engedély kérés ablak megnyitása
            String[] permission = new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                };
            ActivityCompat.requestPermissions(this, permission, 0);
            //return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, locationListener
            );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, locationListener
                    );
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myTimer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        };
        myTimer.schedule(timerTask, 1000, 5000);
    }

    private void TimerMethod() {
        this.runOnUiThread(TimerTick);
    }

    private final Runnable TimerTick = new Runnable() {
        @Override
        public void run() {
            textViewGps.setText("longitude: " + longitude + "\n" + "latitude: " + latitude);
            if (writePermission) {
                try {
                    Naplozas.kiir(longitude, latitude);
                } catch (IOException e) {
                    Log.d("Kiiras: ", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    };

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