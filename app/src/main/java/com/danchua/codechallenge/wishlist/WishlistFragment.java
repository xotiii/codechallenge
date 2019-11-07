package com.danchua.codechallenge.wishlist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danchua.codechallenge.CodeChallenge;
import com.danchua.codechallenge.R;
import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.home.HomeResultListAdapter;
import com.danchua.codechallenge.utils.ResourceRequestScreen;

public class WishlistFragment extends Fragment implements View.OnClickListener {

    private WishlistViewModel mWishlistViewModel;

    private HomeResultListAdapter mHomeResultListAdapter;

    private ResourceRequestScreen mResourceRequestScreen;

    private RecyclerView mRecyclerViewWishList;

    private SwipeRefreshLayout mSwipeRefreshLayoutList;

    private TextView mTextViewWishListCount;
    private TextView mTextViewEmptyMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_fragment, container, false);

        mRecyclerViewWishList = view.findViewById(R.id.wishlist_fragment_recyclerview_list);

        mSwipeRefreshLayoutList = view.findViewById(R.id.wishlist_fragment_swiperefreshlayout);

        mTextViewWishListCount = view.findViewById(R.id.wishlist_fragment_textview_count);
        mTextViewEmptyMessage = view.findViewById(R.id.wishlist_fragment_textview_empty);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mResourceRequestScreen = (ResourceRequestScreen) getActivity();
        mResourceRequestScreen.setToolbarTitle(getString(R.string.wishlist_label));
        mResourceRequestScreen.toggleToolbarBackButton(true);

        mWishlistViewModel = ViewModelProviders.of(this).get(WishlistViewModel.class);
        mWishlistViewModel.init(((CodeChallenge) getActivity().getApplication()).getRepository());

        mHomeResultListAdapter = new HomeResultListAdapter(getContext(), this::onClick);

        mRecyclerViewWishList.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerViewWishList.setAdapter(mHomeResultListAdapter);

        mSwipeRefreshLayoutList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWishList();
            }
        });

        loadWishList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_list_item_cardivew_container:
                mResourceRequestScreen.showDetailDialog((Result) v.getTag());
                break;
        }
    }

    public void loadWishList() {
        mSwipeRefreshLayoutList.setRefreshing(true);
        mWishlistViewModel.getAllWishList().observe(this, result -> {
            mSwipeRefreshLayoutList.setRefreshing(false);

            mHomeResultListAdapter.setResultList(result);
            mHomeResultListAdapter.notifyDataSetChanged();

            if (mHomeResultListAdapter.getItemCount() == 0) {
                mTextViewEmptyMessage.setVisibility(View.VISIBLE);
            } else {
                mTextViewEmptyMessage.setVisibility(View.GONE);
            }
            mTextViewWishListCount.setText(getString(R.string.wishlist_count_label) + result.size());
        });
    }

}
