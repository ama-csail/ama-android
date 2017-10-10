package io.github.ama_csail.ama;

import android.app.Activity;
import android.content.Intent;

/**
 * An accessible version of and Android activity.
 */
public class AccessibleActivity extends Activity {

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

}
