package com.connecttix.speedlimit.views.activitys;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.config.ConstValue;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.models.InteresPointModel;
import com.connecttix.speedlimit.models.RouteHistoryModel;
import com.connecttix.speedlimit.models.RouteModel;
import com.connecttix.speedlimit.models.StrechModel;
import com.connecttix.speedlimit.network.Protocol;
import com.connecttix.speedlimit.utils.ConnectionDetector;
import com.connecttix.speedlimit.utils.LocationClass;
import com.connecttix.speedlimit.utils.Utils;
import com.connecttix.speedlimit.views.dialog.CommonDialog;
import com.connecttix.speedlimit.views.fragments.CategoryRouteFragment;
import com.connecttix.speedlimit.views.fragments.HistoryRouteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context context;
    ConnectionDetector connectionDetector;
    ProgressDialog dialog;
    CategoryModel categoryModel;
    RouteModel routeModel;
    StrechModel strechModel;
    InteresPointModel interesPointModel;
    Protocol protocol;
    public String url;
    LayoutInflater inflater;
    public ArrayList<RouteHistoryModel>routeHistoryModels;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        protocol= new Protocol();
        context=this;
        connectionDetector = new ConnectionDetector(context);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new CategoryRouteFragment()).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragemte = null;

                switch (item.getItemId()){
                    case R.id.navigation_history_route:
                        selectFragemte =  new HistoryRouteFragment();

                        break;
                    case R.id.navigation_route_category:
                        selectFragemte =  new CategoryRouteFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,selectFragemte).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_update){
            if(connectionDetector.isConnectingToInternet()){
                new updateTask().execute(true);
            } else {
                Toast.makeText(context,"El equipo no tiene conexión a internet",Toast.LENGTH_LONG).show();
            }
        }
        else if (id == R.id.action_report) {
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            SharedPreferences sharedPrefVal = getSharedPreferences("values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefVal.edit();
            editor.putString("notificado_velocidad","no");
            editor.apply();

            View v = inflater.inflate(R.layout.alert_confirm_pass, null);
            EditText et_password = (EditText) v.findViewById(R.id.et_password_confirm);
            Button btnFire = (Button)v.findViewById(R.id.btn_ok_confrm);
            Button btnCancel = (Button)v.findViewById(R.id.btn_cancel_confirm);
            builder.setView(v);
            alertDialog = builder.create();
            // Add action buttons
            btnFire.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(et_password.getText().toString().equals(sharedPref.getString("password","@Josepe20@"))){
                                Utils.generarReporte(context);
                                alertDialog.dismiss();


                            }else {
                                Toast.makeText(context,"Contraseña invalida",Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();

                            }

                        }
                    }
            );
            btnCancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    }
            );
            alertDialog.show();
        }
        else if (id == R.id.action_logout) {
            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            SharedPreferences sharedPref = context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            SharedPreferences sharedPrefVal = getSharedPreferences("values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefVal.edit();
            editor.putString("notificado_velocidad","no");
            editor.apply();

            View v = inflater.inflate(R.layout.alert_confirm_pass, null);
            EditText et_password = (EditText) v.findViewById(R.id.et_password_confirm);
            Button btnFire = (Button)v.findViewById(R.id.btn_ok_confrm);
            Button btnCancel = (Button)v.findViewById(R.id.btn_cancel_confirm);
            builder.setView(v);
            alertDialog = builder.create();
            // Add action buttons
            btnFire.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(et_password.getText().toString().equals(sharedPref.getString("password","@Josepe20@"))){

                                alertDialog.dismiss();

                                CommonDialog.showLogoutDialog(MainActivity.this,context);

                            }else {
                                Toast.makeText(context,"Contraseña invalida",Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();

                            }

                        }
                    }
            );
            btnCancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    }
            );
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(context,"No puede salir de la aplicación",Toast.LENGTH_LONG).show();
        //super.onBackPressed();
    }

    class updateTask extends AsyncTask<Boolean, Void, String> {
        @Override
        protected void onPreExecute() {
            SqliteClass.getInstance(context).databasehelp.appStrechSql.deleteStrechTable();
            SqliteClass.getInstance(context).databasehelp.appRouteSql.deleteRouteTable();
            SqliteClass.getInstance(context).databasehelp.appCategorySql.deleteCategoryTable();
            SqliteClass.getInstance(context).databasehelp.appInteresPoint.deleteInteresPointTable();

            dialog = ProgressDialog.show(context, "", getString(R.string.action_loading), true);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            finish();

            startActivity(intent);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Boolean... booleans) {
            SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
            String token = sharedPref.getString("token","");
            try {
                /** Guardando Categorias **/
                url = ConstValue.GET_DATA;
                JSONArray jsonarray = new JSONArray(protocol.getJson(url,token));
                JSONObject jsonObject= jsonarray.getJSONObject(0);
                JSONArray jsonArrayCategory = (JSONArray) jsonObject.get("categories");
                for(int i=0;i<jsonArrayCategory.length();i++){
                    JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(i);

                    categoryModel = new CategoryModel();
                    categoryModel.setId_category(jsonObjectCategory.getInt("id"));
                    categoryModel.setName(jsonObjectCategory.getString("name"));
                    categoryModel.setDescription(jsonObjectCategory.getString("description"));

                    SqliteClass.getInstance(context).databasehelp.appCategorySql.addCategory(categoryModel);

                    /** Guardando Route **/
                    JSONArray jsonArrayRoute = (JSONArray) jsonObjectCategory.get("routes");
                    for (int j=0;j<jsonArrayRoute.length();j++) {
                        JSONObject jsonObjectRoute = jsonArrayRoute.getJSONObject(j);
                        routeModel = new RouteModel();
                        routeModel.setId_route(jsonObjectRoute.getInt("routeId"));
                        routeModel.setId_fk_category(jsonObjectCategory.getInt("id"));
                        routeModel.setName(jsonObjectRoute.getString("routeName"));
                        routeModel.setOriginLati(jsonObjectRoute.getDouble("originLatitude"));
                        routeModel.setOriginLong(jsonObjectRoute.getDouble("originLongitude"));
                        routeModel.setDetinationLati(jsonObjectRoute.getDouble("destinationLatitude"));
                        routeModel.setDestinationLong(jsonObjectRoute.getDouble("destinationLongitude"));

                        SqliteClass.getInstance(context).databasehelp.appRouteSql.addCRoute(routeModel);

                        /** Guardando Strech **/
                        JSONArray jsonArrayStrech = (JSONArray) jsonObjectRoute.get("stretch");
                        for (int k=0;k<jsonArrayStrech.length();k++) {
                            JSONObject jsonObjectStrech = jsonArrayStrech.getJSONObject(k);
                            strechModel = new StrechModel();
                            strechModel.setId_strech(jsonObjectStrech.getInt("id"));
                            strechModel.setId_fk_route(jsonObjectRoute.getInt("routeId"));
                            strechModel.setName(jsonObjectStrech.getString("stretchName"));
                            strechModel.setOriginLati(jsonObjectStrech.getDouble("latitudeOrigin"));
                            strechModel.setOriginLong(jsonObjectStrech.getDouble("longitudeOrigin"));
                            strechModel.setDestinationLati(jsonObjectStrech.getDouble("latitudeDestination"));
                            strechModel.setDestinationLong(jsonObjectStrech.getDouble("longitudeDestination"));
                            strechModel.setDistance(jsonObjectStrech.getDouble("distance"));

                            JSONArray jsonArraySpeed = (JSONArray) jsonObjectStrech.get("Speed");
                            JSONObject jsonObjectSpeed = jsonArraySpeed.getJSONObject(0);
                            strechModel.setSpeedLimit(jsonObjectSpeed.getDouble("speedLimit"));

                            JSONArray jsonArrayInteresPoints = (JSONArray) jsonObjectStrech.get("interestPoints");
                            if(jsonArrayInteresPoints.length()>0){
                                strechModel.setExistPoint("si");
                            }
                            else {
                                strechModel.setExistPoint("no");

                            }
                            SqliteClass.getInstance(context).databasehelp.appStrechSql.addStrech(strechModel);

                            for(int n=0;n<jsonArrayInteresPoints.length();n++){
                                JSONObject jsonObjectInteresPoint = jsonArrayInteresPoints.getJSONObject(n);
                                interesPointModel= new InteresPointModel();
                                interesPointModel.setId_point_interes(jsonObjectInteresPoint.getInt("id"));
                                interesPointModel.setName(jsonObjectInteresPoint.getString("name"));
                                interesPointModel.setId_strech(jsonObjectStrech.getInt("id"));
                                interesPointModel.setLatitude(jsonObjectInteresPoint.getDouble("latitude"));
                                interesPointModel.setLongitude(jsonObjectInteresPoint.getDouble("longitude"));
                                interesPointModel.setRadius(jsonObjectInteresPoint.getDouble("radius"));
                                interesPointModel.setNumber(jsonObjectInteresPoint.getInt("number"));
                                interesPointModel.setType(jsonObjectInteresPoint.getInt("type"));
                                interesPointModel.setSpeed_limit(jsonObjectInteresPoint.getInt("speedLimit"));
                                interesPointModel.setWarning_radius(jsonObjectInteresPoint.getDouble("warning_radius"));

                                SqliteClass.getInstance(context).databasehelp.appInteresPoint.addInteresPoint(interesPointModel);
                            }

                        }

                    }

                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
