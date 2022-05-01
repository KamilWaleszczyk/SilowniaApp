package model;

public class Cwiczenie {
    private Integer Id;
    private String Nazwa;
    private String PartiaCiala;
    private String Opis;
    private Integer Serie;
    public Cwiczenie(){}

    public Cwiczenie(Integer Id, String Nazwa,String PartiaCiala, String Opis, Integer Serie){
        this.Id = Id;
        this.Nazwa = Nazwa;
        this.PartiaCiala = PartiaCiala;
        this.Opis = Opis;
        this.Serie = Serie;
    }
    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String Nazwa) {
        this.Nazwa = Nazwa;
    }

    public String getPartiaCiala() { return PartiaCiala; }

    public void setPartiaCiala(String PartiaCiala) { this.PartiaCiala = PartiaCiala; }

    public String getOpis() {
        return Opis;
    }

    public void setOpis(String Opis) {
        this.Opis = Opis;
    }

    public Integer getSerie() { return Serie; }

    public void setSerie(Integer Serie) {
        this.Serie = Serie;
    }



}


