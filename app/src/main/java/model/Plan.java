package model;

import java.util.List;

public class Plan {
    private Integer Id;
    private String Nazwa;
    private List<Integer> ListaIdCwiczen;

    public Plan(){}
    public Plan(Integer Id,String Nazwa,List<Integer> ListaIdCwiczen)
    {
        this.Id =Id;
        this.Nazwa=Nazwa;
        this.ListaIdCwiczen=ListaIdCwiczen;
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

    public List<Integer> getListaIdCwiczen() {return ListaIdCwiczen;}

    public void setListaIdCwiczen(List<Integer> ListaIdCwiczen) {this.ListaIdCwiczen = ListaIdCwiczen;}

}
