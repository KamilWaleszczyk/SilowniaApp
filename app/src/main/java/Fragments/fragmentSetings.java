package Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aplikacja.projektzespokowy2019.MainActivity;
import aplikacja.projektzespokowy2019.R;
import model.Informacje;
import model.Waga;

import static android.content.ContentValues.TAG;

public class fragmentSetings extends Fragment {

    private View v;
    private View v1;
    private View v2;
    private View v3;
    private LinearLayout linearLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private String OldPass;
    private FirebaseUser user;
    Fragment fragment = null;
    private Informacje inf;
    private DatabaseReference databaseReference;
    private DatePickerDialog datePicker;
    private           String Data = null;
    private EditText AktualizacjaImie;
    private EditText AktualizacjaData;
    private EditText AktualizacjaWzrost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ustwaiasz widoki na g??owny widok cardview na usuwanie planu ,zmiane has??a i aktualizacje inforamcji
        v = inflater.inflate(R.layout.activity_fragments_setings, container, false);v1 = inflater.inflate(R.layout.layout_usun_konto,container,false);
        v1 = inflater.inflate(R.layout.layout_usun_konto,container,false);
        v2 = inflater.inflate(R.layout.layout_zmiana_hasla,container,false);
        v3 = inflater.inflate(R.layout.laytout_aktualizacja_info,container,false);
      // zczytywanie komponentow
        linearLayout = (LinearLayout) v.findViewById(R.id.activity_fragmet_settings);
         AktualizacjaImie =(EditText) v3.findViewById(R.id.editTextAktualizacjaImie);
        AktualizacjaData =(EditText) v3.findViewById(R.id.editTextAktualizacjaData);
         AktualizacjaWzrost =(EditText) v3.findViewById(R.id.editTextAktualizacjaWzrost);
         // progres dialog
        progressDialog = new ProgressDialog(getActivity());
        // dodanie i pobranei z bazy danych
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        // wywo??ujesz funkcje
        UsunKonto();
        ZmianaHasla();
        AktualizacjaInfo();
        // zwracasz widok
        return v;
    }

    public void UsunKonto()
    {

        TextView text  = (TextView) v1.findViewById(R.id.nameUsunKonto);
        text.setText("Usu?? Konto");
        // usowasz widok z g??ownego layoouta i ponizej go dodajesz
        // usowasz go daltego bo wywala b????dy
        linearLayout.removeView(v1);
        linearLayout.addView(v1);
        // zczytujesz buttona
        Button UsunKonto = (Button) v1.findViewById(R.id.buttonUsunKonto);
        // onclick na buttona
        UsunKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // wywietlasz dialog z decyzja czy napewno chcesz to zrobic
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Czy Napewno Chcesz Usun???? Konto?");
                        dialog.setMessage("Usuni??cie wszystkich danych spowoduje utrat?? wszysytkich danych zwiazanych z tym u??ytkownikiem");
                        // onclick na buttona usu??
                        dialog.setPositiveButton("Usu??", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // wyswietlasz progres dilog
                                progressDialog.setMessage("Usuwanie Konta trwa ...");
                                progressDialog.show();
                                // Baza danych
                                firebaseUser = firebaseAuth.getCurrentUser();
                                // delete na usuwanie konta
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.cancel();
                                        // jeseli sie uda??o
                                        if(task.isSuccessful())
                                        {
                                            DatabaseReference baza = FirebaseDatabase.getInstance().getReference();
                                            baza.child("Informacje").child(firebaseUser.getUid()).setValue(null);
                                            Toast.makeText(getActivity(), "Konto zosta??o Usuni??te", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra("czyWylogowac","nie");
                                            startActivity(intent);
                                            return;

                                        }else
                                        {
                                            Toast.makeText(getActivity(), "Co?? posz??o nie tak , Spr??buj Ponownie", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                });
                            }
                        });
                        // onclick na anuluj
                        dialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        // bufudje si dialog i pokazuje sie
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.show();
            }
        });
    }

    public void ZmianaHasla()
    {

        TextView text  = (TextView) v2.findViewById(R.id.nameZmainaHasla);
        text.setText("Zmie?? Has??o");
        linearLayout.removeView(v2);
        linearLayout.addView(v2);

        // final aby mozna by??o uzyc tej zmiennej g??ebeiej w funkcjach
        final EditText OldPassw = (EditText) v2.findViewById(R.id.editTextZmianaHasla);
        OldPassw.setText("");
        Button ZmienHaslo = (Button) v2.findViewById(R.id.buttonZamianaHasla);
// musisz podac stare has??o aby przejs?? autoryzacje
        ZmienHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // jesli nie  jest pusty edittext
                if(!OldPassw.getText().toString().equals(""))
                {
                    // pobierasz stare has??o
                    OldPass = OldPassw.getText().toString().trim();
                    // urzytkownik z danycmi
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    // pobierasz email urzytkownika
                    final String email = user.getEmail();

                    AuthCredential credential = EmailAuthProvider.getCredential(email, OldPass);

                    // procedura do zmaiany has??a
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // jesli poda??es dobre stare has??o to wyswietla ci sie edittexty na nowe has??a
                                LinearLayout linear = (LinearLayout) v2.findViewById(R.id.linearLayoutZmianaHasla);
                                ConstraintLayout constraint = (ConstraintLayout) v2.findViewById(R.id.constraintLayoutZmianaHasla);
                                constraint.setVisibility(View.GONE);
                                linear.setVisibility(View.VISIBLE);
                                Button buttonzmiana = (Button) v2.findViewById(R.id.buttonZmienHaslo2);
                                // onClick na zmianie has??a
                                buttonzmiana.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        EditText PotwierdzPassword = (EditText) v2.findViewById(R.id.editTextZmianaHaslaPoprawneNowe);
                                        EditText Password = (EditText) v2.findViewById(R.id.editTextZmianaHaslaNowe);

                                        // pobierasz nowe has??o i potwierdzeie
                                        final String password = Password.getText().toString().trim();
                                        String potwierdzPassword = PotwierdzPassword.getText().toString().trim();

                                        // validacja podanych danych
                                        if (TextUtils.isEmpty(password) || password.length() < 4 || password.length() > 10) {
                                            Toast.makeText(getActivity(), "Podaj Poprawne Has??o", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        if (!password.equals(potwierdzPassword)) {
                                            Toast.makeText(getActivity(), "Has??a si?? nie zgadzaj??", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        progressDialog.setMessage("Zmiana Has??a trwa ...");
                                        progressDialog.show();

                                        // tutaj aktualizajusz has??o
                                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Co?? posz??o nie tak ,Spr??buj ponownie za chwile", Toast.LENGTH_LONG).show();
                                                    fragment = new fragmentSetings();
                                                } else {
                                                    Toast.makeText(getActivity(), "Has??o Zmodyfikowane", Toast.LENGTH_LONG).show();
                                                    fragment = new fragmentHome();
                                                }
                                                progressDialog.dismiss();

                                                if (fragment != null) {
                                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                    ft.replace(R.id.content_frame, fragment);
                                                    ft.commit();
                                                }
                                            }
                                        });
                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), "Uwierzytelnianie nie powiod??o si??, Podaj Poprawne Has??o!", Toast.LENGTH_LONG).show();
                                fragment = new fragmentSetings();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "Wprowad?? Stare Has??o", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void AktualizacjaInfo()
    {

        TextView text  = (TextView) v3.findViewById(R.id.nameAktualizacjaInfo);
        text.setText("Zaktualizuj Swoje Dane");
        linearLayout.removeView(v3);
        linearLayout.addView(v3);

        final Button Aktualizuj = (Button) v3.findViewById(R.id.buttonAktualizacjaInfo);
        final RadioButton PlecM = (RadioButton) v3.findViewById(R.id.radioMezczyznaAkt);
        final RadioButton PlecK = (RadioButton) v3.findViewById(R.id.radioKobietaAkt);

        // PObierasz z bazy danych Informacje po to aby wype??ni?? na wstepie jakie by??y wczesniejsze inforamcje
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String Uid;
        // Uid to jest id uzytkownika
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        // podajesz sciezke do pobrania dnaych skad ma pobrac
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/" + Uid);
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // do inf pobierasz informacje
                 inf = dataSnapshot.getValue(Informacje.class);
                 if(inf!=null)
                 {
                     // wypissane sa poprzednie dane
                     AktualizacjaImie.setText(inf.getImie());
                     AktualizacjaData.setText(inf.getDataUrodzenia());
                     AktualizacjaWzrost.setText(inf.getWzrost().toString());
                     String Plec = inf.getPlec();

                     if(Plec.equals("Me??czyzna"))
                     {
                         PlecM.setChecked(true);
                     }
                     else if(Plec.equals("Kobieta"))
                     {
                         PlecK.setChecked(true);
                     }

                     // znow kalendaz tak samo jak w w PanelInfo
                     Button data = (Button) v3.findViewById(R.id.buttonDateTimePickerAktualizcja);
                     data.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                             final Integer dzien = Integer.parseInt(AktualizacjaData.getText().toString().substring(0,2));
                             final  Integer mies = Integer.parseInt(AktualizacjaData.getText().toString().substring(3,4));
                             final Integer rok = Integer.parseInt(AktualizacjaData.getText().toString().substring(5,9));
                             datePicker = new DatePickerDialog(getContext(),
                                     new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                             month = month+1;
                                             AktualizacjaData.setText(day + "/" + month + "/" + year);
                                         }
                                     }, rok, mies, dzien);
                             datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                             datePicker.show();
                         }
                     });
                 }
                 else
                 {
                     // jesli nie masz informacji w bazie danych
                     AktualizacjaData.setText("");
                     AktualizacjaData.setHint("Date Urodzenia");
                     AktualizacjaImie.setText("");
                     AktualizacjaImie.setHint("Imi??");
                     AktualizacjaWzrost.setText("");
                     AktualizacjaWzrost.setHint("Wzrost");
                     Button data = (Button) v3.findViewById(R.id.buttonDateTimePickerAktualizcja);
                     data.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                             datePicker = new DatePickerDialog(getContext(),
                                     new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                             month = month+1;
                                             AktualizacjaData.setText(day + "/" + month + "/" + year);
                                         }
                                     }, 2000, 0, 1);
                             datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                             datePicker.show();
                         }
                     });
                 }


                 // ONclik na button aktualzuj
                Aktualizuj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pocztkowo jest walidacja
                        int licznik=0;
                        String Sex = null, Imie = null;
                        Integer Wzrost = null;
                        if (PlecM.isChecked()) {
                            Sex = "Me??czyzna";
                        } else if (PlecK.isChecked()) {
                            Sex = "Kobieta";
                        }

                        if(Sex == null)
                        {
                            Toast.makeText(getActivity(), "Podaj P??e??", Toast.LENGTH_LONG).show();

                        }
                        else {
                            licznik++;
                        }

                        if(AktualizacjaImie.getText().toString().equals(""))
                        {
                            Toast.makeText(getActivity(), "Wprowad?? Imi??", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Imie = AktualizacjaImie.getText().toString();
                            licznik++;
                        }


                        if(AktualizacjaData.getText().toString().equals(""))
                        {
                            Toast.makeText(getActivity(), "Wprowad?? Date Urodzenia", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Data = AktualizacjaData.getText().toString();
                            licznik++;
                        }

                        if (AktualizacjaWzrost.getText().toString().equals(""))
                        {
                            Toast.makeText(getActivity(), "Wprowad?? Wzrost", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if( Integer.parseInt(AktualizacjaWzrost.getText().toString())<120 || Integer.parseInt(AktualizacjaWzrost.getText().toString())>250 )
                            {
                                Toast.makeText(getActivity(),  "Wzrost musi byc w granicach (120-250)cm", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Wzrost = Integer.parseInt(AktualizacjaWzrost.getText().toString());
                                licznik++;
                            }

                        }

                        // jesli wszystkie dane sa poprawne czyli licznik==4
if(licznik==4)
{
    Informacje nowe = null;
    List<Waga> list = new ArrayList<Waga>();
    // dodawana jest waga do listy wag (aktualizowana lista)
    Waga w = new Waga("",0);
    list.add(w);
    if(inf==null)
    {
        nowe = new Informacje(Imie,Data,Sex,Wzrost,list);
    }
    else
    {
        nowe = new Informacje(Imie,Data,Sex,Wzrost,inf.getListaWagi());
    }
    // wrzucane sa nowe dane do bazy danych
    databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(nowe);
    Toast.makeText(getActivity(), "Zaktualizowano Dane o U??ytkowniku", Toast.LENGTH_LONG).show();
    fragment = new fragmentHome();

    if (fragment != null) {
        // zmiana widoku na g??owny czyli Home
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment,"HOME");
        ft.commit();
    }
}

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //zmieniasz tytul widoku
        getActivity().setTitle("Ustawienia");
    }
}

