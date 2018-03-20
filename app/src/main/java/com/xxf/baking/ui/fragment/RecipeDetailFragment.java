package com.xxf.baking.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xxf.baking.Constants;
import com.xxf.baking.R;
import com.xxf.baking.adapter.DividerItemDecoration;
import com.xxf.baking.adapter.RecipeDetailAdapter;
import com.xxf.baking.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2018/2/14.
 */

public class RecipeDetailFragment extends Fragment {

    public static final int UPDATA_DATA = 1;

    @BindView(R.id.recyclerview_recipe_detail)
    RecyclerView mRecyclerViewDetail;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private URL url = null;
    private String jsonResponse;

    private List<String> receipDetails = new ArrayList<>();

    private RecipeDetailAdapter mRecipeDetailAdapter;

    private int position;

    private String mToolbarTitle;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_DATA:
                    mRecipeDetailAdapter.setData(receipDetails);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        initToolbar();

//        fetchData(Constants.API.RECIPE_JSON);
        try {
            url = new URL(Constants.API.RECIPE_JSON);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadTask().execute(url);
        initRecyclerView();
        return view;
    }

    private void initToolbar(){
        mToolbar.setNavigationContentDescription(R.string.add_to_desk);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"aaa",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        position = getActivity().getIntent().getIntExtra("position", 0);
        mToolbarTitle = getActivity().getIntent().getStringExtra(getString(R.string.RecipeName));
        mToolbar.setTitle(mToolbarTitle);
    }

    private void initRecyclerView() {
        mRecipeDetailAdapter = new RecipeDetailAdapter(getContext(), receipDetails, (AppCompatActivity) getActivity());
        mRecyclerViewDetail.setAdapter(mRecipeDetailAdapter);
        mRecyclerViewDetail.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void fetchData(final String httpUrl) {

        if (isOnline()) {
            try {
                url = new URL(httpUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        if (receipDetails != null) {
                            receipDetails.clear();
                        }
                        parseJson(jsonResponse);
                        Message message = new Message();
                        message.what = UPDATA_DATA;
//                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            mRecyclerViewDetail.setVisibility(View.GONE);
        }

    }

    public int getPosition(){
        return position;
    }

    private void parseJson(String json) throws JSONException {

        JSONArray recipes = new JSONArray(json);
        JSONObject recipe = recipes.getJSONObject(position);
//        Log.d("position", String.valueOf(position));
        receipDetails.add(getContext().getString(R.string.Ingredients));

        JSONArray stepJsonArray = recipe.getJSONArray("steps");
        for (int i = 0; i < stepJsonArray.length(); i++) {
            JSONObject stepJsonObject = stepJsonArray.getJSONObject(i);
            String shortDescription = stepJsonObject.getString("shortDescription");
//            Log.d("shortDescription",shortDescription);
            receipDetails.add(shortDescription);
        }


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private class DownloadTask extends AsyncTask<URL, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(URL... urls) {
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (receipDetails != null) {
                receipDetails.clear();
            }
            try {
                parseJson(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mRecipeDetailAdapter.setData(receipDetails);
        }
    }

}
