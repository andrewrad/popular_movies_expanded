package com.a.b.moviesapp.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/15/2016.
 */
public class Result {

    private int totalCount;
    private boolean incompleteResults;
    private List<TrailerResults> items = new ArrayList<TrailerResults>();

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<TrailerResults> getItems() {
        return items;
    }

    public void setItems(List<TrailerResults> items) {
        this.items = items;
    }
}