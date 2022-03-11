package xyz.lavaliva.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Model.UserInfo;
import Model.Utils;
import xyz.lavaliva.market.Adres.AdresaDostavki;
import xyz.lavaliva.market.ZakazList.SpisokZakazov;

public class OformlenieZakaza extends AppCompatActivity implements View.OnClickListener {
    RequestQueue requestQueue;
//    String adresPost ="";
//    String adresPunkt ="";
    String typeDostavkiLang;
//    String typeDostavki;
    String userId;
    Button btZakazat;
    JSONArray jsTovari= new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oformlenie_zakaza);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        UserInfo userInfo=new UserInfo(this);
        userId=userInfo.getUserId();
        Bundle bundle=getIntent().getExtras();
//        typeDostavki=bundle.getString("typeDostavki");
        //adresPost=bundle.getString("adresPost");
//        adresPunkt=bundle.getString("adresPunkt");
//        System.out.println(typeDostavki+" "+adresPunkt+" "+adresPost);
        showSQLUniversal(userId, Korzina.typeDostavki);
        btZakazat=findViewById(R.id.btKupit);
        btZakazat.setOnClickListener(this);


    }

    void showSQLUniversal(final String userId, final  String typeDostavki){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_NA_OPLATU_POVTORNO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jArrAdr = jsonObject.getJSONArray("adres");
                            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayOplata);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            linLayout.removeAllViews();
                            TextView tvKOplate =findViewById(R.id.tvKOplateOplata);
                            Double kOplate=0.0;
                            View itemSposobDostavki = layoutInflater.inflate
                                    (R.layout.row_sposob_oplati, linLayout, false);
                            View itemAdres = layoutInflater.inflate
                                    (R.layout.row_adresa_dostavki_v_zakaze, linLayout, false);



                            TextView tvIzmenitSposob=itemSposobDostavki.findViewById(R.id.tvIzmenitAdres);
                            TextView tvIzmenitAdres=itemAdres.findViewById(R.id.tvIzmenitAdres);
                            if(typeDostavki.equals("punktvidahi")){
                                showAdresPunkt(jArrAdr, itemAdres);
                            }
                            else{
                                showAdresPost(jArrAdr, itemAdres, tvIzmenitAdres);
                            }
                            TextView tvSposob=itemSposobDostavki.findViewById(R.id.tvSposobDostavki);
                            if(typeDostavki.equals("kurier")){
                                typeDostavkiLang=getResources().getString(R.string.kurier);
                            }
                            else if (typeDostavki.equals("punktvidahi")){
                                typeDostavkiLang=getResources().getString(R.string.punkt);
                            }
                            else if (typeDostavki.equals("pohta")){
                                typeDostavkiLang=getResources().getString(R.string.post);
                            }
                            tvSposob.setText(typeDostavkiLang);

                            tvIzmenitSposob.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createIntentSposob();
                                }
                            });
                            tvIzmenitAdres.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    createIntentAdres(Korzina.adresPost, Korzina.adresPunkt);
                                }
                            });


                            linLayout.addView(itemSposobDostavki);
                            linLayout.addView(itemAdres);
                            showKorzina(jsonObject, kOplate, layoutInflater, linLayout, tvKOplate);

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


                parameters.put("pokupatel", userId);
                parameters.put("typedostavki", typeDostavki);
                if(typeDostavki.equals("punktvidahi")) {
                    parameters.put("idadres", Korzina.adresPunkt);
                }else{
                    parameters.put("idadres", Korzina.adresPost);
                }

                return parameters;
            }


        };
        requestQueue.add(stringRequest);

    }
    void sendZakaz(final JSONObject jsonArray){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_ZAKAZ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
//                            System.out.println(jsonObject);
                            createIntentZakazat();
                            finish();
                        }catch (JSONException e){
                            System.out.println("\n ERR senZakaz "+response);
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
//                System.out.println(" VOHLO =   "+jsonArray);


                parameters.put("jsarr", jsonArray.toString());
                parameters.put("pokupatel", "1");
                parameters.put("idadres", "43");
                parameters.put("typedostavki", "pohta");


                return parameters;
            }


        };
        requestQueue.add(stringRequest);

    }
    void showKorzina(JSONObject jsonObject, Double kOplate, LayoutInflater layoutInflater,
                     LinearLayout linLayout, TextView tvKOplate){
        try {
            JSONArray jsKorzina = jsonObject.getJSONArray("korzina");

                            for (int i = 0; i < jsKorzina.length(); i++) {
                                JSONObject jsonRow = jsKorzina.getJSONObject(i);


//                                System.out.println(jsonRow.getString("cenaskidka")+" cenaskidka");
                                String tovarNaimenovanie = jsonRow.getString("naimenovanie");
                                final String tovarId = jsonRow.getString("tovar");
                                final Double[] kolihestvo = {jsonRow.getDouble("kolihestvo")};
                                Double cena = jsonRow.getDouble("cena");
                                Double cenaskidka=0.0;
                                if(!jsonRow.getString("cenaskidka").equals("null")&&
                                        !jsonRow.getString("cenaskidka").equals("0.0")&&
                                        !jsonRow.getString("cenaskidka").equals("0,0")&&
                                        !jsonRow.getString("cenaskidka").equals("0"))
                                {
                                    cenaskidka = jsonRow.getDouble("cenaskidka");
//                                    System.out.println("Doub cena "+cenaskidka.toString());
                                }
                                int skidka=0;
                                if(!jsonRow.getString("skidka").equals("null")) {
                                    skidka = jsonRow.getInt("skidka");
                                }
                                Double finalCena=0.0;
                                if(cenaskidka >0){
                                    finalCena=jsonRow.getDouble("cenaskidka");
                                }
                                else if(skidka>0) {
                                    finalCena = cena * (100 - skidka) / 100;
                                }
                                else {
                                    finalCena=cena;
                                }
                                String foto = jsonRow.getString("foto");

                                kOplate = kOplate + finalCena*kolihestvo[0
                                        ];
//                                System.out.println("k Oplate "+kOplate+" "+finalCena);

                                final View item = layoutInflater.inflate(R.layout.row_tovar_k_oplate,
                                        linLayout, false);
                                ImageView ivTovar = item.findViewById(R.id.ivTovar);
                                Picasso.get().load(foto).into(ivTovar);
                                TextView tvNazvanie = (TextView) item.findViewById(R.id.tvNaimenovanieKOplate);
                                TextView tvCena = (TextView) item.findViewById(R.id.tvCenaOplata);
                                TextView tvKolihestvo = (TextView) item.findViewById(R.id.tvKolihestvoOplata);
                                TextView tvItogoPoTovaruOplata = (TextView) item.findViewById(R.id.tvItogoPoTovaruOplata);




                                tvNazvanie.setText(tovarNaimenovanie);
                                jsonRow.put("fincena",finalCena);
                                tvCena.setText(finalCena.toString());
                                String kolihestvoToString = String.format("%.0f", kolihestvo[0]);
                                String cenaKolihestvaTovara = String.format("%, (.2f", kolihestvo[0] * finalCena);
                                String kOplateToString = String.format("%, (.2f", kOplate);


                                tvKolihestvo.setText(kolihestvoToString);
                                tvItogoPoTovaruOplata.setText(cenaKolihestvaTovara);
                                tvKOplate.setText(kOplateToString);
//
                                linLayout.addView(item);
                                jsTovari.put(i,jsonRow);
                            }

        }catch (JSONException e){
            System.out.println("\n ERR "+jsonObject.toString());
        }

    }
    void showAdresPost(JSONArray jArrAdr, View itemAdres, TextView tvIzmenitSposob){
try {
    JSONObject jsonRow2 = jArrAdr.getJSONObject(0);
    String adresStr = jsonRow2.getString("ulica");
    final String adresDostavkiId = jsonRow2.getString("idAdres");
    adresStr = adresStr + ", " + jsonRow2.getString("kvartira") +
            "\n" + jsonRow2.getString("gorod") + ", "
            + jsonRow2.getString("strana") + "\n"
            + jsonRow2.getString("index") + "\n"
            + jsonRow2.getString("nomer_mobilnogo")
    ;
    TextView tvFioVZakaze = itemAdres.findViewById(R.id.tvSposobDostavki);
    tvFioVZakaze.setText(jsonRow2.getString("kont-lico"));
    TextView tvAdresVZakaze = itemAdres.findViewById(R.id.tvAdresDostavki);
    tvAdresVZakaze.setText(adresStr);
                    tvIzmenitSposob.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createIntentAdres(Korzina.adresPost, Korzina.adresPunkt);
                        }
                    });

                }catch (JSONException e){

                }
    }
    void showAdresPunkt(JSONArray jArrAdr, View itemAdres ){
        try {
            JSONObject jsonRow2 = jArrAdr.getJSONObject(0);
            String adresStr = jsonRow2.getString("adres");
            final String adresDostavkiId = jsonRow2.getString("id");
            adresStr = adresStr + ", \n" + jsonRow2.getString("vremyaraboti") +
                    "\n" +
                    jsonRow2.getString("kontakt");
            TextView tvFioVZakaze = itemAdres.findViewById(R.id.tvSposobDostavki);
            tvFioVZakaze.setText(jsonRow2.getString("name"));
            TextView tvAdresVZakaze = itemAdres.findViewById(R.id.tvAdresDostavki);
            tvAdresVZakaze.setText(adresStr);
        }catch (JSONException e){

        }
    }
    void createIntentAdres(String adresPost, String adresPunkt){
        Intent intent=new Intent(getApplicationContext(), AdresaDostavki.class);
        intent.putExtra("typeadres",Korzina.typeDostavki);
        intent.putExtra("adresPost", adresPost);
        intent.putExtra("adresPunkt", adresPunkt);
        startActivityForResult(intent, 1);
    }
    void createIntentSposob( ){
        Intent intent=new Intent(getApplicationContext(), SposobOplatiIDostavki.class);
        intent.putExtra("typeDostavki", Korzina.typeDostavki);
        startActivityForResult(intent, 2);
    }
    void createIntentZakazat( ){
        Intent intent=new Intent(getApplicationContext(), SpisokZakazov.class);
        intent.putExtra("typeDostavki", Korzina.typeDostavki);
        startActivityForResult(intent, 3);
//        System.out.println("zakaz activity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                Korzina.adresPost = data.getStringExtra("adresPost");
                Korzina.adresPunkt = data.getStringExtra("adresPunkt");
                showSQLUniversal(userId, Korzina.typeDostavki);
            }
        }
        else if(requestCode==2){
            if (resultCode == RESULT_OK) {
                Korzina.typeDostavki =data.getStringExtra("sposobtext");
                showSQLUniversal(userId, Korzina.typeDostavki);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btKupit:
                JSONObject jsArrToSend = new JSONObject();
                try {
                    jsArrToSend.put("korzina",jsTovari);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendZakaz(jsArrToSend);
                break;
        }
    }
}
