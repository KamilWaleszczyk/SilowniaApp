package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aplikacja.projektzespokowy2019.AdapterDoWyboruPlanu;
import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.CwiczenieDoPlanu;
import model.Plan;
import model.Seria;
import model.WykonanyPlan;

import static android.content.ContentValues.TAG;

public class fragmentWykonajPlan extends Fragment {

    private List<Plan> listaPlanow = new ArrayList<Plan>();
    private List<Cwiczenie> listaCwiczen = new ArrayList<Cwiczenie>();
    private List<WykonanyPlan> wPlany = new ArrayList<WykonanyPlan>();

    private ListView listView;
    private TextView WybierzHead;
    private TextView WykonajHead;
    private Button ZakonczTrening;
    private ViewGroup linearLayout;
    private ConstraintLayout cLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Chronometer chrono;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View v = inflater.inflate(R.layout.activity_fragment_wykonaj_plan, container, false);
        listView = (ListView) v.findViewById(R.id.listViewWykonajPlan);
        WybierzHead = (TextView) v.findViewById(R.id.WybierzPlanHead);
        WykonajHead = (TextView) v.findViewById(R.id.WykonajPlanHead);
        ZakonczTrening = (Button) v.findViewById(R.id.buttonZakonczTrening);
        linearLayout = (ViewGroup) v.findViewById(R.id.LinearLayoutDoWykonaniaPlanu);
        cLayout = (ConstraintLayout) v.findViewById(R.id.constraintWykonaj) ;
        chrono = (Chronometer) v.findViewById(R.id.chronoForWorkout);
        chrono.stop();
        PobierzDane();
        listaCwiczen.size();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return v;
    }


    public void PobierzDane() {
        listaPlanow.clear();
        listaCwiczen.clear();
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie cw = childDataSnapshot.getValue(Cwiczenie.class);
                    listaCwiczen.add(cw);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Standardowe_Cwiczenia");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie cw = childDataSnapshot.getValue(Cwiczenie.class);
                    listaCwiczen.add(cw);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });


        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("wlasne_plany").child(currentFirebaseUser.getUid().toString());
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Plan pl = childDataSnapshot.getValue(Plan.class);
                    listaPlanow.add(pl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        DatabaseReference ref5 = FirebaseDatabase.getInstance().getReference().child("wykonany_plan").child(currentFirebaseUser.getUid().toString());
        ref5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    WykonanyPlan cw = childDataSnapshot.getValue(WykonanyPlan.class);
                    wPlany.add(cw);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference().child("Standardowe_Plany");
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Plan pl = childDataSnapshot.getValue(Plan.class);
                    listaPlanow.add(pl);
                    WybierzPlan();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    public void WybierzPlan() {
        if (listaPlanow.isEmpty()) {
            Toast.makeText(getActivity(), "Brak Planów Treningowych", Toast.LENGTH_LONG).show();
            Fragment fragment = new fragmentHome();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            AdapterDoWyboruPlanu adapter = new AdapterDoWyboruPlanu(getActivity(), listaPlanow, listaCwiczen);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Czy Napewno Chcesz Rozpocząć Trening");
                    dialog.setPositiveButton("Rozpocznij", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chrono.setBase(SystemClock.elapsedRealtime());
                            chrono.start();
                            WykonajPlan(position);
                        }
                    });
                    dialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();

                }
            });
        }

    }

    public void WykonajPlan(final int position) {

        listView.setVisibility(View.GONE);
        WybierzHead.setVisibility(View.GONE);
        WykonajHead.setVisibility(View.VISIBLE);
        ZakonczTrening.setVisibility(View.VISIBLE);
        cLayout.setVisibility(View.VISIBLE);
        chrono.setVisibility(View.VISIBLE);
        WykonajHead.setText(listaPlanow.get(position).getNazwa().toString());


        final Integer idPlanu = listaPlanow.get(position).getId();

        String[] nameCwiczenArray = new String[listaPlanow.get(position).getListaIdCwiczen().size()];
        final Integer[] serie = new Integer[listaPlanow.get(position).getListaIdCwiczen().size()];
        for (Integer i = 0; i < listaPlanow.get(position).getListaIdCwiczen().size(); i++) {
            for (Integer j = 0; j < listaCwiczen.size(); j++) {
                if (listaPlanow.get(position).getListaIdCwiczen().get(i).toString().equals(listaCwiczen.get(j).getId().toString())) {
                    nameCwiczenArray[i] = listaCwiczen.get(j).getNazwa().toString();
                    serie[i] = listaCwiczen.get(j).getSerie();
                }
            }
        }
        final List<EditText> idEditTextPowtorzenia = new ArrayList<EditText>();
        final List<EditText> idEditTextCiezar = new ArrayList<EditText>();

        for (Integer i = 0; i < nameCwiczenArray.length; i++) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view2 = inflater.inflate(R.layout.layout_plan, null, true);
            TextView nameTextField = (TextView) view2.findViewById(R.id.idNazwaCwiczeniaWykonajPlan);
            ViewGroup linearLayout2 = (ViewGroup) view2.findViewById(R.id.idListaSeriiWykonajPlan);
            nameTextField.setText("Nazwa Cwiczenia : " + nameCwiczenArray[i]);
            linearLayout.addView(view2);


            for (Integer j = 0; j < serie[i]; j++) {
                View view1 = inflater.inflate(R.layout.layout_seria, null, true);
                TextView text = (TextView) view1.findViewById(R.id.idKtoraSeriaWykonajPlan);
                EditText editPowtorzenia = (EditText) view1.findViewById(R.id.editTextPowtorzeniaWykonajPlan);
                EditText editCiezar = (EditText) view1.findViewById(R.id.editTextCiezarWykonajPlan);
                idEditTextCiezar.add(editCiezar);
                idEditTextPowtorzenia.add(editPowtorzenia);
                editCiezar.setId(j);
                editPowtorzenia.setId(j);
                int k = j + 1;
                text.setText("Seria " + k);
                linearLayout2.addView(view1);
            }

        }

        ZakonczTrening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int licznik = 1;
                int licznik2 =1;
                String[] powtorzenia = new String[idEditTextPowtorzenia.size()];

                chrono.stop();
                for (int i = 0; i < idEditTextPowtorzenia.size(); i++) {
                    if (!idEditTextPowtorzenia.get(i).getText().toString().equals("")) {
                        powtorzenia[i] = idEditTextPowtorzenia.get(i).getText().toString();
                        licznik = 0;
                    } else {
                        licznik++;
                    }
                }

                String[] ciezary = new String[idEditTextCiezar.size()];
                for (int i = 0; i < idEditTextCiezar.size(); i++) {
                    if (!idEditTextCiezar.get(i).getText().toString().equals("")) {
                        ciezary[i] = idEditTextCiezar.get(i).getText().toString();
                        licznik2 = 0;
                    } else {
                        licznik2++;
                    }
                }

                if (licznik ==0  && licznik2==0)
                {
                    List<Seria> ListaSerii = new ArrayList<Seria>();

                    for (Integer i = 0; i < powtorzenia.length; i++) {
                        Seria s = new Seria(Integer.parseInt(powtorzenia[i]), Integer.parseInt(ciezary[i]));
                        ListaSerii.add(s);
                    }
                    List<CwiczenieDoPlanu> listaCw = new ArrayList<CwiczenieDoPlanu>();

                    for (Integer i = 0; i < serie.length; i++) {
                        List<Seria> listaS = new ArrayList<Seria>();
                        for (Integer j = 0; j < serie[i]; j++) {
                            listaS.add(ListaSerii.get(j));
                        }
                        CwiczenieDoPlanu cw = new CwiczenieDoPlanu(listaPlanow.get(position).getListaIdCwiczen().get(i), listaS);
                        listaCw.add(cw);
                    }
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

                    long elapsedMillis = (SystemClock.elapsedRealtime() - chrono.getBase()) / 1000;
                    int workoutTime = (int) (elapsedMillis / 60);

                    final String formattedDate = df.format(c);
                    final WykonanyPlan wPlan = new WykonanyPlan(idPlanu, listaCw, formattedDate, workoutTime); //tutaj dodac czas

                    Integer licznik1 = 0;
                    if (wPlany.isEmpty()) {
                        wPlany.add(wPlan);
                    } else {
                        for (int i = 0; i < wPlany.size(); i++) {
                            if (formattedDate.toString().equals(wPlany.get(i).getData().toString()) && wPlan.getId().toString().equals(wPlany.get(i).getId().toString())) {
                                wPlany.get(i).setListaCwiczen(wPlan.getListaCwiczen());
                            } else {
                                licznik1++;
                            }
                        }
                    }
                    int l = Integer.parseInt(licznik1.toString());
                    if (l == wPlany.size()) {
                        wPlany.add(wPlan);
                    }
                    int s1=0;
                    int s2=0;
                    for(int i=0;i<ciezary.length;i++)
                    {
                        s1+=Integer.parseInt(ciezary[i]);
                        s2+=Integer.parseInt(powtorzenia[i]);
                    }
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Koniec Treningu");
                    alertDialog.setMessage("Czas Treningu : " + workoutTime + "\nSrednia Ciężaru :" + (s1/ciezary.length) + "\nSrednia Powtórzeń : " + (s2/powtorzenia.length) );
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Zakończ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child("wykonany_plan").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).setValue(wPlany);
                                    Toast.makeText(getActivity(), "Zakończono Trening", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    Fragment fragment = new fragmentHome();
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.content_frame, fragment,"HOME");
                                    ft.commit();
                                }
                            });
                    alertDialog.show();

                }
                else
                {
                    Toast.makeText(getActivity(), "Uzupełnij Dane", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Wykonaj Plan");
    }
}