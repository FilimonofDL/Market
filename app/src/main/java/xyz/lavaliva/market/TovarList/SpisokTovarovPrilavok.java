package xyz.lavaliva.market.TovarList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import Model.ILoadMore;
import Model.O;
import Model.UserInfo;
import Model.Utils;
import xyz.lavaliva.market.Korzina;
import xyz.lavaliva.market.Poisk.PoiskActivity;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.ResPoiskaStranica.TovariAdapterResPoiska;

public class SpisokTovarovPrilavok extends AppCompatActivity {

    public RecyclerView recMain;
    private TovariAdapterPrilavok tovariAdapterPrilavok;
    List<TovariObjPrilavok> tovariLists ;

    public static int dliinaZagruzki = 14;
    ProgressBar progressBar ;
    public static  String userId;
    static TextView tvKorzinaCount;
    public static int korzinaCount1;
    public static RequestQueue requestQueue;
    public static ImageButton ibKorzina;
    TextView tvPoiskPrilavok;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovari_recikler);
        tovariLists=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        tvKorzinaCount=findViewById(R.id.tvKorzinaCount);
        tvPoiskPrilavok=findViewById(R.id.tvPoiskPrilavok);
        final Intent intentPoisk=new Intent(this, PoiskActivity.class);
        tvPoiskPrilavok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( intentPoisk);
            }
        });
        korzinaCount1=0;
        ibKorzina=findViewById(R.id.ibKorzina);
        final Intent ibKorzinaIntent=new Intent(this, Korzina.class);
        ibKorzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ibKorzinaIntent);
            }
        });

        userId=new UserInfo(this).getUserId();

        recMain = findViewById(R.id.recMain);
        recMain.setLayoutManager(new LinearLayoutManager(this));
        tovariAdapterPrilavok = new TovariAdapterPrilavok(recMain, this, tovariLists);

        recMain.setAdapter(tovariAdapterPrilavok);
        loadUrlData(0);
//        O.o(Integer.toString(korzinaCount1)+" TOVARA");




        tovariAdapterPrilavok.setLoadMore(new ILoadMore(){

            @Override
            public void onLoadMore() {

                int index = tovariLists.size();
                loadUrlData(index);

                tovariAdapterPrilavok.notifyDataSetChanged();
                tovariAdapterPrilavok.setLoaded();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvKorzinaCount.setText(Integer.toString(korzinaCount1));
    }

    //
    private void loadUrlData(final int indexStart) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_TOVAR, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);


                    JSONArray array = jsonObject.getJSONArray("tovar");

                    for (int i = 0; i < array.length(); i++){
                        JSONObject jo = array.getJSONObject(i);

                        Double  cenaSkidka;
                        if(!jo.getString("cenaskidka").equals("null")){
                            cenaSkidka=jo.getDouble("cenaskidka");
                        }else{
                            cenaSkidka=0.0;
                        }
                        boolean selected=false;
                        if(!jo.getString("selected").equals("null")){
                            selected=jo.getBoolean("selected");
                        }
                        int kolihestvo=0;
                        if(!jo.getString("kolihestvo").equals("null")){
                            kolihestvo=jo.getInt("kolihestvo");
                        }
                        korzinaCount1=korzinaCount1+kolihestvo;
                        TovariObjPrilavok tovars = new TovariObjPrilavok(jo.getString("id"),
                                jo.getString("naimenovanie"),
                                jo.getInt("skidka"),
                                jo.getString("foto"),
                                jo.getDouble("cena"),
                                cenaSkidka,
                                kolihestvo,
                                selected
                                );
                        tovariLists.add(tovars);
                        tovariAdapterPrilavok.setItems();
                    }
                    tovariAdapterPrilavok.notifyDataSetChanged();
                    tovariAdapterPrilavok.setLoaded();
                    if(korzinaCount1>0){
                        tvKorzinaCount.setVisibility(View.VISIBLE);
                        tvKorzinaCount.setText(Integer.toString(korzinaCount1));
                    }


                } catch (JSONException e) {
                    System.out.println("\n ERR"+response.toString());
                    e.printStackTrace();
                }

        }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();


                parameters.put("vetka", "2");
                parameters.put("indexstart", Integer.toString(indexStart));
                parameters.put("count", Integer.toString(dliinaZagruzki));
                parameters.put("pokupatel", userId);
                System.out.println("Otpravka na server iz tovar id ");
                return parameters;
            }};

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        progressBar.setVisibility(View.VISIBLE);
    }}


