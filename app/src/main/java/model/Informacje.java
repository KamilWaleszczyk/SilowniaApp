package model;

import java.util.List;

public class Informacje {
    private String Imie;
    private String DataUrodzenia;
    private String Plec;
    private Integer Wzrost;
    private List<Waga> ListaWagi;


    public Informacje() {
    }

    public Informacje(String Imie, String DataUrodzenia, String Plec, Integer Wzrost, List<Waga> ListaWagi) {
        this.Imie = Imie;
        this.DataUrodzenia = DataUrodzenia;
        this.Plec = Plec;
        this.ListaWagi = ListaWagi;
        this.Wzrost = Wzrost;
    }

    public String getImie() {
        return Imie;
    }

    public void setImie(String Imie) {
        this.Imie = Imie;
    }

    public String getDataUrodzenia() {
        return DataUrodzenia;
    }

    public void setDataUrodzenia(String DataUrodzenia) {
        this.DataUrodzenia = DataUrodzenia;
    }

    public String getPlec() {
        return Plec;
    }

    public void setPlec(String Plec) {
        this.Plec = Plec;
    }

    public Integer getWzrost() {
        return Wzrost;
    }

    public void setWzrost(Integer Wzrost) {
        this.Wzrost = Wzrost;
    }

    public List<Waga> getListaWagi() {
        return ListaWagi;
    }

    public void setListaWagi(List<Waga> ListaWagi) {
        this.ListaWagi = ListaWagi;
    }

}