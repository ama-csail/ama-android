package io.github.ama_csail.ama;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

import io.github.ama_csail.ama.util.storage.SystemConfig;

import static android.content.Context.ACCESSIBILITY_SERVICE;

/**
 * The core class for basic AMA functionality.
 * @author Aaron Vontell
 */
public class AMA {

    //section Constants

    private static final String TALKBACK_PACKAGE = "com.google.android.marvin.talkback";

    //section AMA Versioning and Information

    /**
     * Returns a string representing the version of this AMA library.
     * @return a string representing the version of this AMA library.
     */
    public String getVersion() {
        return SystemConfig.VERSION;
    }

    /**
     * Returns a url which contains information about the AMA library.
     * @return a url which contains information about the AMA library.
     */
    public String getHomepage() {
        return SystemConfig.HOMEPAGE;
    }


    // section Static Helper Method

    /**
     * Checks to see if TalkBack is installed (note that this is the Google version of TalkBack)
     * @param context The calling activity
     * @return true if TalkBack is installed on this device
     */
    public static boolean isTalkBackInstalled(Context context) {

        List<ApplicationInfo> packages;
        PackageManager pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(TALKBACK_PACKAGE))
                return true;
        }
        return false;

    }

    /**
     * Checks to see if TalkBack is currently enabled
     * @param context The calling activity
     * @return true if TalkBack is enabled
     */
    public static boolean isTalkBackEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        return am.isEnabled();
    }

    /**
     * Checks to see if explore by touch (provided via TalkBack) is currently enabled
     * @param context The calling activity
     * @return true if explore by touch is enabled
     */
    public static boolean isExploreByTouchEnabled(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        return am.isTouchExplorationEnabled();
    }

    /**
     * Returns an Intent that will open Google Play Store, and link to the TalkBack app page
     * @return The intent that will open the play store at the TalkBack page
     */
    public static Intent getTalkBackPlayIntent() {

        return new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + TALKBACK_PACKAGE));

    }

}
