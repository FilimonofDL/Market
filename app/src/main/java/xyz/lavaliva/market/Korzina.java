package xyz.lavaliva.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.O;
import Model.UserInfo;
import Model.Utils;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;

public class Korzina extends AppCompatActivity implements View.OnClickListener {
    ScrollView scrollView;
    RequestQueue requestQueue;
    UserInfo userInfo;
    static String userId;
    static String typeDostavki;
    public static String adresPost;
    public static String adresPunkt;
    Double kOplate=0.0;
//    Double korzinaCount=0.0;
    TextView tvKorzinaCount;
    TextView tvKOplate;
//    String korzCountFormat= String.format("%, (.0f",korzinaCount);
    Button btKupit;
    Button btNahatPokupki;
    ImageButton imageButtonDel;
   public static  String kolihTovarovString="kolihTovarovString";

    CheckBox mainCheckBox;
    ArrayList<Double> arDoubKOplate=new ArrayList<Double>();
    Boolean mainChSwitch=true;
    String tempKol="";
    JSONArray jsTovari= new JSONArray();
    ConstraintLayout constraintLayoutBotom;


    ArrayList <CheckBox> listCheckBox = new ArrayList<CheckBox>();


    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(getApplicationContext(), SpisokTovarovPrilavok.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(kolihTovarovString, SpisokTovarovPrilavok.korzinaCount1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korzina);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        userInfo=new UserInfo(this);
        scrollView= (ScrollView) findViewById(R.id.scrolTovari);
        userId=userInfo.getUserId();
        tvKorzinaCount=findViewById(R.id.tvKorzinaCount);
        mainCheckBox = findViewById(R.id.chMain);
        constraintLayoutBotom=findViewById(R.id.constraintLayoutBotom);
        btNahatPokupki =findViewById(R.id.btMainPage);
        btNahatPokupki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        tvKOplate =findViewById(R.id.tvKOplate);
        imageButtonDel=findViewById(R.id.ibKorzina);


        imageButtonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDel="";
                for (int y=0; y<listCheckBox.size(); y++){
                    if(listCheckBox.get(y).isChecked()){
                        try {

                            idDel=idDel+"'"+jsTovari.getJSONObject(y).getString("id")+"',";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                idDel=idDel.substring(0, idDel.length() - 1);
                System.out.println("del str "+idDel);
                delTovari(idDel);
            }
        });
        showSQL();


    }




