package com.epresidential.rebtest.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epresidential.rebtest.R;
import com.epresidential.rebtest.ui.CountriesFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by daniele on 02/09/16.
 */
public class CountriesAdapter extends CursorAdapter {

    public static final String LOG_TAG = CountriesAdapter.class.getSimpleName();

    public static class ViewHolder {
        public final ImageView imageCountry;
        public final TextView textCountryName;
        public final TextView textCountryCapital;
        public final TextView textCountryPopulation;

        public ViewHolder(View view) {
            imageCountry = (ImageView) view.findViewById(R.id.image_country);
            textCountryName = (TextView) view.findViewById(R.id.text_country_name);
            textCountryCapital = (TextView) view.findViewById(R.id.text_country_capital);
            textCountryPopulation = (TextView) view.findViewById(R.id.text_country_population);
        }
    }

    public CountriesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_country_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String imageUrl = cursor.getString(CountriesFragment.COL_FLAG_WEB_URL);
        String countryName = cursor.getString(CountriesFragment.COL_COUNTRY_NAME);
        String countryCapital = cursor.getString(CountriesFragment.COL_CAPITAL);
        long population = cursor.getLong(CountriesFragment.COL_POPULATION);
        viewHolder.textCountryName.setText(countryName);
        viewHolder.textCountryCapital.setText(countryCapital);
        if(imageUrl != null){
            Picasso.with(context).load(imageUrl).into(viewHolder.imageCountry);
        }
        viewHolder.textCountryPopulation.setText(Long.toString(population));
    }
}
