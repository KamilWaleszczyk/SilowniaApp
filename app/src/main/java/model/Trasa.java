package model;

public class Trasa {

    private String Latitude;
    private String Longitude;

    public Trasa(){}

    public Trasa (String Latitude , String Longitude)
    {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }


    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
