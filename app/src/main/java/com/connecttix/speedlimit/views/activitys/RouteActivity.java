package com.connecttix.speedlimit.views.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.config.ConstValue;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.InteresPointModel;
import com.connecttix.speedlimit.models.RouteHistoryModel;
import com.connecttix.speedlimit.models.StrechModel;
import com.connecttix.speedlimit.utils.Position;
import com.connecttix.speedlimit.utils.ServicioSonido;
import com.connecttix.speedlimit.utils.Utils;
import com.connecttix.speedlimit.utils.VozNotification;
import com.connecttix.speedlimit.views.dialog.CommonDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity {
    MapView map;
    public MyLocationNewOverlay mLocationOverlay;

    public ArrayList<StrechModel> strechModels;
    public ArrayList<InteresPointModel> interesPointModels;

    Context ctx = this;
    public TextView tv_strech_name, tv_vel_max, tv_vel_actual;
    public TextView  tv_vel_max_2, tv_vel_actual_2;
    CardView crdvw_detalle;

    LinearLayout lnyt_content;
    RelativeLayout rltyt_content_low;
    FloatingActionButton btn_myposotion, btn_seguir, btn_expand;
    Location destinationPoint=new Location("Destination");
    Location originPoint=new Location("Origination");
    Location interesPoint=new Location("Pointe");

    public int strech=0;
    public int contador=0;
    public String is_point;
    public GeoPoint sitesPoints;

    //public MediaPlayer mediaPlayer;
    public double lastLatitud=0.0;
    public double lastLongitud=0.0;

    TextToSpeech textToVoice;
    RouteHistoryModel routeHistoryModel;

    LocationManager locManager;
    LocationListener locListener;
    Context context;

    public double velMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

        context = this;

        lnyt_content = (LinearLayout) findViewById(R.id.lnyt_content);
        rltyt_content_low = (RelativeLayout) findViewById(R.id.rltyt_content_low);

        crdvw_detalle = (CardView) findViewById(R.id.crdvw_detalle);
        SharedPreferences sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE);

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("seguir","si");
        editor.apply();

        //Habilitamos la voz para las notificaciones
        final Locale spanish = new Locale("es", "ES");

        textToVoice = new TextToSpeech(getApplicationContext(), status1 -> {
            if (status1 != TextToSpeech.ERROR) {
                textToVoice.setLanguage(spanish);
            }
        });
        routeHistoryModel =  new RouteHistoryModel();

        btn_myposotion = (FloatingActionButton) findViewById(R.id.ic_center_map);
        btn_seguir = (FloatingActionButton) findViewById(R.id.ic_seguir);
        btn_expand= (FloatingActionButton) findViewById(R.id.ic_window_complete);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(11);
        map.setBuiltInZoomControls(false);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), map);
        mLocationOverlay.enableMyLocation();
        //mLocationOverlay.enableFollowLocation();

        map.getOverlays().add(mLocationOverlay);

        /** Datos cabecera **/
        strechModels = SqliteClass.getInstance(context).databasehelp.appStrechSql.getAllStrechesByRoute(sharedPref.getString("id_route","01"));
        Collections.reverse(strechModels);

        //mediaPlayer = MediaPlayer.create(this, R.raw.velocidad);
        //mediaPlayer.start();
        interesPointModels = SqliteClass.getInstance(context).databasehelp.appInteresPoint.getAllInteresPointsByIdStrech(String.valueOf(strechModels.get(strech).getId_strech()));

        tv_vel_max = (TextView) findViewById(R.id.tv_vel_max);
        tv_vel_max_2= (TextView) findViewById(R.id.tv_vel_max_2);
        tv_vel_max.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
        tv_vel_max_2.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
        tv_vel_actual = (TextView) findViewById(R.id.tv_vel_actual);
        tv_vel_actual_2 = (TextView) findViewById(R.id.tv_vel_actual_2);
        tv_vel_actual.setText("0");
        tv_vel_actual_2.setText("0");
        tv_strech_name = (TextView) findViewById(R.id.tv_strech_name);
        tv_strech_name.setText(strechModels.get(strech).getName());

        Location locationInicial = Position.getLastBestLocation(context);
        // versiones con android 6.0 o superior
        if(locationInicial==null){
            Toast.makeText(ctx,"El equipo esta buscando su posicion",Toast.LENGTH_LONG).show();
        }else{
            //if(sharedPref.getString("seguir","no").equals("si")){
                map.getController().animateTo(new GeoPoint(locationInicial));
                map.getController().setCenter(new GeoPoint(locationInicial));
            //}

        }

        /** Mandar a mi posición **/
        btn_myposotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = Position.getLastBestLocation(context);

                if(lastLatitud!=0.0 && lastLongitud!=0.0){
                    map.getController().animateTo(new GeoPoint(location));
                    map.getController().setCenter(new GeoPoint(location));
                }
            }
        });

        btn_seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sharedPref.getString("seguir","no").equals("si")){
                    Toast.makeText(ctx,"Dejar de seguir",Toast.LENGTH_LONG).show();

                    editor.putString("seguir","no");
                    editor.apply();
                } else {
                    Toast.makeText(ctx,"Siguiendo posicion",Toast.LENGTH_LONG).show();
                    editor.putString("seguir","si");
                    editor.apply();
                }

            }
        });

        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnyt_content.getVisibility() == View.VISIBLE){
                    rltyt_content_low.setVisibility(View.VISIBLE);
                    lnyt_content.setVisibility(View.GONE);
                } else {
                    rltyt_content_low.setVisibility(View.GONE);
                    lnyt_content.setVisibility(View.VISIBLE);
                }
            }
        });

        //Permiso
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            actualizarPosicion();
        }else{
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            Toast.makeText(this,"La aplicación necesita usar el GPS",Toast.LENGTH_SHORT).show();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            new routeMapTask().execute(strech);
        }
    }

    public static double minDistance(pair A, pair B, pair E) {

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

    public static final double lat2y(double aLat) {
        return ((1 - Math.log(Math.tan(aLat * Math.PI / 180) + 1 / Math.cos(aLat * Math.PI / 180)) / Math.PI) / 2 * Math.pow(2, 0)) * 256;
    }

    @Override
    protected void onResume() {
        actualizarPosicion();
        context = this;
        map = (MapView) findViewById(R.id.map);

        super.onResume();
    }

    public static final double lon2x(double lon) {
        return (lon + 180f) / 360f * 256f;
    }

    private void actualizarPosicion() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            Marker startMarkersite = new Marker(map);


            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPref = getSharedPreferences("values", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if(location ==null){

                    if(sharedPref.getString("notificacion_gps","no").equals("no")){
                        VozNotification.notificationAudible(textToVoice,"El equipo esta fuera de una zona con señal");
                        editor.putString("notificacion_gps","si");
                        editor.apply();
                        contador=0;
                    }
                }
                else{

                    if(sharedPref.getString("seguir","no").equals("si")){
                        map.getController().animateTo(new GeoPoint(location));
                        map.getController().setCenter(new GeoPoint(location));
                    }

                    if(contador<=3){
                        contador++;
                    }

                    if(contador==4){
                        contador++;
                        double Distancia = 0.0;
                        double Distanciaaux=Double.MAX_VALUE;
                        int cantTramos=strechModels.size();
                        for (int i = 0 ; i<cantTramos;i++){
                            //strech=0;
                            double x1 = lat2y(strechModels.get(i).getOriginLati());
                            double y1 = lon2x(strechModels.get(i).getOriginLong());

                            double x2 = lat2y(strechModels.get(i).getDestinationLati());
                            double y2 = lon2x(strechModels.get(i).getDestinationLong());

                            double x = lat2y(location.getLatitude());
                            double y = lon2x(location.getLongitude());
                            Distancia = minDistance(new pair(x1,y1),new pair(x2,y2),new pair(x,y)) ;

                            if(Distancia<Distanciaaux){
                                Distanciaaux = Distancia;
                                strech =i;
                            }
                        }
                        tv_vel_max.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                        tv_vel_max_2.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                        new routeMapTask().execute(strech);

                    }

                    if(contador==5) {

                        lastLatitud = location.getLatitude();
                        lastLongitud = location.getLongitude();

                        originPoint.setLatitude(strechModels.get(strech).getOriginLati());
                        originPoint.setLongitude(strechModels.get(strech).getOriginLong());
                        destinationPoint.setLatitude(strechModels.get(strech).getDestinationLati());
                        destinationPoint.setLongitude(strechModels.get(strech).getDestinationLong());

                        map.getOverlays().remove(mLocationOverlay);
                        editor.putString("notificacion_gps", "no");
                        editor.apply();
                        SharedPreferences sharedPrefLogin = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);

                        map.getOverlays().remove(startMarkersite);
                        map.invalidate();
                        sitesPoints = new GeoPoint(location.getLatitude(), location.getLongitude());
                        startMarkersite = new Marker(map);
                        startMarkersite.setPosition(sitesPoints);
                        startMarkersite.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_truck, null));
                        startMarkersite.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        map.getOverlays().add(startMarkersite);

                        tv_strech_name.setText(strechModels.get(strech).getName());

                        velMax = strechModels.get(strech).getSpeedLimit();

                        /* Area de puntos interes */
                        if (interesPointModels.size() > 0) {
                            for (int i = 0; i < interesPointModels.size(); i++) {
                                interesPoint.setLongitude(interesPointModels.get(i).getLongitude());
                                interesPoint.setLatitude(interesPointModels.get(i).getLatitude());
                                location.distanceTo(interesPoint);
                                double distanciaPoint = location.distanceTo(interesPoint);
                                if (distanciaPoint < interesPointModels.get(i).getWarning_radius()) {

                                    if (sharedPref.getString("notificado", "no").equals("no")) {
                                        VozNotification.notificationAudible(textToVoice, "Esta cerca de un punto de interes");
                                        editor.putString("notificado", "si");
                                        editor.apply();
                                    }
                                    velMax = interesPointModels.get(i).getSpeed_limit();
                                    tv_vel_max.setText(String.valueOf(interesPointModels.get(i).getSpeed_limit()));
                                    tv_vel_max_2.setText(String.valueOf(interesPointModels.get(i).getSpeed_limit()));
                                    is_point = "si";
                                } else {
                                    velMax = strechModels.get(strech).getSpeedLimit();
                                    tv_vel_max.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                                    tv_vel_max_2.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                                    editor.putString("notificado", "no");
                                    editor.apply();
                                    is_point = "no";
                                }
                            }
                        }

                        //convertir velocidad a KM/H
                        float speedInMs = location.getSpeed();
                        int currentSpeed = (int) Math.round(speedInMs * 3.6);
                        tv_vel_actual.setText(String.valueOf(currentSpeed));
                        tv_vel_actual_2.setText(String.valueOf(currentSpeed));


                        /** Exceso de velocidad **/
                        if (currentSpeed > velMax) {
                            //mediaPlayer.start();
                            if (sharedPref.getString("notificado_velocidad", "no").equals("no")) {
                                startService(new Intent(context, ServicioSonido.class));

                                routeHistoryModel.setDate(Utils.getFecha() + " " + Utils.getHora());
                                routeHistoryModel.setUuid_device(sharedPrefLogin.getString("uuid", ""));
                                routeHistoryModel.setId_strech(String.valueOf(strechModels.get(strech).getName()));
                                routeHistoryModel.setId_route(sharedPref.getString("name_route", ""));// desde Route Adapter
                                routeHistoryModel.setId_category_route(sharedPref.getString("name_category", ""));// desde Route Adapter
                                routeHistoryModel.setVel_strech(String.valueOf(velMax));
                                routeHistoryModel.setVel_falta(String.valueOf(currentSpeed));
                                routeHistoryModel.setIs_point(is_point);

                                SqliteClass.getInstance(ctx).databasehelp.appRouteHistory.addRouteHistory(routeHistoryModel);

                                //VozNotification.notificationAudible(textToVoice, "Esta pasando el limite de velocidad");
                                editor.putString("notificado_velocidad", "si");
                                editor.apply();

                            }

                        } else {
                            //mediaPlayer.stop();
                            stopService(new Intent(context, ServicioSonido.class));
                            editor.putString("notificado_velocidad", "no");
                            editor.apply();
                        }

                        /** Control de distancia **/
                        double distanceFromOriginToCurrent = location.distanceTo(destinationPoint);
                        //Toast.makeText(context, "tramo " +strech , Toast.LENGTH_LONG).show();

                        /** Alerta de tramo **/
                        // calcular la distancia esta proxima a 200 mts
                        if(location.distanceTo(destinationPoint)<ConstValue.RadioMinuimStrechAlert && sharedPref.getString("notificado_tramo","no").equals("no")) {

                            editor.putString("notificado_tramo", "si");
                            editor.apply();
                            VozNotification.notificationAudible(textToVoice, "Esta Proximo a la Ruta " + strechModels.get(strech).getName());
                        }

                        /** cambio de tramo **/
                        if (distanceFromOriginToCurrent < ConstValue.RadioMinuimStrech) {
                            map.getOverlays().clear();
                            strech++;
                            editor.putString("notificado_velocidad", "no");
                            editor.putString("notificado_tramo", "no");
                            editor.apply();
                            if (strech == strechModels.size()) {
                                locManager.removeUpdates(locListener);
                                VozNotification.notificationAudible(textToVoice, "Ah terminado la ruta");

                                new MaterialAlertDialogBuilder(RouteActivity.this)
                                        .setTitle("Salir")
                                        .setMessage("Ah finalizado la ruta entera.")
                                        .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                finish();

                                                Intent i = new Intent(RouteActivity.this, MainActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(i);

                                            }
                                        })
                                        .show();

                            } else {

                                editor.putString("strech", String.valueOf(strech));
                                editor.apply();
                                new routeMapTask().execute(strech);
                                tv_vel_max.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                                tv_vel_max_2.setText(String.valueOf(strechModels.get(strech).getSpeedLimit()));
                            }

                        }
                    }

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locListener);

        } else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locListener);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locManager.removeUpdates(locListener);
    }

    class routeMapTask extends AsyncTask<Integer, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Integer... integers) {
            map.getOverlays().clear();
            is_point="no";
            interesPointModels = SqliteClass.getInstance(context).databasehelp.appInteresPoint.getAllInteresPointsByIdStrech(String.valueOf(strechModels.get(strech).getId_strech()));

            VozNotification.notificationAudible(textToVoice,"Tramo "+strechModels.get(strech).getName());

            GeoPoint pointInteres;
            if(interesPointModels.size()>0){
                for (int i=0;i<interesPointModels.size();i++) {
                    pointInteres = new GeoPoint(interesPointModels.get(i).getLatitude(),interesPointModels.get(i).getLongitude());
                    Marker interPointMarker= new Marker(map);
                    interPointMarker.setPosition(pointInteres); //pone el marker en la posocion
                    interPointMarker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_punto_interes,null));
                    interPointMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    interPointMarker.setTitle(interesPointModels.get(i).getName());
                    map.getOverlays().add(interPointMarker);
                }
            }

            GeoPoint startPoint = new GeoPoint(strechModels.get(integers[0]).getOriginLati(),strechModels.get(integers[0]).getOriginLong());
            Marker startMarker= new Marker(map);
            startMarker.setPosition(startPoint); //pone el marker en la posocion
            startMarker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_origin,null));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);

            GeoPoint endPoint = new GeoPoint(strechModels.get(integers[0]).getDestinationLati(), strechModels.get(integers[0]).getDestinationLong());
            Marker endMarker= new Marker(map);
            endMarker.setPosition(endPoint); //pone el marker en la posocion
            endMarker.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_destino,null));
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(endMarker);

            map.invalidate();
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        if(rltyt_content_low.getVisibility() == View.GONE){
            rltyt_content_low.setVisibility(View.VISIBLE);
            lnyt_content.setVisibility(View.GONE);
        }else {
            CommonDialog.returnDialog(RouteActivity.this,context);
        }
        //super.onBackPressed();
    }

    static class pair {
        double F, S;
        public pair(double F, double S)
        {
            this.F = F;
            this.S = S;
        }
        public pair() {
        }

    }

}

