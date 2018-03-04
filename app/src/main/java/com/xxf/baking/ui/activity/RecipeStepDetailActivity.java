package com.xxf.baking.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xxf.baking.R;
import com.xxf.baking.ui.fragment.RecipeStepDetailFragment;

/**
 * Created by dell on 2018/2/14.
 */

public class RecipeStepDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_recipe_step_detail,new RecipeStepDetailFragment())
//                    .addToBackStack(null)
                .commit();
    }
}
