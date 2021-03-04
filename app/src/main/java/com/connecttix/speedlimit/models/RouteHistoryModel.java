package com.connecttix.speedlimit.models;

public class RouteHistoryModel {
    private int id;
    private String date;
    private String uuid_device;
    private String id_category_route;
    private String id_route;
    private String id_strech;
    private String vel_strech;
    private String vel_falta;// la velocidad con la que exedio la velocidad
    private String is_point;//

    public RouteHistoryModel() {}

    public RouteHistoryModel(int id, String date, String uuid_device, String id_category_route, String id_route, String id_strech, String vel_strech, String vel_falta) {
        this.id = id;
        this.date = date;
        this.uuid_device = uuid_device;
        this.id_category_route = id_category_route;
        this.id_route = id_route;
        this.id_strech = id_strech;
        this.vel_strech = vel_strech;
        this.vel_falta = vel_falta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUuid_device() {
        return uuid_device;
    }

    public void setUuid_device(String uuid_device) {
        this.uuid_device = uuid_device;
    }

    public String getId_category_route() {
        return id_category_route;
    }

    public void setId_category_route(String id_category_route) {
        this.id_category_route = id_category_route;
    }

    public String getId_route() {
        return id_route;
    }

    public void setId_route(String id_route) {
        this.id_route = id_route;
    }

    public String getId_strech() {
        return id_strech;
    }

    public void setId_strech(String id_strech) {
        this.id_strech = id_strech;
    }

    public String getVel_strech() {
        return vel_strech;
    }

    public void setVel_strech(String vel_strech) {
        this.vel_strech = vel_strech;
    }

    public String getVel_falta() {
        return vel_falta;
    }

    public void setVel_falta(String vel_falta) {
        this.vel_falta = vel_falta;
    }

    public String getIs_point() {
        return is_point;
    }

    public void setIs_point(String is_point) {
        this.is_point = is_point;
    }
}
