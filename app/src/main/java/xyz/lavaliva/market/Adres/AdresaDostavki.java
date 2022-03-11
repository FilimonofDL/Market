package xyz.lavaliva.market.Adres;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.UserInfo;
import Model.Utils;
import xyz.lavaliva.market.Korzina;
import xyz.lavaliva.market.R;

public class AdresaDostavki extends AppCompatActivity {

    RequestQueue requestQueue;
    String userId=null;
//    String typeDostavki="";
//    String adresId;
//    String adresPost;
    String adresPunkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresa_dostavki);

    UserInfo userInfo=new UserInfo(this);
     userId =userInfo.getUserId();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        String typeadres=bundle.getString("typeadres");
        Korzina.adresPost=bundle.getString("adresPost");
        Korzina.adresPunkt=bundle.getString("adresPunkt");
        if(typeadres.equals("punktvidahi")){
            showSQLPunkt(adresPunkt);
        }
        else  if(typeadres.equals("pohta")|
                typeadres.equals("kurier")){
            showSQLPost(Korzina.adresPost);
        }


    }
    void createIntent(){
        Intent intent=new Intent(this, DobavitAdresDostavki.class);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showSQLPost(Korzina.adresPost);
    }

    void clearAllRadio(ArrayList<RadioButton> ar){

        for(int i =0; i<ar.size() ; i++){
            ar.get(i).setChecked(false);
        }
    }

    void showSQLPost(final String adresPost){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_ADRESA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");
                            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayOplata);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();
                            View dobAdres = layoutInflater.inflate(R.layout.row_novi_adres, linLayout, false);
                            dobAdres.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createIntent();
                                }
                            });
                            linLayout.addView(dobAdres);
                            final ArrayList <RadioButton> arrayRadio=new ArrayList<RadioButton>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);
                                final String id     =    jsonRow.getString("idAdres");
                                final View item = layoutInflater.inflate(R.layout.row_adresa_dostavki,
                                        linLayout, false);
                                TextView tvKontLico = (TextView) item.findViewById(R.id.tvSposobDostavki);
                                tvKontLico.setText( jsonRow.getString("kont-lico"));
                               TextView tvAdres = (TextView) item.findViewById(R.id.tvAdresPoDostavke);
                               String adresStr = jsonRow.getString("ulica");
                               adresStr = adresStr+ ", "+jsonRow.getString("kvartira")+
                               "\n"+jsonRow.getString("gorod")+", "
                                       +jsonRow.getString("strana")+"\n"
                                       +jsonRow.getString("index")+"\n"
                                       +jsonRow.getString("nomer_mobilnogo");
                              //System.out.println(adresStr);
                                tvAdres.setText( adresStr);
                               String idAdresChBox=jsonRow.getString("idAdres");
                                final RadioButton radioButtonAdresMain=item.findViewById(R.id.radioAdres);
                                arrayRadio.add(radioButtonAdresMain);
                                if(idAdresChBox.equals(adresPost)) {
                                    radioButtonAdresMain.setChecked(true);
                                }
                                final String finalAdresStr = adresStr;
                                radioButtonAdresMain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearAllRadio(arrayRadio);
                                        radioButtonAdresMain.setChecked(true);
                                        Intent intent = new Intent();
                                        Korzina.adresPost= finalAdresStr;
                                        //Korzina.adresPostId=id;
                                        intent.putExtra("adresPost", id);
                                        intent.putExtra("adresPunkt", finalAdresStr);
                                        setResult(RESULT_OK, intent);
                                        setGlavniyAdresPost(id);
                                        finish();
                                    }
                                });
                                linLayout.addView(item);
                            }
                        } catch (JSONException e) {

                            System.out.println("\n ERR"+response.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }}){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();

                parameters.put("pokupatel", "1");
                return parameters;
            }};
        requestQueue.add(stringRequest);
    }

    private void setGlavniyAdresPost(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.GHANGE_GLAVNIY_ADRES_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HttpClient", "success! response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idpokupatelya", userId);
                params.put("idadres", id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    void showSQLPunkt(final String adresPunkt){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_ADRESA_PUNKTOV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("punkti");
                            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayOplata);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();

                            final ArrayList <RadioButton> arrayRadio=new ArrayList<RadioButton>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonRow = jsonArray.getJSONObject(i);

                                final String id     =    jsonRow.getString("id");
                                final View item = layoutInflater.inflate(R.layout.row_adresa_dostavki, linLayout, false);
                                TextView tvIzmenit=item.findViewById(R.id.tvIzmenitAdres);
                                tvIzmenit.setVisibility(View.INVISIBLE);
                                TextView tvKontLico = (TextView) item.findViewById(R.id.tvSposobDostavki);
                                tvKontLico.setText( jsonRow.getString("name"));
                               TextView tvAdres = (TextView) item.findViewById(R.id.tvAdresPoDostavke);
                               String adresStr = jsonRow.getString("adres");
                               adresStr = adresStr+ ", \n"+jsonRow.getString("vremyaraboti")+
                               "\n"+jsonRow.getString("kontakt")
                               ;
                                tvAdres. setText( adresStr);
                                final RadioButton radioButtonAdresMain=item.findViewById(R.id.radioAdres);
                                arrayRadio.add(radioButtonAdresMain);
                                if(id.equals(adresPunkt)) {
                                    radioButtonAdresMain.setChecked(true);
                                }
                                radioButtonAdresMain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearAllRadio(arrayRadio);
                                        radioButtonAdresMain.setChecked(true);
                                        Intent intent = new Intent();
                                        intent.putExtra("adresPost", Korzina.adresPost);
                                        intent.putExtra("adresPunkt", id);
                                        setResult(RESULT_OK, intent);
                                        setMainAdres(id);
                                        finish();
                                    }
                                });

                                linLayout.addView(item);
                            }
                        } catch (JSONException e) {

                            System.out.println("\n ERR"+response.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();

                return parameters;
            }


        };
        requestQueue.add(stringRequest);
    }

    private void setMainAdres(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Utils.GHANGE_GLAVNIY_ADRES_PUNKT,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HttpClient", "success! response: " + response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idpokupatelya", userId);
                params.put("idadres", id);
                return params;
            }
    };
        requestQueue.add(stringRequest);
}}












































