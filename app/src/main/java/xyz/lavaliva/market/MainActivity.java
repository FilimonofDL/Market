package xyz.lavaliva.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import Model.UserInfo;
import xyz.lavaliva.market.TovarList.SpisokTovarovPrilavok;
import xyz.lavaliva.market.ZakazList.SpisokZakazov;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent=new Intent(this, SpisokZakazov.class);
        final Intent intent2=new Intent(this, SpisokTovarovPrilavok.class);
        intent.putExtra("vetka", "1");
        intent.putExtra("id", "1");
        UserInfo userInfo=new UserInfo(this);
        userInfo.setUserId("1");
        Button bt1=findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });
        Button bt2=findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent2);
            }
        });
//        bt1.performClick();
        bt2.performClick();


    }
}
