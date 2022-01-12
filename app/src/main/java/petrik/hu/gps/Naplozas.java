package petrik.hu.gps;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Naplozas {

    //fájlba írás, ezért kell IOException
    public static void kiir(double longitude, double latitude) throws IOException {
        //Dátum lekérdetése
        Date datum = Calendar.getInstance().getTime();

        //Dátum formázás
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formazottDatum = dateFormat.format(datum);

        //CSV fájl létrehozásához szükséges segéd változó
        String sor = String.format("%f, %f, %s", longitude, latitude, formazottDatum);

        //Belső tárhely állapotának vizsgálása
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //Fájlba íráshoz File + Buffered writer
            File file = new File(Environment.getExternalStorageDirectory(), "gps_adatok.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append(sor);

            //Sortörés, olyan mint a /n
            bufferedWriter.append(System.lineSeparator());
            bufferedWriter.close();
        }
    }
}
