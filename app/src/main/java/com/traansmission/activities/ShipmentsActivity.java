package com.traansmission.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.traansmission.R;
import com.traansmission.adapters.ShipmentsAdapter;
import com.traansmission.api.RestClient;
import com.traansmission.models.PaginatedShipments;
import com.traansmission.models.Shipment;
import com.traansmission.services.BackgroundLocationService;
import com.traansmission.shared.Constants;
import com.traansmission.shared.ErrorHandler;
import com.traansmission.shared.ErrorHandler.ErrorCallback;
import com.traansmission.shared.Notification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ShipmentsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int nextPage;
    private boolean isLoading = false;
    private String nextPageUrl = "";
    private SwipeRefreshLayout swipeContainer;
    private View mProgressView;
    Toolbar toolbar;
    private RelativeLayout emptyView;
    private AppCompatImageButton emptyRefreshButton;

    @Override
    protected void onDestroy() {
        Notification.setWasKilled(getApplicationContext(),true);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String pendingShipmentJson = Notification.getPendingShipmentJson(getApplicationContext());
        if(pendingShipmentJson != null){
            Intent shipmentIntent = new Intent(getApplicationContext(), ShipmentActivity.class);
            shipmentIntent.putExtra("shipment", pendingShipmentJson);
            startActivity(shipmentIntent);
        }
        Notification.setPendingShipmentJson(getApplicationContext(),null);
        Notification.setWasKilled(getApplicationContext(),false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipments);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mProgressView = findViewById(R.id.shipments_progress);
        toolbar = (Toolbar) findViewById(R.id.toolbar_shipments);
        mRecyclerView = (RecyclerView) findViewById(R.id.shipments_list);
        emptyView = (RelativeLayout) findViewById(R.id.empty_view);
        emptyRefreshButton = (AppCompatImageButton) findViewById(R.id.empty_shipments_refresh_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShipmentsAdapter(new ArrayList<Shipment>(), this);
        isLoading = false;
        nextPage = 1;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setPaginatedResults(nextPage, true);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                if(isLoading) {
                    swipeContainer.setRefreshing(false);
                } else {
                    refreshResults();
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if ((mLayoutManager.getChildCount() + mLayoutManager.findFirstVisibleItemPosition()) >= mLayoutManager.getItemCount()) {
                        Log.d("TAG", "End of list");
                        if (!isLoading && nextPageUrl != null) {
                            setPaginatedResults(nextPage, false);
                        }
                    }
                }
            }
        });

        emptyRefreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshResults();
            }
        });

        //TODO: Add to startup activity (post user login)
        // Start background location updates (Started service)
        Intent backgroundLocationIntent = new Intent(this, BackgroundLocationService.class);
        startService(backgroundLocationIntent);
    }

    private void refreshResults() {
        mAdapter.removeAll();
        nextPageUrl = "";
        nextPage = 1;
        mAdapter.notifyDataSetChanged();
        setPaginatedResults(nextPage, true);
    }

    private void startLoading(boolean showProgress) {
        swipeContainer.setRefreshing(false);
        isLoading = true;
        if (showProgress) {
            showProgress(true);
        }
        emptyView.setVisibility(View.GONE);
    }

    private void endLoading() {
        swipeContainer.setRefreshing(false);
        isLoading = false;
        showProgress(false);
    }


    //TODO: Make showProgress generic so it can be shared among other activities
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            swipeContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            swipeContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    swipeContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            swipeContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setPaginatedResults(int page, boolean showProgress) {
        // Load next page of data
        startLoading(showProgress);
        Call<PaginatedShipments> call = RestClient.getInstance(getApplicationContext()).apiServiceAuthenticated().getPaginatedShipments(page);

        call.enqueue(new Callback<PaginatedShipments>() {
            @Override
            public void onResponse(Call<PaginatedShipments> call, Response<PaginatedShipments> response) {
                PaginatedShipments pShipments = response.body();
                ArrayList<Shipment> shipments = pShipments.results;
                for(Shipment shipment : shipments) {
                    mAdapter.add(0, shipment);
                }
                if(shipments.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
                nextPageUrl = pShipments.next;
                nextPage++;
                endLoading();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
            @Override
            public void onFailure(Call<PaginatedShipments> call, Throwable t) {
                ErrorHandler.getInstance().processError(t, "Unable to get shipments", getApplicationContext(), new ErrorCallback() {
                    @Override
                    public void alertDismissed(Constants.ERR_TYPE errorType) {

                    }
                });
                endLoading();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        toolbar.inflateMenu(R.menu.menu_main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch (item.getItemId()) {
            case R.id.miSettings:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
        }
        return true;
    }
}

