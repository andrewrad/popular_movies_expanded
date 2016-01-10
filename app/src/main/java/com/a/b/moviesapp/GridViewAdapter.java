package com.a.b.moviesapp;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew on 1/7/2016.
 */
public class GridViewAdapter extends RecyclerView.Adapter <GridViewAdapter.MyViewHolder>{
    String TAG="GridViewAdapter";
    Integer mPicHeight=0;
    private static RecyclerClickListener mListener;
//    private final LayoutInflater inflater;
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

        Log.e(TAG, "display metrics: " + point.x + ", y: " + point.y+", PicHeight: "+height);
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

//        view.setOnClickListener(this);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Movie current=mMovies.get(position);
//        holder.mTextView.setText("testing a bunch");
//        holder.mImageView.setImageResource(R.mipmap.ic_launcher);
//        String fullUrl="http://image.tmdb.org/t/p/w500/"+current.getMovieUrl();
        String fullUrl="http://image.tmdb.org/t/p/w780/"+current.getMovieUrl();
        Log.e(TAG, "fullUrl: " + fullUrl);

//        Picasso.with(holder.mImageView.getContext())
//                .load(R.mipmap.hadoop_png)
////                .fit()
//                .centerCrop()
////                .load(URLEncoder.encode(fullUrl))
//                .into(holder.mImageView);

        Glide.with(mContext)
//                .load(R.mipmap.hadoop_png)
//                .load(R.drawable.hadoop_png)
                .load(fullUrl)
                .placeholder(R.drawable.placeholder_vertical)
                .into(holder.mImageView);

//        holder.mImageView.setImageResource(R.drawable.hadoop);

//        Uri uri = Uri.parse(movie.getPoster());
//        Context c = holder.mImageView.getContext();
//        Glide.with(c).load(uri).placeholder(R.drawable.image).into(viewHolder.imageView);
//        viewHolder.setPosition(i);

//        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.movie_picture);
            itemView.setOnClickListener(this);
//            mImageView.setOnClickListener(this);
//            mTextView=(TextView) itemView.findViewById(R.id.wordage);
        }

        @Override
        public void onClick(View v) {
            Log.e("MyViewHolder","clicked!");
            mListener.recyclerClicked(v,this.getLayoutPosition());
        }
    }
}