package com.xxf.baking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.xxf.baking.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dell on 2018/3/9.
 */

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    public static List<String> mList = new ArrayList<>();
    private URL url;
    private String jsonResponse;

    /*
     * 构造函数
     */
    public MyRemoteViewsFactory(Context context, Intent intent) {

        mContext = context;
    }

    /*
     * MyRemoteViewsFactory调用时执行，这个方法执行时间超过20秒回报错。
     * 如果耗时长的任务应该在onDataSetChanged或者getViewAt中处理
     */
    public static final int UPDATA_DATA = 1;
//    private Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UPDATA_DATA:
////                    recipeCardAdapter.setData(recipeNames);
//            }
//        }
//    };

    @Override
    public void onCreate() {
        // 需要显示的数据

//        fetchData(Constants.API.RECIPE_JSON);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("recipeNames",0);
        Set set = sharedPreferences.getStringSet("string",null);
        mList = new ArrayList<>(set);

    }

    private void fetchData(final String httpUrl) {

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
                        if (mList != null) {
                            mList.clear();
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

        }



    private void parseJson(String json) throws JSONException {

        JSONArray recipes = new JSONArray(json);
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipe = recipes.getJSONObject(i);
            String name = recipe.getString("name");
//            Log.d("name",name);

            mList.add(name);

        }

    }




    /*
     * 当调用notifyAppWidgetViewDataChanged方法时，触发这个方法
     * 例如：MyRemoteViewsFactory.notifyAppWidgetViewDataChanged();
     */
    @Override
    public void onDataSetChanged() {

    }

    /*
     * 这个方法不用多说了把，这里写清理资源，释放内存的操作
     */
    @Override
    public void onDestroy() {
        mList.clear();
    }

    /*
     * 返回集合数量
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /*
     * 创建并且填充，在指定索引位置显示的View，这个和BaseAdapter的getView类似
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= mList.size())
            return null;
        String content = mList.get(position);
        // 创建在当前索引位置要显示的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.recipe_widget_provider);

        // 设置要显示的内容
        rv.setTextViewText(R.id.recipe_name, content);

        // 填充Intent，填充在AppWdigetProvider中创建的PendingIntent
        Intent intent = new Intent();
        // 传入点击行的数据
        intent.putExtra("content", content);
        rv.setOnClickFillInIntent(R.id.recipe_name, intent);

        return rv;
    }

    /*
     * 显示一个"加载"View。返回null的时候将使用默认的View
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /*
     * 不同View定义的数量。默认为1（本人一直在使用默认值）
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /*
     * 返回当前索引的。
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 如果每个项提供的ID是稳定的，即她们不会在运行时改变，就返回true（没用过。。。）
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
