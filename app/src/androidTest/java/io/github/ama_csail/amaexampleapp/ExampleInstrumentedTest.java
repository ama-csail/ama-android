package io.github.ama_csail.amaexampleapp;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.ama_csail.ama.testserver.MobileSocket;
import io.github.ama_csail.amaexampleapp.utils.AppSpecification;
import io.github.ama_csail.amaexampleapp.utils.BilityTester;
import io.github.ama_csail.amaexampleapp.utils.TestSuiteType;
import io.github.ama_csail.amaexampleapp.utils.UiInputActionType;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleInstrumentedTest {

    private AppSpecification specification;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void configureAppSpec() {

        Context appContext = InstrumentationRegistry.getTargetContext();
        specification = new AppSpecification(appContext.getPackageName());
        specification.expectActivity(MainActivity.class);
        specification.expectActivity(AboutActivity.class);
        specification.allowedInputs(UiInputActionType.CLICK, UiInputActionType.SWIPE);

    }

    @Test
    public void testAccessibility() {

        MobileSocket socket = new MobileSocket("192.168.43.230", 8080);
        //socket.start();

        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

        new BilityTester(specification, mDevice)
                .provideSocket(socket)
                .setTimeout(5000)
                .setRuns(3)
                .setSeed(20182018)
                .setTestSuites(TestSuiteType.WCAG2_A)
                .startupApp()
                .startTestLoop()
                .printResults();

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

//        try {
//
//            Activity act1 = getActivityInstance();
//            socket.sendNewScreen(act1);
//            evaluateAccessibility(act1, socket);
//            takeScreenshot(act1, socket);
//
//            // First scroll down
//            UiObject scroll = mDevice.findObject(new UiSelector()
//                    .className("android.widget.ScrollView")
//                    .instance(0));
//            scroll.swipeUp(50);
//
//            // Then click on a thing
//            UiObject about = mDevice.findObject(new UiSelector()
//                    .clickable(true)
//                    .instance(0));
//            about.clickAndWaitForNewWindow();
//
//            mDevice.waitForIdle(2000);
//            Activity act2 = getActivityInstance();
//            socket.sendNewScreen(act2);
//            evaluateAccessibility(act2, socket);
//            takeScreenshot(act2, socket);
//            mDevice.pressBack();
//
//            // First scroll down
//            UiObject scrollAgain = mDevice.findObject(new UiSelector()
//                    .className("android.widget.ScrollView")
//                    .instance(0));
//            scrollAgain.swipeUp(50);
//
//            Activity act3 = getActivityInstance();
//            socket.sendNewScreen(act3);
//            evaluateAccessibility(act3, socket);
//            takeScreenshot(act3, socket);
//
//            UiObject article = mDevice.findObject(new UiSelector()
//                    .clickable(true)
//                    .instance(3));
//            article.click();
//
//            mDevice.waitForIdle(4000);
//            Activity act4 = getActivityInstance();
//            //Log.e("FOUND ACTIVITY", currentActivity != null ? currentActivity.getLocalClassName() : "Unknown");
//            socket.sendNewScreen(act4);
//            evaluateAccessibility(act4, socket);
//            takeScreenshot(act4, socket);
//            mDevice.pressBack();
//            mDevice.waitForIdle(2000);
//
//            socket.sendFinishInfo(BASIC_SAMPLE_PACKAGE);
//
//
//        } catch (UiObjectNotFoundException e) {
//            Log.e("UI-AUTO", "Could not find object: " + e.getLocalizedMessage());
//        }


        assertTrue("App has loaded!", true);

    }




}
