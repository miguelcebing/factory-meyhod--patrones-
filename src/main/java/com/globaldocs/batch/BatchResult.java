package com.globaldocs.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects results from batch document processing.
 * Tracks successes, errors, and provides summary statistics.
 */
public final class BatchResult {

    private int total;
    private int success;
    private final List<String> errors = new ArrayList<>();

    public void registerSuccess() {
        success++;
        total++;
    }

    public void registerError(String fileName, String reason) {
        errors.add(String.format("%s -> %s", fileName, reason));
        total++;
    }

    public int getTotal() { return total; }
    public int getSuccess() { return success; }
    public List<String> getErrors() { return Collections.unmodifiableList(errors); }

    public int getSuccessRate() {
        return total == 0 ? 0 : (int) ((success * 100.0) / total);
    }

    public String getSummary() {
        return String.format("Batch finished: %d/%d processed successfully (%d%%)",
                success, total, getSuccessRate());
    }
}