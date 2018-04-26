package io.github.ama_csail.amaexampleapp.utils;

import java.util.List;

/**
 * Defines a specification of an application which can be used for automated testing
 * @author Aaron Vontell
 */
public class AppSpecification {

    private String packageName;

    // Definitions for the application tree
    private List<Class> knownActivities;
    private List<String> knownViewIds;

    public AppSpecification() {

    }

    public void expectActivity(Class activityClass) {

    }

    public void expectId(String id) {

    }



}
