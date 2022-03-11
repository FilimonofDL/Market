package xyz.lavaliva.market.ZakazList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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
import Model.Utils;
import xyz.lavaliva.market.R;

public class SpisokZakazov extends AppCompatActivity {

    private RecyclerView recMain;
    private ZakazAdapter zakazAdapter;
    int dliinaZagruzki = 14;
    ProgressBar progressBar ;

    List<ZakazObj> zakazObjsList= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spisok_zakazov);
         progressBar=findViewById(R.id.progressBar2);

        recMain = findViewById(R.id.recMain);
        recMain.setLayoutManager(new LinearLayoutManager(this));

        zakazAdapter = new ZakazAdapter(recMain, this, zakazObjsList);
        recMain.setAdapter(zakazAdapter);
        loadUrlData(0,dliinaZagruzki);
        zakazAdapter.setLoadMore(new ILoadMore(){

            @Override
            public void onLoadMore() {

                int index = zakazObjsList.size();
                int end = index + dliinaZagruzki;
                loadUrlData(index, end);

                zakazAdapter.notifyDataSetChanged();
                zakazAdapter.setLoaded();


            }
        });
    }
//
     private void loadUrlData(final int indexStart, final int indexEnd) {


         StringRequest stringRequest = new StringRequest(Request.Method.POST,
                 Utils.SHOW_ZAKAZI_SCROL, new Response.Listener<String>() {


             @Override
             public void onResponse(String response) {
               try {
                   progressBar.setVisibility(View.GONE);
                   JSONObject jsonObject=new JSONObject(response);
                   JSONArray jsonObjectZakaz=jsonObject.getJSONArray("zakaz");
                   O.o("resp "+response);
                   for (int y =0; y<jsonObjectZakaz.length(); y++){
                       JSONObject jsonObjectTovarFor= (JSONObject) jsonObjectZakaz.get(y);
                       JSONArray jsonObjectTovarArrFor=jsonObjectTovarFor.getJSONArray("tovar");
//                       O.o("Y= "+Integer.toString(y)+" "+jsonObjectTovarFor.getString("id")+" "+Integer.toString(jsonObjectZakaz.length()));
                       List <TovarObjZakaz> tovariList = new ArrayList<>();
                       Double vsegoPoZakazu=0.0;
                       for(int t=0;t<jsonObjectTovarArrFor.length();t++){
                           JSONObject jsTovOneFor=jsonObjectTovarArrFor.getJSONObject(t);
//                           O.o("T   ="+Integer.toString(t)+" "+jsTovOneFor.getString("idtovarzakaz")+" "+Integer.toString(jsonObjectTovarArrFor.length()));
                           TovarObjZakaz tovarObjZakaz =new TovarObjZakaz(
                                   jsTovOneFor.getString("idtovarzakaz"),
                                   jsTovOneFor.getString("tovar"),
                                   jsTovOneFor.getString("naimenovanie"),
                                   jsTovOneFor.getString("foto"),
                                   jsTovOneFor.getString("kolihestvovtovare"),
                                   jsTovOneFor.getString("cena"),
                                   jsTovOneFor.getString("primehanie")

                                   );
                           tovariList.add(tovarObjZakaz);
                           Double cena=jsTovOneFor.getDouble("cena");
                           Double kolihestvovtovare=jsTovOneFor.getDouble("kolihestvovtovare");
                           vsegoPoZakazu=vsegoPoZakazu+cena*kolihestvovtovare;
                       }
                       String noZakaza=jsonObjectTovarFor.getString("id");
                       String datazakaza=jsonObjectTovarFor.getString("datazakaza");
                       String vsegoDenegStr=String.format("%.2f", vsegoPoZakazu);

                       ZakazObj zakazObj =new ZakazObj(noZakaza,datazakaza, vsegoDenegStr, tovariList );
                       zakazObjsList.add(zakazObj);
//                       O.o(Integer.toString(zakazObjsList.size())+" size zakaz List");
                       zakazAdapter.setItems();
                   }
                   zakazAdapter.notifyDataSetChanged();
                   zakazAdapter.setLoaded();
                 } catch (JSONException e) {
                     System.out.println("\n ERR"+response.toString());
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         }){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> parameters = new HashMap<String,String>();

                 parameters.put("pokupatel", "1");
                 parameters.put("startindex", Integer.toString(indexStart));
                 int endToServ=indexEnd-indexStart;
                 parameters.put("endindex", Integer.toString(endToServ));
                 O.o(parameters.toString()+" param to Send");
                 return parameters;
             }};
         progressBar.setVisibility(View.VISIBLE);
         RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
         requestQueue.add(stringRequest);
     }

}