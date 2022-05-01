package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;
import model.Waga;

import static android.content.ContentValues.TAG;

public class fragmentMojeZdrowie extends Fragment {

    public static List<Waga> Lista = new ArrayList<Waga>();
    private EditText Waga1;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private View v;
    private View v1;
    private  LinearLayout linearLayout;
    private TextView text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.activity_fragment_moje_zdrowie, container, false);
        v1 = inflater.inflate(R.layout.layout_waga,container,false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) v.findViewById(R.id.activity_fragmet_moje_zdrowie_layout);
        text  = (TextView) v1.findViewById(R.id.nameWaga);

        DownloadFromDataBase();
        return v;
    }

    public void DownloadFromDataBase()
    {
        Lista.clear();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("Informacje").child(currentFirebaseUser.getUid().toString()).child("listaWagi");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Waga w = childDataSnapshot.getValue(Waga.class);
                    Lista.add(w);
                }
                Waga();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void AddToDataBase()
    {
        databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).child("listaWagi").setValue(Lista);
        Toast.makeText(getActivity(), "Zaktualizowano Wagę !", Toast.LENGTH_LONG).show();
    }


    public void Waga()
    {
        linearLayout.removeView(v1);
        linearLayout.addView(v1);
        Waga1 = (EditText) v1.findViewById(R.id.editextAktualzacjaWagi);
        Waga1.setText("");

        Button buttonAktualizuj  = (Button) v1.findViewById(R.id.buttonAktualizuj);

        buttonAktualizuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Waga1.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Wprowadź Wage", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if( Integer.parseInt(Waga1.getText().toString())<40 || Integer.parseInt(Waga1.getText().toString())>250 )
                    {
                        Toast.makeText(getActivity(),  "Waga musi byc w granicach (40-250)kg", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c);
                        Integer licznik=-1;
                        for(Integer i=0;i<Lista.size();i++)
                        {
                            if(Lista.get(i).getData().equals(formattedDate))
                            {
                                licznik = i;
                            }
                        }

                        if(licznik!=-1)
                        {
                            Lista.get(licznik).setWaga(Integer.parseInt(Waga1.getText().toString()));
                        }
                        else
                        {
                            Waga w = new Waga(formattedDate,Integer.parseInt(Waga1.getText().toString()));
                            if(Lista.size()==1)
                            {
                                if(Lista.get(0).getWaga().equals(0))
                                {
                                    Lista.clear();
                                }
                            }
                            Lista.add(w);
                        }
                        AddToDataBase();
                        Lista.clear();


                        Fragment fragment = new fragmentHome();

                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment,"HOME");
                            ft.commit();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Moje Zdrowie");
    }
}
