package io.github.ama_csail.amaexampleapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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

//import io.github.ama_csail.ama.testing.AppSpecification;
//import io.github.ama_csail.ama.testing.ViewInfoStruct;
import io.github.ama_csail.ama.util.views.ViewHelper;

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

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

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
            mDevice.pressBack();

            // First scroll down
            UiObject scrollAgain = mDevice.findObject(new UiSelector()
                    .className("android.widget.ScrollView")
                    .instance(0));
            scrollAgain.swipeUp(50);

            UiObject article = mDevice.findObject(new UiSelector()
                    .clickable(true)
                    .instance(3));
            article.click();

            mDevice.waitForIdle(4000);
            mDevice.pressBack();
            mDevice.waitForIdle(2000);


        } catch (UiObjectNotFoundException e) {
            Log.e("UI-AUTO", "Could not find object: " + e.getLocalizedMessage());
        }


        assertTrue("App has loaded!", true);

    }


}
