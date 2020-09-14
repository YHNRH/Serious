package com.example.serious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {
    RadioGroup rg;
    int status;
    EditText login;
    EditText pass;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(VERTICAL);
       login = new EditText(this);
        login.setHint("Введите логин");
        pass = new EditText(this);
        pass.setHint("Введите пароль");
        Button but = new Button(this);

        rg = new RadioGroup(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case 1:
                        status = 1; // registration
                        break;
                    case 2:
                        status = 2; // autentification
                        break;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(rg.getCheckedRadioButtonId()), Toast.LENGTH_LONG).show();
            }
        });
        RadioButton reg = new RadioButton(this);
        RadioButton avt = new RadioButton(this);
        reg.setText("Регистрация");
        avt.setText("Авторизация");
        rg.addView(reg);
        reg.setChecked(true);
        rg.addView(avt);
        rg.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(rg);
        ll.addView(login);
        ll.addView(pass);
        ll.addView(but);
        setContentView(ll);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                switch (status){
                    case 1: // Регистрация
                        int kostyl1 = 0; // 0 -- sucseed 1 -- Пользователь существует
                        Cursor cursor1 = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                        if (cursor1.moveToFirst()) {
                            int nameIndex = cursor1.getColumnIndex(DBHelper.KEY_NAME);
                            int passIndex = cursor1.getColumnIndex(DBHelper.KEY_PASS);
                            do {
                                if(cursor1.getString(nameIndex).equals(login.getText().toString()))
                                {
                                    kostyl1 = 1; // Пользователь существует
                                }
                            } while (cursor1.moveToNext());
                        } else
                            Log.d("mLog","0 rows");

                        cursor1.close();
                        switch (kostyl1) {
                            case 0: contentValues.put(DBHelper.KEY_NAME, login.getText().toString());
                                    contentValues.put(DBHelper.KEY_PASS, pass.getText().toString());
                                    contentValues.put(DBHelper.KEY_ROLE, 0);

                                    database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                                    Intent i2 = new Intent(MainActivity.this, UserActivity.class);
                                    i2.putExtra("login", login.getText());
                                    startActivity(i2);
                                    break;
                            case 1:
                                Toast.makeText(getApplicationContext(),"Пользователь с таким именем уже существует", Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                    case 2: // Авторизация
                        int kostyl = 0; // 0 -- default 1 -- user succseed 2 -- admin succseed
                        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                        if (cursor.moveToFirst()) {
                            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                            int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASS);
                            int roleIndex = cursor.getColumnIndex(DBHelper.KEY_ROLE);
                            do {
                                if(cursor.getString(nameIndex).equals(login.getText().toString()) && cursor.getString(passIndex).equals(pass.getText().toString()))
                                {
                                    if(cursor.getInt(roleIndex)==0)
                                        kostyl = 1;
                                    else
                                        kostyl = 2;
                                }
                         /*       Log.d("mLog", //"ID = " + cursor.getInt(idIndex) +
                                        ", name = " + cursor.getString(nameIndex)
                                        //+
                                        // ", email = " + cursor.getString(emailIndex)
                                );*/
                            } while (cursor.moveToNext());
                        } else
                            Log.d("mLog","0 rows");

                        cursor.close();
                        switch (kostyl) {
                            case 1:  Intent i = new Intent(MainActivity.this, UserActivity.class);
                            i.putExtra("login", login.getText());
                            i.putExtra("role", 0);
                            startActivity(i);
                            break;
                            case 2:  Intent i1 = new Intent(MainActivity.this, AdminActivity.class);
                                i1.putExtra("login", login.getText());
                                startActivity(i1);
                                break;
                            default:   Toast.makeText(getApplicationContext(), "Пользователя с таким паролем не существует", Toast.LENGTH_LONG).show();
                            break;

                        }
                        break;

                }
                dbHelper.close();
                //Intent i = new Intent(MainActivity.this,AdminActivity.class);
                //startActivity(i);


            }
        });
    }
}