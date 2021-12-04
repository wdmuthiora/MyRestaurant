package com.moringaschool.myrestaurants;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.moringaschool.myrestaurants.ui.RestaurantListActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RestaurantListActivityInstrumentationTest {

    @Rule
    public ActivityScenarioRule<RestaurantListActivity> activityTestRule =
            new ActivityScenarioRule<>(RestaurantListActivity.class);
    private View activityDecorView;

    @Before
    public void setUp() {
        activityTestRule.getScenario().onActivity(new ActivityScenario.ActivityAction<RestaurantListActivity>() {
            @Override
            public void perform(RestaurantListActivity activity) {
                activityDecorView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void listItemClickDisplaysToastWithCorrectRestaurant() {
        String restaurantName = "Mi Mero Mole";

        onData(anything())
                .inAdapterView(withId(R.id.recyclerView))
                .atPosition(0)
                .perform(click());
        onView(withText(restaurantName)).inRoot(withDecorView(not(activityDecorView)))
                .check(matches(withText(restaurantName)));
    }
}