    void showSQL (){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SHOW_KORZINA,
    new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                JSONObject jsonObject= new JSONObject(response.toString());

                JSONArray jsonArray = jsonObject.getJSONArray("korzina");
                JSONArray jsonArrayDostavka = jsonObject.getJSONArray("typedostavki");
                JSONArray jsonArrayDostavkaAdresa = jsonObject.getJSONArray("adres");
                JSONObject jsonRowType = jsonArrayDostavka.getJSONObject(0);
                JSONObject jsonRowAdres = jsonArrayDostavkaAdresa.getJSONObject(0);
                typeDostavki=jsonRowType.getString("type");
                adresPost=jsonRowAdres.getString("adrespoumolhaniypohta");
                adresPunkt=jsonRowAdres.getString("adrespoumolhaniypunktvidahi");
                O.o("PUNKT VIDAHI "+adresPunkt);
              //  System.out.println(typeDostavki+" TYPE");
                LinearLayout linLayout = (LinearLayout) findViewById(R.id.linerLayKorzina);
                LayoutInflater layoutInflater = getLayoutInflater();
                linLayout.removeAllViews();
                if(jsonArray.length()==0){
                    btNahatPokupki.setVisibility(View.VISIBLE);
                    mainCheckBox.setVisibility(View.GONE);
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                        if(i==0){
                            mainCheckBox.setVisibility(View.VISIBLE);
                        }
                    btNahatPokupki.setVisibility(View.INVISIBLE);
                    mainCheckBox.setVisibility(View.VISIBLE);
                    JSONObject jsonRow = jsonArray.getJSONObject(i);
//                    System.out.println("ROW "+jsonRow);

                    final String id     =    jsonRow.getString("id");
                    String tovarNaimenovanie     =    jsonRow.getString("naimenovanie");
//                    System.out.println("tovar v korzine  "+tovarNaimenovanie);
                    final String tovarId     =    jsonRow.getString("tovar");
                    final String cenaskidka     =    jsonRow.getString("cenaskidka");
                    final String skidkaStr     =    jsonRow.getString("skidka");
                    final Double[] kolihestvo = {jsonRow.getDouble("kolihestvo")};
                    Double cena     =    jsonRow.getDouble("cena");
                    int skidka=0;
                    Double cenaSkidkaDouble=0.0;
                    if(!skidkaStr.equals("null")) {
                        skidka = jsonRow.getInt("skidka");
                    }
                    if(!cenaskidka.equals("null")) {
                        cenaSkidkaDouble = jsonRow.getDouble("cenaskidka");
                    }
                    final boolean selected = jsonRow.getBoolean("selected");
                    if(cenaSkidkaDouble>0){
                        cena=cenaSkidkaDouble;
                    }
                    else {
                        cena=cena*(100-skidka)/100;
                    }
                    String foto =jsonRow.getString("foto");



                    final View item = layoutInflater.inflate(R.layout.row_korzina, linLayout, false);
                    ImageView ivTovar =item.findViewById(R.id.ivTovar);
                    Picasso.get().load(foto).into(ivTovar);
                    TextView tvNazvanie = (TextView) item.findViewById(R.id.tvAdresPoDostavke);
                    TextView tvCena= (TextView) item.findViewById(R.id.tvSposobDostavki);
                    final EditText etKolihestvo= (EditText) item.findViewById(R.id.tvKolihestvoKorzina);
                    final CheckBox chKorzina=item.findViewById(R.id.chKorzina);
                    listCheckBox.add(i,chKorzina);
                        chKorzina.setChecked(selected);
                        if(selected){
                            kOplate=kOplate+cena* kolihestvo[0];
                            SpisokTovarovPrilavok.korzinaCount1=SpisokTovarovPrilavok.korzinaCount1+ kolihestvo[0].intValue();

                            arDoubKOplate.add(cena* kolihestvo[0]);
                        }
                    final Double finalCena = cena;


                    tvNazvanie.setText(tovarNaimenovanie);
                    String cenaFormated = String.format("%, (.2f",cena);
                    tvCena.setText(cenaFormated);
                    final String kolihestvoFormatedString = String.format("%.0f", kolihestvo[0]);


                    etKolihestvo.setText(kolihestvoFormatedString);
                    final int finalI = i;
                    final Double finalCena1 = cena;
                    final Double finalCena2 = cena;
                    etKolihestvo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if(actionId==6){
                                if(chKorzina.isChecked()){
                                    SpisokTovarovPrilavok.korzinaCount1 = SpisokTovarovPrilavok.korzinaCount1 - kolihestvo[0].intValue();
                                    kOplate=kOplate- finalCena2 *kolihestvo[0];
                                    String chKol = etKolihestvo.getText().toString();
                                    kolihestvo[0]  = Double.parseDouble(chKol);
                                    SpisokTovarovPrilavok.korzinaCount1 = SpisokTovarovPrilavok.korzinaCount1 + kolihestvo[0].intValue();
                                    kOplate=kOplate+finalCena2*kolihestvo[0];
                                    tvKOplate.setText(kOplate.toString());

                                    etKolihestvo.setText(chKol);
                                    sendKolih(tovarId, userId, chKol, chKorzina);
                                    krasniyShethik();

                                }
                                else{
                                    String chKol = etKolihestvo.getText().toString();
                                    kolihestvo[0]  = Double.parseDouble(chKol);
                                    etKolihestvo.setText(chKol);
                                    sendKolih(tovarId, userId, chKol, chKorzina);
                                }
                                System.out.println("Action " + actionId + " ev= " + event);



//                                System.out.println("kolih = "+kolihestvo[0]+", kOpl= "+kOplate);

                            }return false;
                        }

                    });
                    etKolihestvo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
