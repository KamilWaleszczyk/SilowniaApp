package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.CwiczenieDoPlanu;
import model.NazwaPlanuiId;
import model.Plan;
import model.Seria;
import model.WykonanyPlan;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class fragmentStatystyka extends Fragment {


    private List<WykonanyPlan> doneWorkoutPlans = new ArrayList<WykonanyPlan>();
    private List<WykonanyPlan> doneWorkoutPlansForChar = new ArrayList<WykonanyPlan>();
    private List<Plan> ownWorkoutPlans = new ArrayList<Plan>();
    private List<Plan> standardWoroutPlans = new ArrayList<Plan>();
    private List<Cwiczenie> ownExercises = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> standardExercises = new ArrayList<Cwiczenie>();
    private List<NazwaPlanuiId> chosenNamePlanAndId = new ArrayList<NazwaPlanuiId>();
    private BubbleSeekBar bubbleSeekBar;
    private Activity mActivity;

    ArrayList<String> namesOfPlans = new ArrayList<String>();
    ArrayList<Integer> idOfPlans = new ArrayList<Integer>();
    LineChart chart;
    LinearLayout linLayoutButtony;
    LinearLayout linLayoutWykresy;
    TextView t1, t2;
    Button b1, b2, b3;
    int countOfLastPlans =5;
    Button clickButton;
    private TextView t;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        final View v = inflater.inflate(R.layout.activity_fragment_statystyka, container, false);
        linLayoutButtony = (LinearLayout) v.findViewById(R.id.linearLayoutButtony);
        linLayoutWykresy = (LinearLayout) v.findViewById(R.id.linearLayoutWykresy);

        t1 = (TextView) v.findViewById(R.id.text1);
        t2 = (TextView) v.findViewById(R.id.text2);
        t = (TextView) v.findViewById(R.id.TextViewIle);

        bubbleSeekBar = (BubbleSeekBar) v.findViewById(R.id.btnStat) ;
        setConfigToBubbleBar();

        bubbleSeekBar.setVisibility(View.GONE);

        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                countOfLastPlans = bubbleSeekBar.getProgress();
                drawCharts(clickButton);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

        PobierzDane();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
    }

    private void setConfigToBubbleBar() {

       bubbleSeekBar.getConfigBuilder()
                .min(5)
                .max(15)
                .progress(1)
                .sectionCount(2)
                .trackColor(ContextCompat.getColor(mActivity, R.color.color_gray))
                .secondTrackColor(ContextCompat.getColor(mActivity, R.color.color_blue))
                .thumbColor(ContextCompat.getColor(mActivity, R.color.color_blue))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(mActivity, R.color.color_gray))
                .sectionTextSize(10)
                .showThumbText()
                .thumbTextColor(ContextCompat.getColor(mActivity, R.color.colorWhite))
                .thumbTextSize(18)
                .bubbleTextSize(14)
                .showSectionMark()
                .seekStepSection()
                .touchToSeek()
                .sectionTextPosition(BubbleSeekBar.TextPosition.SIDES)
                .build();
    }



    public void PobierzDane() {

        //doneWorkoutPlans.clear();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        //pobieranie własnych planów
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("wlasne_plany").child(currentFirebaseUser.getUid().toString());
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            ///////////////////////////////////////////////
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Plan p = childDataSnapshot.getValue(Plan.class);
                    ownWorkoutPlans.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

//pobieranie standardowych planow
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("Standardowe_Plany");
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            ///////////////////////////////////////////////
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Plan p = childDataSnapshot.getValue(Plan.class);
                    standardWoroutPlans.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
//pobieranie wlasnych cwiczen
        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            ///////////////////////////////////////////////
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie c = childDataSnapshot.getValue(Cwiczenie.class);
                    ownExercises.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
        //pobieranie standardowych cwiczen
        DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference().child("Standardowe_Cwiczenia");
        ref5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie c = childDataSnapshot.getValue(Cwiczenie.class);
                    standardExercises.add(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wykonany_plan").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    WykonanyPlan w = childDataSnapshot.getValue(WykonanyPlan.class);
                    doneWorkoutPlans.add(w);
                }
                Boolean checkName = true;
                if(doneWorkoutPlans.isEmpty())
                {
                    t.setText("Brak Danych");
                }
                //wybieranie nazw planow i wpisywanie ich do listy
                for (Integer i = 0; i < doneWorkoutPlans.size(); i++) {

                    //dodawanie nazw i id planow w wlasnych planach
                    for (Plan p : ownWorkoutPlans) {


                        if (doneWorkoutPlans.get(i).getId().equals(p.getId())) {

                            Integer id = doneWorkoutPlans.get(i).getId();
                            for (NazwaPlanuiId nPId : chosenNamePlanAndId) {
                                if (id.equals(nPId.getId())) {
                                    checkName = false;
                                    break;
                                }
                            }
                            if (checkName == true) {
                                chosenNamePlanAndId.add(new NazwaPlanuiId(p.getNazwa(), p.getId()));
                                break;
                            }
                        }
                        checkName = true;
                    }

                    for (Plan p : standardWoroutPlans) {
                        if (doneWorkoutPlans.get(i).getId().equals(p.getId())) {

                            Integer id = doneWorkoutPlans.get(i).getId();
                            for (NazwaPlanuiId nPId : chosenNamePlanAndId) {
                                if (id.equals(nPId.getId())) {
                                    checkName = false;
                                    break;
                                }
                            }

                            if (checkName == true) {
                                chosenNamePlanAndId.add(new NazwaPlanuiId(p.getNazwa(), p.getId()));
                                break;
                            }
                        }
                        checkName = true;
                    }
                }

                for (int i = 0; i < chosenNamePlanAndId.size(); i++) {
                    if(getActivity()!=null)
                    {
                        Button NazwaPlanu = new Button(getActivity());
                        LinearLayout Layout = new LinearLayout(getContext());
                        TextView Text = new TextView(getContext());
                        NazwaPlanu.setText(chosenNamePlanAndId.get(i).getNazwa().toString());
                        Drawable d = getResources().getDrawable(R.drawable.button_background);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        NazwaPlanu.setLayoutParams(params);
                        NazwaPlanu.setBackground(d);
                        NazwaPlanu.setId(i);
                        linLayoutButtony.addView(NazwaPlanu);
                        NazwaPlanu.setOnClickListener(listener);
                    }

                }

            }


            public void setButtonBackground() {
                final int childCount = linLayoutButtony.getChildCount();
                Integer inte = 0;
                Button b;
                for (int i = 0; i < childCount; i++) {
                    View v = linLayoutButtony.getChildAt(i);
                    inte = v.getId();
                    b = (Button) v.findViewById(inte); //pobranie kliknietego buttona
                    b.setBackground(getResources().getDrawable(R.drawable.button_background));


                }
            }

            //onClick na wybranie planu do statystyki
            public View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setButtonBackground();
                    Integer inte = v.getId();
                    Button b = (Button) v.findViewById(inte); //pobranie kliknietego buttona
                    b.setBackground(getResources().getDrawable(R.drawable.button_click_background));

