package io.github.ama_csail.ama.menu.modules;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.ama_csail.ama.R;
import io.github.ama_csail.ama.menu.MenuHelper;
import io.github.ama_csail.ama.menu.MenuModule;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverMenu.SectionId;

/**
 * The section / module representing the glossary for the accessibility menu.
 * @author Aaron Vontell
 */
public class GlossaryModule implements Content, MenuModule {

    private Context context;
    private String title;
    private LinearLayout moduleView;
    private LinearLayout glossaryList;
    private int layoutRes;

    public GlossaryModule(@NonNull Context context, @NonNull String pageTitle, @LayoutRes int layoutRes) {
        this.context = context.getApplicationContext();
        this.title = pageTitle;
        this.layoutRes = layoutRes;
        this.moduleView = createScreenView();
    }

    // Methods that are used to manipulate and change the content of this section / module

    // MODEL DEFINITION
    private Map<String, String> glossary; //TODO: Eventually, I would like to make this it's own object rather than a HashMap
    private boolean dirty;

    // CONTROLLER DEFINITIONS

    // Some important methods for startup and configuration

    private LinearLayout createScreenView() {

        LinearLayout glossaryView = (LinearLayout) LayoutInflater
                .from(this.context)
                .inflate(layoutRes, null);

        // Prep the view
        glossaryView.setBackgroundColor(this.context.getResources().getColor(android.R.color.white));
        glossaryList = glossaryView.findViewById(R.id.glossary_list);

        return glossaryView;
    }

    private void showGlossary() {

        // TODO: Sort the glossary by term

        glossaryList.removeAllViews();
        for (String term : glossary.keySet()) {
            View termView = LayoutInflater.from(this.context)
                    .inflate(R.layout.glossary_element, null);
            ((TextView) termView.findViewById(R.id.term_view)).setText(term);
            ((TextView) termView.findViewById(R.id.definition_view)).setText(glossary.get(term));
            glossaryList.addView(termView);
        }

    }

    /**
     * Replaces the current glossary with the given glossary
     * @param glossary The new glossary
     */
    public void setGlossary(Map<String, String> glossary) {
        this.glossary = glossary;
        this.dirty = true;
    }

    /**
     * Adds all items from the given glossary to the existing glossary
     * @param glossary The terms -> definitions mapping to add to the existing glossary
     */
    public void putGlossary(Map<String, String> glossary) {
        if (this.glossary != null && glossary != null) {
            this.glossary.putAll(glossary);
        } else if (glossary != null) {
            setGlossary(glossary);
        }
        this.dirty = glossary != null && glossary.size() > 0;
    }

    /**
     * Deletes all items from the existing glossary
     */
    public void clearGlossary() {
        if (this.glossary != null) {
            this.glossary.clear();
            this.dirty = true;
        }
    }

    /**
     * Refreshes the contents of this glossary module, if the glossary has been dirtied.
     */
    @Override
    public void refreshContents() {
        if (dirty) {
            this.showGlossary();
            dirty = false;
        }
    }

    @NonNull
    @Override
    public View getView() {
        return this.moduleView;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        // Don't need to do anything here at the moment
    }

    @Override
    public void onHidden() {
        // Don't need to do anything here at the moment
    }
}
