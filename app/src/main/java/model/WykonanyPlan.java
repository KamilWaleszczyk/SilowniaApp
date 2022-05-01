package model;

import java.util.List;

public class WykonanyPlan {

    private String Data;
    private Integer idPlanu;
    private Integer czasWykonania;
    private List<CwiczenieDoPlanu> listaCwiczen;


    public WykonanyPlan() {
    }

    public WykonanyPlan(Integer idPlanu, List<CwiczenieDoPlanu> listaCwiczen, String Data, Integer czasWykonania) {
        this.idPlanu = idPlanu;
        this.listaCwiczen = listaCwiczen;
        this.Data = Data;
        this.czasWykonania = czasWykonania;
    }

    public Integer getId() {
        return idPlanu;
    }

    public void setId(Integer idPlanu) {
        this.idPlanu = idPlanu;
    }

    public List<CwiczenieDoPlanu> getListaCwiczen() {
        return listaCwiczen;
    }

    public void setListaCwiczen(List<CwiczenieDoPlanu> listaCwiczen) {
        this.listaCwiczen = listaCwiczen;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public Integer getCzasWykonania() {
        return czasWykonania;
    }

    public void setCzasWykonania(Integer czasWykonania) {
        this.czasWykonania = czasWykonania;
    }

}