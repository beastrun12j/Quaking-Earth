
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>
{

    private EarthquakeAdapter mAdapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=3&limit=20";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(LOG_TAG,"OnCreate Method is called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        ListView earthquakeListView = (ListView) findViewById(R.id.list_of_earthquakes);
        earthquakeListView.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loadingIndicator);
        mLoadingIndicator.setIndeterminate(true);

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Earthquake currentEarthquake = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                startActivity(websiteIntent);

            }
        });

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null && activeNetwork.isConnected())
        {
            Log.e(LOG_TAG, "Loader Manager Called");
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }

        else
        {
            View loadingIndicator = findViewById(R.id.loadingIndicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No Internet connection.");
        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle)
    {
        Log.e(LOG_TAG,"Creating the Loader");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes)
    {

        mLoadingIndicator.setVisibility(View.GONE);

        Log.e(LOG_TAG,"Loader Task finished");

        mEmptyStateTextView.setText("No Earthquakes Found.");

        mAdapter.clear();

        if(earthquakes != null && !earthquakes.isEmpty())
        {
           mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader)
    {
        Log.e(LOG_TAG,"Resetting the Loader");
        mAdapter.clear();
    }

}
