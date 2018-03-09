package com.xxf.baking.adapter;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxf.baking.R;
import com.xxf.baking.RecipeWidgetProvider;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dell on 2018/3/4.
 */

public class WidgetConfigureAdapter extends RecyclerView.Adapter<WidgetConfigureAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mNames;
    private int mAppWidgetId;
    private AppCompatActivity mActivity;

    public WidgetConfigureAdapter(AppCompatActivity activity,Context context, List<String> names,int mAppWidgetId){
        mContext = context;
        mNames = names;
        this.mAppWidgetId = mAppWidgetId;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_card,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
//                intent.putExtra("position",viewHolder.getLayoutPosition());
//                intent.putExtra(mContext.getString(R.string.RecipeName),mNames.get(viewHolder.getLayoutPosition()));
//                mContext.startActivity(intent);

                SharedPreferences.Editor prefs = mContext.getSharedPreferences(mContext.getString(R.string.prefs_name),0).edit();
                prefs.putInt(mContext.getString(R.string.pref_position),viewHolder.getLayoutPosition());
                prefs.putString(mContext.getString(R.string.RecipeName),mNames.get(viewHolder.getLayoutPosition()));

                prefs.commit();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                RecipeWidgetProvider.updateAppWidget(mContext, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                mActivity.setResult(RESULT_OK, resultValue);
                mActivity.finish();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mNames.get(position));
    }

    public void setData(List<String> names){
        mNames = names;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.recipe_name);
        }
    }

}
