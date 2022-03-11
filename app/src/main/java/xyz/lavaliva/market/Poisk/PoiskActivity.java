package xyz.lavaliva.market.Poisk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.ILoadMore;
import Model.O;
import Model.Utils;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.ResPoiskaStranica.ResPoiskaStranicaActivity;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;

public class PoiskActivity extends AppCompatActivity {
    EditText etVvodPoisk;
    RecyclerView recPoisk;
    PoiskAdapter poiskAdapter;
    List<PoiskTextObj> poiskTextObjsList;
    int dliinaZagruzki = 20;
    static String iskomiyText = "";
    static int dlinnaVvedennogoTextaVPoiske = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisk);
        etVvodPoisk = findViewById(R.id.etVvodPoisk);
        recPoisk = findViewById(R.id.recPoisk);

        recPoisk.setLayoutManager(new LinearLayoutManager(this));
        poiskTextObjsList = new ArrayList<>();
        poiskAdapter = new PoiskAdapter(recPoisk, this, poiskTextObjsList);
        recPoisk.setAdapter(poiskAdapter);

        etVvodPoisk.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                iskomiyText = editable.toString();
                dlinnaVvedennogoTextaVPoiske = iskomiyText.length();
                poiskTextObjsList.clear();

                if (!iskomiyText.equals("")) {
                    PoiskAdapter.loadHistory = false;
                    loadUrlData(iskomiyText, 0);
                    poiskAdapter.setLoadMore(new ILoadMore() {

                        @Override
                        public void onLoadMore() {

                            int index = poiskTextObjsList.size();
                            loadUrlData(iskomiyText, index);
                        }
                    });
//    O.o("IF POISK");
                } else {
                    poiskTextObjsList.clear();
                    PoiskAdapter.loadHistory = true;
                    loadHistoryPoiska(0);
                    poiskAdapter.setLoadMore(new ILoadMore() {
                        @Override
                        public void onLoadMore() {
                            int index = poiskTextObjsList.size();
                            loadHistoryPoiska(index);
                        }
                    });
                }
            }
        });
        poiskTextObjsList.clear();
        PoiskAdapter.loadHistory = true;
        loadHistoryPoiska(0);
//        loadUrlData("l", 0);
        poiskAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                int index = poiskTextObjsList.size();
                loadHistoryPoiska(index);
//                loadUrlData("l",index);
                poiskAdapter.setLoaded();
            }
        });
        //Для того что бы по нажатия лупы в клавиатуре покаазать товары из базы
        etVvodPoisk.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH))
                        || (i == EditorInfo.IME_ACTION_SEARCH)) {
                    iskomiyText = textView.getText().toString();
                    poiskTextObjsList.clear();

                    createIntentPageNaidenieTovari(iskomiyText);
                }
                return false;
            }
        });

    }

    private void createIntentPageNaidenieTovari(String iskomiyText) {
        PoiskAdapter.poiskTextObjVibraniy=new PoiskTextObj(iskomiyText);
        Intent intent =new Intent(this, ResPoiskaStranicaActivity.class);
        startActivity(intent);

    }

    private void loadPoiskTovaraPoBaze(String iskomiyText, int i) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_TOVAR_DLIA_POISKA, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("naideno");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        PoiskTextObj poiskTextObj = new PoiskTextObj(jo.getString("textzaprosa"));
                        poiskTextObjsList.add(poiskTextObj);
                        poiskAdapter.setItems();

                    }
                    poiskAdapter.notifyDataSetChanged();
                    poiskAdapter.setLoaded();

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


//                parameters.put("iskattext", text);
//                parameters.put("startindex", Integer.toString(startindex));

                parameters.put("countselect", Integer.toString(dliinaZagruzki));

                //System.out.println("Otpravka na server iz tovar id "+id);
                return parameters;
            }
        };


        SpisokTovarovPrilavok.requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        poiskTextObjsList.clear();
    }

    private void loadHistoryPoiska(final int startindex) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.LOAD_HISTORY_POISKA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("naideno");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        PoiskTextObj poiskTextObj = new PoiskTextObj(jo.getString("textzaprosa"));
                        poiskTextObjsList.add(poiskTextObj);
//                        O.o(poiskTextObj.getText()+" hist JO");
                    }
                    poiskAdapter.notifyDataSetChanged();
                    poiskAdapter.setLoaded();

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
                parameters.put("pokupatel", SpisokTovarovPrilavok.userId);
                parameters.put("startindex", Integer.toString(startindex));
                parameters.put("countselect", Integer.toString(dliinaZagruzki));
                O.o(parameters.toString() + " par to send");
                return parameters;
            }
        };
        SpisokTovarovPrilavok.requestQueue.add(stringRequest);
    }


    private void loadUrlData(final String text, final int startindex) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_POISK_TEXT, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("naideno");
                    O.o(response + " otvet");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = array.getJSONObject(i);
                        PoiskTextObj poiskTextObj = new PoiskTextObj(jo.getString("textzaprosa"));
                        poiskTextObjsList.add(poiskTextObj);
                        poiskAdapter.setItems();

                    }
                    poiskAdapter.notifyDataSetChanged();
                    poiskAdapter.setLoaded();

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


                parameters.put("iskattext", text);
                parameters.put("startindex", Integer.toString(startindex));

                parameters.put("countselect", Integer.toString(dliinaZagruzki));

                //System.out.println("Otpravka na server iz tovar id "+id);
                return parameters;
            }
        };


        SpisokTovarovPrilavok.requestQueue.add(stringRequest);

    }
}
