package com.connecttix.speedlimit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.StrechModel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.connecttix.speedlimit", appContext.getPackageName());
        assertEquals(1, TramoActual(-71.49327009916306, -16.41981149251388));


    }
    public static final double lat2y(double aLat) {
        return ((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, 0)) * 256;
    }

    public static final double lon2x(double lon) {
        return (lon + 180f) / 360f * 256f;
    }
    public static int TramoActual(double Latitud, double Longitud){
        ArrayList<StrechModel> tramos = new ArrayList<StrechModel>();
        SharedPreferences sharedPref = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("values", Context.MODE_PRIVATE);
        tramos = SqliteClass.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext()).databasehelp.appStrechSql.getAllStrechesByRoute(sharedPref.getString("id_route","01"));

        double Distancia = 0.0;
        double Distanciaaux=Double.MAX_VALUE;
        int tramo=0;
        for(int i = 0 ; i<= tramos.size()-1 ; i++ ){
            double x1 = lat2y(tramos.get(i).getOriginLati());
            double y1 = lon2x(tramos.get(i).getOriginLong());

            double x2 = lat2y(tramos.get(i).getDestinationLati());
            double y2 = lon2x(tramos.get(i).getDestinationLong());

            double x = lat2y(Latitud);
            double y = lon2x(Longitud);

            Distancia = minDistance(new pair(x1,y1),new pair(x2,y2),new pair(x,y)) ;

            if(Distancia<Distanciaaux){
                Distanciaaux = Distancia;
                tramo = i+1;
            }


        }
        return tramo;
    }

    public static double minDistance(pair A, pair B, pair E)
    {

        // vector AB
        pair AB = new pair();
        AB.F = B.F - A.F;
        AB.S = B.S - A.S;

        // vector BP
        pair BE = new pair();
        BE.F = E.F - B.F;
        BE.S = E.S - B.S;

        // vector AP
        pair AE = new pair();
        AE.F = E.F - A.F;
        AE.S = E.S - A.S;

        // Variables to store dot product
        double AB_BE, AB_AE;

        // Calculating the dot product
        AB_BE = (AB.F * BE.F + AB.S * BE.S);
        AB_AE = (AB.F * AE.F + AB.S * AE.S);

        // Minimum distance from
        // point E to the line segment
        double reqAns = 0;

        // Case 1
        if (AB_BE > 0)
        {

            // Finding the magnitude
            double y = E.S - B.S;
            double x = E.F - B.F;
            reqAns = Math.sqrt(x * x + y * y);
        }

        // Case 2
        else if (AB_AE < 0)
        {
            double y = E.S - A.S;
            double x = E.F - A.F;
            reqAns = Math.sqrt(x * x + y * y);
        }

        // Case 3
        else
        {

            // Finding the perpendicular distance
            double x1 = AB.F;
            double y1 = AB.S;
            double x2 = AE.F;
            double y2 = AE.S;
            double mod = Math.sqrt(x1 * x1 + y1 * y1);
            reqAns = Math.abs(x1 * y2 - y1 * x2) / mod;
        }
        return reqAns;
    }

}


class pair
{
    double F, S;
    public pair(double F, double S)
    {
        this.F = F;
        this.S = S;
    }
    public pair() {
    }
}

class StrechModelClass {     // FK
    private double originLati;
    private double originLong;
    private double destinationLati;
    private double destinationLong;

    public double getOriginLati() {
        return originLati;
    }

    public void setOriginLati(double originLati) {
        this.originLati = originLati;
    }

    public double getOriginLong() {
        return originLong;
    }

    public void setOriginLong(double originLong) {
        this.originLong = originLong;
    }

    public double getDestinationLati() {
        return destinationLati;
    }

    public void setDestinationLati(double destinationLati) {
        this.destinationLati = destinationLati;
    }

    public double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public StrechModelClass(double originLati, double originLong, double destinationLati, double destinationLong) {
        this.originLati = originLati;
        this.originLong = originLong;
        this.destinationLati = destinationLati;
        this.destinationLong = destinationLong;
    }
}
