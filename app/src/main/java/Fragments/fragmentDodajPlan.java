package Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aplikacja.projektzespokowy2019.CustomListAdapter;
import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.Plan;

import static android.content.ContentValues.TAG;

public class fragmentDodajPlan extends Fragment {

    private Button btDodajCwiczenie;
    private Integer i = 0;
    private List<Integer> listaWybranychBiceps = new ArrayList<Integer>();
    private List<Integer> listaWybranychTriceps = new ArrayList<Integer>();
    private List<Integer> listaWybranychKlata = new ArrayList<Integer>();
    private List<Integer> listaWybranychBrzuch = new ArrayList<Integer>();
    private List<Integer> listaWybranychNogi = new ArrayList<Integer>();
    private List<Integer> listaWybranychPlecy = new ArrayList<Integer>();
    private List<Integer> listaWybranychBarki = new ArrayList<Integer>();
    private List<Integer> listaWybranychInne = new ArrayList<Integer>();
    private List<Cwiczenie> listaBiceps = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaTriceps = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaKlata = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaBrzuch = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaNogi = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaPlecy = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaBarki = new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaInne= new ArrayList<Cwiczenie>();
    private List<Cwiczenie> listaWszystkich = new ArrayList<Cwiczenie>();
    ListView listView;
    ListView listView1;
    private Button Biceps;
    private Button Triceps;
    private Button Klata;
    private Button Brzuch;
    private Button Nogi;
    private Button Plecy;
    private Button Barki;
    private Button Inne;
    private Button DodajPlan;
    private TextInputLayout textInputNazwaPlanu;
    CustomListAdapter adapter1 = null;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Pattern workoutName_check = Pattern.compile("[^a-z0-9()ąęńźżłóść ]", Pattern.CASE_INSENSITIVE);
    private Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_fragment_dodaj_plan, container, false);

        textInputNazwaPlanu = v.findViewById(R.id.text_input_naz_planu);

        btDodajCwiczenie = (Button) v.findViewById(R.id.btnDodajCwiczenieDoPlanu);
        listView1 = (ListView) v.findViewById(R.id.listViewDialogg);
        DodajPlan = (Button) v.findViewById(R.id.btnDodajPlan);
        final Random generator = new Random();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        btDodajCwiczenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaWszystkich.clear();
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_dialog2);
                dialog.setCancelable(true);
                Biceps = (Button) dialog.findViewById(R.id.buttonBiceps);
                Triceps = (Button) dialog.findViewById(R.id.buttonTriceps);
                Klata = (Button) dialog.findViewById(R.id.buttonKlata);
                Brzuch = (Button) dialog.findViewById(R.id.buttonBrzuch);
                Nogi = (Button) dialog.findViewById(R.id.buttonNogi);
                Plecy = (Button) dialog.findViewById(R.id.buttonPlecy);
                Barki = (Button) dialog.findViewById(R.id.buttonBarki);
                Inne = (Button) dialog.findViewById(R.id.buttonInne);

                dialog.show();

                Biceps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaBiceps, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychBiceps.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });

                Triceps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);


                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaTriceps, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychTriceps.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });


                        dialog1.show();
                    }

                });

                Klata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaKlata, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychKlata.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });

                Brzuch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaBrzuch, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychBrzuch.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });

                Nogi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaNogi, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychNogi.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });

                Plecy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaPlecy, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychPlecy.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });
                Barki.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaBarki, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychBarki.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });
                Inne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dialog1 = new Dialog(getActivity());
                        dialog1.setContentView(R.layout.layout_dialog);
                        dialog1.setCancelable(true);

                        listView = (ListView) dialog1.findViewById(R.id.listViewDialog);
                        CustomListAdapter adapter = new CustomListAdapter(getActivity(), listaInne, 0);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                listaWybranychInne.add(position);
                                ListView();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });
            }
        });

        DodajPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> ListaKoncowa = new ArrayList<Integer>();
                if (!validateNazwaPlanu()) {

                } else {
                    for (Integer i = 0; i < listView1.getCount(); i++) {
                        View w = listView1.getChildAt(i);
                        CheckBox check = (CheckBox) w.findViewById(R.id.checkBox1);
                        if (check.isChecked()) {
                            ListaKoncowa.add(adapter1.getLista().get(i).getId());
                        }

                        if(ListaKoncowa.isEmpty())
                        {
                            Toast.makeText(getActivity(), "Brak wybranych Cwiczeń", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Integer w1 = generator.nextInt(10000000);
                            Plan p = new Plan(w1, textInputNazwaPlanu.getEditText().getText().toString(), ListaKoncowa);
                            databaseReference.child("wlasne_plany").child(firebaseAuth.getCurrentUser().getUid()).child(w1.toString()).setValue(p);
                            Toast.makeText(getActivity(), "Dodano Plan", Toast.LENGTH_LONG).show();
                            textInputNazwaPlanu.getEditText().setText("");
                            Fragment fragment = new fragmentHome();

                            if (fragment != null) {
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment,"HOME");
                                ft.commit();
                            }
                        }
                    }
                }
            }
        });

        return v;
    }

    private boolean validateNazwaPlanu() {


        String workout_name = textInputNazwaPlanu.getEditText().getText().toString().trim();
        Matcher m = workoutName_check.matcher(workout_name);
        Matcher m1 = special.matcher(workout_name);
        Boolean b = m.find();
        Boolean b1 = m1.find();

        if (workout_name.isEmpty()) {
            textInputNazwaPlanu.setError("Pole nie może być puste.");
            return false;
        } else if (b) {
            textInputNazwaPlanu.setError("Zakazane znaki. Dozwolone [A-Z][a-z][0-9].");
            return false;
        } else if (b1) {
            textInputNazwaPlanu.setError("Niedozwolony znak [!@#$%&*()_+=|<>?{}\\[\\]~-]");
            return false;
        } else {
            textInputNazwaPlanu.setError(null);
            return true;
        }
    }


    public void ListView() {

        for (Integer i = 0; i < listaBiceps.size(); i++) {
            for (Integer j = 0; j < listaWybranychBiceps.size(); j++) {
                if (i == listaWybranychBiceps.get(j)) {
                    listaWszystkich.add(listaBiceps.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaTriceps.size(); i++) {
            for (Integer j = 0; j < listaWybranychTriceps.size(); j++) {
                if (i == listaWybranychTriceps.get(j)) {
                    listaWszystkich.add(listaTriceps.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaKlata.size(); i++) {
            for (Integer j = 0; j < listaWybranychKlata.size(); j++) {
                if (i == listaWybranychKlata.get(j)) {
                    listaWszystkich.add(listaKlata.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaBrzuch.size(); i++) {
            for (Integer j = 0; j < listaWybranychBrzuch.size(); j++) {
                if (i == listaWybranychBrzuch.get(j)) {
                    listaWszystkich.add(listaBrzuch.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaNogi.size(); i++) {
            for (Integer j = 0; j < listaWybranychNogi.size(); j++) {
                if (i == listaWybranychNogi.get(j)) {
                    listaWszystkich.add(listaNogi.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaPlecy.size(); i++) {
            for (Integer j = 0; j < listaWybranychPlecy.size(); j++) {
                if (i == listaWybranychPlecy.get(j)) {
                    listaWszystkich.add(listaPlecy.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaBarki.size(); i++) {
            for (Integer j = 0; j < listaWybranychBarki.size(); j++) {
                if (i == listaWybranychBarki.get(j)) {
                    listaWszystkich.add(listaBarki.get(i));
                }
            }
        }
        for (Integer i = 0; i < listaInne.size(); i++) {
            for (Integer j = 0; j < listaWybranychInne.size(); j++) {
                if (i == listaWybranychInne.get(j)) {
                    listaWszystkich.add(listaInne.get(i));
                }
            }
        }


        adapter1 = new CustomListAdapter(getActivity(), listaWszystkich, 1);
        listView1.setAdapter(adapter1);


    }

    public void basicQueryValueListener() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("wlasne_cwiczenia").child(currentFirebaseUser.getUid().toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaBiceps.clear();
                listaTriceps.clear();
                listaKlata.clear();
                listaBrzuch.clear();
                listaNogi.clear();
                listaPlecy.clear();
                listaBarki.clear();
                listaInne.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Cwiczenie cw = childDataSnapshot.getValue(Cwiczenie.class);
                    if(cw.getPartiaCiala().toString().equals("Biceps"))
                    {
                        listaBiceps.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Triceps"))
                    {
                        listaTriceps.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Klatka Piersiowa"))
                    {
                        listaKlata.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Brzuch"))
                    {
                        listaBrzuch.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Nogi"))
                    {
                        listaNogi.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Plecy"))
                    {
                        listaPlecy.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Barki"))
                    {
                        listaBarki.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Inne"))
                    {
                        listaInne.add(cw);
                    }
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
                    if(cw.getPartiaCiala().toString().equals("Biceps"))
                    {
                        listaBiceps.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Triceps"))
                    {
                        listaTriceps.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Klatka Piersiowa"))
                    {
                        listaKlata.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Brzuch"))
                    {
                        listaBrzuch.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Nogi"))
                    {
                        listaNogi.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Plecy"))
                    {
                        listaPlecy.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Barki"))
                    {
                        listaBarki.add(cw);
                    }
                    else if(cw.getPartiaCiala().toString().equals("Inne"))
                    {
                        listaInne.add(cw);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Dodaj Plan");
    }

    @Override
    public void onStart() {
        super.onStart();
        basicQueryValueListener();

    }
}
