package aplikacja.projektzespokowy2019;

// importujesz wszystkie bibloiteki
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PanelLogowaniaActivity extends Fragment {

    View v;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button buttonLogin;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private ProgressDialog progressDialog;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // przypisanie widku
            View v = inflater.inflate(R.layout.activity_panel_logowania, container, false);
            // zczytujesz  butoony i edittexty z xml layouta
            buttonLogin = (Button) v.findViewById(R.id.buttonZaloguj);
            editTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
            editTextPassword = (EditText) v.findViewById(R.id.editTextPassword);
            // mAuth i mUser jest do BAzy Danych
            // Potrzeben do Autoryzacji urzytkownika czy istnieje i logowanie
            mAuth = FirebaseAuth.getInstance();
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            // mUser jest do zcztujesz id urzytkownika z bazy
            //Progres dialog ładowanie
            progressDialog = new ProgressDialog(getActivity());

            // onClicka na button zaloguj
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // po wcisnieciu buttona wywołuje sie ta metoda
                    userLogin();
                }
            });
            // zwaracasz widok
            return v;
    }

    private void userLogin(){
            // zczytujemy wartości z edittextów  password i email
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        // validacja email
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Podaj Email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Podaj Hasło",Toast.LENGTH_LONG).show();
            return;
        }

        // sprawdzasz czy jest internet
        if (isConnectingToInternet(getContext()) == false) {
            Toast.makeText(getContext(), "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
        }else
        { // w przeciwnym wypadku  odpalsz progres dialog
            progressDialog.setMessage("Logowanie Trwa...");
            // wyswietla sie
            progressDialog.show();
            // tutaj jest logowanie bezposrednio z emial i pass
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // jesli sie zalogował
                            if(task.isSuccessful()){
                                // Powiadomienei wyswietlamy
                                Toast.makeText(getActivity(),"Zalogowano",Toast.LENGTH_LONG).show();
                                // zmieniamy activity (inne okno) Menu sie odpala
                                Intent intent = new Intent(getActivity(), PanelMenuActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getActivity(),"Logowanie Nieudane", Toast.LENGTH_LONG).show();
                            }
                            // progres dialog usuwsz
                            progressDialog.dismiss();
                        }
                    });
        }

    }

    // tutaj mamy metode sprawdzajaca czy jest podpiecie do internetu
    private boolean isConnectingToInternet(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
