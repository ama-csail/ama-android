package io.github.ama_csail.amaexampleapp;

import android.os.Bundle;

import io.github.ama_csail.ama.AccessibleActivity;

public class MainActivity extends AccessibleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableMenu();

    }
}