//wybranie id planu
                    clickButton = b;
                    bubbleSeekBar.setVisibility(View.VISIBLE);
                    drawCharts(b);


                }
            };


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void drawCharts(Button b) {

        Integer idPlan = 0;

        for (int i = 0; i < chosenNamePlanAndId.size(); i++) {
            if (chosenNamePlanAndId.get(i).getNazwa().equals(b.getText())) {
                idPlan = chosenNamePlanAndId.get(i).getId();
            }
        }


        List<WykonanyPlan> chosenDoneWorkout = new ArrayList<WykonanyPlan>(); //lista do wybranego planu z wykonywanych
        List<Integer> listOfExamplesId = new ArrayList<>();
        List<String> namesOfExamples = new ArrayList<>();
//pobranie nazw cwiczen

        //pobieranie planow z datami
        for (WykonanyPlan w : doneWorkoutPlans) {
            if (w.getId().equals(idPlan))
                chosenDoneWorkout.add(w);
        }
        //pobranie id cwiczen
        for (int x = 0; x < chosenDoneWorkout.get(0).getListaCwiczen().size(); x++) {
            listOfExamplesId.add(chosenDoneWorkout.get(0).getListaCwiczen().get(x).getId());
        }
        //pobieranie nazw cwiczen
        for (Integer id : listOfExamplesId) {
            for (Cwiczenie c : standardExercises) {
                if (c.getId().toString().equals(id.toString()))
                {
                    namesOfExamples.add(c.getNazwa());
                    break;
                }
            }
            for (Cwiczenie c : ownExercises) {
                if (c.getId().toString().equals(id.toString()))
                {
                    namesOfExamples.add(c.getNazwa());
                    break;
                }
            }
        }


        linLayoutWykresy.removeAllViews();
        //wstawienie wykresu z danego dnia

        Integer exercisesCount = chosenDoneWorkout.get(0).getListaCwiczen().size();
        final String[] Daty = new String[chosenDoneWorkout.size()];

        chart = new LineChart(getContext());


        for (int exCou = 0; exCou < exercisesCount; exCou++) {
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<Entry> entries1 = new ArrayList<>();

            int start ;

            //tutaj ile ostatnich planow
            if(chosenDoneWorkout.size() - countOfLastPlans <=0)
            {
                start =0;
            }
            else
            {
                start =chosenDoneWorkout.size() - countOfLastPlans ;
            }
            for (int x = start    ; x < chosenDoneWorkout.size(); x++) {

                Integer srPowt = 0;
                Integer srCiez = 0;
                Integer sum = 0;
                Integer count = 0;
                for (Seria s : chosenDoneWorkout.get(x).getListaCwiczen().get(exCou).getListaSerii()) {
                    srPowt += s.getPowtorzenia();
                    srCiez += s.getCiezar();
                    count++;
                }
                entries.add(new Entry(x, (srPowt / count)));
                entries1.add(new Entry(x, srCiez / count));
                Daty[x] = chosenDoneWorkout.get(x).getData();

            }

            chart = new LineChart(getContext());
            LineDataSet lineDataSet1 = new LineDataSet(entries, "Srednia Ciężaru");
            LineDataSet lineDataSet2 = new LineDataSet(entries1, "Srednia Powtórzeń");
            lineDataSet1.setColor(RED);
            lineDataSet2.setColor(GREEN);
            lineDataSet1.setCircleSize(3);
            lineDataSet2.setCircleSize(3);
            lineDataSet1.setLineWidth(2);
            lineDataSet2.setLineWidth(2);
            lineDataSet1.setCircleColor(BLACK);
            lineDataSet2.setCircleColor(BLACK);
            lineDataSet1.setValueTextSize(8);
            lineDataSet2.setValueTextSize(8);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet1);
            dataSets.add(lineDataSet2);


            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setValueFormatter(new IndexAxisValueFormatter(Daty));
            xAxis.setGranularity(1f);

            chart.getDescription().setText("");

            chart.getDescription().setTextSize(14);
            chart.getDescription().setTextColor(Color.WHITE);
            xAxis.setLabelRotationAngle(45f);

            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(false);


            YAxis yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setGranularity(1f);


            LineData data = new LineData(dataSets);
            chart.setData(data);
            chart.animateX(200); //animacja do wykresu
            chart.invalidate();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1000);

            chart.setLayoutParams(params);

           TextView tv = new TextView(this.getContext());
           tv.setText(namesOfExamples.get(exCou).toString());
           tv.setGravity(Gravity.CENTER);
           linLayoutWykresy.addView(tv);
            linLayoutWykresy.addView(chart);
        }
    }

    public void setBackgroundButtonOfCount() {

        if (countOfLastPlans == 5) {
            b1.setBackground(getResources().getDrawable(R.drawable.button_click_background));
            b2.setBackground(getResources().getDrawable(R.drawable.button_background));
            b3.setBackground(getResources().getDrawable(R.drawable.button_background));
        } else if (countOfLastPlans == 10) {
            b1.setBackground(getResources().getDrawable(R.drawable.button_background));
            b2.setBackground(getResources().getDrawable(R.drawable.button_click_background));
            b3.setBackground(getResources().getDrawable(R.drawable.button_background));
        } else if (countOfLastPlans == 15) {
            b1.setBackground(getResources().getDrawable(R.drawable.button_background));
            b2.setBackground(getResources().getDrawable(R.drawable.button_background));
            b3.setBackground(getResources().getDrawable(R.drawable.button_click_background));
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Statystyka");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
