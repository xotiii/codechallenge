package com.danchua.codechallenge.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.danchua.codechallenge.CodeChallenge;
import com.danchua.codechallenge.R;
import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.utils.ResourceRequestScreen;

public class HomeFragment extends Fragment implements View.OnClickListener{

    /**
     * ViewModel for Home
     * */
    private HomeViewModel mHomeViewModel;

    /**
     * RecyclerView Adapter for Result List
     * */
    private HomeResultListAdapter mHomeResultListAdapter;

    /**
     * Resources on screen from MainActivity
     * */
    private ResourceRequestScreen mResourceRequestScreen;

    /**
     * View components
     * */
    private RecyclerView mRecyclerViewResultList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mTextViewEmptyMessage;
    private TextView mTextViewLastVisit;

    /**
     *
     * Inflate layout to Fragment
     *
     * */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mRecyclerViewResultList = view.findViewById(R.id.home_fragment_recyclerview_results);

        mSwipeRefreshLayout = view.findViewById(R.id.home_fragment_swiperefresh_results);

        mTextViewEmptyMessage = view.findViewById(R.id.home_fragment_textview_empty);
        mTextViewLastVisit = view.findViewById(R.id.home_fragment_textview_lastvisit);

        setHasOptionsMenu(true);

        return view;
    }

    /**
     *
     * Initialize necessary components
     * Components - ViewModel, Adapter, RecyclerView
     *
     * Initialize Resource Screens
     * Set Toolbar title
     * Remove Toolbar back button
     * Initialize View Model for Home
     * Set up Adapter and RecyclerView for List
     *
     * */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mResourceRequestScreen = (ResourceRequestScreen) getActivity();
        mResourceRequestScreen.setToolbarTitle(getString(R.string.app_name));
        mResourceRequestScreen.toggleToolbarBackButton(false);

        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.init(((CodeChallenge) getActivity().getApplication()).getRepository());

        mHomeResultListAdapter = new HomeResultListAdapter(getContext(), this::onClick);

        mRecyclerViewResultList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerViewResultList.setAdapter(mHomeResultListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadResultList();
            }
        });

        loadLastVisit();

        loadResultList();
    }

    /**
     *
     * Add Menu to Toolbar
     *
     * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     *
     * Set methods for menu items
     *
     * Navigate to Wishlist page
     *
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_wishlist:
                Navigation.findNavController(getView())
                        .navigate(R.id.action_homeFragment_to_wishlistFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.result_list_item_cardivew_container:
                mResourceRequestScreen.showDetailDialog((Result) v.getTag());
                break;
        }
    }

    /**
     *
     * Load and display last visit date
     *
     * */
    public void loadLastVisit() {
        mTextViewLastVisit.setText(getString(R.string.last_visit_label)
                + DateFormat.format("MM/dd/yyyy hh:mmaa", mHomeViewModel.getLastVisit()).toString());
    }

    /**
     *
     *  Observe LiveData for incoming data from API request
     *
     *  Toggles empty message depending on list count
     *
     * */
    public void loadResultList() {
        mSwipeRefreshLayout.setRefreshing(true);
        mHomeViewModel.getResultList().observe(this, result -> {
            mSwipeRefreshLayout.setRefreshing(false);
            switch (result.status) {
                case SUCCESS:
                    mHomeResultListAdapter.setResultList(result.data.getResults());
                    mHomeResultListAdapter.notifyDataSetChanged();
                    break;
                case LOADING:
                    break;
                case CLIENT_ERROR:
                    break;
                case SERVER_ERROR:
                    break;

            }
            if(mHomeResultListAdapter.getItemCount()==0) {
                mTextViewEmptyMessage.setVisibility(View.VISIBLE);
            } else {
                mTextViewEmptyMessage.setVisibility(View.GONE);
            }
        });
    }

}
