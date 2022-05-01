package model;

public class Seria {
    private Integer Powtorzenia;
    private Integer Ciezar;

    public Seria(){}

    public Seria(Integer Powtorzenia, Integer Ciezar)
    {
        this.Ciezar = Ciezar;
        this.Powtorzenia = Powtorzenia;
    }

    public Integer getCiezar() { return Ciezar; }

    public void setCiezar(Integer Ciezar) {
        this.Ciezar = Ciezar;
    }

    public Integer getPowtorzenia() { return Powtorzenia; }

    public void setPowtorzenia(Integer Powtorzenia) {
        this.Powtorzenia = Powtorzenia;
    }
}
