package aplikacja.projektzespokowy2019;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Informacje;
import model.Waga;

public class PanelInfoOUzytkownikuActivity extends AppCompatActivity {

    private Button buttonGotowe;
    private EditText Imie;
    private EditText DataUrodzenia;
    private EditText Waga3;
    private EditText Wzrost;
    private TextInputLayout textInpImie;
    private TextInputLayout textInpWaga;
    private TextInputLayout textInpWzrost;
    private RadioButton Kobieta;
    private RadioButton Mezczyzna;
    private Button Kalendaz;
    private DatePickerDialog datePicker;
    private String Plec;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<Waga> Waga1 = new ArrayList<Waga>();
    private Pattern name_check = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // wczytujesz widok layouta z xml
        setContentView(R.layout.activity_panel_info);
        // zczytujemy wszystkie komponenty z xml
        buttonGotowe = (Button) findViewById(R.id.buttonGotowe);
        DataUrodzenia = (EditText) findViewById(R.id.editTextData);
        textInpWzrost = (TextInputLayout) findViewById(R.id.textInputWzrost);
        textInpWaga = (TextInputLayout) findViewById(R.id.textInputWaga);
        textInpImie = (TextInputLayout) findViewById(R.id.textInputImie);
        // radio butttony
        Kobieta = (RadioButton) findViewById(R.id.radioKobieta);
        Mezczyzna = (RadioButton) findViewById(R.id.radioMezczyzna);
        // button na odpalenie kalendaza
        Kalendaz = (Button) findViewById(R.id.buttonDateTimePicker);
       // Baza danych potrzebene do dodania informacji do bazy danych Firebase relacyjna baza danych
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Oclick na Klanedaz
        Kalendaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // wyswietla sie kalendaz
                datePicker = new DatePickerDialog(PanelInfoOUzytkownikuActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // to jest potrzebne przy wypisywaniu były poprawne daty np 1 zamiast zera w miesiacu
                                month = month+1;
                                // po wybraniu daty zmieniamy w dataurodzenia date (text)
                                DataUrodzenia.setText(day + "/" + month + "/" + year);
                                // ustawiasz domyslna date
                            }
                        }, 2000, 0, 1);
                // pokazywanie kalendaza
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        // Onclick na Button Gotowe
        buttonGotowe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sprawdzamy poprawnosc danych
                if (validateName()) {
                    if( validateVieght())
                    {
                        if(validateHeight())
                        {
                            // tutaj sprawdzamy co mamy wybrane płeć
                            if (Kobieta.isChecked()) {
                                Plec = "Kobieta";
                            } else if (Mezczyzna.isChecked()) {
                                Plec = "Meżczyzna";
                            }
                            // jeśli data jest wybrana to dodajemy dane do bazy danych
                            if(!DataUrodzenia.getText().toString().equals(""))
                            {
                                // pobieramy date z kalendarza jak wybraliśmy
                                Date c = Calendar.getInstance().getTime();
                                // wyswietlamy date w konsoli nie potrzebne
                                System.out.println("Current time => " + c);

                                // to jest kowersja daty w formacie Date na format String bo baza musi przyjac Stringa
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(c);
                                // Jest zrobiona obiekt Waga ktory ma date i Wage  POtrzebne jest to do Listy wag ktora jest potrzeban do wykresu wag
                                Waga w = new Waga(formattedDate, Integer.parseInt(textInpWaga.getEditText().getText().toString()   ));
                                // Dodajesz do listy obiekt
                                Waga1.add(w);
                                // tworzymy obiket informacje  pobierasz imie editexta i zmiana na stringa  i pozniej dodajemy liste wag
                                Informacje Informacje = new Informacje(textInpImie.getEditText().getText().toString(),
                                        DataUrodzenia.getText().toString(), Plec, Integer.parseInt(textInpWzrost.getEditText().getText().toString()),
                                        Waga1);
                                // tutaj wrzucmy obiekt Inforacje do bazy danych
                                // podajesz  Iformacje , id Urztykownika  , na koncu zmieniasz wartosc  na ten oiekt ktory zosatł wczesniej utworzony
                                // Dodajesz to do bazy danych po to aby pozniej wyswietlac inforamcje na temat urzytkownika i śledzić jego zmiane wagi ciała
                                databaseReference.child("Informacje").child(firebaseAuth.getCurrentUser().getUid()).setValue(Informacje);
                                // przechodzisz z bierzacego okna do Menu jestes zarejestrowany i masz uzupełnione dane
                                Intent intent = new Intent(PanelInfoOUzytkownikuActivity.this, PanelMenuActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                // jesli nie mamy daty urodenia wyswietlamy komunikat
                                Toast.makeText(getApplicationContext(),"Podaj Date Urodzenia",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });

    }

    // funkcja ktora sprawdza poprwnosc wpisanej wysokosci
    private boolean validateHeight() {

        if (textInpWzrost.getEditText().getText().toString().isEmpty()) {
            textInpWzrost.setError("Pole nie może być puste.");
            return false;
        } else if (Integer.parseInt(textInpWzrost.getEditText().getText().toString()) < 120 || Integer.parseInt(textInpWzrost.getEditText().getText().toString()) > 250) {
            textInpWzrost.setError("Wzrost musi byc w granicach (120-250)cm");
            return false;
        } else {
            return true;
        }
    }

    // funkcja sprawdzajaca  wage
    private boolean validateVieght() {

        if (textInpWaga.getEditText().getText().toString().isEmpty()) {
            textInpWaga.setError("Pole nie może być puste.");
            return false;
        } else if ((Integer.parseInt(textInpWaga.getEditText().getText().toString()) < 40 || Integer.parseInt(textInpWaga.getEditText().getText().toString())> 250) ){
            textInpWaga.setError("Waga musi byc w granicach (40-250)kg");
            return false;
        } else {
            return true;
        }
    }



// funkcje sprawdzajaca imie
    private boolean validateName() {
        String workout_name = textInpImie.getEditText().getText().toString().trim();
        Matcher m = name_check.matcher(workout_name);
        Boolean b = m.find();

        if (workout_name.isEmpty()) {
            textInpImie.setError("Pole nie może być puste.");
            return false;
        } else if (b) {
            textInpImie.setError("Zakazane znaki. Dozwolone [A-Z][a-z].");
            return false;
        } else {
            textInpImie.setError(null);
            return true;
        }
    }

}
