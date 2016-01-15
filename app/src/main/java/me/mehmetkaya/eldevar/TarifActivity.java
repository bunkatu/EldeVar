package me.mehmetkaya.eldevar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TarifActivity extends Activity {
    ImageView tarif_resim;
    TextView tarif_malzeme;
    TextView tarif_detay;
    TextView tarif_isim;
    LinearLayout yemek_name_holder;
    CardView tarif_card;
    CardView malzemeler_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif);
        Intent intent=getIntent();
        String id=intent.getStringExtra("TARIF_ID");
        tarif_resim=(ImageView)findViewById(R.id.detay_card_resim);
        tarif_malzeme=(TextView)findViewById(R.id.tarif_malzemeler);
        tarif_detay=(TextView)findViewById(R.id.tarif_detay);
        tarif_isim=(TextView)findViewById(R.id.detayYemekName);
        yemek_name_holder=(LinearLayout)findViewById(R.id.detayYemekNameHolder);
        tarif_card=(CardView)findViewById(R.id.tarif_card);
        malzemeler_card=(CardView)findViewById(R.id.malzemeler_card);

        TarifTask tarifTask=new TarifTask();
        tarifTask.execute(id);

    }
    public class TarifTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            Webb webb = Webb.create();

            Response<JSONObject> response = webb.get("http://mehmetkaya.me/eldevar/api/v1/tarifAl")
                    .param("tarifId", params[0])
                    .ensureSuccess()
                    .asJsonObject();

            return response.getBody();

        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result!=null){
                Log.d("NOTNULL", "Not_null");
                Log.i("asd", result.toString());
                try {
                    if(result.getString("durum").equals("tamam")){

                        JSONObject tarif=result.getJSONObject("tarif");
                        //UrlImageViewHelper.setUrlDrawable(tarif_resim, tarif.getString("yol"), R.drawable.place_holder, 3600000);
                        UrlImageViewHelper.loadUrlDrawable(getApplicationContext(), tarif.getString("yol"), new UrlImageViewCallback() {
                            @Override
                            public void onLoaded(ImageView ımageView, Bitmap bitmap, String s, boolean b) {
                                tarif_resim.setImageBitmap(bitmap);
                                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette palette) {
                                        int bgColor = palette.getMutedColor(getApplicationContext().getResources().getColor(android.R.color.black));
                                        int color1=palette.getDarkVibrantColor(getApplicationContext().getResources().getColor(android.R.color.black));
                                        int color2=palette.getLightVibrantColor(getApplicationContext().getResources().getColor(android.R.color.black));
                                        yemek_name_holder.setBackgroundColor(bgColor);
                                        tarif_card.setCardBackgroundColor(color1);
                                        malzemeler_card.setCardBackgroundColor(color2);
                                    }
                                });
                            }
                        });
                        JSONArray tarif_malzemeler=tarif.getJSONArray("malzemeler");
                        String malzemeler="Malzemeler: \n";
                        for(int i=0; i<tarif_malzemeler.length();i++){
                            JSONObject malzeme=tarif_malzemeler.getJSONObject(i);
                            malzemeler+="-->"+malzeme.getString("adi")+"\n";
                        }
                        tarif_malzeme.setText(malzemeler);
                        String tarif_detay_s=tarif.getString("tarif");
                        tarif_detay.setText(tarif_detay_s);
                        tarif_isim.setText(tarif.getString("adi"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
