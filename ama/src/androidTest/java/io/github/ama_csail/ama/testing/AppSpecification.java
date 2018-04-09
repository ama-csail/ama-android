package io.github.ama_csail.ama.testing;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of an application that should be tested. Can be prepopulated by the user,
 * or we can attempt to construct it through a monkey process.
 */
public class AppSpecification {

    private String packageName;
    private List<Class> activities;
    private List<ActivityTestRule> activityRules;

    public AppSpecification(String packageName) {
        this.packageName = packageName;
        this.activities = new ArrayList<>();
        this.activityRules = new ArrayList<>();
    }

    public void addActivity(Class cl) {
        this.activities.add(cl);
        activityRules.add(new ActivityTestRule<Activity>(cl));
    }

}
