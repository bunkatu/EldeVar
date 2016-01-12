package me.mehmetkaya.eldevar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.tokenautocomplete.TokenCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.saeid.fabloading.LoadingView;

public class MainActivity extends AppCompatActivity implements TokenCompleteTextView.TokenListener {
    CompletionView cmpview;
    ArrayList<Malzeme> malzemeler=new ArrayList<Malzeme>();
    ArrayList<String> eldekiler=new ArrayList<String>();
    ArrayAdapter<Malzeme> malzemeArrayAdapter;
    LinearLayout horizontalLayout;
    HorizontalScrollView scrollView;
    LinearLayout mainContent;
    RelativeLayout loadingContent;
    LoadingView mLoadingView;

    LinearLayout horizontalContainer;
    int ilkEfekt;
    int asdq;
    int efektFadeOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ilkEfekt = 0;
        efektFadeOut = 0;
        getSupportActionBar().hide();
        scrollView=(HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        horizontalLayout=(LinearLayout)findViewById(R.id.horzontal_layout);
        horizontalContainer = (LinearLayout)findViewById(R.id.horizontalContainer);

        mainContent = (LinearLayout)findViewById(R.id.mainContent);
        loadingContent = (RelativeLayout) findViewById(R.id.loadingContent);

        mLoadingView = (LoadingView) findViewById(R.id.loading_view);

        mLoadingView.addAnimation(R.color.colorPrimaryDark, R.drawable.anim1, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(R.color.colorPrimaryDark, R.drawable.anim2, LoadingView.FROM_TOP);
        mLoadingView.addAnimation(R.color.colorPrimaryDark, R.drawable.anim3, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(R.color.colorPrimaryDark, R.drawable.anim4, LoadingView.FROM_BOTTOM);
        mLoadingView.startAnimation();

        MalzemeTask malzemeTask=new MalzemeTask();
        malzemeTask.execute();
        malzemeArrayAdapter = new ArrayAdapter<Malzeme>(this, android.R.layout.simple_list_item_1,malzemeler);

        cmpview = (CompletionView)findViewById(R.id.search_view);
        cmpview.setAdapter(malzemeArrayAdapter);
        cmpview.setTokenListener(this);
        if (savedInstanceState == null) {
            cmpview.setHint("Elinizdeki malzemeler ");
        }




    }
    public class MalzemeTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://mehmetkaya.me/eldevar/api/v1/malzemeAl");


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            return forecastJsonStr;

        }

        @Override
        protected void onPostExecute(String result){
            if(result!=null){
                Log.d("NOTNULL","Not_null");
                try{
                    JSONObject jsonObject=new JSONObject(result);
                    if(jsonObject.getString("durum").equals("tamam")){
                        Log.d("TAMAM","tamam");
                        JSONArray gelen_malzemeler= jsonObject.getJSONArray("malzemeler");
                        for(int i=0; i<gelen_malzemeler.length();i++){
                            malzemeArrayAdapter.add(new Malzeme(gelen_malzemeler.get(i).toString()));
                            /*malzemeler.add(new Malzeme(gelen_malzemeler.get(i).toString()));Tek obje yarat hem adaptore hem liste ekle*/
                        }
                        malzemeArrayAdapter.notifyDataSetChanged();
                        Log.d("EKLEDI", "ekledi");
                        Log.d("MALZEMELER", malzemeler.toString());
                        mLoadingView.pauseAnimation();
                        loadingContent.setVisibility(View.GONE);
                        mainContent.setVisibility(View.VISIBLE);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public class TarifTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {

            Webb webb = Webb.create();

            Response<JSONObject> response = webb.post("http://mehmetkaya.me/eldevar/api/v1/tarifBul")
                    .param("malzemeler", params[0])
                    .ensureSuccess()
                    .asJsonObject();

            return response.getBody();

        }

        @Override
        protected void onPostExecute(JSONObject result){
            if(result!=null){
                Log.d("NOTNULL","Not_null");
                Log.i("asd", result.toString());
                try {
                    if(result.getString("durum").equals("tamam")){

                        JSONArray tarifler = result.getJSONArray("tarifler");

                        horizontalLayout.removeAllViewsInLayout();

                        for(int i = 0; i < tarifler.length(); i++){


                            final JSONObject tarif = tarifler.getJSONObject(i);
                            final String id=tarif.getString("id");
                            View asd=LayoutInflater.from(getApplicationContext()).inflate(R.layout.tarif_layout, null);
                            ImageView tarif_resmi=(ImageView)asd.findViewById(R.id.tarif_resmi);
                            UrlImageViewHelper.setUrlDrawable(tarif_resmi, tarif.getString("yol"), null, 3600000);
                            TextView tarif_adi=(TextView)asd.findViewById(R.id.tarif_icerik);
                            tarif_adi.setText(tarif.getString("adi"));

                            if(i != 0) {
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                lp.setMargins(30, 0, 0, 0);

                                asd.setLayoutParams(lp);
                            }
                            tarif_resmi.setOnClickListener(new View.OnClickListener(){
                                public void onClick(View v){
                                    Log.d("TIKLAMA","tikladı");
                                    Intent intent =new Intent(getApplicationContext(),TarifActivity.class);
                                    intent.putExtra("TARIF_ID",id);
                                    startActivity(intent);
                                }
                            });
                            horizontalLayout.addView(asd);
                        }


                        if(tarifler.length() == 0){

                            horizontalContainer.animate()
                                    .translationY(-horizontalContainer.getHeight()).alpha(0.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            horizontalContainer.setVisibility(View.GONE);
                                        }
                                    });
                            horizontalContainer.setVisibility(View.GONE);
                            ilkEfekt = 0;
                        }else  if(ilkEfekt == 0) {

                            horizontalContainer.animate().translationY(0).alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    horizontalContainer.setVisibility(View.VISIBLE);
                                    horizontalContainer.setAlpha(0.0f);
                                }
                            });
                            ilkEfekt = 1;
                            Log.d("ANIMATION","Actim");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onTokenAdded(Object o) {
        Malzeme elde = (Malzeme)o;
        eldekiler.add(elde.getIsim());
        JSONArray jsonArray=new JSONArray(eldekiler);
        TarifTask tarif = new TarifTask();
        tarif.execute(jsonArray.toString());

    }

    @Override
    public void onTokenRemoved(Object o) {
        Malzeme elde = (Malzeme) o;
        eldekiler.remove(elde.getIsim());
        JSONArray jsonArray=new JSONArray(eldekiler);
        TarifTask tarif = new TarifTask();
        tarif.execute(jsonArray.toString());
    }
}
