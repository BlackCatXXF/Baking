package com.xxf.baking;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

//    @Rule
//    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule = new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.xxf.baking", appContext.getPackageName());

//        mActivityTestRule.getActivity().getSupportFragmentManager().
//                beginTransaction().replace(R.id.activity_recipe_detail, new RecipeDetailFragment())
//                .commitAllowingStateLoss();
//        onView(withId(R.id.toolbar)).check(matches(withToolbarTitle("Brownies")));
    }

//    public static Matcher<View> withToolbarTitle(CharSequence title) {
//        return withToolbarTitle(is(title));
//    }
//
//    public static Matcher<View> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
//        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
//            @Override
//            public boolean matchesSafely(Toolbar toolbar) {
//                return textMatcher.matches(toolbar.getTitle());
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with toolbar title: ");
//                textMatcher.describeTo(description);
//            }
//        };
//
//
//    }
}
