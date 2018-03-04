package com.xxf.baking.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xxf.baking.R;
import com.xxf.baking.ui.fragment.ChooseRecipeFragment;

public class ChooseRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipe);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_choose_recipe,new ChooseRecipeFragment())
//                    .addToBackStack(null)
                .commit();
    }
}
