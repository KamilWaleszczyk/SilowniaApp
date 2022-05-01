package model;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class OstatniaTrasa {
    private  List<LatLng> Trasa;

    public OstatniaTrasa(){}

    public OstatniaTrasa(List<LatLng> Trasa)
    {
        this.Trasa = Trasa;
    }

    public List<LatLng> getTrasa() {
        return Trasa;
    }

    public void setTrasa(List<LatLng> Trasa) {
       this.Trasa = Trasa;
    }
}


