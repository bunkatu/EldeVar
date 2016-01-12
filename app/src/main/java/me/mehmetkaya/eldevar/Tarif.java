package me.mehmetkaya.eldevar;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mehmetkaya on 12.01.16.
 */
public class Tarif {
    private String tarifId;
    private String tarifName;
    private String tarifContent;
    private String[] tarifMalzemeler;
    private String tarifImage;

    public Tarif(JSONObject jsonString){
        try {
            this.tarifId = jsonString.getString("id");
            this.tarifName = jsonString.getString("adi");
            this.tarifContent = jsonString.getString("tarif");
            this.tarifImage = jsonString.getString("yol");


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

    public String[] getTarifMalzemeler() {
        return tarifMalzemeler;
    }

    public String getTarifImage() {
        return tarifImage;
    }

    public int getImageResourceId(Context context){
        return context.getResources().getIdentifier(this.tarifImage, "drawable", context.getPackageName());
    }
}
