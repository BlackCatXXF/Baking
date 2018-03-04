package com.xxf.baking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxf.baking.R;
import com.xxf.baking.ui.activity.RecipeDetailActivity;

import java.util.List;

/**
 * Created by dell on 2018/2/14.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mNames;

    public RecipeCardAdapter(Context context, List<String> names){
        mContext = context;
        mNames = names;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipe_card,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra("position",viewHolder.getLayoutPosition());
                intent.putExtra(mContext.getString(R.string.RecipeName),mNames.get(viewHolder.getLayoutPosition()));
                mContext.startActivity(intent);
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
