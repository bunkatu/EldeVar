package me.mehmetkaya.eldevar;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TarifActivity extends AppCompatActivity {
    ImageView tarif_resim;
    TextView tarif_malzeme;
    TextView tarif_detay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        String id=intent.getStringExtra("TARIF_ID");
        tarif_resim=(ImageView)findViewById(R.id.tarif_resim_detay);
        tarif_malzeme=(TextView)findViewById(R.id.tarif_malzemeler);
        tarif_detay=(TextView)findViewById(R.id.tarif_detay);
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
                        UrlImageViewHelper.setUrlDrawable(tarif_resim, tarif.getString("yol"), R.drawable.place_holder, 3600000);
                        JSONArray tarif_malzemeler=tarif.getJSONArray("malzemeler");
                        String malzemeler="Malzemeler: \n";
                        for(int i=0; i<tarif_malzemeler.length();i++){
                            JSONObject malzeme=tarif_malzemeler.getJSONObject(i);
                            malzemeler+="-->"+malzeme.getString("adi")+"\n";
                        }
                        tarif_malzeme.setText(malzemeler);
                        String tarif_detay_s=tarif.getString("tarif");
                        tarif_detay.setText(tarif_detay_s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
