package com.epresidential.rebtest.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.epresidential.rebtest.R;
import com.epresidential.rebtest.data.RebtestContract;
import com.epresidential.rebtest.model.Countries;
import com.epresidential.rebtest.model.Country;
import com.epresidential.rebtest.rest.HttpResponseException;
import com.epresidential.rebtest.rest.JsonHttpResponseListener;
import com.epresidential.rebtest.rest.RestCountriesClient;
import com.epresidential.rebtest.sync.CountriesSyncAdapter;
import com.epresidential.rebtest.ui.adapter.CountriesAdapter;
import com.epresidential.rebtest.utils.Constants;
import com.epresidential.rebtest.utils.StoreUtils;

import java.util.Vector;

/**
 * Created by daniele on 02/09/16.
 */
public class CountriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = CountriesFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = "fragment_articles";

    private CountryListener mListener;
    private CountriesAdapter mArticlesAdapter;
    private ListView mArticlesListView;
    private int mCurrentListPosition;

    private static final int COUNTRIES_LOADER = 0;

    private static final String[] COUNTRIES_COLUMNS = {
            RebtestContract.CountryEntry.TABLE_NAME + "." + RebtestContract.CountryEntry._ID,
            RebtestContract.CountryEntry.COLUMN_COUNTRY_NAME,
            RebtestContract.CountryEntry.COLUMN_COUNTRY_CODE,
            RebtestContract.CountryEntry.COLUMN_FLAG_WEB_URL,
            RebtestContract.CountryEntry.COLUMN_POPULATION,
            RebtestContract.CountryEntry.COLUMN_CAPITAL
    };

    // These indices are tied to ARTICLES_COLUMNS projection.  If ARTICLES_COLUMNS changes, these must change.
    public static final int COL_COUNTRY_ID = 0;
    public static final int COL_COUNTRY_NAME = 1;
    public static final int COL_COUNTRY_CODE = 2;
    public static final int COL_FLAG_WEB_URL = 3;
    public static final int COL_POPULATION = 4;
    public static final int COL_CAPITAL = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateCountries();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_countries, container, false);
        mArticlesListView = (ListView) rootView.findViewById(R.id.countries_listview);
        mArticlesAdapter = new CountriesAdapter(getActivity(), null, 0);
        mArticlesListView.setAdapter(mArticlesAdapter);

        mArticlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem()
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                //Calls the listener method to show the action bar notification
                mListener.onItemSelected(cursor, position);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //initLoader calls onCreateLoader which performs the query in background
        getLoaderManager().initLoader(COUNTRIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void updateCountries() {

        CountriesSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //String sortOrder = RebtestContract.CountryEntry.COLUMN_PUB_DATE + " DESC";
        Uri articleUri = RebtestContract.CountryEntry.CONTENT_URI;

        //get all articles from db sorted starting from the newest
        return new CursorLoader(getActivity(),
                articleUri,
                COUNTRIES_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mArticlesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //we release any resources that we might be using
        mArticlesAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (CountryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CountryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCurrentListPosition(int position){
        mCurrentListPosition = position;
    }

    public interface CountryListener {
        /**
         * FragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Cursor article, int position);
    }
}
