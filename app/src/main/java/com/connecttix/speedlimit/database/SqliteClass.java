package com.connecttix.speedlimit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.connecttix.speedlimit.models.CategoryModel;
import com.connecttix.speedlimit.models.InteresPointModel;
import com.connecttix.speedlimit.models.RouteHistoryModel;
import com.connecttix.speedlimit.models.RouteModel;
import com.connecttix.speedlimit.models.StrechModel;

import java.io.File;
import java.util.ArrayList;

public class SqliteClass {

    /* @TABLE_APP_CATEGORY */
    public static final String TABLE_APP_CATEGORY = "app_category";
    public static final String KEY_CATID= "cat_id";
    public static final String KEY_CATIDCAT= "cat_id_category";
    public static final String KEY_CATNAM= "cat_name";
    public static final String KEY_CATDES= "cat_description";

    /* @TABLE_APP_ROUTE */
    public static final String TABLE_APP_ROUTE = "app_route";
    public static final String KEY_ROUID= "rou_id";
    public static final String KEY_ROUIDROU= "rou_id_route";
    public static final String KEY_ROUIDCAT= "rou_id_category";
    public static final String KEY_ROUNAM= "rou_name";
    public static final String KEY_ROUORILATI= "rou_origin_lati";
    public static final String KEY_ROUORILONG= "rou_origin_long";
    public static final String KEY_ROUDESLATI= "rou_destination_lati";
    public static final String KEY_ROUDESLONG= "rou_destination_long";

    /* @TABLE_APP_STRECH */
    public static final String TABLE_APP_STRECH = "app_strech";
    public static final String KEY_STRID= "stre_id";
    public static final String KEY_STRIDSTR= "stre_id_strech";
    public static final String KEY_STRIDROU= "stre_id_route";
    public static final String KEY_STRNAM= "stre_name";
    public static final String KEY_STRORILATI= "stre_origin_lati";
    public static final String KEY_STRORILONG= "stre_origin_long";
    public static final String KEY_STRDESLATI= "stre_destination_lati";
    public static final String KEY_STRDESLONG= "stre_destination_long";
    public static final String KEY_STRDIST= "stre_distance";
    public static final String KEY_STRSPELIM= "stre_speed_limit";

    /* @TABLE_APP_ROUTE_HISTORY */
    public static final String TABLE_APP_ROUTE_HISTORY = "app_route_history";
    public static final String KEY_ROUHISID= "rout_hist_id";
    public static final String KEY_ROUHISDAT= "rout_hist_date";
    public static final String KEY_ROUHISUUIDEV= "rout_hist_uuid_device";
    public static final String KEY_ROUHISIDCATROU= "rout_hist_id_ctegory_route";
    public static final String KEY_ROUHISIDROU= "rout_hist_id_route";
    public static final String KEY_ROUHISIDSTR= "rout_hist_id_strech";
    public static final String KEY_ROUHISVELSTR= "rout_vel_strrech";
    public static final String KEY_ROUHISVELFAL= "rout_vel_falta";
    public static final String KEY_ROUHISISPOI= "rout_vel_is_point"; // si - no

    /* @TABLE_APP_INTERES_POINT */
    public static final String TABLE_APP_INTERES_POINT = "app_interes_point";
    public static final String KEY_INTPOIID = "int_poi_id";//sql
    public static final String KEY_INTPOIIDINT = "int_poi_id_point_interes";//api
    public static final String KEY_INTPOIIDSTR = "int_poi_id_strech";//api
    public static final String KEY_INTPOINAM = "int_poi_name";//api
    public static final String KEY_INTPOILATI = "int_poi_latitude";//api
    public static final String KEY_INTPOILONGI = "int_poi_longitude";//api
    public static final String KEY_INTPOIRAD = "int_poi_radius";//api
    public static final String KEY_INTPOINUM = "int_poi_number";//api
    public static final String KEY_INTPOITYP = "int_poi_type";//api
    public static final String KEY_INTPOISPELIM = "int_poi_speedLimit";//api
    public static final String KEY_INTPOIWARRAD= "int_poi_warning_radius";//api


    public DatabaseHelper databasehelp;
    private static SqliteClass SqliteInstance = null;

    private SqliteClass(Context context) {
        databasehelp = new DatabaseHelper(context);
    }

