package com.xxf.baking;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.xxf.baking.ui.activity.ChooseRecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by dell on 2018/3/6.
 */
@RunWith(AndroidJUnit4.class)
public class ChooseRecipeActivityTest {


    @Rule
    public ActivityTestRule<ChooseRecipeActivity> mActivityTestRule = new ActivityTestRule<>(ChooseRecipeActivity.class);

    @Test
    public void clickRecyclerViewItem(){
        onView(withId(R.id.recyclerview_choose_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.recyclerview_choose_recipe))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Brownies")), click()));
    }



}
