package xyz.lavaliva.market.ResPoiskaStranica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.O;
import Model.Utils;
import xyz.lavaliva.market.Poisk.PoiskAdapter;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;
import xyz.lavaliva.market.TovarList.TovariAdapterPrilavok;
import xyz.lavaliva.market.TovarList.TovariObjPrilavok;

public class ResPoiskaStranicaActivity extends AppCompatActivity {
    ImageButton ibKorzina;
    ProgressBar progressBar;
    TextView tvKorzinaCount;

    RecyclerView recPoiskRes;
    TovariAdapterResPoiska tovariAdapterResPoiska;
    List<TovariObjPrilavok> tovariObjPrilavoks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari_recikler);
        ibKorzina = findViewById(R.id.ibKorzina);
        progressBar = findViewById(R.id.progressBar);
        tvKorzinaCount = findViewById(R.id.tvKorzinaCount);
        recPoiskRes = findViewById(R.id.recMain);
        recPoiskRes.setLayoutManager(new LinearLayoutManager(this));
        tovariObjPrilavoks = new ArrayList<>();
        tovariAdapterResPoiska = new TovariAdapterResPoiska(recPoiskRes, this, tovariObjPrilavoks);
        recPoiskRes.setAdapter(tovariAdapterResPoiska);

        showResPoiska(PoiskAdapter.poiskTextObjVibraniy.getText(), 0);


    }

    private void showResPoiska(final String iskattext, final int indexStart) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_TOVAR_DLIA_POISKA, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    O.o(jsonObject.toString() + " J Obj");
                    JSONArray array = jsonObject.getJSONArray("naideno");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        Double cenaSkidka;
                        if (!jo.getString("cenaskidka").equals("null")) {
                            cenaSkidka = jo.getDouble("cenaskidka");
                        } else {
                            cenaSkidka = 0.0;
                        }
                        boolean selected = false;
                        if (!jo.getString("selected").equals("null")) {
                            selected = jo.getBoolean("selected");
                        }
                        int kolihestvo = 0;
                        if (!jo.getString("kolihestvo").equals("null")) {
                            kolihestvo = jo.getInt("kolihestvo");
                        }
                        TovariObjPrilavok tovars = new TovariObjPrilavok(
                                jo.getString("id"),
                                jo.getString("naimenovanie"),
                                jo.getInt("skidka"),
                                jo.getString("foto"),
                                jo.getDouble("cena"),
                                cenaSkidka,
                                kolihestvo,
                                selected
                        );
                        tovariObjPrilavoks.add(tovars);
                        tovariAdapterResPoiska.setItems();
//                        O.o(tovars.getNaimenovanie()+" obj naimenov");
                    }
                    tovariAdapterResPoiska.notifyDataSetChanged();
                    tovariAdapterResPoiska.setLoaded();
//                    if(korzinaCount1>0){
//                        tvKorzinaCount.setVisibility(View.VISIBLE);
//                        tvKorzinaCount.setText(Integer.toString(korzinaCount1));
//                    }


                } catch (JSONException e) {
                    System.out.println("\n ERR" + response.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("iskattext", iskattext);
                parameters.put("startindex", Integer.toString(indexStart));
                parameters.put("countselect", Integer.toString(SpisokTovarovPrilavok.dliinaZagruzki));
                parameters.put("pokupatel", SpisokTovarovPrilavok.userId);
                O.o(parameters.toString() + " param to send");
                return parameters;
            }
        };

        SpisokTovarovPrilavok.requestQueue = Volley.newRequestQueue(getApplicationContext());
        SpisokTovarovPrilavok.requestQueue.add(stringRequest);
        progressBar.setVisibility(View.VISIBLE);
    }
}
