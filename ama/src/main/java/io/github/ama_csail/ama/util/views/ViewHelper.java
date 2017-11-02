package io.github.ama_csail.ama.util.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * A collection of utilities for various view modifications, adjustments, etc...
 * @author Aaron Vontell
 */
public class ViewHelper {

    /**
     * Returns a list of all TextViews contained (and possibly including) within view
     * @param view The view to search through for TextView objects
     * @return a list of all TextViews contained within view
     */
    public static List<TextView> getAllTextViews(View view) {

        Stack<View> front = new Stack<>();
        List<TextView> textViews = new LinkedList<>();
        front.add(view);

        // Traverse through the tree, finding TextViews as we go
        while (!front.empty()) {
            View v = front.pop();
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    front.add(child);
                }
            } else if (v instanceof TextView) {
                textViews.add((TextView) v);
            }
        }

        return textViews;

    }

}
