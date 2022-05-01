package model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Aktywność {

    private Date Data;
    private String Odległość;
    private Integer Kalorie;
    private String Czas;
    private List<Trasa> Trasa;
    private List<Float> Predkosc;

    public Aktywność(){}

    public Aktywność(Date Data,String Odleglosc,Integer Kalorie,String Czas,List<Trasa> Trasa,List<Float> Predkosc)
    {
        this.Data = Data;
        this.Odległość = Odleglosc;
        this.Kalorie = Kalorie;
        this.Czas = Czas;
        this.Trasa = Trasa;
        this.Predkosc = Predkosc;
    }

    public String getCzas() {
        return Czas;
    }

    public List<Float> getPredkosc() {
        return Predkosc;
    }

    public List<Trasa> getTrasa() {
        return Trasa;
    }

    public void setTrasa(List<Trasa> trasa) {
        Trasa = trasa;
    }

    public void setPredkosc(List<Float> predkosc) {
        Predkosc = predkosc;
    }

    public void setCzas(String czas) {
        Czas = czas;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public Integer getKalorie() {
        return Kalorie;
    }

    public void setKalorie(Integer kalorie) {
        Kalorie = kalorie;
    }

    public String getOdległość() {
        return Odległość;
    }

    public void setOdległość(String odległość) {
        Odległość = odległość;
    }
}