    public static SqliteClass getInstance(Context context) {
        if (SqliteInstance == null) {
            SqliteInstance = new SqliteClass(context);
        }
        return SqliteInstance;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        public Context context;
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "app_speed_limit.db";

        public AppCategorySql appCategorySql;
        public AppRouteSql appRouteSql;
        public AppStrechSql appStrechSql;
        public AppRouteHistory appRouteHistory;
        public AppInteresPoint appInteresPoint;


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;

            appCategorySql = new AppCategorySql();
            appRouteSql = new AppRouteSql();
            appStrechSql = new AppStrechSql();
            appRouteHistory= new AppRouteHistory();
            appInteresPoint= new AppInteresPoint();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /* @TABLE_CATEGORY */
            String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_APP_CATEGORY + "("
                    + KEY_CATID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CATIDCAT + " TEXT," + KEY_CATNAM + " TEXT," + KEY_CATDES +  " TEXT )";

            /* @TABLE_ROUTE */
            String CREATE_TABLE_ROUTE = "CREATE TABLE " + TABLE_APP_ROUTE + "("
                    + KEY_ROUID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ROUIDROU + " TEXT," + KEY_ROUIDCAT + " TEXT," + KEY_ROUNAM +  " TEXT,"
                    + KEY_ROUORILATI + " TEXT," + KEY_ROUORILONG + " TEXT," + KEY_ROUDESLATI + " TEXT," +KEY_ROUDESLONG + " TEXT )";

            /* @TABLE_STRECH */
            String CREATE_TABLE_STRECH = "CREATE TABLE " + TABLE_APP_STRECH + "("
                    + KEY_STRID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STRIDSTR + " TEXT," + KEY_STRIDROU + " TEXT," + KEY_STRNAM + " TEXT,"
                    + KEY_STRORILATI + " TEXT," + KEY_STRORILONG + " TEXT," + KEY_STRDESLATI + " TEXT," + KEY_STRDESLONG + " TEXT,"
                    + KEY_STRDIST + " TEXT," + KEY_STRSPELIM + " TEXT )";

            /* @TABLE_ROUTE_HISTORY */
            String CREATE_TABLE_ROUTE_HISTORY = "CREATE TABLE " + TABLE_APP_ROUTE_HISTORY + "("
                    + KEY_ROUHISID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ROUHISDAT + " TEXT," + KEY_ROUHISUUIDEV + " TEXT,"
                    + KEY_ROUHISVELSTR + " TEXT,"  + KEY_ROUHISVELFAL + " TEXT," + KEY_ROUHISISPOI + " TEXT,"
                    + KEY_ROUHISIDCATROU + " TEXT," + KEY_ROUHISIDSTR + " TEXT," + KEY_ROUHISIDROU + " TEXT )";

            /* @TABLE_INTERES_POINT */
            String CREATE_TABLE_INTERES_POINT =  "CREATE TABLE " + TABLE_APP_INTERES_POINT + "("
                    + KEY_INTPOIID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_INTPOIIDINT + " TEXT," +KEY_INTPOIIDSTR + " TEXT,"
                    + KEY_INTPOINAM + " TEXT," + KEY_INTPOILATI + " TEXT," + KEY_INTPOILONGI + " TEXT,"
                    + KEY_INTPOIRAD + " TEXT," + KEY_INTPOINUM + " TEXT," + KEY_INTPOITYP + " TEXT,"
                    + KEY_INTPOISPELIM + " TEXT," + KEY_INTPOIWARRAD + " TEXT )";

            /* @EXECSQL_CREATE */
            db.execSQL(CREATE_TABLE_CATEGORY);
            db.execSQL(CREATE_TABLE_ROUTE);
            db.execSQL(CREATE_TABLE_STRECH);
            db.execSQL(CREATE_TABLE_ROUTE_HISTORY);
            db.execSQL(CREATE_TABLE_INTERES_POINT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            /* @EXECSQL_DROP */
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_ROUTE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_STRECH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_ROUTE_HISTORY);

            onCreate(db);

        }

        public boolean checkDataBase(){
            File dbFile = new File(context.getDatabasePath(DATABASE_NAME).toString());
            return dbFile.exists();
        }
        public void deleteDataBase(){
            context.deleteDatabase(DATABASE_NAME);
        }

        /* @CLASS_CATEGORYSQL */
        public class AppCategorySql{
            public AppCategorySql() {}

            public void deleteCategoryTable(){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_CATEGORY,null,null);
                db.close();
            }