//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import Model.UserInfo;
//import Model.Utils;
//import xyz.lavaliva.market.Korzina;
//import xyz.lavaliva.market.R;
//
//public class SpisokTovarovPrilavok extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter adapter;
//    private List<TovariObjPrilavok> tovariLists;
//    TextView tvKorzinaCount;
//    TextView tvKKorzine;
//    ImageButton ibKorzina;
//    int korzinaCount;
//    int resultVKorzinu =23;
////    String userId=new UserInfo(this).getUserId();
//    String userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tovari_recikler);
//
//        tvKorzinaCount=findViewById(R.id.tvKorzinaCount);
//        tvKKorzine=findViewById(R.id.tvKKorzine);
//        ibKorzina=findViewById(R.id.ibKorzina);
//        final Intent intentKorzina=new Intent(this, Korzina.class);
//        ibKorzina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               startActivityForResult(intentKorzina, resultVKorzinu);
//            }
//
//        });
//
//        userId=new UserInfo(this).getUserId();
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        tovariLists = new ArrayList<>();
//
//        loadUrlData();
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==resultVKorzinu){
//            Bundle bundle=getIntent().getExtras();
//            Double kolihTovarovDoubl=bundle.getDouble(Korzina.kolihTovarovString);
//
//            tvKorzinaCount.setText(String.format("%, (.0f",kolihTovarovDoubl));
//            korzinaCount= kolihTovarovDoubl.intValue();
//            if(kolihTovarovDoubl==0.0){
//                tvKorzinaCount.setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//    private void loadUrlData() {
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        System.out.println("+++");
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                Utils.SHOW_TOVAR, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                progressDialog.dismiss();
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray array = jsonObject.getJSONArray("tovar");
//
//                    for (int i = 0; i < array.length(); i++){
//
//                        JSONObject jo = array.getJSONObject(i);
////                        System.out.println("new JO "+ jo);
//                        Double  cenaSkidka;
//                        if(!jo.getString("cenaskidka").equals("null")){
//                            cenaSkidka=jo.getDouble("cenaskidka");
//                        }else{
//                            cenaSkidka=0.0;
//                        }
//                        boolean selected=false;
//                        if(!jo.getString("selected").equals("null")){
//                            selected=jo.getBoolean("selected");
//                        }
//                        int kolihestvo=0;
//                        if(!jo.getString("kolihestvo").equals("null")){
//                            kolihestvo=jo.getInt("kolihestvo");
//                        }
//                        TovariObjPrilavok tovars = new TovariObjPrilavok(jo.getString("id"),
//                                jo.getString("naimenovanie"),
//                                jo.getInt("skidka"),
//                                jo.getString("foto"),
//                                jo.getDouble("cena"),
//                                cenaSkidka,
//                                kolihestvo,
//                                selected
//                                );
//                        tovariLists.add(tovars);
//
//
//                    }
//                    if(!jsonObject.getJSONArray("korzina").getJSONObject(0).
//                            getString("SUM(korzina.kolihestvo)").equals("null")) {
//                        korzinaCount = jsonObject.getJSONArray("korzina").getJSONObject(0).
//                                getInt("SUM(korzina.kolihestvo)");
//                    }else {
//                        korzinaCount=0;
//                    }
//                    adapter = new TovariAdapterPrilavok(tovariLists, getApplicationContext(), tvKorzinaCount, korzinaCount, userId);
//                    recyclerView.setAdapter(adapter);
//
//                } catch (JSONException e) {
//
//                    System.out.println("\n ERR"+response.toString());
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SpisokTovarovPrilavok.this, "Error" + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> parameters = new HashMap<String,String>();
//
//                parameters.put("vetka", "2");
//                parameters.put("pokupatel", userId);
//                //System.out.println("Otpravka na server iz tovar id "+id);
//                return parameters;
//            }
//
//        }
//
//                ;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//    }
//}
