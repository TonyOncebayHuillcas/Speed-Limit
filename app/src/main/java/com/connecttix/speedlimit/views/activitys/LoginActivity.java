package com.connecttix.speedlimit.views.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.connecttix.speedlimit.R;
import com.connecttix.speedlimit.config.ConstValue;
import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.models.InteresPointModel;
import com.connecttix.speedlimit.models.RouteModel;
import com.connecttix.speedlimit.models.StrechModel;
import com.connecttix.speedlimit.network.Protocol;
import com.connecttix.speedlimit.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText et_usuario,et_password;
    Button btn_login;

    Context context;
    Protocol protocol;

    ProgressDialog dialog;
    ProgressBar progressBar;
    ConnectionDetector cd;
    CategoryModel categoryModel;
    RouteModel routeModel;
    StrechModel strechModel;
    InteresPointModel interesPointModel;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        context=getApplicationContext();
        cd=new ConnectionDetector(context);
        protocol= new Protocol();

        /** Permiso para ubicación **/
        int accessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int accesswite = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if(accessFinePermission!= PackageManager.PERMISSION_GRANTED &&
                accessCoarsePermission!=PackageManager.PERMISSION_GRANTED &&
                accessLocationPermission!=PackageManager.PERMISSION_GRANTED &&
                accesswite!=PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);}

        /** Mantener sesión abierta  **/
        SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        String logueado=sharedPref.getString("logueado","inactive");

        if(logueado.equals("active")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

        /** Datos de interfas **/
        progressBar = (ProgressBar) findViewById(R.id.loading);
        et_usuario = (EditText) findViewById(R.id.et_username);//et_usuario.setText("lasbambas");
        et_password = (EditText) findViewById(R.id.et_password);//et_password.setText("@Josepe20@");

        btn_login = (Button) findViewById(R.id.bt_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_usuario.getText().length()>0 && et_password.getText().length()>0){
                    if (cd.isConnectingToInternet()) {
                        new LoginTask().execute(true);
                    }else{
                        Toast.makeText(context,"Su dispositivo no cuenta con conexión a internet.",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context,"No ha ingresado un usuario o la contraseña",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class LoginTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            //dialog = ProgressDialog.show(LoginActivity.this, "", "Cargando", true);
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(Boolean... booleans) {
            String usuario=et_usuario.getText().toString();
            String password=et_password.getText().toString();
            try {
                JSONObject jsonLogin = new JSONObject();
                jsonLogin.put("username",usuario);
                jsonLogin.put("password",password);

                JSONObject result=protocol.postJsonLogin(ConstValue.AUTH_LOGIN,jsonLogin);

                if(result.getString("token").equals(null)){
                    Toast.makeText(context," Error al recuperar datos del portal",Toast.LENGTH_LONG).show();
                } else {
                    Log.i("Token",result.getString("token"));
                    /** Guardando token **/
                    @SuppressLint("HardwareIds") String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("token", result.getString("token"));
                    editor.putString("password", password);
                    editor.putString("uuid", uuid);
                    String token= result.getString("token");
                    editor.apply();

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
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //dialog.dismiss();
            progressBar.setVisibility(View.GONE);

            /** Activar el estado de sessión main **/
            SharedPreferences sharedPref = getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("logueado", "active");
            editor.apply();

            /** Redireccionar a clase main **/
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            super.onPostExecute(s);
        }
    }

}
