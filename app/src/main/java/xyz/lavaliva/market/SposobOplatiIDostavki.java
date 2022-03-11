package xyz.lavaliva.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

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

import java.util.HashMap;
import java.util.Map;

import Model.UserInfo;
import Model.Utils;

public class SposobOplatiIDostavki extends AppCompatActivity {

    RequestQueue requestQueue;
    String typeDostavki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sposob_oplati);
        RadioGroup radioGroup=findViewById(R.id.rgroupSposobOplati);
        final Intent intent=new Intent();
        UserInfo userInfo=new UserInfo(this);
        final String userId=userInfo.getUserId();
        Bundle bundle=getIntent().getExtras();
        typeDostavki=bundle.getString("typeDostavki");

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        RadioButton radioButton=findViewById(R.id.radioButton);
        RadioButton radioButton2=findViewById(R.id.radioButton2);
        RadioButton radioButton3=findViewById(R.id.radioButton3);
if (typeDostavki.equals("kurier")){
    radioButton.setChecked(true);
}
else if (typeDostavki.equals("punktvidahi")){
    radioButton2.setChecked(true);
}
else if (typeDostavki.equals("pohta")){
    radioButton3.setChecked(true);
}
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton :
                        intent.putExtra("sposob", "1");
                        intent.putExtra("sposobtext", "kurier");

                        break;
                    case R.id.radioButton2 :
                        intent.putExtra("sposob", "2");
                        intent.putExtra("sposobtext", "punktvidahi");

                        break;
                    case R.id.radioButton3 :
                        intent.putExtra("sposob", "3");
                        intent.putExtra("sposobtext", "pohta");
                        break;

                }
                Switch aSwitch =findViewById(R.id.swSposobOplUmolh);
                if(aSwitch.isChecked()){
                    defaultSposobSQL(userId, intent.getStringExtra("sposob"));
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    void defaultSposobSQL (final String userId, final String sposob){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.SPOSOB_DEF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("serv");

                        } catch (JSONException e) {

//                            System.out.println("\n ERR"+response.toString());
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

                parameters.put("sposob", sposob);
                parameters.put("pokupatel", userId);
                return parameters;
            }

        }

                ;
        requestQueue.add(stringRequest);

    }
}
