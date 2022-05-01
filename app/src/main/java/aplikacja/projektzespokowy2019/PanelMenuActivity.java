package aplikacja.projektzespokowy2019;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Fragments.fragmentDodajCwiczenie;
import Fragments.fragmentDodajPlan;
import Fragments.fragmentHome;
import Fragments.fragmentKalendazAktywnosci;
import Fragments.fragmentMojeZdrowie;
import Fragments.fragmentSetings;
import Fragments.fragmentSledzenieTrasy;
import Fragments.fragmentStatystyka;
import Fragments.fragmentUsunAktywnosc;
import Fragments.fragmentWykonajPlan;
import model.Informacje;

public class PanelMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TextView nazwaUzytkownika;
    TextView adresEmail;
    private DatabaseReference reference_Database;
    private FirebaseDatabase firebase_Database;
    private FirebaseAuth mAuth;
    private DatabaseReference dReference;
    private Informacje info;
    String Imie;
    FirebaseDatabase mDatabase; //usunac
    DatabaseReference  mDbRef;

    String nazwa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_panel_zmenu); //ustawia caly layaout

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;
        fragment = new fragmentHome();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment,"HOME");
            ft.commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START); //zamyka drawera

        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragmentHome f = (fragmentHome) getSupportFragmentManager().findFragmentByTag("HOME");
            if (f != null && f.isVisible()) {
                super.onBackPressed();
            }
            else
            {
                ft.replace(R.id.content_frame,new fragmentHome(),"HOME");
                ft.commit();
            }
        }
    }

    public void basicQueryValueListener() {

        String Uid;
        String  emeil;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Informacje/"+Uid+"/imie");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Imie=dataSnapshot.getValue(String.class);
                nazwaUzytkownika.setText("Witaj " + Imie);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        nazwaUzytkownika = (TextView) findViewById(R.id.tVNazwaUzytkownika);
       adresEmail = (TextView) findViewById(R.id.tVadresEmail);
        reference_Database = FirebaseDatabase.getInstance().getReference(); //tutaj referencja, zeby dzialalo
        emeil = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        adresEmail.setText(emeil);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.panel_zmenu, menu);
            basicQueryValueListener();
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Fragment fragment1 = null;

        if (id == R.id.nav_Wyloguj) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_MojeZdrowie) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentMojeZdrowie();
            }
        } else if (id == R.id.nav_Statystyka) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentStatystyka();
            }
        } else if (id == R.id.navWykonajPlan) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentWykonajPlan();
            }
        }else if (id == R.id.nav_dodaj_plan) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentDodajPlan();
            }
        }else if (id == R.id.nav_dodaj_cwiczenie) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentDodajCwiczenie();
            }
        }else if (id == R.id.nav_usun_aktywnosc) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentUsunAktywnosc();
            }
        }else if (id == R.id.nav_home) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentHome();
            }
        }else if (id == R.id.nav_settings) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentSetings();
            }
        }
        else if (id == R.id.nav_sledzenie) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentSledzenieTrasy();
            }
        } else if (id == R.id.nav_Kalendaz) {
            if (isConnectingToInternet(this) == false) {
                Toast.makeText(this, "Brak dostepu do sieci", Toast.LENGTH_LONG).show();
            }else {
                fragment = new fragmentKalendazAktywnosci();
            }
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        if(fragment1 !=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment1,"HOME");
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isConnectingToInternet(Context applicationContext) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }
}
