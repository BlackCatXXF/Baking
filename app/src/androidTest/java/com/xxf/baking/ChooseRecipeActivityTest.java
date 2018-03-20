package com.xxf.baking;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.xxf.baking.ui.activity.ChooseRecipeActivity;
import com.xxf.baking.ui.fragment.ChooseRecipeFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by dell on 2018/3/6.
 */
@RunWith(AndroidJUnit4.class)
public class ChooseRecipeActivityTest {


    @Rule
    public ActivityTestRule<ChooseRecipeActivity> mActivityTestRule = new ActivityTestRule<>(ChooseRecipeActivity.class);

    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), ChooseRecipeActivity.class);

    @Before
    public void setup() {
        mActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void clickRecyclerViewItem(){
        ChooseRecipeFragment fragment = new ChooseRecipeFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_choose_recipe, fragment).commitAllowingStateLoss();

        onView(withId(R.id.recyclerview_choose_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click())).check(matches(isFocusable()));
    }



}
