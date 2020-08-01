package com.example.tempapp.Graph_2;

public class DataCollection {

    private String date;
    private float angleEar;
    private float angleShd;
    private float angleWst;

    private String year, month, day;

    DataCollection()  { }

    public void setDate(String s) {
        date = s;
        year = s.substring(0,4); //Log.e("Y:  ", year);
        month = s.substring(5,7); //Log.e("M:  ", month);
        day = s.substring(8,10); //Log.e("D:  ", day);
    }
    public void setEar(float f) { angleEar = f; }
    public void setShd(float f) { angleShd = f; }
    public void setWst(float f) { angleWst = f; }

    public String getDate() { return year+"/"+month+"/"+day; }
    public String getMonthDay() { return month+ " -" + day; }

    public float getEar() { return angleEar; }
    public float getShd() { return angleShd; }
    public float getWst() { return angleWst; }

}
