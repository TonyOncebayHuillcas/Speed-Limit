package com.connecttix.speedlimit.models;

public class InteresPointModel {
    private int id;
    private int id_point_interes;
    private String name;
    private int id_strech;
    private double latitude;
    private double longitude;
    private double radius;
    private int number;
    private int type;
    private int speed_limit;
    private double warning_radius;

    public InteresPointModel(){}

    public InteresPointModel(int id, int id_point_interes, String name, double latitude, double longitude, double radius, int number, int type, int speed_limit, double warning_radius) {
        this.id = id;
        this.id_point_interes = id_point_interes;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.number = number;
        this.type = type;
        this.speed_limit = speed_limit;
        this.warning_radius = warning_radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_point_interes() {
        return id_point_interes;
    }

    public void setId_point_interes(int id_point_interes) {
        this.id_point_interes = id_point_interes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId_strech() {
        return id_strech;
    }

    public void setId_strech(int id_strech) {
        this.id_strech = id_strech;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpeed_limit() {
        return speed_limit;
    }

    public void setSpeed_limit(int speed_limit) {
        this.speed_limit = speed_limit;
    }

    public double getWarning_radius() {
        return warning_radius;
    }

    public void setWarning_radius(double warning_radius) {
        this.warning_radius = warning_radius;
    }
}
