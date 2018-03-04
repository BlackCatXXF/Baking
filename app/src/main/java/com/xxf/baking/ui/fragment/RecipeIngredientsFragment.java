package com.xxf.baking.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxf.baking.Constants;
import com.xxf.baking.R;
import com.xxf.baking.adapter.DividerItemDecoration;
import com.xxf.baking.adapter.RecipeIngredientAdapter;
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
 * Created by dell on 2018/2/27.
 */

public class RecipeIngredientsFragment extends Fragment {

    @BindView(R.id.recyclerview_recipe_ingredients)
    RecyclerView mRecyclerView;

    public static final int UPDATA_DATA = 1;

    private List<String> ingredients = new ArrayList<>();
    private RecipeIngredientAdapter mRecipeIngredientAdapter;
    private URL url;
    private String jsonResponse;
    private int position = new RecipeDetailFragment().getPosition();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_DATA:
                    mRecipeIngredientAdapter.setData(ingredients);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredients,container,false);
        ButterKnife.bind(this, view);

        fetchData(Constants.API.RECIPE_JSON);
        initRecyclerView();

        return view;
    }

    private void initRecyclerView(){
        mRecipeIngredientAdapter = new RecipeIngredientAdapter(getContext(),ingredients);
        mRecyclerView.setAdapter(mRecipeIngredientAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                        if (ingredients != null) {
                            ingredients.clear();
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
        JSONObject recipe = recipes.getJSONObject(position);
        Log.d("position", String.valueOf(position));
        JSONArray ingredientJsonArray = recipe.getJSONArray("ingredients");
        for (int i = 0; i < ingredientJsonArray.length(); i++) {
            JSONObject ingredient = ingredientJsonArray.getJSONObject(i);
            int quantity = ingredient.getInt("quantity");
            String measure = ingredient.getString("measure");
            String food = ingredient.getString("ingredient");

            String s = food +"  "+ quantity+"  " + measure;
            Log.d("s",s);
            ingredients.add(s);

        }

    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }




}
