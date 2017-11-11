package io.github.ama_csail.ama.menu.modules;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import io.github.ama_csail.ama.menu.MenuModule;
import io.mattcarroll.hover.Content;

/**
 * A home module to display within the accessible hover menu. For now, this is required to
 * handle the case where no modules are presented, in which case there is a bug.
 * @author Aaron Vontell
 */
public class HomeModule implements Content, MenuModule{

    private Context context;
    private String title;
    private LinearLayout moduleView;
    private int layoutRes;

    public HomeModule(@NonNull Context context, @NonNull String pageTitle, @LayoutRes int layoutRes) {
        this.context = context.getApplicationContext();
        this.title = pageTitle;
        this.layoutRes = layoutRes;
        this.moduleView = createScreenView();
    }

    private LinearLayout createScreenView() {

        LinearLayout homeView = (LinearLayout) LayoutInflater
                .from(this.context)
                .inflate(layoutRes, null);

        // Prep the view
        homeView.setBackgroundColor(this.context.getResources().getColor(android.R.color.white));

        return homeView;
    }

    @Override
    public void refreshContents() {
        // Do nothing
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

    }

    @Override
    public void onHidden() {

    }
}
