package io.github.ama_csail.ama;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Map;

import io.github.ama_csail.ama.menu.backend.AccessibleHoverMenuService;
import io.mattcarroll.hover.overlay.OverlayPermission;

/**
 * An accessible version of and Android activity.
 */
public class AccessibleActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_HOVER_PERMISSION = 1000;
    private boolean mPermissionsRequested = false;

    private AccessibleHoverMenuService menuService;

    @Override
    public void startActivity(Intent intent) {

//        String intendedClass = intent.getComponent().getClassName();
//        AlternativeUIManager.getInstance();
//
//        UserInterface desiredInterface = AlternativeUIManager.transform();

        super.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On Android M and above we need to ask the user for permission to display the Hover
        // menu within the "alert window" layer.  Use OverlayPermission to check for the permission
        // and to request it.
        if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(this)) {
            @SuppressWarnings("NewApi")
            Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(this);
            startActivityForResult(myIntent, REQUEST_CODE_HOVER_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_HOVER_PERMISSION == requestCode) {
            mPermissionsRequested = true;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public View getRootView() {
        return findViewById(android.R.id.content);
    }

    /**
     * Enables the accessible menu in a collapsed state
     */
    public void enableMenu() {

        Intent intent = new Intent(this, AccessibleHoverMenuService.class);
        startService(intent);

    }

    public void provideGlossary(Map<String, String> glossary) {



    }

    public void disabledMenu() {
        throw new RuntimeException("Not yet implemented!");
    }


    // Vision-based helper methods

    public void enableDyslexiaFont(boolean enabled) {

        // TODO: Caching to restore old font
        if (enabled) {
            View container = getRootView();
            AMA.setFont(this, R.raw.opendyslexicregular,
                    R.raw.opendyslexicbold, R.raw.opendyslexicitalic, "OpenDyslexic", container);
            //AMA.setFontSize(container, 24);
        }

    }

}
