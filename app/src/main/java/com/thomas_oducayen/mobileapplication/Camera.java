package com.thomas_oducayen.mobileapplication;

public class Camera {
    String label;
    String image;
    String owner; //agency
    double[] coords;

    public Camera(String label, String owner, String image, double[] coords) {
        this.label = label;
        this.image = image;
        this.owner = owner;
        this.coords= coords;
    }
}
