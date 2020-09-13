package com.example.serious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ScrollView sv;
    LinearLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        sv = new ScrollView(this);
        dbHelper = new DBHelper(this);
        sv.addView(main);



        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASS);
            int roleIndex = cursor.getColumnIndex(DBHelper.KEY_ROLE);
            do {
                User user = new User(this,cursor.getInt(idIndex),cursor.getString(nameIndex), cursor.getString(passIndex), cursor.getLong(roleIndex));
                main.addView(user.getView());
                } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
        dbHelper.close();
        setContentView(sv);

  /*      but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASS);
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex)
                                + ", pass = " + cursor.getString(passIndex)
                        );
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
                dbHelper.close();
            }
        });*/
    }
    public class User extends View{
        long id;
        String login;
        String pass;
        long role;
        public User(Context context, long id, String login, String pass, long role) {
            super(context);
            this.login = login;
            this.pass = pass;
            this.id = id;
            this.role = role;

        }
        public LinearLayout getView (){
            LinearLayout vert = new LinearLayout(getApplicationContext());
            vert.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout hor = new LinearLayout(getApplicationContext());

            Button del = new Button(getApplicationContext());
            Button role = new Button(getApplicationContext());
            LinearLayout knop = new LinearLayout(getApplicationContext());
            knop.setOrientation(LinearLayout.VERTICAL);

            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setMinimumWidth(550);
            TextView login_tw = new TextView(getApplicationContext());
            TextView pass_tw = new TextView(getApplicationContext());
            TextView razdel = new TextView(getApplicationContext());
            razdel.setBackgroundColor(0xFF000000);
            razdel.setHeight(5);
            del.setText("x");
            del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getIntent().getCharSequenceExtra("login").toString().equals(login) )
                        Toast.makeText(getApplicationContext(),"Не могу удалить этого пользователя", Toast.LENGTH_LONG).show();
                    else
                    {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_NAME + "= \"" + login +"\"", null);
                      //  AdminActivity.super.onCreate(null);
                        hor.setBackgroundColor(0xCCFF0000);
                    }
                }
            });
            login_tw.setText("l: "+login);
            login_tw.setTextSize(30);
            pass_tw.setTextSize(30);
            pass_tw.setText("p: "+pass);
            ll.setOrientation(LinearLayout.VERTICAL);
            vert.addView(razdel);
            knop.addView(role);
            knop.addView(del);
            ll.addView(login_tw);
            ll.addView(pass_tw);
            hor.addView(ll);
            if (this.role ==1)
            {
                ImageView img = new ImageView(getApplicationContext());
                img.setImageResource(R.drawable.corona);
                hor.addView(img);
                ll.setMinimumWidth(422);
            }
            hor.addView(knop);
            vert.addView(hor);
            return vert;
        }
    }

}

