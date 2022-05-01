package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xw.repo.BubbleSeekBar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import aplikacja.projektzespokowy2019.R;
import model.Cwiczenie;

public class fragmentDodajCwiczenie extends Fragment {

    private TextInputLayout textInputNazwaCwiczenia;
    private TextInputLayout textInputOpis;

    private Button buttonDodajCwiczenie;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Activity mActivity;
    private BubbleSeekBar bubbleSeekBar1;
    private TextView tV_partia_ciala;
    String Wybrane = "";

    private Pattern workoutName_check = Pattern.compile("[^a-z0-9()ąęńźżłóść ]", Pattern.CASE_INSENSITIVE);
    private Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_dodaj_cwiczenie, container, false);


        bubbleSeekBar1 = v.findViewById(R.id.demo_3_seek_bar_1);
        setConfigToBubbleBar();

        textInputNazwaCwiczenia = v.findViewById(R.id.text_input_naz_cw);
        textInputOpis = v.findViewById(R.id.text_input_opis);
        tV_partia_ciala = (TextView) v.findViewById(R.id.textViewPartiaCiala);



        buttonDodajCwiczenie = (Button) v.findViewById(R.id.btnDodajCwiczenie);
        final Random generator = new Random();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tV_partia_ciala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.wybPartieCiala));
                final String[] partie = {"Biceps", "Triceps", "Klatka Piersiowa", "Brzuch", "Nogi", "Plecy","Barki","Inne"};
                final int checkedItem = -1; // cow
                builder.setSingleChoiceItems(partie, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Wybrane = partie[which];
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tV_partia_ciala.setText(Wybrane);
               tV_partia_ciala.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        buttonDodajCwiczenie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateNazwaCwiczenia() | !validateOpis() || !validatePartia() ) {
                    return;
                } else {


               String a = textInputNazwaCwiczenia.getEditText().getText().toString();
                       String b= tV_partia_ciala.getText().toString();
                       String cd=  textInputOpis.getEditText().getText().toString();
                       int d =    bubbleSeekBar1.getProgress();


                    Integer w = generator.nextInt(10000000);
                    Cwiczenie c = new Cwiczenie(w, textInputNazwaCwiczenia.getEditText().getText().toString(), tV_partia_ciala.getText().toString(),
                            textInputOpis.getEditText().getText().toString(),
                            bubbleSeekBar1.getProgress());

                    databaseReference.child("wlasne_cwiczenia").child(firebaseAuth.getCurrentUser().getUid()).child(w.toString()).setValue(c);
                    Toast.makeText(getActivity(), "Dodano Cwiczenie", Toast.LENGTH_LONG).show();
                   textInputNazwaCwiczenia.getEditText().setText("");
                    textInputOpis.getEditText().setText("");
                    tV_partia_ciala.setText(R.string.wybPartieCiala);
                }

            }
        });


        return v;
    }

    private boolean validatePartia() {


        if(tV_partia_ciala.getText() == getResources().getString(R.string.wybPartieCiala))
        {
            tV_partia_ciala.setTextColor(getResources().getColor(R.color.colorPrimary));
          Toast.makeText(getView().getContext(),"Nie wybrano partii ciała.",Toast.LENGTH_SHORT).show();

            return false;
        }else
        {
            return true;

        }
    }

    private boolean validateNazwaCwiczenia() {

        String workout_name = textInputNazwaCwiczenia.getEditText().getText().toString().trim();
        Matcher m = workoutName_check.matcher(workout_name);
        Matcher m1 = special.matcher(workout_name);
        Boolean b = m.find();
        Boolean b1 = m1.find();

        if (workout_name.isEmpty()) {
            textInputNazwaCwiczenia.setError("Pole nie może być puste.");
            return false;
        } else if (b) {
            textInputNazwaCwiczenia.setError("Zakazane znaki. Dozwolone [A-Z][a-z][0-9].");
        return false;
        }else if (b1) {
            textInputNazwaCwiczenia.setError("Niedozwolony znak [!@#$%&*()_+=|<>?{}\\[\\]~-]");
            return false;
        } else {
            textInputNazwaCwiczenia.setError(null);
            return true;
        }
    }

    private boolean validateOpis() {
        String usernameInput = textInputOpis.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputOpis.setError("Pole nie może być puste.");
            return false;
        } else if (usernameInput.length() > 150) {
            textInputOpis.setError("Za dużo znaków.");
            return false;
        } else {
            textInputOpis.setError(null);
            return true;
        }
    }


    private void setConfigToBubbleBar() {

        bubbleSeekBar1.getConfigBuilder()
                .min(1)
                .max(10)
                .progress(4)
                .sectionCount(9)
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Dodaj Cwiczenie");
    }
}