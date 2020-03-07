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

public class Gelir_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button btn1;
    TextView bilgiler;
    StringBuilder sonuc;
    Button btn2;
    Button btnGelirKaydet;
    Button btnsil;
    TextView edtTarih;
    EditText edtTutar;
    private veritabani v1;
    Spinner spnKatagori;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gelir);
        tanimla();
        sonuc = new StringBuilder();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
                btn1.setClickable(false);

            }
        });
        btnGeri();
        v1 = new veritabani(this);
        Cursor cursor = kayitGetir();
        kayitGoster(cursor);
        btnGelirKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnGelirKaydet("Gelir",spnKatagori.getSelectedItem().toString(),edtTarih.getText().toString(),edtTutar.getText().toString());
                    /*guncelleme islemi icin asagıdaki 2 satır kod yazılır*/
                    Cursor cursor = kayitGetir();
                    kayitGoster(cursor);
                }
                finally {
                    v1.close();
                }
            }
        });
        btnsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitSil(edtTutar.getText().toString());
                Cursor cursor = kayitGetir();
                kayitGoster(cursor);

            }
        });

    }
    public void tanimla(){
        btn1 = findViewById(R.id.feriha);
        btn2 = findViewById(R.id.btnVazgec);
        btnGelirKaydet=findViewById(R.id.btnGelirKaydet);
        edtTarih=findViewById(R.id.edtTarih);
        edtTutar=findViewById(R.id.edtTutar);
        bilgiler=findViewById(R.id.txtViewBilgiler);
        spnKatagori=findViewById(R.id.spinnerTur);
        btnsil=findViewById(R.id.btnSil);
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
                Intent intent4 = new Intent(Gelir_activity.this,MainActivity.class);
                finish();//bunu sorarsa yeni sayfa açıyor
            }
        });
    }
    private void btnGelirKaydet(String tur, String katagori, String tarih, String tutar){
        if (edtTutar.getText().toString().trim().equals("")||edtTarih.getText().toString().trim().equals("Date")){
            Toast.makeText(getApplicationContext(), "Lütfen Boş Alan Bırakmayınız !", Toast.LENGTH_LONG).show();
        }
        else {
            SQLiteDatabase db = v1.getWritableDatabase();
            ContentValues veriler = new ContentValues();
            veriler.put("tur", tur);
            veriler.put("katagori", katagori);
            veriler.put("tarih", tarih);
            veriler.put("tutar", tutar);
            db.insertOrThrow("hesap", null, veriler);
            gelirtoplam(kayitGetir());
        }

    }
    private String[] sutunlar={"tur", "katagori", "tarih", "tutar"};
    private Cursor kayitGetir(){
        SQLiteDatabase db = v1.getWritableDatabase();
        Cursor okunanlar = db.query("hesap",sutunlar,null,null,null, null, null);
        return okunanlar;
    }

    private void kayitGoster(Cursor goster){
        StringBuilder builder = new StringBuilder();
        while (goster.moveToNext()){
            String turr = goster.getString(goster.getColumnIndex("tur"));
            String katagorii = goster.getString(goster.getColumnIndex("katagori"));
            String tarihh = goster.getString(goster.getColumnIndex("tarih"));
            String tutarr = goster.getString(goster.getColumnIndex("tutar"));
            builder.append("tur: ").append(turr+"\n");
            builder.append("katagori: ").append(katagorii+"\n");
            builder.append("tarih: ").append(tarihh+"\n");
            builder.append("tutar: ").append(tutarr+"\n");
            builder.append("------------------------").append("\n");
        }
        TextView text = findViewById(R.id.txtViewBilgiler);
        text.setText(builder);
    }
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
    }
    private void kayitSil(String tutar){
        SQLiteDatabase db = v1.getReadableDatabase();
        db.delete("hesap", "tutar"+"=?",new String[]{tutar});
    }
}
