 package com.connecttix.speedlimit;

import android.content.Context;
import android.location.Location;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        assertEquals(4, TramoActual(-71.49327009916306, -16.41981149251388));
    }

    public static int TramoActual(double Latitud, double Longitud){
        ArrayList<StrechModelClass> tramos = new ArrayList<StrechModelClass>();

        tramos.add(new StrechModelClass(-71.52730464935303, -16.377915064264684,-71.52271270751953, -16.39360171049271));
        tramos.add(new StrechModelClass(-71.52270197868347, -16.39360171049271,-71.49147033691406, -16.415338595964826));
        tramos.add(new StrechModelClass(-71.49147301912308, -16.41533602308705,- 71.49329423904419, -16.419763895364298));
        tramos.add(new StrechModelClass(-71.49328887462616, -16.419758749725816,-71.4887237548828, -16.468353226287046));
        tramos.add(new StrechModelClass(-71.4887237548828, -16.46833264888347,-71.45267486572266, -16.499299167397574));

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
    public static final double lat2y(double aLat) {
        return ((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, 0)) * 256;
    }

    public static final double lon2x(double lon) {
        return (lon + 180f) / 360f * 256f;
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
