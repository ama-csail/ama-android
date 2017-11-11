package io.github.ama_csail.ama.menu;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.ama_csail.ama.menu.modules.MenuModuleType;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

/**
 * The menu service that will be started to provide an accessibility menu.
 * @author Aaron Vontell
 */
public class AccessibleHoverMenuService extends HoverMenuService {

    public static String IDENTIFIER = "AMAAccessibleMenuService";
    private final IBinder binder = new AccessibleHoverMenuBinder();
    private Set<MenuModuleType> registeredModules;

    private AccessibleHoverMenu menu;

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        // Configure and start your HoverView.
        menu = new AccessibleHoverMenu(getApplicationContext());
        registeredModules = new HashSet<>();
        possiblyRegisterModule(MenuModuleType.HOME);
        hoverView.setMenu(menu);
        hoverView.collapse();
        Log.e("MENU", "Menu started");
    }

    private void possiblyRegisterModule(MenuModuleType type) {
        if (!registeredModules.contains(type)) {
            menu.registerModule(type);
            registeredModules.add(type);
        }
    }


    public void provideGlossary(Map<String, String> glossary) {

        Log.e("MENU", "Menu has received a new glossary. Registering module");
        possiblyRegisterModule(MenuModuleType.GLOSSARY);
        menu.provideGlossary(glossary);

    }

    /**
     * Class used for the client Binder.
     */
    public class AccessibleHoverMenuBinder extends Binder {
        public AccessibleHoverMenuService getService() {
            // Return this instance of AccessibleHoverMenuService so clients can call public methods
            return AccessibleHoverMenuService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
