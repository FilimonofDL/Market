package xyz.lavaliva.market.Adres;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import Model.UserInfo;
import Model.Utils;
import xyz.lavaliva.market.R;

public class DobavitAdresDostavki extends AppCompatActivity {

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dobavit_adres_dostavki);
        UserInfo userInfo=new UserInfo(this);
        final String userId=userInfo.getUserId();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        Button btSave=findViewById(R.id.btSave);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch swAdrUmolhanie=findViewById(R.id.swAdrUmolhanie);
//                String poumolhaniy="false";
                boolean poumolhaniyBool=swAdrUmolhanie.isChecked();


                String idpokupatela=userId;

                EditText etkont_lico=findViewById(R.id.etKonntLico);
                String kont_lico=etkont_lico.getText().toString();

                EditText etTel=findViewById(R.id.etTel);
                String nomer_mobilnogo=etTel.getText().toString();

                EditText etTelKod=findViewById(R.id.etTelKod);
                String kod_telefona=etTelKod.getText().toString();

                EditText etUlica=findViewById(R.id.etUlica);
                String ulica=etUlica.getText().toString();

                EditText etKvartira=findViewById(R.id.etKvartira);
                String kvartira=etKvartira.getText().toString();

                EditText etStrana=findViewById(R.id.etStrana);
                String strana=etStrana.getText().toString();

                EditText etOblast=findViewById(R.id.etOblast);
                String oblast=etOblast.getText().toString();

                EditText etGorod=findViewById(R.id.etGorod);
                String gorod=etGorod.getText().toString();

                EditText etIndex=findViewById(R.id.etIndex);
                String index=etIndex.getText().toString();
                //System.out.println("Click add aders");

                if(poumolhaniyBool){
//                    System.out.println(" YES SWITCH");
                    sendSQLPoUmolhaniy(
                            idpokupatela,
                            kont_lico,
                            nomer_mobilnogo,
                            kod_telefona,
                            ulica,
                            kvartira,
                            strana,
                            oblast,
                            gorod,
                            index);
                }
                else {
                    sendSQL(
                            idpokupatela,
                            kont_lico,
                            nomer_mobilnogo,
                            kod_telefona,
                            ulica,
                            kvartira,
                            strana,
                            oblast,
                            gorod,
                            index);
                }

                Intent intent = new Intent();
                intent.putExtra("save", "ok");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    void sendSQL(
                 final String idpokupatela,
                 final String kont_lico,
                 final String nomer_mobilnogo,
                 final String kod_telefona,
                 final String ulica,
                 final String kvartira,
                 final String strana,
                 final String oblast,
                 final String gorod,
                 final String index
                 ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_ADRES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
////                            JSONObject jsonObject= new JSONObject(response.toString());
//                        } catch (JSONException e) {
//
//                            System.out.println("\n ERR"+response.toString());
//                        }

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


                parameters.put("idpokupatela", idpokupatela );
                parameters.put("kont_lico", kont_lico);
                parameters.put("nomer_mobilnogo", nomer_mobilnogo);
                parameters.put("kod_telefona", kod_telefona);
                parameters.put("ulica", ulica);
                parameters.put("kvartira", kvartira);
                parameters.put("strana", strana);
                parameters.put("oblast", oblast);
                parameters.put("gorod", gorod);
                parameters.put("index", index);
                return parameters;
            }


        };
        requestQueue.add(stringRequest);
    }
    void sendSQLPoUmolhaniy(
                 final String idpokupatela,
                 final String kont_lico,
                 final String nomer_mobilnogo,
                 final String kod_telefona,
                 final String ulica,
                 final String kvartira,
                 final String strana,
                 final String oblast,
                 final String gorod,
                 final String index
                 ){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_ADRES_PO_UMOLHANIY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        try {
////                            JSONObject jsonObject= new JSONObject(response.toString());
//                        } catch (JSONException e) {
//
//                            System.out.println("\n ERR"+response.toString());
//                        }

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


                parameters.put("idpokupatela", idpokupatela );
                parameters.put("kont_lico", kont_lico);
                parameters.put("nomer_mobilnogo", nomer_mobilnogo);
                parameters.put("kod_telefona", kod_telefona);
                parameters.put("ulica", ulica);
                parameters.put("kvartira", kvartira);
                parameters.put("strana", strana);
                parameters.put("oblast", oblast);
                parameters.put("gorod", gorod);
                parameters.put("index", index);
                return parameters;
            }


        };
        requestQueue.add(stringRequest);
    }
}
