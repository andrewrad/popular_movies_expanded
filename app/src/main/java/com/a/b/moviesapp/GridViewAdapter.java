package com.a.b.moviesapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;


import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.pojo.Movie;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew on 1/7/2016.
 */
public class GridViewAdapter extends RecyclerView.Adapter <GridViewAdapter.MyViewHolder>{
//    String TAG="GridViewAdapter";
    Integer mPicHeight=0;
    private static RecyclerClickListener mListener;
    List<Movie>mMovies= Collections.emptyList();
    Context mContext;

    public GridViewAdapter(Context context,RecyclerClickListener clickListener){
        mContext=context;
        mListener=clickListener;
        mPicHeight=getHeight();
    }
    public Integer getHeight(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        Display display = wm.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        float marginFloat=mContext.getApplicationContext().getResources().getDimension(R.dimen.picture_margin);
        float px = marginFloat * (metrics.densityDpi / 160f);
        Integer marginInt=Math.round(px);

        Integer height=3 * ((point.x / 2) -(marginInt * 2))/2;

//        Log.e(TAG, "display metrics: " + point.x + ", y: " + point.y+", PicHeight: "+height);
        return height;
    }

    public void setList(List<Movie>movies){
        mMovies=movies;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.image_box, parent, false);

        view.getLayoutParams().height=mPicHeight;

        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movie current=mMovies.get(position);
        String fullUrl= Constants.TMDB_IMAGE_BASE_URL_LARGE+current.getPosterUrl();

        Glide.with(mContext)
                .load(fullUrl)
                .placeholder(R.drawable.placeholder_vertical)
                .crossFade(500)
//                .error(R.drawable.user_placeholder_error)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(mMovies!=null){
            return mMovies.size();
        }else{
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.movie_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerClicked(v,this.getLayoutPosition());
        }
    }
}