            public void addCategory(CategoryModel model){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_CATNAM, model.getName());
                values.put(KEY_CATIDCAT, model.getId_category());
                values.put(KEY_CATDES, model.getDescription());
                db.insert(TABLE_APP_CATEGORY, null, values);
                db.close();
            }

            public ArrayList<CategoryModel> getAllLocations() {
                ArrayList<CategoryModel> models = new ArrayList<CategoryModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CATEGORY;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        CategoryModel model = new CategoryModel();
                        model.setId_category(cursor.getInt(cursor.getColumnIndex(KEY_CATIDCAT)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_CATNAM)));
                        model.setDescription(cursor.getString(cursor.getColumnIndex(KEY_CATDES)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

            public CategoryModel getCategoryById(String idCategory){
                CategoryModel model = new CategoryModel();
                String selectQuery = "SELECT * FROM " + TABLE_APP_CATEGORY + " WHERE " + KEY_CATIDCAT + " ='" + idCategory + "'" ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    model.setId_category(cursor.getInt(cursor.getColumnIndex(KEY_CATIDCAT)));
                    model.setName(cursor.getString(cursor.getColumnIndex(KEY_CATNAM)));
                    model.setDescription(cursor.getString(cursor.getColumnIndex(KEY_CATDES)));
                }
                return  model;
            }

        }

        /* @CLASS_ROUTESQL */
        public class AppRouteSql{
            public AppRouteSql() {}

            public void deleteRouteTable(){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_ROUTE,null,null);
                db.close();
            }

            public void addCRoute(RouteModel model){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_ROUIDROU, model.getId_route());
                values.put(KEY_ROUIDCAT, model.getId_fk_category());
                values.put(KEY_ROUNAM, model.getName());
                values.put(KEY_ROUORILATI, model.getOriginLati());
                values.put(KEY_ROUORILONG, model.getOriginLong());
                values.put(KEY_ROUDESLATI, model.getDetinationLati());
                values.put(KEY_ROUDESLONG, model.getDestinationLong());
                db.insert(TABLE_APP_ROUTE, null, values);
                db.close();
            }

            public ArrayList<RouteModel> getAllRoutes(){
                ArrayList<RouteModel> models = new ArrayList<RouteModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_ROUTE ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        RouteModel model = new RouteModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROUID)));
                        model.setId_route(cursor.getInt(cursor.getColumnIndex(KEY_ROUIDROU)));
                        model.setId_fk_category(cursor.getInt(cursor.getColumnIndex(KEY_ROUIDCAT)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ROUNAM)));
                        model.setOriginLati(cursor.getDouble(cursor.getColumnIndex(KEY_ROUORILATI)));
                        model.setOriginLong(cursor.getDouble(cursor.getColumnIndex(KEY_ROUORILONG)));
                        model.setDetinationLati(cursor.getDouble(cursor.getColumnIndex(KEY_ROUDESLATI)));
                        model.setDestinationLong(cursor.getDouble(cursor.getColumnIndex(KEY_ROUDESLONG)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

            public ArrayList<RouteModel> getAllRoutesByCategory(String idCategory) {
                ArrayList<RouteModel> models = new ArrayList<RouteModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_ROUTE + " WHERE " + KEY_ROUIDCAT + " ='" + idCategory + "'" ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        RouteModel model = new RouteModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROUID)));
                        model.setId_route(cursor.getInt(cursor.getColumnIndex(KEY_ROUIDROU)));
                        model.setId_fk_category(cursor.getInt(cursor.getColumnIndex(KEY_ROUIDCAT)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_ROUNAM)));
                        model.setOriginLati(cursor.getDouble(cursor.getColumnIndex(KEY_ROUORILATI)));
                        model.setOriginLong(cursor.getDouble(cursor.getColumnIndex(KEY_ROUORILONG)));
                        model.setDetinationLati(cursor.getDouble(cursor.getColumnIndex(KEY_ROUDESLATI)));
                        model.setDestinationLong(cursor.getDouble(cursor.getColumnIndex(KEY_ROUDESLONG)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

        }

        /* @CLASS_STRECHSQL */
        public class AppStrechSql{
            public AppStrechSql() {}

            public void deleteStrechTable(){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_STRECH,null,null);
                db.close();
            }

            public void vaciarTabla(){
                String selectQuery = "DELETE FROM " + TABLE_APP_STRECH;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.execSQL(selectQuery);
                db.close();

            }

            public void addStrech(StrechModel model){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_STRIDSTR, model.getId_strech());
                values.put(KEY_STRIDROU, model.getId_fk_route());
                values.put(KEY_STRNAM, model.getName());
                values.put(KEY_STRORILATI, model.getOriginLati());
                values.put(KEY_STRORILONG, model.getOriginLong());
                values.put(KEY_STRDESLATI, model.getDestinationLati());
                values.put(KEY_STRDESLONG, model.getDestinationLong());
                values.put(KEY_STRDIST, model.getDistance());
                values.put(KEY_STRSPELIM, model.getSpeedLimit());
                db.insert(TABLE_APP_STRECH, null, values);
                db.close();
            }

            public ArrayList<StrechModel> getAllStrechesByRoute(String idRoute) {
                ArrayList<StrechModel> models = new ArrayList<StrechModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_STRECH + " WHERE " + KEY_STRIDROU + " ='" + idRoute + "'" ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        StrechModel model = new StrechModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_STRID)));
                        model.setId_strech(cursor.getInt(cursor.getColumnIndex(KEY_STRIDSTR)));
                        model.setId_fk_route(cursor.getInt(cursor.getColumnIndex(KEY_STRIDROU)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_STRNAM)));
                        model.setOriginLati(cursor.getDouble(cursor.getColumnIndex(KEY_STRORILATI)));
                        model.setOriginLong(cursor.getDouble(cursor.getColumnIndex(KEY_STRORILONG)));
                        model.setDestinationLati(cursor.getDouble(cursor.getColumnIndex(KEY_STRDESLATI)));
                        model.setDestinationLong(cursor.getDouble(cursor.getColumnIndex(KEY_STRDESLONG)));
                        model.setDistance(cursor.getDouble(cursor.getColumnIndex(KEY_STRDIST)));
                        model.setSpeedLimit(cursor.getDouble(cursor.getColumnIndex(KEY_STRSPELIM)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }


            public ArrayList<StrechModel> getAllStreches() {
                ArrayList<StrechModel> models = new ArrayList<StrechModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_STRECH ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        StrechModel model = new StrechModel();
                        model.setId_strech(cursor.getInt(cursor.getColumnIndex(KEY_STRID)));
                        model.setId_fk_route(cursor.getInt(cursor.getColumnIndex(KEY_STRIDROU)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_STRNAM)));
                        model.setOriginLati(cursor.getDouble(cursor.getColumnIndex(KEY_STRORILATI)));
                        model.setOriginLong(cursor.getDouble(cursor.getColumnIndex(KEY_STRORILONG)));
                        model.setDestinationLati(cursor.getDouble(cursor.getColumnIndex(KEY_STRDESLATI)));
                        model.setDestinationLong(cursor.getDouble(cursor.getColumnIndex(KEY_STRDESLONG)));
                        model.setDistance(cursor.getDouble(cursor.getColumnIndex(KEY_STRDIST)));
                        model.setSpeedLimit(cursor.getDouble(cursor.getColumnIndex(KEY_STRSPELIM)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }
        }

        /* @CLASS_ROUTE_HISTORY */
        public class AppRouteHistory{
            public AppRouteHistory() {}

            public void deleteRouteHistoryTable(){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_ROUTE_HISTORY,null,null);
                db.close();
            }

            public void addRouteHistory(RouteHistoryModel model){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                //values.put(KEY_ROUHISID, model.getId());
                values.put(KEY_ROUHISDAT, model.getDate());
                values.put(KEY_ROUHISUUIDEV, model.getUuid_device());
                values.put(KEY_ROUHISIDCATROU, model.getId_category_route());
                values.put(KEY_ROUHISIDROU, model.getId_route());
                values.put(KEY_ROUHISIDSTR, model.getId_strech());
                values.put(KEY_ROUHISVELSTR, model.getVel_strech());
                values.put(KEY_ROUHISVELFAL, model.getVel_falta());
                values.put(KEY_ROUHISISPOI, model.getIs_point());
                db.insert(TABLE_APP_ROUTE_HISTORY, null, values);
                db.close();
            }

            public ArrayList<RouteHistoryModel> getAllRouteHistorys() {
                ArrayList<RouteHistoryModel> models = new ArrayList<RouteHistoryModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_ROUTE_HISTORY;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        RouteHistoryModel model = new RouteHistoryModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROUHISID)));
                        model.setDate(cursor.getString(cursor.getColumnIndex(KEY_ROUHISDAT)));
                        model.setUuid_device(cursor.getString(cursor.getColumnIndex(KEY_ROUHISUUIDEV)));
                        model.setId_category_route(cursor.getString(cursor.getColumnIndex(KEY_ROUHISIDCATROU)));
                        model.setId_route(cursor.getString(cursor.getColumnIndex(KEY_ROUHISIDROU)));
                        model.setId_strech(cursor.getString(cursor.getColumnIndex(KEY_ROUHISIDSTR)));
                        model.setVel_strech(cursor.getString(cursor.getColumnIndex(KEY_ROUHISVELSTR)));
                        model.setVel_falta(cursor.getString(cursor.getColumnIndex(KEY_ROUHISVELFAL)));
                        model.setIs_point(cursor.getString(cursor.getColumnIndex(KEY_ROUHISISPOI)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

        }

        /* @CLASS_ROUTE_HISTORY */
        public class AppInteresPoint{
            public AppInteresPoint() {}

            public void deleteInteresPointTable(){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                db.delete(TABLE_APP_INTERES_POINT,null,null);
                db.close();
            }

            public void addInteresPoint(InteresPointModel model){
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(KEY_INTPOIIDINT, model.getId_point_interes());
                values.put(KEY_INTPOIIDSTR, model.getId_strech());
                values.put(KEY_INTPOINAM, model.getName());
                values.put(KEY_INTPOILATI, model.getLatitude());
                values.put(KEY_INTPOILONGI, model.getLongitude());
                values.put(KEY_INTPOIRAD, model.getRadius());
                values.put(KEY_INTPOINUM, model.getNumber());
                values.put(KEY_INTPOITYP, model.getType());
                values.put(KEY_INTPOISPELIM, model.getSpeed_limit());
                values.put(KEY_INTPOIWARRAD, model.getWarning_radius());
                db.insert(TABLE_APP_INTERES_POINT, null, values);
                db.close();
            }

            public ArrayList<InteresPointModel> getAllInteresPoints() {
                ArrayList<InteresPointModel> models = new ArrayList<InteresPointModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_INTERES_POINT ;
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        InteresPointModel model = new InteresPointModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIID)));
                        model.setId_point_interes(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIIDINT)));
                        model.setId_strech(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIIDSTR)));
                        model.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOILATI)));
                        model.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOILONGI)));
                        model.setRadius(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOIRAD)));
                        model.setNumber(cursor.getInt(cursor.getColumnIndex(KEY_INTPOINUM)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_INTPOINAM)));
                        model.setType(cursor.getInt(cursor.getColumnIndex(KEY_INTPOITYP)));
                        model.setSpeed_limit(cursor.getInt(cursor.getColumnIndex(KEY_INTPOISPELIM)));
                        model.setWarning_radius(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOIWARRAD)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

            public ArrayList<InteresPointModel> getAllInteresPointsByIdStrech(String id) {
                ArrayList<InteresPointModel> models = new ArrayList<InteresPointModel>();
                String selectQuery = "SELECT * FROM " + TABLE_APP_INTERES_POINT + " WHERE " + KEY_INTPOIIDSTR + " ='" + id + "'";
                SQLiteDatabase db = databasehelp.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do{
                        InteresPointModel model = new InteresPointModel();
                        model.setId(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIID)));
                        model.setId_point_interes(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIIDINT)));
                        model.setId_strech(cursor.getInt(cursor.getColumnIndex(KEY_INTPOIIDSTR)));
                        model.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOILATI)));
                        model.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOILONGI)));
                        model.setRadius(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOIRAD)));
                        model.setNumber(cursor.getInt(cursor.getColumnIndex(KEY_INTPOINUM)));
                        model.setType(cursor.getInt(cursor.getColumnIndex(KEY_INTPOITYP)));
                        model.setName(cursor.getString(cursor.getColumnIndex(KEY_INTPOINAM)));
                        model.setSpeed_limit(cursor.getInt(cursor.getColumnIndex(KEY_INTPOISPELIM)));
                        model.setWarning_radius(cursor.getDouble(cursor.getColumnIndex(KEY_INTPOIWARRAD)));
                        models.add(model);
                    } while (cursor.moveToNext());

                }
                cursor.close();
                db.close();
                return models;
            }

        }

    }

}
