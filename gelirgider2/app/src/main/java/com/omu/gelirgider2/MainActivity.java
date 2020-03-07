package com.omu.gelirgider2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnGelir;
    Button btnGider;
    TextView txtGelirToplam;
    TextView txtGiderToplam;
    TextView txtGenelToplam;
    private veritabani v1;
    private String geneltoplam;
    private int netgelir, netgider, alisverisT;
    TextView txtAlisveris, txtAlisverisOran;
    TextView txtFaturalar, txtFaturalarOran;
    TextView txtUlaşım, txtUlaşımOran;
    TextView txtYemek, txtYemekOran;
    TextView txtSaglik, txtSaglikOran;
    TextView txtDiger, txtDigerOran;
    ProgressBar pBarAlisveris, pBarFaturalar, pBarUlasim, pBarYemek, pBarSaglik, pBarDiger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        v1 = new veritabani(this);
        tanimla();
        Cursor cursor = kayitGetir();
        Cursor cursor2 = kayitGetir();
        Cursor cursor3 = kayitGetir();
        gelirtoplam(kayitGetir());
        gidertoplam(giderGetir());
        alisveristoplam(giderGetir());
        faturalartoplam(giderGetir());
        ulasimToplam(giderGetir());
        yemekToplam(giderGetir());
        saglikToplam(giderGetir());
        digerToplam(giderGetir());
        geneltoplam= String.valueOf((netgelir-netgider));
        txtGenelToplam.setText(geneltoplam+" TL");


        btnGelir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Gelir_activity.class);
                startActivity(intent1);
            }
        });
        btnGider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Gider_activity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Cursor cursor = kayitGetir();
        Cursor cursor2 = kayitGetir();
        Cursor cursor3 = kayitGetir();*/
        gelirtoplam(kayitGetir());
        gidertoplam(giderGetir());
        alisveristoplam(giderGetir());
        faturalartoplam(giderGetir());
        ulasimToplam(giderGetir());
        yemekToplam(giderGetir());
        saglikToplam(giderGetir());
        digerToplam(giderGetir());
        geneltoplam= String.valueOf((netgelir-netgider));
        txtGenelToplam.setText(geneltoplam+" TL");
    }

    public void tanimla(){
        btnGelir=findViewById(R.id.btnGelir);
        btnGider=findViewById(R.id.btnGider);
        txtGelirToplam=findViewById(R.id.txtGelirToplam);
        txtGiderToplam=findViewById(R.id.txtGiderToplam);
        txtGenelToplam=findViewById(R.id.txtGenelToplam);
        txtAlisveris=findViewById(R.id.txtAlisveris);
        txtFaturalar=findViewById(R.id.txtFaturalar);
        txtUlaşım=findViewById(R.id.txtUlasim);
        txtYemek=findViewById(R.id.txtYemek);
        txtSaglik=findViewById(R.id.txtSaglik);
        txtDiger=findViewById(R.id.txtDiger);
        txtAlisverisOran=findViewById(R.id.txtAlisverisOran);
        txtFaturalarOran=findViewById(R.id.txtFaturalarOran);
        txtUlaşımOran=findViewById(R.id.txtUlasimOran);
        txtYemekOran=findViewById(R.id.txtYemekOran);
        txtSaglikOran=findViewById(R.id.txtSaglikOran);
        txtDigerOran=findViewById(R.id.txtDigerOran);
        pBarAlisveris=findViewById(R.id.pBarAlisveris);
        pBarFaturalar=findViewById(R.id.pBarFaturalar);
        pBarUlasim=findViewById(R.id.pBarUlasim);
        pBarYemek=findViewById(R.id.pBarYemek);
        pBarSaglik=findViewById(R.id.pBarSaglik);
        pBarDiger=findViewById(R.id.pBarDiger);
    }
    private String[] sutunlar={"tur", "katagori", "tarih", "tutar"};

    private void gelirtoplam(Cursor gelir){
        StringBuilder builder = new StringBuilder();
        String gelirtutar = "0";
        Integer toplam = 0;
        while (gelir.moveToNext()){
            gelirtutar = gelir.getString(gelir.getColumnIndex("tutar"));
            toplam += Integer.parseInt(gelirtutar);
            builder.append("gelirtutar: ").append(gelirtutar+"\n");
            builder.append("------------------------").append("\n");
        }
        netgelir=toplam;
        txtGelirToplam.setText(toplam.toString()+" TL");
    }
    private void gidertoplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String gidertutar = "0";
        Integer toplam = 0;
        while (gider.moveToNext()){
            gidertutar = gider.getString(gider.getColumnIndex("tutar"));
            toplam += Integer.parseInt(gidertutar);

            builder.append("gidertutar: ").append(gidertutar+"\n");
            builder.append("------------------------").append("\n");

        }
        netgider=toplam;
        txtGiderToplam.setText(toplam.toString()+" TL");
    }
    private void alisveristoplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String alisverisToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Alışveriş")) {
                alisverisToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(alisverisToplam);
            }
        }
        txtAlisveris.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtAlisverisOran.setText(0+"%");
            pBarAlisveris.setProgress(1);
        }
        else{
            txtAlisverisOran.setText(((toplam2*100)/netgider)+"%");
            pBarAlisveris.setProgress(((toplam2*100)/netgider));
        }
    }
    private void faturalartoplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String faturalarToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Faturalar")) {
                faturalarToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(faturalarToplam);
            }
        }
        txtFaturalar.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtFaturalarOran.setText(0+"%");
            pBarFaturalar.setProgress(1);
        }
        else{
            txtFaturalarOran.setText(((toplam2*100)/netgider)+"%");
            pBarFaturalar.setProgress(((toplam2*100)/netgider));

        }

    }
    private void ulasimToplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String ulasimToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Ulaşım")) {
                ulasimToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(ulasimToplam);
            }
        }
        txtUlaşım.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtUlaşımOran.setText(0+"%");
            pBarUlasim.setProgress(1);
        }
        else{
            txtUlaşımOran.setText(((toplam2*100)/netgider)+"%");
            pBarUlasim.setProgress(((toplam2*100)/netgider));
        }
    }
    private void yemekToplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String yemekToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Yemek")) {
                yemekToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(yemekToplam);
            }
        }
        txtYemek.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtYemekOran.setText(0+"%");
            pBarYemek.setProgress(1);
        }
        else{
            txtYemekOran.setText(((toplam2*100)/netgider)+"%");
            pBarYemek.setProgress(((toplam2*100)/netgider));
        }
    }
    private void saglikToplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String saglikToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Sağlık")) {
                saglikToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(saglikToplam);
            }
        }
        txtSaglik.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtSaglikOran.setText(0+"%");
            pBarSaglik.setProgress(1);
        }
        else{
            txtSaglikOran.setText(((toplam2*100)/netgider)+"%");
            pBarSaglik.setProgress(((toplam2*100)/netgider));
        }
    }
    private void digerToplam(Cursor gider){
        StringBuilder builder = new StringBuilder();
        String digerToplam = "0";
        Integer toplam2 = 0;
        while (gider.moveToNext()){
            if(gider.getString(gider.getColumnIndex("katagori")).equals("Diğer")) {
                digerToplam = gider.getString(gider.getColumnIndex("tutar"));
                toplam2 += Integer.parseInt(digerToplam);
            }
        }
        txtDiger.setText(toplam2.toString()+" TL");
        if(netgider==0){
            txtDigerOran.setText(0+"%");
            pBarDiger.setProgress(1);
        }
        else{
            txtDigerOran.setText(((toplam2*100)/netgider)+"%");
            pBarDiger.setProgress(((toplam2*100)/netgider));
        }
    }

    private Cursor kayitGetir(){
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar = db.query("hesap",sutunlar,null,null,null, null, null);
        return okunanlar;
    }
    private Cursor giderGetir(){
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar2 = db.query("hesapgider",sutunlar,null,null,null, null, null);
        return okunanlar2;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater =getMenuInflater();
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.gelir:
                Intent intent1 = new Intent(MainActivity.this,Gelir_activity.class);
                startActivity(intent1);
                return true;
            case R.id.gider:
                Intent intent2 = new Intent(MainActivity.this,Gider_activity.class);
                startActivity(intent2);
                return true;
            case R.id.kullaniciEkle:
                Intent intent3 = new Intent(MainActivity.this,Kullanici_kayit_activity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
