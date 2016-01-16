package com.a.b.moviesapp.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/15/2016.
 */
public class TrailerResults {

    private Integer id;
    private List<Result> results = new ArrayList<Result>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}