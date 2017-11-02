package io.github.ama_csail.ama;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.TypedValue;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import java.util.List;

import io.github.ama_csail.ama.util.fonts.FontUtil;
import io.github.ama_csail.ama.util.storage.SystemConfig;
import io.github.ama_csail.ama.util.views.ViewHelper;

import static android.content.Context.ACCESSIBILITY_SERVICE;

/**
 * The core class for basic AMA functionality.
 * @author Aaron Vontell
 */
public class AMA {

    // TODO: When we talk about dimensions, what do we mean?

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

    /**
     * Modifies the given View to use the given Typefaces. If bold and italic typefaces are not
     * specified (i.e. are null), then the given regular type face is used, with a best effort to
     * apply the desired styles. If View is a ViewGroup, the View hierarchy is traversed until
     * TextViews are found. <b>NOTE: It is recommended that the String-overloaded version of this
     * method is used, which ensures caching of the fonts.</b>
     * @param regularTypeface The new default Typeface to use
     * @param boldTypeface A bold typeface to be used (optional)
     * @param italicTypeface An italic typeface to be used (optional)
     * @param view The view to display the new fonts
     */
    public static void setFont(@Nullable Typeface regularTypeface,
                               @Nullable Typeface boldTypeface,
                               @Nullable Typeface italicTypeface,
                               View view) {
        FontUtil.overrideFonts(view, -1, regularTypeface, boldTypeface, italicTypeface);
    }

    /**
     * Modifies the given View to use the given Typefaces. If bold and italic typefaces are not
     * specified (i.e. are null), then the given regular type face is used, with a best effort to
     * apply the desired styles. If View is a ViewGroup, the View hierarchy is traversed until
     * TextViews are found. Each string is a path to the desired font within the given context's
     * assets folder.
     * @param context A context which holds the font assets
     * @param regularTypeface A path in assets to the new default Typeface to use
     * @param boldTypeface A path in assets to the bold typeface to be used (optional)
     * @param italicTypeface A path in assets to the italic typeface to be used (optional)
     * @param view The view to display the new fonts
     */
    public static void setFont(@NonNull Context context,
                               @Nullable String regularTypeface,
                               @Nullable String boldTypeface,
                               @Nullable String italicTypeface,
                               View view) {
        Typeface rt = FontUtil.get(regularTypeface, context);
        Typeface bt = FontUtil.get(boldTypeface, context);
        Typeface it = FontUtil.get(italicTypeface, context);
        setFont(rt, bt, it, view);
    }

    /**
     * Modifies the given View to use the given Typefaces. If bold and italic typefaces are not
     * specified (i.e. are null), then the given regular type face is used, with a best effort to
     * apply the desired styles. If View is a ViewGroup, the View hierarchy is traversed until
     * TextViews are found. Each resource is a raw resource which points to the requested typeface.
     * @param context A context which holds the font assets
     * @param regularTypeface A resource in res/raw to the new default Typeface to use
     * @param boldTypeface A resource in res/raw to the bold typeface to be used (optional)
     * @param italicTypeface A resource in res/raw to the italic typeface to be used (optional)
     * @param identifier A name for this font type to use for caching purposes
     * @param view The view to display the new fonts
     */
    public static void setFont(@NonNull Context context,
                               @RawRes int regularTypeface,
                               @RawRes int boldTypeface,
                               @RawRes int italicTypeface,
                               String identifier,
                               View view) {

        Typeface rt = FontUtil.getFromRes(identifier + "_REG", regularTypeface, context);
        Typeface bt = FontUtil.getFromRes(identifier + "_BOLD", boldTypeface, context);
        Typeface it = FontUtil.getFromRes(identifier + "_ITALIC", italicTypeface, context);
        setFont(rt, bt, it, view);
    }

    /**
     * Overrides the font size of any TextView encapsulated within the given view to be the given
     * size.
     * @param view The view to modify (will modify any contained TextViews)
     * @param size The new font size to set
     */
    public static void setFontSize(View view, @Dimension float size) {
        FontUtil.overrideFonts(view, size, null, null, null);
    }

    /**
     * Increases the font size of each TextView contained with the given view by the given number
     * of pixels
     * @param view The View (or ViewGroup) to change font size within
     * @param amount The amount to increase each font by
     */
    public static void increaseFontSize(View view, float amount) {

        List<TextView> textViews = ViewHelper.getAllTextViews(view);
        for (TextView tv : textViews) {
            float pixelSize = tv.getTextSize();
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixelSize + amount);
        }

    }

    /**
     * Increases the white space around each view by the desired amount (left, right, top, and
     * bottom)
     * @param amount The amount (in pixels) to increase the padding around each view
     * @param views Any number of views that one wishes to increase the padding around
     */
    public static void increaseWhitespace(@Dimension int amount, View ... views) {

        for (View v : views) {
            int top = v.getPaddingTop();
            int bot = v.getPaddingBottom();
            int lef = v.getPaddingLeft();
            int rig = v.getPaddingRight();
            v.setPadding(lef + amount, top + amount, rig + amount, bot + amount );
        }

    }

}
