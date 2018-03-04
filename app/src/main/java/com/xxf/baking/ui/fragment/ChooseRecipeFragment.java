package com.xxf.baking.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxf.baking.Constants;
import com.xxf.baking.R;
import com.xxf.baking.adapter.DividerItemDecoration;
import com.xxf.baking.adapter.RecipeCardAdapter;
import com.xxf.baking.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2018/2/13.
 */

public class ChooseRecipeFragment extends Fragment {

    @BindView(R.id.recyclerview_choose_recipe)
    RecyclerView mRecyclerView;

    public static final int UPDATA_DATA = 1;

    private URL url = null;

    private String jsonResponse;

    private List<String> recipeNames = new ArrayList<>();

    private RecipeCardAdapter recipeCardAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_DATA:
                    recipeCardAdapter.setData(recipeNames);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_recipe,container,false);
        ButterKnife.bind(this,view);

        fetchData(Constants.API.RECIPE_JSON);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView(){
        recipeCardAdapter = new RecipeCardAdapter(getContext(),recipeNames);
        mRecyclerView.setAdapter(recipeCardAdapter);
        if (isPad(getContext())){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        }else {
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void fetchData(final String httpUrl) {

        if (isOnline()){
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
//                        Log.d("jsonResponse",jsonResponse);
                        if (recipeNames != null) {
                            recipeNames.clear();
                        }
                        parseJson(jsonResponse);
                        Message message = new Message();
                        message.what = UPDATA_DATA;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }else {
            mRecyclerView.setVisibility(View.GONE);
        }


    }

    private void parseJson(String json) throws JSONException {

        JSONArray recipes = new JSONArray(json);
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipe = recipes.getJSONObject(i);
            String name = recipe.getString("name");
//            Log.d("name",name);

            recipeNames.add(name);

        }

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }



}
