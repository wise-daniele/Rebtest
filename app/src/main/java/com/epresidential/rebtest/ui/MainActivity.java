package com.epresidential.rebtest.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epresidential.rebtest.R;
import com.epresidential.rebtest.sync.CountriesSyncAdapter;

public class MainActivity extends AppCompatActivity implements CountriesFragment.CountryListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get last position of listview in order to automatically scroll the listview
        CountriesFragment articlesFragment = new CountriesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, articlesFragment, CountriesFragment.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commit();

        CountriesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public void onItemSelected(Cursor article, int position) {

    }
}
