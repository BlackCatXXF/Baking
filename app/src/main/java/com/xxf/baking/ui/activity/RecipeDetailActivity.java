package com.xxf.baking.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xxf.baking.R;
import com.xxf.baking.ui.fragment.RecipeDetailFragment;
import com.xxf.baking.ui.fragment.RecipeStepDetailFragment;

/**
 * Created by dell on 2018/2/14.
 */

public class RecipeDetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (isPad(this)){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_recipe_detail,new RecipeDetailFragment())
//                    .addToBackStack(null)
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_step_detail,new RecipeStepDetailFragment())
//                    .addToBackStack(null)
                    .commit();

        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_recipe_detail,new RecipeDetailFragment())
//                    .addToBackStack(null)
                    .commit();
        }


    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
