package xyz.lavaliva.market.TovarPage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import Model.Utils;
import xyz.lavaliva.market.R;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;
import xyz.lavaliva.market.TovarList.TovariAdapterPrilavok;
import xyz.lavaliva.market.TovarList.TovariObjPrilavok;

public class TovarPageActivity extends AppCompatActivity {
    TextView tvOpisaniePageTovara,
            tvKorzinaCountPageTovara,
            tvCenaPageTovara
    ;
    ImageView ivMainImagePageTovara,
            ivIconkaTelejkiPageTovara;

    ProgressBar prBarPageTovara;
    Button btVKorzinuPageTovara,
            btKKorzinePageTovara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar_page);

        tvOpisaniePageTovara=findViewById(R.id.tvOpisaniePageTovara);
        tvKorzinaCountPageTovara=findViewById(R.id.tvKorzinaCountPageTovara);
        tvCenaPageTovara=findViewById(R.id.tvCenaPageTovara);
        ivMainImagePageTovara=findViewById(R.id.ivMainImagePageTovara);
        ivIconkaTelejkiPageTovara=findViewById(R.id.ivIconkaTelejkiPageTovara);
        btVKorzinuPageTovara=findViewById(R.id.btVKorzinuPageTovara);
        btKKorzinePageTovara=findViewById(R.id.btKKorzinePageTovara);
        ivIconkaTelejkiPageTovara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpisokTovarovPrilavok.ibKorzina.performClick();
            }
        });

        btVKorzinuPageTovara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TovariAdapterPrilavok.buySQL(TovariAdapterPrilavok.tovariObjPrilavokStatic.getId());
                SpisokTovarovPrilavok.korzinaCount1++;
                setKorzinaCount();
                btKKorzinePageTovara.setVisibility(View.VISIBLE);
                btVKorzinuPageTovara.setVisibility(View.GONE);

            }
        });

        btKKorzinePageTovara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpisokTovarovPrilavok.ibKorzina.performClick();
            }
        });

        tvOpisaniePageTovara.setText(TovariAdapterPrilavok.tovariObjPrilavokStatic.getNaimenovanie());
        tvCenaPageTovara.setText(String.format("%, (.2f", TovariAdapterPrilavok.tovariObjPrilavokStatic.getCenaSoSkidkoy()));
        Picasso.get().load(TovariAdapterPrilavok.tovariObjPrilavokStatic.getFoto_url()).into(ivMainImagePageTovara);

        setKorzinaCount();

        //showInfoFromServer();
    }



    private void showInfoFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Utils.SHOW_TOVAR_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    prBarPageTovara.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("tovar");

                    for (int i = 0; i < array.length(); i++){

                    }


                } catch (JSONException e) {
                    System.out.println("\n ERR"+response.toString());
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

                //System.out.println("Otpravka na server iz tovar id "+id);
                return parameters;
            }};
        SpisokTovarovPrilavok.requestQueue.add(stringRequest);
        prBarPageTovara.setVisibility(View.VISIBLE);
    }

    private void setKorzinaCount() {
        if(SpisokTovarovPrilavok.korzinaCount1>0){
            tvKorzinaCountPageTovara.setVisibility(View.VISIBLE);
            tvKorzinaCountPageTovara.setText(Integer.toString(SpisokTovarovPrilavok.korzinaCount1));
        }
    }
}
