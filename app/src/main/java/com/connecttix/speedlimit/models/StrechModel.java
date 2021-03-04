package com.connecttix.speedlimit.models;

public class StrechModel {
    private int id;
    private int id_strech;
    private int id_fk_route;        // FK
    private String name;            //nombre categoria de ruta
    private double originLati;
    private double originLong;
    private double destinationLati;
    private double destinationLong;
    private double distance;
    private double speedLimit;
    private String existPoint;

    public StrechModel(){}

    public StrechModel(int id_strech, int id_fk_route, String name, double originLati, double originLong, double destinationLati, double destinationLong, double distance, double speedLimit) {
        this.id_strech = id_strech;
        this.id_fk_route = id_fk_route;
        this.name = name;
        this.originLati = originLati;
        this.originLong = originLong;
        this.destinationLati = destinationLati;
        this.destinationLong = destinationLong;
        this.distance = distance;
        this.speedLimit = speedLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_strech() {
        return id_strech;
    }

    public void setId_strech(int id_strech) {
        this.id_strech = id_strech;
    }

    public int getId_fk_route() {
        return id_fk_route;
    }

    public void setId_fk_route(int id_fk_route) {
        this.id_fk_route = id_fk_route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getExistPoint() {
        return existPoint;
    }

    public void setExistPoint(String existPoint) {
        this.existPoint = existPoint;
    }
}
