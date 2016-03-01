package com.a.b.moviesapp.pojo;

import com.a.b.moviesapp.pojo.ReviewResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Java object filled by Retrofit, part of retrieving movies and trailers
 * Created by Andrew on 1/20/2016.
 */
public class Reviews {

    private Integer page;
    private List<ReviewResult> results = new ArrayList<ReviewResult>();
    private Integer totalPages;
    private Integer totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ReviewResult> getResults() {
        return results;
    }

    public void setResults(List<ReviewResult> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
