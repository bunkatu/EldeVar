package me.mehmetkaya.eldevar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mehmetkaya on 12.01.16.
 */
public class TarifData {

    private JSONArray allTarifs;

    public TarifData(JSONObject jsonObject){

        try {
            allTarifs = jsonObject.getJSONArray("tarifler");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Tarif> tarifList(){

        ArrayList<Tarif> returnData = new ArrayList<>();

        for(int i = 0; i < allTarifs.length(); i++){
            try {
                returnData.add(new Tarif(allTarifs.getJSONObject(i)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnData;

    }

    public int getCount(){
       return allTarifs.length();
    }

}
