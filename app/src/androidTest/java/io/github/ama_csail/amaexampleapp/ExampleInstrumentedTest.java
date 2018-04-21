package io.github.ama_csail.amaexampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.ama_csail.ama.testserver.ViewInfoStruct;
import io.github.ama_csail.ama.testserver.MobileSocket;
import io.github.ama_csail.ama.util.views.ViewHelper;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleInstrumentedTest {

    private UiDevice mDevice;

    private static final String BASIC_SAMPLE_PACKAGE
            = "io.github.ama_csail.amaexampleapp";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private Random rand = new Random();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("io.github.ama_csail.amaexampleapp", appContext.getPackageName());
    }

    @Test
    public void testSimple() {

        MobileSocket socket = new MobileSocket("18.111.5.156", 8080);
        socket.start();

        mDevice = UiDevice.getInstance(getInstrumentation());

        //AppSpecification app = new AppSpecification(BASIC_SAMPLE_PACKAGE);
        //app.addActivity(MainActivity.class);

        // Start from the home screen
        //mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

        // Send info that the app has started
        socket.sendStartInfo(BASIC_SAMPLE_PACKAGE);

        // First and foremost, grant external write permissions if not already allowed. This will'
        // allow us to save screenshots and logs with all testing results. Once the tests are finished,
        // we will delete the temporary storage.

//        Activity activity = activityTestRule.getActivity();
//        View toSearch = activity.getWindow().getDecorView().getRootView();
//        for (View v : ViewHelper.getAllViews(toSearch)) {
//            ViewInfoStruct thisView = new ViewInfoStruct(v);
//            Log.i("VIEW INFO", thisView.toString());
//        }

//        final File screenFile = new File(Environment.getExternalStorageDirectory(), "screenshotTest.png");
//        final File viewFile = new File(Environment.getExternalStorageDirectory(), "viewFile.xml");
//        try {
//            screenFile.createNewFile();
//            viewFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        mDevice.takeScreenshot(screenFile);
//        Log.e("Screenshot saved at", screenFile.toString());

        //mDevice.waitForIdle(5000);

        try {

            Activity act1 = getActivityInstance();
            socket.sendNewScreen(act1);
            evaluateAccessibility(act1, socket);
            takeScreenshot(act1, socket);

            // First scroll down
            UiObject scroll = mDevice.findObject(new UiSelector()
                    .className("android.widget.ScrollView")
                    .instance(0));
            scroll.swipeUp(50);

            // Then click on a thing
            UiObject about = mDevice.findObject(new UiSelector()
                    .clickable(true)
                    .instance(0));
            about.clickAndWaitForNewWindow();

            mDevice.waitForIdle(2000);
            Activity act2 = getActivityInstance();
            socket.sendNewScreen(act2);
            evaluateAccessibility(act2, socket);
            takeScreenshot(act2, socket);
            mDevice.pressBack();

            // First scroll down
            UiObject scrollAgain = mDevice.findObject(new UiSelector()
                    .className("android.widget.ScrollView")
                    .instance(0));
            scrollAgain.swipeUp(50);

            Activity act3 = getActivityInstance();
            socket.sendNewScreen(act3);
            evaluateAccessibility(act3, socket);
            takeScreenshot(act3, socket);

            UiObject article = mDevice.findObject(new UiSelector()
                    .clickable(true)
                    .instance(3));
            article.click();

            mDevice.waitForIdle(4000);
            Activity act4 = getActivityInstance();
            //Log.e("FOUND ACTIVITY", currentActivity != null ? currentActivity.getLocalClassName() : "Unknown");
            socket.sendNewScreen(act4);
            evaluateAccessibility(act4, socket);
            takeScreenshot(act4, socket);
            mDevice.pressBack();
            mDevice.waitForIdle(2000);

            socket.sendFinishInfo(BASIC_SAMPLE_PACKAGE);


        } catch (UiObjectNotFoundException e) {
            Log.e("UI-AUTO", "Could not find object: " + e.getLocalizedMessage());
        }


        assertTrue("App has loaded!", true);

    }

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

    private void evaluateAccessibility(Activity activity, MobileSocket socket) {

        View toSearch = activity.getWindow().getDecorView().getRootView();
        for (View v : ViewHelper.getAllViews(toSearch)) {
            ViewInfoStruct thisView = new ViewInfoStruct(v);
            if (thisView.needsContentDescription()) {
                socket.sendContentDescriptionMissing(
                        activity,
                        thisView.getShortName(),
                        thisView.getLongName(),
                        thisView.getId(),
                        thisView.getUpperLeftBound(),
                        thisView.getLowerRightBound()
                );
            }
        }

    }

    private void takeScreenshot(Activity activity, MobileSocket socket) {

        // First wait to idle for any changes that are occurring
        mDevice.waitForIdle(2000);

        // Create a file and save it
        Date start = new Date();
        final File screenFile = new File(Environment.getExternalStorageDirectory(), "screen_" + start.toString() + "_" + rand.nextInt());
        mDevice.takeScreenshot(screenFile, 0.5f, 50);

        // Finally, attempt to send that file over websockets
        socket.sendScreenshot(screenFile, activity, start);

    }


}
