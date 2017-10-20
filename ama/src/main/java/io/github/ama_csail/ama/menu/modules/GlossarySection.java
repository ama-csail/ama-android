package io.github.ama_csail.ama.menu.modules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.ama_csail.ama.R;
import io.github.ama_csail.ama.menu.backend.MenuHelper;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverMenu.SectionId;

/**
 * The section / module representing the glossary for the accessibility menu.
 * @author Aaron Vontell
 */
public class GlossarySection {

    public final static String IDENTIFIER = "AMAGlossary";
    public final static String TITLE = "Glossary";
    public final static int ICON_RESOURCE = R.drawable.ama;

    public static HoverMenu.Section getGlossarySection(Context context) {
        SectionId sectionId = new SectionId(IDENTIFIER);
        Content content = new GlossaryContent(context, TITLE);
        ImageView tabView = MenuHelper.getTabView(context, ICON_RESOURCE);
        return new HoverMenu.Section(sectionId, tabView, content);
    }

    private GlossarySection() {}

    private static class GlossaryContent implements Content {

        private Context context;
        private String title;
        private View pageView;

        public GlossaryContent(@NonNull Context context, @NonNull String pageTitle) {
            this.context = context.getApplicationContext();
            this.title = pageTitle;
            this.pageView = createScreenView();
        }

        private View createScreenView() {
            TextView wholeScreen = new TextView(this.context);
            wholeScreen.setText("Screen: " + this.title);
            wholeScreen.setGravity(Gravity.CENTER);
            return wholeScreen;
        }

        @NonNull
        @Override
        public View getView() {
            return this.pageView;
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

}
