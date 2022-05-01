package aplikacja.projektzespokowy2019;

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

public class PanelRejestracjiActivity extends Fragment {

    View v;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button buttonNoweKonto;
    private EditText editTextEmail;
    private  EditText editTextPassword;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_panel_rejestracji, container, false);

        buttonNoweKonto = (Button) v.findViewById(R.id.buttonZalozKonto);
        editTextEmail = (EditText) v.findViewById(R.id.editTextEmailRejestracja);
        editTextPassword = (EditText) v.findViewById(R.id.editTextPasswordRejestracja);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(getActivity());

        buttonNoweKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return v;
    }


    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) ||  !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getActivity(),"Podaj Prawidłowy Adres Email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password) || password.length() < 4 || password.length() > 10 ){
            Toast.makeText(getActivity(),"Podaj Prawidłowe Hasło",Toast.LENGTH_LONG).show();
            return;
        }

        if (isConnectingToInternet(getContext()) == false) {
            Toast.makeText(getContext(), "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
        }else
        {
            progressDialog.setMessage("Rejestracja trwa ...");
            progressDialog.show();

            // tworzysz urzytkownika
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getActivity(),"Rejestracja Udana",Toast.LENGTH_LONG).show();
                                // tutaj przechodzisz do okna w ktorym uzupełniasz swoje dane
                                Intent intent = new Intent(getActivity(), PanelInfoOUzytkownikuActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getActivity(),"Błąd Rejestracji",Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
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
