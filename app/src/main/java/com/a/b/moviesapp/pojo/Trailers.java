package com.a.b.moviesapp.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Java object filled by Retrofit, part of retrieving movies and trailers
 * Created by Andrew on 1/20/2016.
 */
public class Trailers {

    private List<Youtube> youtube = new ArrayList<Youtube>();

    public List<Youtube> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<Youtube> youtube) {
        this.youtube = youtube;
    }
}
