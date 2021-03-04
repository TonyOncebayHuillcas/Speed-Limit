package com.connecttix.speedlimit.models;

public class RouteModel {
    private int id;
    private int id_route;
    private int id_fk_category;        // FK
    private String name;            //nombre categoria de ruta
    private double originLati;
    private double originLong;
    private double detinationLati;
    private double destinationLong;

    public RouteModel(){}

    public RouteModel(int id_route, int id_fk_category, String name, double originLati, double originLong, double detinationLati, double destinationLong) {
        this.id_route = id_route;
        this.id_fk_category = id_fk_category;
        this.name = name;
        this.originLati = originLati;
        this.originLong = originLong;
        this.detinationLati = detinationLati;
        this.destinationLong = destinationLong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public int getId_fk_category() {
        return id_fk_category;
    }

    public void setId_fk_category(int id_fk_category) {
        this.id_fk_category = id_fk_category;
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

    public double getDetinationLati() {
        return detinationLati;
    }

    public void setDetinationLati(double detinationLati) {
        this.detinationLati = detinationLati;
    }

    public double getDestinationLong() {
        return destinationLong;
    }

    public void setDestinationLong(double destinationLong) {
        this.destinationLong = destinationLong;
    }
}
