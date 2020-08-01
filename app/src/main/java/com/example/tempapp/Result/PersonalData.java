package com.example.tempapp.Result;

public class PersonalData {
    private float[] ear_x = new float[2];
    private float[] ear_y = new float[2];
    private float[] shoulder_x = new float[2];
    private float[] shoulder_y = new float[2];
    private float[] waist_x = new float[2];
    private float[] waist_y = new float[2];

    public void setEar_X(float f, int i) { this.ear_x[i] = f; }
    public void setEar_Y(float f, int i) {
        this.ear_y[i] = f;
    }
    public void setShoulder_X(float f, int i) {
        this.shoulder_x[i] = f;
    }
    public void setShoulder_Y(float f, int i) {
        this.shoulder_y[i] = f;
    }
    public void setWaist_X(float f, int i) {
        this.waist_x[i] = f;
    }
    public void setWaist_Y(float f, int i) {
        this.waist_y[i] = f;
    }

    public float[] getEar_X() {
        return ear_x;
    }
    public float[] getEar_Y() {
        return ear_y;
    }
    public float[] getShoulder_X() {
        return shoulder_x;
    }
    public float[] getShoulder_Y() {
        return shoulder_y;
    }
    public float[] getWaist_X() { return waist_x; }
    public float[] getWaist_Y() {
        return waist_y;
    }
}
