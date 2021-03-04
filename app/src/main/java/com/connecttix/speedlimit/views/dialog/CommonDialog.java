package com.connecttix.speedlimit.views.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.connecttix.speedlimit.database.SqliteClass;
import com.connecttix.speedlimit.views.activitys.LoginActivity;
import com.connecttix.speedlimit.views.activitys.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CommonDialog {

    public static void showLogoutDialog(Activity activity, Context context){
        new MaterialAlertDialogBuilder(activity)
                .setTitle("Salir")
                .setMessage("¿Está seguro de salir de la aplicación? Todos los datos no enviados serán eliminados.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SqliteClass.getInstance(context).databasehelp.appCategorySql.deleteCategoryTable();
                        SqliteClass.getInstance(context).databasehelp.appRouteSql.deleteRouteTable();
                        SqliteClass.getInstance(context).databasehelp.appStrechSql.deleteStrechTable();
                        SqliteClass.getInstance(context).databasehelp.appInteresPoint.deleteInteresPointTable();
                        SqliteClass.getInstance(context).databasehelp.appRouteHistory.deleteRouteHistoryTable();

                        SharedPreferences sharedPref = context.getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("logueado", "inactive");
                        editor.apply();
                        activity.finish();
                        Intent i = new Intent(activity, LoginActivity.class);
                        activity.startActivity(i);
                    }
                })
                .show();
    }

    public static void returnDialog(Activity activity, Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        new MaterialAlertDialogBuilder(activity)
                .setTitle("Salir")
                .setMessage("¿Está seguro de salir de la ruta? Aun no te terminado todos los tramos")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("strech","0");
                        editor.apply();
                        Intent i = new Intent(activity, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        activity.startActivity(i);
                        activity.finish();
                    }
                })
                .show();
    }
}
