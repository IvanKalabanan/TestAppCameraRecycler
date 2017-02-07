package test.ivacompany.com.testappcamerarecycler.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import test.ivacompany.com.testappcamerarecycler.R;
import test.ivacompany.com.testappcamerarecycler.fragments.MainFragment;
import test.ivacompany.com.testappcamerarecycler.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.initRealm();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer,
                        new MainFragment(),
                        MainFragment.TAG)
                .commit();
    }
}
