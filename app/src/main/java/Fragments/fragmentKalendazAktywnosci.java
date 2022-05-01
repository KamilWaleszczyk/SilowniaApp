package Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aplikacja.projektzespokowy2019.R;
import model.Aktywność;
import model.Cwiczenie;
import model.Informacje;

import static android.content.ContentValues.TAG;


public class fragmentKalendazAktywnosci extends Fragment  implements OnMapReadyCallback {
    private View v;
    private TextView tvRok;
    private  List < Event > events;
    private CompactCalendarView compactCalendarView;
    private List<Aktywność> Bieganie = new ArrayList<Aktywność>();
    private List<Aktywność> Rower = new ArrayList<Aktywność>();
    private int cItem=-1;
    private CardView cInfo;
    private Marker mCurrLocationMarker;
    private CardView cMapa;
    private GoogleMap mMap;
    private Aktywność a ;
    private PolylineOptions polyOptions;
    private List<LatLng> TrasaLista = new ArrayList<LatLng>();
    private CardView cWykres;
    private LineChart Wykres;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_fragment_kalendarz_aktywnosci, container, false);
        compactCalendarView = (CompactCalendarView) v.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setShouldDrawDaysHeader(true);
        cInfo = (CardView) v.findViewById(R.id.CardViewInfo);
        cMapa = (CardView) v.findViewById(R.id.CardViewMapaaa);
        cWykres = (CardView) v.findViewById(R.id.CardViewWykres);
        Wykres = (LineChart) v.findViewById(R.id.Chart12);
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.Mapaa);
        mapFragment.getMapAsync(this);
        tvRok = (TextView) v.findViewById(R.id.textViewRok);
        // wywołanie funkcji pobierajacej dane z Firebase
        basicQueryValueListener();
        String Year =compactCalendarView.getFirstDayOfCurrentMonth().toString().substring(29,34) + " -" + compactCalendarView.getFirstDayOfCurrentMonth().toString().substring(3,7);
        tvRok.setText(Year);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                events = compactCalendarView . getEvents (dateClicked);
                if(!events.isEmpty())
                {
                    SelectEventFromCallendar();
                }
                else {
                    Toast.makeText(getActivity(), "Brak Aktywności w Tym Dniu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String Year =compactCalendarView.getFirstDayOfCurrentMonth().toString().substring(29,34) + " -" + compactCalendarView.getFirstDayOfCurrentMonth().toString().substring(3,7);
                tvRok.setText(Year);
            }
        });
        return v;
    }

    // Pobieranie Danych z Firebase
    public void basicQueryValueListener() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Aktywność").child(currentFirebaseUser.getUid().toString()).child("Bieganie");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Aktywność nowa =childDataSnapshot.getValue(Aktywność.class);
                    Bieganie.add(nowa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Aktywność").child(currentFirebaseUser.getUid().toString()).child("Jazada_Rowerem");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Aktywność nowa =childDataSnapshot.getValue(Aktywność.class);
                    Rower.add(nowa);
                }
                AddObjectsToCalendar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    public void AddObjectsToCalendar()
    {
        for(int i=0;i<Bieganie.size();i++)
        {
            long dataEvent  = Bieganie.get(i).getData().getTime();
            Event nowy = new Event(Color.GREEN,dataEvent,"Bieganie : " + Bieganie.get(i).getCzas());
            compactCalendarView.addEvent(nowy);
        }

        for(int i=0;i<Rower.size();i++)
        {
            long dataEvent  = Rower.get(i).getData().getTime();
            Event nowy = new Event(Color.RED,dataEvent,"Jazada Rowerem : " + Rower.get(i).getCzas());
            compactCalendarView.addEvent(nowy);
        }
    }

    public void SelectEventFromCallendar()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz Aktywność");
        final String[] partie = new String[events.size()];
        for(int i=0;i<events.size();i++)
        {
            partie[i]=events.get(i).getData().toString();
        }
        final int checkedItem = -1; // cow
        builder.setSingleChoiceItems(partie, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cItem = which;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cItem!=-1)
                {
                    cInfo.setVisibility(View.VISIBLE);
                    TextView tAktywnosc = (TextView) v.findViewById(R.id.AktywnoscInfo);
                    TextView tCzas = (TextView) v.findViewById(R.id.CzasInfo);
                    TextView tOdleglosc = (TextView) v.findViewById(R.id.OdlegloscInfo);
                    TextView tKalorie = (TextView) v.findViewById(R.id.KalorieInfo);
                    TextView tSrednia = (TextView) v.findViewById(R.id.SredniaPredkoscInfo);
                    long time = events.get(cItem).getTimeInMillis();
                    if(events.get(cItem).getData().toString().length()<18) // Bieganie
                    {
                        for (int i=0;i<Bieganie.size();i++)
                        {
                            if(Bieganie.get(i).getData().getTime() == time)
                            {
                                a = Bieganie.get(i);
                            }
                        }
                        tAktywnosc.setText("Aktywność : Bieganie");
                    }
                    else // Jazda Rowerem
                    {
                        for (int i=0;i<Rower.size();i++)
                        {
                            if(Rower.get(i).getData().getTime() == time)
                            {
                                a = Rower.get(i);
                            }
                        }
                        tAktywnosc.setText("Aktywność : Jazda Rowerem");
                    }

                    tCzas.setText("Czas : " + a.getCzas().toString());
                    tOdleglosc.setText("Odległość : " + a.getOdległość().toString() + "m");
                    tKalorie.setText("Kalorie : " + a.getKalorie().toString());
                    Float srednia;
                    Float Suma=0f;
                    int licznik=0;
                    for(int i=0;i<a.getPredkosc().size();i++)
                    {
                        Suma+=a.getPredkosc().get(i) * 3.6f;
                        licznik++;
                    }
                    srednia = Suma/licznik;
                    DecimalFormat df = new DecimalFormat("0.00");

                    tSrednia.setText("Srednia Prędkość : " + df.format(srednia) +"km/h");

                    ToLatLngList();
                    drawPolyLineOnMap(TrasaLista);
                    cMapa.setVisibility(View.VISIBLE);
                    cWykres.setVisibility(View.VISIBLE);
                    drawChart();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void drawChart() { //rysowanie wykresu
        if (!a.getPredkosc().isEmpty())
        {

            ArrayList<Entry> values = new ArrayList<>();
            List<Float> Predkosc = new ArrayList<Float>();
            Predkosc = a.getPredkosc();

            for (int x = 0; x < Predkosc.size(); x++) {
                values.add(new Entry(x, Predkosc.get(x) * 3.6f));
            }

            LineDataSet set1 ;

            if (Wykres.getData() != null &&
                    Wykres.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) Wykres.getData().getDataSetByIndex(0);
                set1.setValues(values);
                XAxis x = Wykres.getXAxis();
                x.setEnabled(false);
                YAxis yRight= Wykres.getAxisRight();
                yRight.setEnabled(false);
                YAxis yLeft= Wykres.getAxisLeft();
                yLeft.setEnabled(true);
                set1.setDrawFilled(true);
                Wykres.getXAxis().setDrawGridLines(true);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setDrawFilled(true);
                set1.setDrawValues(false);
                set1.setFillColor(ContextCompat.getColor(getContext(),R.color.color_green));
                set1.setColor(ContextCompat.getColor(getContext(),R.color.color_green));
                set1.setFillAlpha(255);
                set1.setDrawCircles(false);
                Wykres.setDescription(null);
                Wykres.getData().notifyDataChanged();
                Wykres.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(values, "Prędkość km/h");
                set1.setColor(Color.parseColor("#cecd42"));
                set1.setDrawFilled(true);
                XAxis x = Wykres.getXAxis();
                x.setEnabled(false);
                YAxis yRight= Wykres.getAxisRight();
                yRight.setEnabled(false);
                YAxis yLeft= Wykres.getAxisLeft();
                yLeft.setEnabled(true);
                Wykres.getXAxis().setDrawGridLines(true);
                set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                set1.setDrawFilled(true);
                set1.setDrawValues(false);
                set1.setFillColor(ContextCompat.getColor(getContext(),R.color.color_green));
                set1.setColor(ContextCompat.getColor(getContext(),R.color.color_green));
                set1.setFillAlpha(255);
                set1.setDrawCircles(false);
                Wykres.setDescription(null);


                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                Wykres.setData(data);
                Wykres.notifyDataSetChanged();
                Wykres.invalidate();
            }
        }
    }


    public void ToLatLngList()
    {
        Double lat;
        Double lng;
        LatLng position;
        for(int i=0;i<a.getTrasa().size();i++)
        {
          lat =  Double.parseDouble(a.getTrasa().get(i).getLatitude());
          lng =  Double.parseDouble(a.getTrasa().get(i).getLongitude());
          position = new LatLng(lat,lng);
          TrasaLista.add(position);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }

    public void drawPolyLineOnMap(List<LatLng> list) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        polyOptions = new PolylineOptions();
        polyOptions.color(Color.RED);
        polyOptions.width(20);
        polyOptions.addAll(list);
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TrasaLista.get(TrasaLista.size()/2),17.0f));
        mMap.addPolyline(polyOptions);
        LatLng start = new LatLng(TrasaLista.get(0).latitude,TrasaLista.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Start"));

        LatLng koniec = new LatLng(TrasaLista.get(TrasaLista.size()-1).latitude,TrasaLista.get(TrasaLista.size()-1).longitude);
        mMap.addMarker(new MarkerOptions().position(koniec).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Koniec"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(list.size()-1)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Kalendarz Aktywności");
    }
}
