package com.connecttix.speedlimit.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.RouteHistoryModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Utils {

    public static String getFecha(){
        final Calendar calendario = Calendar.getInstance();
        int yy = calendario.get(Calendar.YEAR);
        int mm = calendario.get(Calendar.MONTH);
        int dd = calendario.get(Calendar.DAY_OF_MONTH);
        mm++;
        return  yy+"-"+mm+"-"+dd;
    }

    public static String getHora(){
        final Calendar calendario = Calendar.getInstance();
        int hora, minutos, segundos;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        return  hora+":"+minutos+":"+segundos;
    }

    public static void generarReporte(Context context){
        ArrayList<RouteHistoryModel> routeHistoryModels;

        routeHistoryModels = SqliteClass.getInstance(context).databasehelp.appRouteHistory.getAllRouteHistorys();
        String contenido ="CATEGORIA \tRUTA \tTRAMO \tFECHA \tVELOCIDAD LIMITE \tVELOCIDAD EXCESIVA \tTIPO \n";
        String faltaLinea="";
        String type="Tramo";
        for(int i=0; i<routeHistoryModels.size();i++){
            if(routeHistoryModels.get(i).getIs_point().equals("si")){
                type="Punto Interes";
            }
            contenido=contenido
                    + routeHistoryModels.get(i).getId_category_route()+" \t"
                    + routeHistoryModels.get(i).getId_route()+" \t"
                    + routeHistoryModels.get(i).getId_strech()+" \t"
                    + routeHistoryModels.get(i).getDate() +" \t"
                    + routeHistoryModels.get(i).getVel_strech()+" \t"
                    + routeHistoryModels.get(i).getVel_falta()+" \t"
                    + type +"\n";
        }
        //contenido = contenido+"\n"+faltaLinea;


        generateNoteOnSD(context,"Reporte.txt",contenido);
    }
    public static void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Reporte");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Reporte Generado", Toast.LENGTH_SHORT).show();
            SqliteClass.getInstance(context).databasehelp.appRouteHistory.deleteRouteHistoryTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

