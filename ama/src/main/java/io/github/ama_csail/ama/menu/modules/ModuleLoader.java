package io.github.ama_csail.ama.menu.modules;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import io.github.ama_csail.ama.R;
import io.github.ama_csail.ama.menu.MenuHelper;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;

/**
 * Class for creating various modules to be used within the menu.
 * @author Aaron Vontell
 */
public class ModuleLoader {

    public static HoverMenu.Section getHomeModule(Context context) {

        final String IDENTIFIER = "AMAHome";
        final String TITLE = "Home";
        final int ICON_RESOURCE = R.drawable.ama;
        final int LAYOUT_RESOURCE = R.layout.home_module;

        Content home = new HomeModule(context, TITLE, LAYOUT_RESOURCE);

        return createSection(context, IDENTIFIER, ICON_RESOURCE, home);

    }

    public static HoverMenu.Section getGlossaryModule(Context context) {

        final String IDENTIFIER = "AMAGlossary";
        final String TITLE = "Glossary";
        final int ICON_RESOURCE = R.drawable.ama;
        final int LAYOUT_RESOURCE = R.layout.glossary_module;

        Content glossary = new GlossaryModule(context, TITLE, LAYOUT_RESOURCE);

        return createSection(context, IDENTIFIER, ICON_RESOURCE, glossary);

    }

    private static HoverMenu.Section createSection(Context context, String identifier,
                                                   @DrawableRes int iconRes, Content module) {

        HoverMenu.SectionId sectionId = new HoverMenu.SectionId(identifier);
        ImageView tabView = MenuHelper.getTabView(context, iconRes);
        return new HoverMenu.Section(sectionId, tabView, module);

    }

    public static HoverMenu.Section getModule(Context context, MenuModuleType type) {
        switch (type) {
            case GLOSSARY:
                return getGlossaryModule(context);
            case HOME:
                return getHomeModule(context);
            default:
                throw new RuntimeException("Module type is not a valid module.");
        }
    }

    private ModuleLoader() {}

}
