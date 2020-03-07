package com.omu.gelirgider2;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class Gider_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button btn1;
    TextView bilgiler;
    StringBuilder sonuc;
    Button btn2;
    Button btnGiderKaydet;
    Button btnsil2;
    TextView edtTarih;
    EditText edtTutar;
    private veritabani v1;
    Spinner spnKatagori;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gider);
        tanimla();
        sonuc = new StringBuilder();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });
        btnGeri();
        v1 = new veritabani(this);
        Cursor cursor1 = giderGetir();
        giderGoster(cursor1);
        btnGiderKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnGiderKaydet("Gider",spnKatagori.getSelectedItem().toString(),edtTarih.getText().toString(),edtTutar.getText().toString());
                    /*guncelleme islemi icin asagıdaki 2 satır kod yazılır*/
                    Cursor cursor = giderGetir();
                    giderGoster(cursor);
                }
                finally {
                    v1.close();
                }
            }
        });
        btnsil2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitSil(edtTutar.getText().toString());
                Cursor cursor2 = giderGetir();
                giderGoster(cursor2);
            }
        });

    }
    public void tanimla(){
        btn1 = findViewById(R.id.feriha);
        btn2 = findViewById(R.id.btnVazgec);
        btnGiderKaydet=findViewById(R.id.btnGiderKaydet);
        edtTarih=findViewById(R.id.edtTarih);
        edtTutar=findViewById(R.id.edtTutar);
        bilgiler=findViewById(R.id.txtViewBilgiler);
        spnKatagori=findViewById(R.id.spinnerTur);
        btnsil2=findViewById(R.id.btnSil2);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sonuc.append(dayOfMonth).append(" - ").append(month).append(" - ").append(year);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        edtTarih.setText(sonuc);
    }
    public void btnGeri(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(Gider_activity.this,MainActivity.class);
                finish();//bunu sorarsa yeni sayfa açıyor
            }
        });
    }
    private void btnGiderKaydet(String tur, String katagori, String tarih, String tutar){
        if (edtTutar.getText().toString().trim().equals("")||edtTarih.getText().toString().trim().equals("Date")){
            Toast.makeText(getApplicationContext(), "Lütfen Boş Alan Bırakmayınız !", Toast.LENGTH_LONG).show();
        }
        else{
            SQLiteDatabase db = v1.getWritableDatabase();
            ContentValues veriler = new ContentValues();
            veriler.put("tur", tur);
            veriler.put("katagori", katagori);
            veriler.put("tarih", tarih);
            veriler.put("tutar", tutar);
            db.insertOrThrow("hesapgider", null, veriler);
            gidertoplam(giderGetir());
        }
    }
    private String[] sutunlar={"tur", "katagori", "tarih", "tutar"};
    private Cursor giderGetir(){
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar2 = db.query("hesapgider",sutunlar,null,null,null, null, null);
        return okunanlar2;
    }

    private void giderGoster(Cursor goster1){
        StringBuilder builder = new StringBuilder();
        while (goster1.moveToNext()){
            String turr = goster1.getString(goster1.getColumnIndex("tur"));
            String katagorii = goster1.getString(goster1.getColumnIndex("katagori"));
            String tarihh = goster1.getString(goster1.getColumnIndex("tarih"));
            String tutarr = goster1.getString(goster1.getColumnIndex("tutar"));
            builder.append("tur: ").append(turr+"\n");
            builder.append("katagori: ").append(katagorii+"\n");
            builder.append("tarih: ").append(tarihh+"\n");
            builder.append("tutar: ").append(tutarr+"\n");
            builder.append("------------------------").append("\n");
        }
        TextView text = findViewById(R.id.txtViewBilgiler);
        text.setText(builder);
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
    }
    private void kayitSil(String tutar){
        SQLiteDatabase db = v1.getReadableDatabase();
        db.delete("hesapgider", "tutar"+"=?",new String[]{tutar});
    }
}
