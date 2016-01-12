package me.mehmetkaya.eldevar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mehmetkaya on 12.01.16.
 */
public class YemekListAdapter extends RecyclerView.Adapter<YemekListAdapter.ViewHolder> {

    Context mContext;
    JSONObject tarifler;
    OnItemClickListener mItemClickListener;

    public YemekListAdapter(Context context, JSONObject tarifler){
        mContext = context;
        this.tarifler = tarifler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tarif tarif = new TarifData(tarifler).tarifList().get(position);
        final ViewHolder customHolder = holder;
        holder.yemekName.setText(tarif.getTarifName());
        //Picasso.with(mContext).load(tarif.getTarifImage()).into(holder.yemekResim);
        Bitmap photo;
        Picasso.with(mContext).load(tarif.getTarifImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                customHolder.yemekResim.setImageBitmap(bitmap);
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                        customHolder.yemekNameHolder.setBackgroundColor(bgColor);
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return new TarifData(tarifler).getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout mainHolder;
        public LinearLayout yemekNameHolder;
        public TextView yemekName;
        public ImageView yemekResim;

        public ViewHolder(View itemView){

            super(itemView);
            mainHolder = (LinearLayout)itemView.findViewById(R.id.mainHolder);
            yemekName = (TextView)itemView.findViewById(R.id.yemekName);
            yemekNameHolder = (LinearLayout)itemView.findViewById(R.id.yemekNameHolder);
            yemekResim = (ImageView)itemView.findViewById(R.id.yemekCardResim);
            mainHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v){

            if(mItemClickListener != null){
                mItemClickListener.onItemClick(itemView, getPosition());
            }

        }




    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public String getTarifID(int position){

        try {
            JSONArray tarifList = tarifler.getJSONArray("tarifler");

            return tarifList.getJSONObject(position).getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



}
