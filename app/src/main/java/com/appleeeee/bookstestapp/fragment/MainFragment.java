package com.appleeeee.bookstestapp.fragment;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appleeeee.bookstestapp.R;
import com.appleeeee.bookstestapp.adapter.BookAdapter;
import com.appleeeee.bookstestapp.api.Api;
import com.appleeeee.bookstestapp.model.Books;
import com.appleeeee.bookstestapp.model.Item;
import com.appleeeee.bookstestapp.other.SpacesItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appleeeee.bookstestapp.other.Constans.KEY;
import static com.appleeeee.bookstestapp.other.Constans.Q_KEY;

public class MainFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private BookAdapter adapter;
    private ArrayList<Item> items;
    private ArrayList<Item> newItems;
    private String query;
    private SpacesItemDecoration itemDecoration;
    private StaggeredGridLayoutManager mStaggeredLayout;
    private int startIndex;
    private boolean loading;
    private Bundle bundle;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        query = bundle.getString(Q_KEY);
        items = new ArrayList<>();
        newItems = new ArrayList<>();
        startIndex = 0;
        adapter = new BookAdapter(getActivity());
        getScreenOrientation();
        itemDecoration = new SpacesItemDecoration(8);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(mStaggeredLayout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mStaggeredLayout.getChildCount();
                int totalItemCount = mStaggeredLayout.getItemCount();
                int pastVisibleItems = mStaggeredLayout.findFirstVisibleItemPositions(null)[1];
                if (dy > 0) {
                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems * 2) >= totalItemCount) {
                            loading = false;
                            makeRequest();
                        }
                    }
                }
            }
        });
        makeRequest();
        return v;
    }

    public void makeRequest() {
        Api.getInstance().getApiInterface().getBooks(query, startIndex, KEY)
                .enqueue(new Callback<Books>() {
                    @Override
                    public void onResponse(Call<Books> call, Response<Books> response) {
                        if (response.code() == 200) {
                            newItems = (ArrayList<Item>) response.body().getItems();
                            items.addAll(newItems);
                            adapter.setItemList(items);
                            startIndex += 10;
                        }
                        loading = true;
                    }

                    @Override
                    public void onFailure(Call<Books> call, Throwable t) {
                        loading = true;
                        Toast.makeText(getActivity(), getString(R.string.error_in_response), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getScreenOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mStaggeredLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mStaggeredLayout = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
    }
}
