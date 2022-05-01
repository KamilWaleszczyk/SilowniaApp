package model;

public class NazwaPlanuiId {

    public String nazwa;
    public Integer id;

    public NazwaPlanuiId(String nazwa,Integer id) {
        this.id = id;
        this.nazwa = nazwa;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getId() {
        return id;
    }

    public void Integer(Integer id) {
        this.id = id;
    }
}
