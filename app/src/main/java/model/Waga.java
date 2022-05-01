package model;

public class Waga {

    private String Data;
    private Integer Waga;

    public Waga(){}

    public Waga(String Data ,Integer Waga)
    {
        this.Data = Data;
        this.Waga = Waga;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public Integer getWaga() {
        return Waga;
    }

    public void setWaga(Integer Waga) { this.Waga = Waga; }

}
