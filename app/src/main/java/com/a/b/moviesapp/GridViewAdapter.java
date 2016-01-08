package com.a.b.moviesapp;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * Created by Andrew on 1/7/2016.
 */
public class GridViewAdapter extends RecyclerView.Adapter <GridViewAdapter.MyViewHolder> implements View.OnClickListener{
    String TAG="GridViewAdapter";

    private final LayoutInflater inflater;
    List<Movie>mMovies= Collections.emptyList();
    Context mContext;

    public GridViewAdapter(Context context, List<Movie> movies) {
        mContext=context;
        inflater= LayoutInflater.from(context);
        mMovies=movies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.image_box,null,false);
        view.setOnClickListener(this);
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
        Log.e("RecyclerView", "fullUrl: " + fullUrl);
//        Picasso.with(holder.mImageView.getContext())
//                .load(Uri.parse(fullUrl))
////                .load(URLEncoder.encode(fullUrl))
//                .into(holder.mImageView);
//        Picasso.with(holder.mImageView.getContext())
//                .load(R.mipmap.hadoop_png)
////                .fit()
//                .centerCrop()
////                .load(URLEncoder.encode(fullUrl))
//                .into(holder.mImageView);

        Glide.with(mContext)
//                .load(R.mipmap.hadoop_png)
                .load(fullUrl)
                .placeholder(R.drawable.placeholder_vertical)
                .into(holder.mImageView);


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

    @Override
    public void onClick(View v) {
        Log.e(TAG,"clickeded!!");
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;
//        TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.movie_picture);
//            mImageView.setOnClickListener(this);
//            mTextView=(TextView) itemView.findViewById(R.id.wordage);
        }

        @Override
        public void onClick(View v) {
            Log.e("MyViewHolder","clicked!");
        }
    }
}