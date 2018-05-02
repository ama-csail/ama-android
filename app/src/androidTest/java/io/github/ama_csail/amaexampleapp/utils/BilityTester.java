package io.github.ama_csail.amaexampleapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import io.github.ama_csail.ama.testserver.MobileSocket;
import io.github.ama_csail.ama.util.views.ViewHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * A class that runs the accessibility tests given app specification and physical device
 * @author Aaron Vontell
 */
public class BilityTester {

    private AppSpecification app;
    private UiDevice uiDevice;
    private MobileSocket socket;

    private Random rand;

    private int LAUNCH_TIMEOUT = 5000;
    private int TEST_RUNS = 1;
    private int MAX_ACTIONS = 5;
    private List<TestSuiteType> runningSuites;

    // RESULTS
    private int actionCount = 0;

    public BilityTester(AppSpecification app, UiDevice uiDevice) {
        this.app = app;
        this.uiDevice = uiDevice;
        this.rand = new Random();
    }

    // Setters

    public BilityTester setTimeout(int timeout) {
       this.LAUNCH_TIMEOUT = timeout;
       return this;
    }

    public BilityTester provideSocket(MobileSocket socket) {
        this.socket = socket;
        return this;
    }

    public BilityTester setRuns(int numRuns) {
        this.TEST_RUNS = numRuns;
        return this;
    }

    public BilityTester setMaxActions(int actions) {
        this.MAX_ACTIONS = actions;
        return this;
    }

    public BilityTester setSeed(long seed) {
        this.rand.setSeed(seed);
        return this;
    }

    public BilityTester setTestSuites(TestSuiteType ... testSuites) {
        this.runningSuites = Arrays.asList(testSuites);
        return this;
    }

    // Runners

    public BilityTester startupApp() {

        String appPackageName = app.getPackageName();

        // Send info that the app has started
        socket.sendStartInfo(appPackageName);

        // Start from the home screen
        //mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = uiDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        uiDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(appPackageName);

        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        uiDevice.wait(Until.hasObject(By.pkg(appPackageName).depth(0)),
                LAUNCH_TIMEOUT);

        uiDevice.waitForIdle();

        printTestSetup();

        return this;

    }

