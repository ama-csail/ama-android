package io.github.ama_csail.amaexampleapp.utils;

/**
 * A structure which holds information regarding a test from an individual guideline
 */
public class TestResult {

    private boolean passed;
    private String testTitle;
    private String testDescription;
    private String testError;
    private String suggestion;
    private String identifier;

    public TestResult(String testTitle, String testDescription) {
        this.testTitle = testTitle;
        this.testDescription = testDescription;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getTestError() {
        return testError;
    }

    public void setTestError(String testError) {
        this.testError = testError;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "passed=" + passed +
                ", testTitle='" + testTitle + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", testError='" + testError + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
