package com.xxf.baking.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxf.baking.R;
import com.xxf.baking.ui.activity.RecipeStepDetailActivity;
import com.xxf.baking.ui.fragment.RecipeIngredientsFragment;
import com.xxf.baking.ui.fragment.RecipeStepDetailFragment;

import java.util.List;

/**
 * Created by dell on 2018/2/26.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private Context mContext;

    private List<String> receipDetails;

    private AppCompatActivity mActivity;

    public RecipeDetailAdapter(Context context, List<String> receipDetail, AppCompatActivity activity) {
        mContext = context;
        this.receipDetails = receipDetail;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_detail, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPad(mContext)){
                    if(viewHolder.getLayoutPosition() == 0){
                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.recipe_step_detail, new RecipeIngredientsFragment())
//                    .addToBackStack(null)
                                .commit();
                    }else {

                        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                        recipeStepDetailFragment.setStepPosition(viewHolder.getLayoutPosition() - 1);

                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.recipe_step_detail, recipeStepDetailFragment)
//                    .addToBackStack(null)
                                .commit();
//                        Intent intent = new Intent(mContext, RecipeStepDetailActivity.class);
//                        int stepPosition = viewHolder.getLayoutPosition() - 1;
//                        intent.putExtra(mContext.getString(R.string.stepPosition),stepPosition);
//                        mActivity.startActivity(intent);
                    }
                }else {
                    if(viewHolder.getLayoutPosition() == 0){
                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_recipe_detail, new RecipeIngredientsFragment())
//                    .addToBackStack(null)
                                .commit();
                    }else {
                        Intent intent = new Intent(mContext, RecipeStepDetailActivity.class);
                        int stepPosition = viewHolder.getLayoutPosition() - 1;
                        intent.putExtra(mContext.getString(R.string.stepPosition),stepPosition);
                        mActivity.startActivity(intent);
                    }
                }


            }
        });

        return viewHolder;
    }


    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(receipDetails.get(position));

    }

    @Override
    public int getItemCount() {
        return receipDetails.size();
    }

    public void setData(List<String> datas) {
        receipDetails = datas;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.recipe_detail);
        }
    }


}
