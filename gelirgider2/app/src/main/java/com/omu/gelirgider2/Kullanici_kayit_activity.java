package com.omu.gelirgider2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Kullanici_kayit_activity extends AppCompatActivity {
    Button kKaydet;
    EditText kKullaniciAdi, kKullaniciSifre, kKullaniciSifre2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_kayit);
        tanimla();
    }
    public void tanimla(){
        kKaydet=findViewById(R.id.btnKKaydet);
        kKullaniciAdi=findViewById(R.id.edtKKullaniciAdi);
        kKullaniciSifre=findViewById(R.id.edtKSifre);
        kKullaniciSifre2=findViewById(R.id.edtKSifre2);
    }
}
