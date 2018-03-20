package com.xxf.baking;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xxf.baking.ui.activity.ChooseRecipeActivity;
import com.xxf.baking.ui.fragment.ChooseRecipeFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

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
    public void clickRecyclerViewItem() {
        ChooseRecipeFragment fragment = new ChooseRecipeFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_choose_recipe, fragment).commitAllowingStateLoss();

        onView(withId(R.id.recyclerview_choose_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle("Brownies")));
    }

    public static Matcher<View> withToolbarTitle(CharSequence title) {
        return withToolbarTitle(is(title));
    }

    public static Matcher<View> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };


    }
}
