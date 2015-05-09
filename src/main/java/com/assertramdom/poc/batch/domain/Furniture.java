package com.assertramdom.poc.batch.domain;

/**
 * Created on 09/05/15.
 */
public class Furniture {

    private String name;
    private double cost;

    public Furniture() {
    }

    public Furniture(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
