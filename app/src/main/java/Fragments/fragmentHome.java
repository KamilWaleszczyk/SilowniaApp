package Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.Informacje;
import model.OstatniaTrasa;
import model.Plan;
import model.Waga;
import model.WykonanyPlan;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class fragmentHome extends Fragment  implements OnMapReadyCallback {

    private TextView tVwzrost;
    private TextView tVwaga;
    private TextView tvplec;
    Informacje inf;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<Entry> entries1 = new ArrayList<>();
    LineChart weightChart;
    private String AktualnaWaga;
    private Marker mCurrLocationMarker;
    private Integer AktualnyWzrost;
    private String OstatniaData;
    private PolylineOptions polyOptions;
    private List<Waga> wagiK = new ArrayList<Waga>();
    private Integer NaleznaWagaCiala;
    private List<LatLng> trasa = new ArrayList<LatLng>();
    private  CardView cardMapa;
    private GoogleMap mMap;
    private CardView cardViewInfo;
    private CardView cardViewWykres;
    private TextView tInternet;
    private Handler handler;
    private View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.activity_fragment_home, container, false);
tInternet = (TextView) v.findViewById(R.id.textviewInternet);
cardViewInfo = (CardView) v.findViewById(R.id.cardView);
cardViewWykres = (CardView) v.findViewById(R.id.cardViewWykres);



        if (isConnectingToInternet(getContext()) == true) {
            if(handler!=null)
            {
                handler.removeCallbacks(runnable);
            }
            tInternet.setVisibility(View.GONE);
            cardViewWykres.setVisibility(View.VISIBLE);
            cardViewInfo.setVisibility(View.VISIBLE);
            basicQueryValueListener();
            weightChart = v.findViewById(R.id.weightChart);
            cardMapa = (CardView) v.findViewById(R.id.CardViewMapaPodgląd);
            final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.CardViewMapa);
            mapFragment.getMapAsync(this);
            tVwaga = (TextView) v.findViewById(R.id.tV_home_waga);
            tVwzrost = (TextView) v.findViewById(R.id.tV_home_wzrost);
            tvplec = (TextView) v.findViewById(R.id.tV_home_plec);
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        else
        {
            handler = new Handler();
            handler.postDelayed(runnable, 5000);

        }
        return v;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentHome f = (fragmentHome) getActivity().getSupportFragmentManager().findFragmentByTag("HOME");
            ft.detach(f);
            ft.attach(f);
            ft.commit();
            handler.postDelayed(this, 5000);
        }
    };

    private void formatChart() {
        weightChart.setTouchEnabled(false);
        weightChart.setPinchZoom(true);
        weightChart.animate();
        XAxis x = weightChart.getXAxis();
        x.setEnabled(false);
        YAxis yRight= weightChart.getAxisRight();
        yRight.setEnabled(false);
        YAxis yLeft= weightChart.getAxisLeft();
        yLeft.setEnabled(false);
        yLeft.setDrawAxisLine(false);
        weightChart.setDescription(null);
    }



    private void drwaweightChart() { //rysowanie wykresu
        if (!inf.getListaWagi().get(0).getWaga().equals(0))
        {
            AktualnyWzrost=Integer.parseInt(inf.getWzrost().toString());
            if(inf.getPlec().toString().equals("Kobieta"))
            {
                NaleznaWagaCiala= AktualnyWzrost-100 - ((AktualnyWzrost-150)/2);
            }
            else if(inf.getPlec().toString().equals("Meżczyzna"))
            {
                NaleznaWagaCiala= AktualnyWzrost-100 - ((AktualnyWzrost-150)/4);
            }

            ArrayList<Entry> values = new ArrayList<>();
            ArrayList<Entry> values2 = new ArrayList<>();
            wagiK = inf.getListaWagi();

            for (int x = 0; x < wagiK.size(); x++) {
                values.add(new Entry(x, wagiK.get(x).getWaga()));
                values2.add(new Entry(x,NaleznaWagaCiala));
                if(x==wagiK.size()-1)
                {
                    OstatniaData = wagiK.get(x).getData().toString();
                    AktualnaWaga= wagiK.get(x).getWaga().toString();
                }
            }

            LineDataSet set1;
            LineDataSet set2;
            if (weightChart.getData() != null &&
                    weightChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) weightChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) weightChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                set2.setValues(values2);

                weightChart.getData().notifyDataChanged();
                weightChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(values, "Ostatnie wagi");
                set2 = new LineDataSet(values2,"Poprawna waga Ciała");
                set1.setColor(Color.parseColor("#cecd42"));
                set2.setColor(Color.parseColor("#ff0000"));
                set1.setCircleColor(R.color.colorPink);
                set1.setValueTextSize(10f);
                set2.setValueTextSize(10f);
                formatChart();

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                dataSets.add(set2);
                LineData data = new LineData(dataSets);
                weightChart.setData(data);
                weightChart.notifyDataSetChanged();
                weightChart.invalidate();
            }
        }
    }

    private void setInformation() {
        if ( inf!=null)
        {
            tvplec.setText("Płeć: " + inf.getPlec());
            tVwzrost.setText("Wzrost: " + inf.getWzrost());
            tVwaga.setText("Aktualna Waga: " + AktualnaWaga);
        }
        else
        {
            tvplec.setText("Uzupełnij Dane !");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    public void basicQueryValueListener() {

        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/" + Uid);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inf = dataSnapshot.getValue(Informacje.class);

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("OstatniaTrasa").child( FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("trasa");
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            Double lat;
                            Double lng;
                            LatLng position;
                            for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                                lat = Double.parseDouble(childDataSnapshot.child("latitude").getValue().toString());
                                lng = Double.parseDouble(childDataSnapshot.child("longitude").getValue().toString());
                                position = new LatLng(lat, lng);
                               trasa.add(position);
                            }
                        }
                        if(!trasa.isEmpty())
                        {
                          cardMapa.setVisibility(View.VISIBLE);
                          drawPolyLineOnMap(trasa);
                        }

//                        drwaweightChart();
                        setInformation();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(trasa.get(trasa.size()/2),17.0f));
        mMap.addPolyline(polyOptions);
        LatLng start = new LatLng(trasa.get(0).latitude,trasa.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(start).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Start"));

        LatLng koniec = new LatLng(trasa.get(trasa.size()-1).latitude,trasa.get(trasa.size()-1).longitude);
        mMap.addMarker(new MarkerOptions().position(koniec).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Koniec"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(list.get(list.size()-1)));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
    }

    private boolean isConnectingToInternet(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

}