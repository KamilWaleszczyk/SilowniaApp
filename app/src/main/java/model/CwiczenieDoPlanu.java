package model;

import java.util.List;

public class CwiczenieDoPlanu {

    private Integer id;
    private List<Seria> listaSerii;

    public CwiczenieDoPlanu(){}
    public CwiczenieDoPlanu(Integer id, List<Seria> listaSerii)
    {
        this.id=id;
        this.listaSerii=listaSerii;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Seria> getListaSerii() {return listaSerii;}

    public void setListaSerii(List<Seria> listaSerii) {this.listaSerii = listaSerii;}
}