//                            System.out.println("change focus "+etKolihestvo.getId()+" "+hasFocus);

                            String pustStr="";
                            if(hasFocus==true) {
                                tempKol=etKolihestvo.getText().toString();
                                etKolihestvo.setText(pustStr);
                            }
                            if(hasFocus==false) {

                                if(etKolihestvo.getText().toString().equals(pustStr)){
                                    etKolihestvo.setText(tempKol);
//                                    System.out.println("//////");
                                }
                                else {
//                                    System.out.println(etKolihestvo.getText()+" gettext");
                                    kolihestvo[0]=Double.parseDouble(etKolihestvo.getText().toString());
                                    krasniyShethik();
                                }
                            }
                        }
                    });
                    final ImageButton btAdd=item.findViewById(R.id.ibAddTovar);

                    chKorzina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            TextView tvKOplate2 =findViewById(R.id.tvKOplate);
                            Boolean allCheck=true;
                            Boolean allNotCheck=true;
                            for (int c = 0; c< listCheckBox.size(); c++){
                                if(!listCheckBox.get(c).isChecked()){
                                    allCheck=false;
                                    allNotCheck=false;
                                }
                            }
                            if(allNotCheck||listCheckBox.size()==0){
                                imageButtonDel.setVisibility(View.INVISIBLE);
                                System.out.println("VIKLUHIT");
                            }else {
                                System.out.println("Propusk");
                            }
                            if(allCheck){
                                mainChSwitch=true;
                                mainCheckBox.setChecked(true);
                            }else {
                                mainChSwitch=false;
                                mainCheckBox.setChecked(false);
                                mainChSwitch=true;
                            }

                            if(chKorzina.isChecked()){
                                imageButtonDel.setVisibility(View.VISIBLE);
                                SpisokTovarovPrilavok.korzinaCount1=SpisokTovarovPrilavok.korzinaCount1+ kolihestvo[0].intValue();
                                krasniyShethik();
                                trueSelected(id);
                                kOplate=kOplate+ finalCena* kolihestvo[0];
                              // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" kOplate "+kOplate);
                                String formattedDoubleKOplate = String.format("%, (.2f",kOplate);
                                tvKOplate2.setText(formattedDoubleKOplate);

                            }
                            else{
                                SpisokTovarovPrilavok.korzinaCount1=SpisokTovarovPrilavok.korzinaCount1- kolihestvo[0].intValue();
                                krasniyShethik();
                                falseSelected(id);
                                kOplate=kOplate- finalCena* kolihestvo[0];
                                String formattedDoubleKOplate = String.format("%, (.2f",kOplate);
                                tvKOplate2.setText(formattedDoubleKOplate);
                               // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" kOplate "+kOplate);
                            }
                        }
                    });
                    ImageButton btDel=item.findViewById(R.id.ibDelTovar);
                    btAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SpisokTovarovPrilavok.korzinaCount1=SpisokTovarovPrilavok.korzinaCount1+1;
                            krasniyShethik();
                            chKorzina.setChecked(true);
                            buySQL(tovarId, userId);
                            kolihestvo[0] = kolihestvo[0] +1;
                            String formattedDouble = String.format("%.0f", kolihestvo[0]);
                            etKolihestvo.setText(formattedDouble);
                            TextView tvKOplate2 =findViewById(R.id.tvKOplate);
                            if(chKorzina.isChecked()){
                                kOplate=kOplate+ finalCena;
                                //System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" kOplate "+kOplate);
                                String formattedDoubleKOplate = String.format("%, (.2f",kOplate);
                                tvKOplate2.setText(formattedDoubleKOplate);
                            }
                        }
                    });

                    btDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(kolihestvo[0] >1) {
                                delSQL(tovarId, userId);
                                 kolihestvo[0] = kolihestvo[0] - 1;
                                String formattedDouble = String.format("%.0f", kolihestvo[0]);
                                etKolihestvo.setText(formattedDouble);

                                krasniyShethik();
                                TextView tvKOplate2 =findViewById(R.id.tvKOplate);
                                if(chKorzina.isChecked()){
                                    SpisokTovarovPrilavok.korzinaCount1=SpisokTovarovPrilavok.korzinaCount1-1;
                                    String korzCountFormat = String.format("%, (.0f",SpisokTovarovPrilavok.korzinaCount1);
                                    tvKorzinaCount.setText(korzCountFormat);
                                    kOplate=kOplate- finalCena;
                                   // System.out.println("kolih "+kolihestvo[0]+" cena "+finalCena+" kOplate "+kOplate);
                                    String formattedDoubleKOplate = String.format("%, (.2f",kOplate);
                                    tvKOplate2.setText(formattedDoubleKOplate);
                                }

                            }


                        }
                    });
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           //createIntentAdres(id);
                        }
                    });
                    jsTovari.put(i,jsonRow);

                    linLayout.addView(item);
                }

                            //Включить главный чекбокс , если выбраны все товары
                            boolean chAllShowIn=true;
                            for (int b=0;b<listCheckBox.size();b++){
                                if (!listCheckBox.get(b).isChecked()){
                                    chAllShowIn=false;
                                }
                            }
                            if(chAllShowIn){
                                mainCheckBox.setChecked(true);
                            }
                            //Включить главный чекбокс , если выбраны все товары
                krasniyShethik();
                String formattedDoubleKOplate = String.format("%, (.2f",kOplate);
                tvKOplate.setText(formattedDoubleKOplate);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        // scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
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


    String id=userInfo.getUserId();
    parameters.put("pokupatel", id);
    return parameters;
}
        };
        btKupit=findViewById(R.id.btKupit);
        btKupit.setOnClickListener(this);
        requestQueue.add(stringRequest);
        mainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mainChSwitch){
                    if (mainCheckBox.isChecked()) {
                        for (int z = 0; z < listCheckBox.size(); z++) {
                            listCheckBox.get(z).setChecked(true);
                        }
                    } else {
                        for (int z = 0; z < listCheckBox.size(); z++) {
                            listCheckBox.get(z).setChecked(false);
                        }
                    }
            }
            }
        });


    }
    void krasniyShethik(){
        String korzCountFormat = Integer.toString(SpisokTovarovPrilavok.korzinaCount1);
        tvKorzinaCount.setText(korzCountFormat);
        tvKOplate.setText(kOplate.toString());
        if(SpisokTovarovPrilavok.korzinaCount1==0){
            tvKorzinaCount.setVisibility(View.INVISIBLE);
            constraintLayoutBotom.setVisibility(View.GONE);
            imageButtonDel.setVisibility(View.INVISIBLE);
            mainCheckBox.setChecked(false);

        }
        else {
            tvKorzinaCount.setVisibility(View.VISIBLE);
            constraintLayoutBotom.setVisibility(View.VISIBLE);
            imageButtonDel.setVisibility(View.VISIBLE);
        }
    }

    void buySQL (final String tovar, final  String pokupatel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.BUY_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");

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

                parameters.put("tovar", tovar);
                parameters.put("pokupatel", pokupatel);
               // System.out.println("BUY "+tovar+" "+pokupatel);
                return parameters;
            }

        }

                ;
        requestQueue.add(stringRequest);

    }
    void sendKolih (final String tovar, final  String pokupatel, final String count, final  CheckBox checkBox){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SEND_COUNT_TOVARA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            String jsonArray = jsonObject.getString("error");
//                            System.out.println(jsonArray);
                            if(!checkBox.isChecked()){
                                checkBox.setChecked(true);
//                                System.out.println("set cheBox");
                            }
                            else {
//                                System.out.println("no ch Box "+checkBox.toString());
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

                parameters.put("tovar", tovar);
                parameters.put("count", count);
                parameters.put("pokupatel", pokupatel);
//                System.out.println(tovar+" "+count+" "+pokupatel);
                return parameters;
            }

        }

                ;
        requestQueue.add(stringRequest);

    }
    void  delSQL (final String tovar, final  String pokupatel){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());

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

                parameters.put("tovar", tovar);
                parameters.put("pokupatel", pokupatel);
               // System.out.println("BUY "+tovar+" "+pokupatel);
                return parameters;
            }

        }

                ;
        requestQueue.add(stringRequest);

    }
    void  delTovari ( final  String tovari){
        final Intent intent=new Intent(this, Korzina.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DEL_TOVAR_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            startActivity(intent);
                            finish();


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

                parameters.put("tovari", tovari);
                parameters.put("pokupatel", "1");
//                System.out.println("Param  "+parameters);
                return parameters;
            }

        }

                ;
        requestQueue.add(stringRequest);

    }
    void  trueSelected (final String idKorzina){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.TRUE_SELECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
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

                parameters.put("idKorzina", idKorzina);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }
    void  falseSelected (final String idKorzina){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FALSE_SELECTED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
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

                parameters.put("idKorzina", idKorzina);
                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btKupit:
                Intent intent=new Intent(this, OformlenieZakaza.class);
                intent.putExtra("typeDostavki",typeDostavki);
                intent.putExtra("adresPost",adresPost);
                intent.putExtra("adresPunkt",adresPunkt);
                startActivity(intent);
                break;
        }
    }
}
