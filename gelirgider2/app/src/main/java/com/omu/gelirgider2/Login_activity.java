package com.omu.gelirgider2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login_activity extends AppCompatActivity {
    Button btnGiris;
    Button btnKaydet;
    Button btnGiris1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tanimla();
        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Login_activity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(Login_activity.this,Kullanici_kayit_activity.class);
                startActivity(intent2);
            }
        });
    }
    public void tanimla(){
        btnGiris =findViewById(R.id.btnGiris);
        btnKaydet=findViewById(R.id.btnKaydet);
    }
}
