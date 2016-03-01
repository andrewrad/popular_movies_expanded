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
import android.widget.TextView;


import com.a.b.moviesapp.other.Constants;
import com.a.b.moviesapp.pojo.Movie;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * This class is the adapter for visually filling the recycler view with movie poster images.
 * Each view holder has a default textview that shows the movie title. If the movie poster image
 * fails to load, the title will still occupy the space
 * Created by Andrew on 1/7/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder>{
    String TAG="RecyclerViewAdapter";
    Integer mPicHeight=0;
    private static RecyclerClickListener mListener;
    List<Movie>mMovies= Collections.emptyList();
    Context mContext;

    /**
     * Constructor; sets context, sets custom clickListener, and calls getHeight()
     * @param context general application context
     * @param clickListener custom clicklistener
     */
    public RecyclerViewAdapter(Context context, RecyclerClickListener clickListener){
        mContext=context;
        mListener=clickListener;
        mPicHeight=getHeight();
    }

    /**
     * Calculates the window size for each movie poster image. If the height is not set, the RecyclerView will
     * overcompensate and give the movie poster window too much room and there will be large height gaps between
     * images.
     * On phones the movie posters will form 2 columns, tablets have 3. Since each image from themoviedb is the
     * same aspect ratio, we can calculate the height based on width of the device.
     *
     * @return height of one movie poster image
     */
    public Integer getHeight(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        Display display = wm.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        float marginFloat=mContext.getApplicationContext().getResources().getDimension(R.dimen.picture_margin);
        float px = marginFloat * (metrics.densityDpi / 160f);
        Integer marginInt=Math.round(px);

        Integer height;
        if(mContext.getResources().getBoolean(R.bool.isTablet)) {
            height=3 * ((point.x / 2) -(marginInt * 3))/6;
        }else{
            height=3 * ((point.x / 2) -(marginInt * 2))/2;
        }
//        Log.e(TAG, "display metrics, x:" + point.x + ", y: " + point.y+", PicHeight: "+height+", marginInt: "+marginInt);
        return height;
    }

    /**
     * Sets the list of movies that need to be shown in this RecyclerViewAdapter
     * @param movies list of movies that need to be shown in the RecyclerView
     */
    public void setList(List<Movie>movies){
        mMovies=movies;
        notifyDataSetChanged();
    }

    /**
     * Required for RecyclerViewAdapter
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.image_box, parent, false);

        view.getLayoutParams().height=mPicHeight;   //sets the window height for each movie poster

        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    /**
     * Sets the title and move poster image. The title is behind the image on a FrameLayout. We set the
     * title first then set the image. If the image fails to load, the user will still see the title.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(mMovies!=null) {
            Movie current = mMovies.get(position);
            String fullUrl = Constants.TMDB_IMAGE_BASE_URL_LARGE + current.getPosterUrl();

//            Log.e(TAG, "poster url: "+mMovies.get(position).getMovieTitle()+", "+fullUrl);

            holder.mTitleView.setText(mMovies.get(position).getMovieTitle());   //set the title

            Glide.with(mContext)    //Glide used to place movie poster from themoviedb server
                    .load(fullUrl)
                .placeholder(R.drawable.blank_image)
                    .crossFade(400)     //crossFade for a smooth image fill
                    .into(holder.mImageView);
        }
    }

    /**
     * Required for RecyclerView implementation
     * @return number of movies
     */
    @Override
    public int getItemCount() {
        if(mMovies!=null){
            return mMovies.size();
        }else{
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageView;   //holds the movie poster image
        TextView mTitleView;    //holds the movie title

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleView=(TextView) itemView.findViewById(R.id.title_view);
            mImageView=(ImageView) itemView.findViewById(R.id.movie_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerClicked(v,this.getLayoutPosition());
        }
    }
}