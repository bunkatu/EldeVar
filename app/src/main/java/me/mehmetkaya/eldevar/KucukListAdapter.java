package me.mehmetkaya.eldevar;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by kullanici on 13.01.2016.
 */
public class KucukListAdapter extends RecyclerView.Adapter<KucukListAdapter.ViewHolder> {

    Context mContext;
    JSONObject tarifler;
    OnItemClickListener mItemClickListener;
    public KucukListAdapter(Context context,JSONObject jsonObject){
        this.mContext=context;
        this.tarifler=jsonObject;
    }

    public void changeTarifList(JSONObject newlist){

        tarifler=newlist;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.small_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tarif tarif = new TarifData(tarifler).tarifList().get(position);
        final ViewHolder customHolder = holder;
        holder.yemekName.setText(tarif.getTarifName());
        //Picasso.with(mContext).load(tarif.getTarifImage()).into(holder.yemekResim);
        Bitmap photo;
        UrlImageViewHelper.loadUrlDrawable(mContext, tarif.getTarifImage(), new UrlImageViewCallback() {
            @Override
            public void onLoaded(ImageView ımageView, Bitmap bitmap, String s, boolean b) {
                customHolder.yemekResim.setImageBitmap(bitmap);
                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                        customHolder.yemekNameHolder.setBackgroundColor(bgColor);
                    }
                });
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
            mainHolder = (LinearLayout)itemView.findViewById(R.id.kucuk_main_holder);
            yemekName = (TextView)itemView.findViewById(R.id.kucuk_yemek_name);
            yemekNameHolder = (LinearLayout)itemView.findViewById(R.id.kucuk_yemek_name_holder);
            yemekResim = (ImageView)itemView.findViewById(R.id.kucuk_yemek_kart_resim);
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
    public JSONObject getTarif(int position){
        try {
            JSONArray tarifList = tarifler.getJSONArray("tarifler");
            return tarifList.getJSONObject(position);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

}
