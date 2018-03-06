package com.xxf.baking.ui.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xxf.baking.Constants;
import com.xxf.baking.R;
import com.xxf.baking.adapter.DividerItemDecoration;
import com.xxf.baking.adapter.WidgetConfigureAdapter;
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
 * Created by dell on 2018/3/4.
 */

public class AppWidgetConfigureActivity extends AppCompatActivity {

    @BindView(R.id.widget_configure_recyclerview)
    RecyclerView mRecyclerView;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static final int UPDATA_DATA = 1;

    private URL url = null;

    private String jsonResponse;

    private List<String> recipeNames = new ArrayList<>();

    private WidgetConfigureAdapter mWidgetConfigureAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_DATA:
                    mWidgetConfigureAdapter.setData(recipeNames);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        setContentView(R.layout.app_widget_configure);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


        fetchData(Constants.API.RECIPE_JSON);
        initRecyclerView();

    }

    private void initRecyclerView(){
        mWidgetConfigureAdapter = new WidgetConfigureAdapter(this,this,recipeNames,mAppWidgetId);
        mRecyclerView.setAdapter(mWidgetConfigureAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


}