    public void startTestLoop() {

        // First, get information about the current screen, and what is shown
        // If the current activity instance is not good, go back
        boolean shouldGoBack = false;
        Activity currentActivity = getActivityInstance();
        if (currentActivity != null) {
            evaluateAccessibility(currentActivity);
        } else {
            shouldGoBack = true;
        }


        // TODO: If goal state has been reached, terminate the test loop
        if (actionCount >= MAX_ACTIONS) {
            return;
        }

        // Then, come up with an estimate for what next action to take
        UiInputActionType nextAction = !shouldGoBack ? getNextActionType() : UiInputActionType.BACK;
        UiObject subject = getNextActionSubject(nextAction);

        // If no subject was found for this action, retry
        if (subject == null) {

            startTestLoop();

        } else {

            // Finally, execute that action
            try {
                Log.e("ACTION", "Performing " + nextAction + " on " + subject.getClassName());
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            executeNextAction(nextAction, subject);

            // Call the start loop again
            uiDevice.waitForIdle(2000);
            startTestLoop();

        }

    }

    public String printTestSetup() {

        String toPrint = "Running following test suites: " + TextUtils.join(", ", runningSuites);
        Log.e("BILITY TEST", toPrint);

        return toPrint;

    }

    public BilityTester printResults() {
        return this;
    }


    // Private helper functions

    private Activity getActivityInstance(){

        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                if (it.hasNext()) {
                    currentActivity[0] = it.next();
                }
            }
        });

        return currentActivity[0];
    }

    private void evaluateAccessibility(Activity activity) {

        View toSearch = activity.getWindow().getDecorView().getRootView();
        int passCount = 0;
        int totalCount = 0;
        List<TestResult> failingTests = new ArrayList<>();
        List<View> allViews = ViewHelper.getAllViews(toSearch);
        Log.e("Views", "" + allViews.size());
        for (View v : allViews) {
            totalCount++;
            TestResult res = WCAG2.testPrinciple_1_1_1(v);
            if (res.isPassed()) {
                passCount++;
            } else {
                failingTests.add(res);
            }
        }

        Log.e("Fail count", "" + failingTests.size());
        Log.e("TEST RESULTS", passCount + "/" + totalCount + " views passed WCAG2 1.1.1");
        for(TestResult test : failingTests) {
            // Log.e("FAILED", test.toString());
            System.out.println(test.toString());
        }

    }

    private void takeScreenshot(Activity activity) {

        // First wait to idle for any changes that are occurring
        uiDevice.waitForIdle(2000);

        // Create a file and save it
        Date start = new Date();
        final File screenFile = new File(Environment.getExternalStorageDirectory(), "screen_" + start.toString() + "_" + rand.nextInt());
        uiDevice.takeScreenshot(screenFile, 0.5f, 50);

        // Finally, attempt to send that file over websockets
        socket.sendScreenshot(screenFile, activity, start);

    }

    // Randomization

    private static final double CLICK_PROB_BOUND = 0.01;
    private static final double SWIPE_PROB_BOUND = 0.9;
    private static final double BACK_PROB_BOUND = 0.90001;
    private static final double LONGCLICK_PROB = 1.00;

    private UiInputActionType getNextActionType() {

        float probDecider = rand.nextFloat();
        if (probDecider <= CLICK_PROB_BOUND) {
            return UiInputActionType.CLICK;
        } else if (probDecider <= SWIPE_PROB_BOUND) {
            return UiInputActionType.SWIPE;
        } else if (probDecider <= BACK_PROB_BOUND) {
            return UiInputActionType.BACK;
        } else {
            return UiInputActionType.LONGCLICK;
        }

    }

    private UiObject getNextActionSubject(UiInputActionType type) {

        switch (type) {
            case SWIPE:
                int scrollBound = uiDevice.findObjects(By.scrollable(true)).size();
                if (scrollBound <= 0) return null;
                return uiDevice.findObject(new UiSelector()
                        .scrollable(true)
                        .instance(rand.nextInt(scrollBound)));
            case LONGCLICK:
                int longBound = uiDevice.findObjects(By.longClickable(true)).size();
                if (longBound <= 0) return null;
                return uiDevice.findObject(new UiSelector()
                        .scrollable(true)
                        .instance(rand.nextInt(longBound)));
            default:
            case CLICK:
                int clickBound = uiDevice.findObjects(By.clickable(true)).size();
                if (clickBound <= 0) return null;
                return uiDevice.findObject(new UiSelector()
                        .clickable(true)
                        .instance(rand.nextInt(clickBound)));
        }

    }

    // TUNABLE PARAMETERS:
    private static final double SWIPE_DOWN_PROB_BOUND = 0.1;
    private static final double SWIPE_UP_PROB_BOUND = 0.9;
    private static final double SWIPE_LEFT_PROB_BOUND = 0.95;
    private static final double SWIPE_RIGHT_PROB_BOUND = 1.00;
    private static final int SWIPE_MINIMUM = 50;
    private static final int SWIPE_MAXIMUM = 150;

    private void executeNextAction(UiInputActionType type, UiObject subject) {

        try {
            switch (type) {
                case CLICK:
                    subject.click();
                    break;
                case SWIPE:
                    float probDecider = rand.nextFloat();
                    int amount = ((int) Math.floor(rand.nextFloat() * (SWIPE_MAXIMUM - SWIPE_MINIMUM))) + SWIPE_MINIMUM;
                    if (probDecider <= SWIPE_DOWN_PROB_BOUND) {
                        subject.swipeDown(amount);
                        break;
                    }
                    if (probDecider <= SWIPE_UP_PROB_BOUND) {
                        subject.swipeUp(amount);
                        break;
                    }
                    if (probDecider <= SWIPE_LEFT_PROB_BOUND) {
                        subject.swipeLeft(amount);
                        break;
                    }
                    if (probDecider <= SWIPE_RIGHT_PROB_BOUND) {
                        subject.swipeRight(amount);
                        break;
                    }
                case LONGCLICK:
                    subject.longClick();
                    break;
                case BACK:
                    uiDevice.pressBack();
                    break;
            }
            actionCount++;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }


    }

}
