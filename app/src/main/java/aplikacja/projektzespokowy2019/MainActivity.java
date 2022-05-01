package aplikacja.projektzespokowy2019;




import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FirebaseAuth firebaseAuth;


    @BindView(R.id.tablica)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String decyzja = getIntent().getStringExtra("czyWylogowac");

        if(decyzja == null)
        {
            if (user != null) {
                finish();
                Intent intent = new Intent(MainActivity.this, PanelMenuActivity.class);
                startActivity(intent);
            }
        }

    }



    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PanelLogowaniaActivity(), "Logowanie");
        adapter.addFragment(new PanelRejestracjiActivity(), "Rejestracja");
        viewPager.setAdapter(adapter);
    }
}
