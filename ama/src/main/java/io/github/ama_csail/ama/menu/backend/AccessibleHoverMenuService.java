package io.github.ama_csail.ama.menu.backend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

/**
 * The menu service that will be started to provide an accessibility menu.
 * @author Aaron Vontell
 */
public class AccessibleHoverMenuService extends HoverMenuService {

    public static String IDENTIFIER = "AMAAccessibleMenuService";

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        // Configure and start your HoverView.
        AccessibleHoverMenu menu = new AccessibleHoverMenu(getApplicationContext());
        menu.registerModule();
        hoverView.setMenu(menu);
        hoverView.expand();
        Log.e("MENU", "Menu started");
    }

}
