package me.mehmetkaya.eldevar;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mehmetkaya on 12.01.16.
 */
public class Tarif implements Serializable {
    private String tarifId;
    private String tarifName;
    private String tarifContent;
    private ArrayList<String> tarifMalzemeler=new ArrayList<>();
    private String tarifImage;

    public Tarif(JSONObject jsonString){
        try {
            Log.d("TARIF_MALZEMELER", jsonString.toString());
            this.tarifId = jsonString.getString("id");
            this.tarifName = jsonString.getString("adi");
            this.tarifContent = jsonString.getString("tarif");
            this.tarifImage = jsonString.getString("yol");
            JSONArray malzemelerJson = jsonString.getJSONArray("malzemeler");
            for(int i=0; i<malzemelerJson.length();i++){
                String malzeme=malzemelerJson.getJSONObject(i).getString("adi");
                Log.d("MALZEME",malzeme);
                tarifMalzemeler.add(malzeme);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTarifId() {
        return tarifId;
    }

    public String getTarifName() {
        return tarifName;
    }

    public String getTarifContent() {
        return tarifContent;
    }

    public ArrayList<String> getTarifMalzemeler() {
        return tarifMalzemeler;
    }

    public String getTarifImage() {
        return tarifImage;
    }

    public int getImageResourceId(Context context){
        return context.getResources().getIdentifier(this.tarifImage, "drawable", context.getPackageName());
    }
}
