package io.github.ama_csail.amaexampleapp;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import io.github.ama_csail.ama.AccessibleActivity;

public class MainActivity extends AccessibleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableMenu();
        enableDyslexiaFont(true);
        provideGlossary(AccessibleDefinitions.getGlossary());

    }


    public static class AccessibleDefinitions {

        public static Map<String, String> getGlossary() {
            Map<String, String> terms = new HashMap<>();
            terms.put("accessible", "To be usable in different contexts, no matter the operator");
            terms.put("glossary", "A list of terms and definitions");
            return terms;
        }

    }

}
