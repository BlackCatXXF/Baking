package com.xxf.baking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxf.baking.R;

import java.util.List;

/**
 * Created by dell on 2018/2/27.
 */

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    private Context mContext;

    private List<String> mIngredients;

    public RecipeIngredientAdapter(Context context,List<String> ingredients){
        mContext = context;
        this.mIngredients = ingredients;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_ingredient,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mIngredients.get(position));

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public void setData(List<String> datas){
        mIngredients = datas;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_ingredient);
        }
    }

}
