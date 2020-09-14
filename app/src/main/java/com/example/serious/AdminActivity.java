package com.example.serious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AdminActivity extends AppCompatActivity {
    DBHelper dbHelper;
    ScrollView sv;
    LinearLayout main;
    int kategory;
    EditText name;
    EditText cost;
    EditText kolvo;
    // категории 0 -- users, 1 -- tovars
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        jojo(0);
    }

    public void jojo (int cats)
    {
        kategory = cats;
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        sv = new ScrollView(this);
        dbHelper = new DBHelper(this);

        LinearLayout bot_menu = new LinearLayout(getApplicationContext());
        bot_menu.setMinimumHeight(80);
        bot_menu.setMinimumWidth(MATCH_PARENT);
        bot_menu.setGravity(Gravity.CENTER);

        LinearLayout vertLL = new LinearLayout(getApplicationContext());
        vertLL.setOrientation(LinearLayout.VERTICAL);

        TextView menu_users = new TextView(getApplicationContext());
        menu_users.setText("Users");
        menu_users.setGravity(Gravity.CENTER);
        menu_users.setHeight(80);
        menu_users.setBackgroundColor(0x220000FF);
        menu_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kategory!=0) {
                    jojo(0);
                }
                else

                    Toast.makeText(getApplicationContext(),"Ты дегенерат!", Toast.LENGTH_LONG).show();
            }
        });
        TextView menu_tovs = new TextView(getApplicationContext());
        menu_tovs.setText("Tovs");
        menu_tovs.setGravity(Gravity.CENTER);
        menu_tovs.setHeight(80);
        menu_tovs.setBackgroundColor(0x220000FF);
        menu_tovs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kategory!=1)
                    jojo(1);
            }
        });
        TextView client = new TextView(getApplicationContext());
        client.setText("Перейти к клиенту");
        client.setGravity(Gravity.CENTER);
        client.setHeight(80);
        client.setBackgroundColor(0x220000FF);
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, UserActivity.class );
                i.putExtra("login", getIntent().getCharSequenceExtra("login"));
                i.putExtra("role", 1);
                startActivity(i);
            }
        });


switch(cats) {
    case 0:
    SQLiteDatabase database = dbHelper.getWritableDatabase();
    Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

    if (cursor.moveToFirst()) {
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASS);
        int roleIndex = cursor.getColumnIndex(DBHelper.KEY_ROLE);
        do {
            User user = new User(this, cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(passIndex), cursor.getLong(roleIndex));
            main.addView(user.getView());
        } while (cursor.moveToNext());
    } else
        Log.d("mLog", "0 rows");

    cursor.close();
    dbHelper.close();
    break;

    case 1 : // Товары
        name = new EditText(getApplicationContext());
        name.setHint("Название");
        kolvo = new EditText(getApplicationContext());
        kolvo.setHint("Количество");
        cost = new EditText(getApplicationContext());
        cost.setHint("Цена");
        Button add = new Button(getApplicationContext());
        add.setText("Add");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                        int kostyl1 = 0; // 0 -- sucseed 1 -- Пользователь существует
                        Cursor cursor1 = database.query(DBHelper.TABLE_TOVS, null, null, null, null, null, null);

                        if (cursor1.moveToFirst()) {
                            int nameIndex = cursor1.getColumnIndex(DBHelper.KEY_NAME);
                            do {
                                if(cursor1.getString(nameIndex).equals(name.getText().toString()))
                                {
                                    kostyl1 = 1; // Пользователь существует
                                }
                            } while (cursor1.moveToNext());
                        } else
                            Log.d("mLog","0 rows");

                        cursor1.close();
                        switch (kostyl1) {
                            case 0: contentValues.put(DBHelper.KEY_NAME, name.getText().toString());
                                contentValues.put(DBHelper.KEY_KOLVO, kolvo.getText().toString());
                                contentValues.put(DBHelper.KEY_COST, cost.getText().toString());

                                database.insert(DBHelper.TABLE_TOVS, null, contentValues);
                                jojo(1);
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(),"Товар с таким именем уже существует", Toast.LENGTH_LONG).show();
                                break;
                        }
            }
        });
        Tovar t = new Tovar(getApplicationContext(), 1 ,"Kapusta",100,12);
        SQLiteDatabase database1 = dbHelper.getWritableDatabase();
        Cursor cursor1 = database1.query(DBHelper.TABLE_TOVS, null, null, null, null, null, null);

        if (cursor1.moveToFirst()) {
            int idIndex = cursor1.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor1.getColumnIndex(DBHelper.KEY_NAME);
            int kolvoIndex = cursor1.getColumnIndex(DBHelper.KEY_KOLVO);
            int costIndex = cursor1.getColumnIndex(DBHelper.KEY_COST);
            do {
                Tovar tovar = new Tovar(this, cursor1.getInt(idIndex), cursor1.getString(nameIndex), cursor1.getLong(kolvoIndex), cursor1.getLong(costIndex));
                main.addView(tovar.getView());
            } while (cursor1.moveToNext());
        } else
            Log.d("mLog", "0 rows");

        cursor1.close();
        dbHelper.close();
        main.addView(name);
        main.addView(kolvo);
        main.addView(cost);
        main.addView(add);
        main.addView(t.getView());


}
       bot_menu.addView(menu_users);
        bot_menu.addView(menu_tovs);
        bot_menu.addView(client);

        sv.addView(main);

        vertLL.addView(bot_menu);
        vertLL.addView(sv);
        setContentView(vertLL);
    }
    public class User extends View{
        long id;
        String login;
        String pass;
        long role1;
        public User(Context context, long id, String login, String pass, long role) {
            super(context);
            this.login = login;
            this.pass = pass;
            this.id = id;
            this.role1 = role;

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
            role.setText("ch role");
            del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getIntent().getCharSequenceExtra("login").toString().equals(login) )
                        Toast.makeText(getApplicationContext(),"Не могу изменить роль этого пользователя", Toast.LENGTH_LONG).show();
                    else
                    {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_NAME + "= \"" + login+ "\"", null);
                        jojo(0);
                    }
                }
            });
            role.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues contentValues = new ContentValues();
                    if (role1==1)
                    contentValues.put(DBHelper.KEY_ROLE, 0);
                    else
                    contentValues.put(DBHelper.KEY_ROLE, 1);

                    if(getIntent().getCharSequenceExtra("login").toString().equals(login) )
                        Toast.makeText(getApplicationContext(),"Не могу удалить этого пользователя", Toast.LENGTH_LONG).show();
                    else
                    {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_NAME + "= \"" + login + "\"", null);
                       jojo(0);
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
            if (this.role1 ==1)
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





    public class Tovar extends View{
        long id;
        String name;
        long kolvo;
        long cost;
        public Tovar(Context context, long id, String name, long kolvo, long cost) {
            super(context);
            this.name = name;
            this.cost= cost;
            this.id = id;
            this.kolvo = kolvo;

        }
        public LinearLayout getView (){
            LinearLayout vert = new LinearLayout(getApplicationContext());
            vert.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout hor = new LinearLayout(getApplicationContext());

          //  Button del = new Button(getApplicationContext());
           // Button role = new Button(getApplicationContext());
           // LinearLayout knop = new LinearLayout(getApplicationContext());
          //  knop.setOrientation(LinearLayout.VERTICAL);
            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setMinimumWidth(550);
            TextView nazv_tw = new TextView(getApplicationContext());
            TextView cost_tw = new TextView(getApplicationContext());
            TextView razdel = new TextView(getApplicationContext());
            razdel.setBackgroundColor(0xFF000000);
            razdel.setHeight(5);
           // del.setText("x");
           // role.setText("ch role");
           /* del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getIntent().getCharSequenceExtra("login").toString().equals(login) )
                        Toast.makeText(getApplicationContext(),"Не могу изменить роль этого пользователя", Toast.LENGTH_LONG).show();
                    else
                    {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_NAME + "= \"" + login+ "\"", null);
                        jojo(0);
                    }
                }
            });
            role.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues contentValues = new ContentValues();
                    if (role1==1)
                        contentValues.put(DBHelper.KEY_ROLE, 0);
                    else
                        contentValues.put(DBHelper.KEY_ROLE, 1);

                    if(getIntent().getCharSequenceExtra("login").toString().equals(login) )
                        Toast.makeText(getApplicationContext(),"Не могу удалить этого пользователя", Toast.LENGTH_LONG).show();
                    else
                    {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_NAME + "= \"" + login + "\"", null);
                        jojo(0);
                    }
                }
            });*/
            nazv_tw.setText("name: "+name);
            nazv_tw.setTextSize(30);
            cost_tw.setTextSize(30);
            cost_tw.setText("p: "+cost);
            ll.setOrientation(LinearLayout.VERTICAL);
            vert.addView(razdel);
            ll.addView(nazv_tw);
            ll.addView(cost_tw);
            hor.addView(ll);

            TextView kol_tw = new TextView(getApplicationContext());
            kol_tw.setText("Количество\n"+kolvo);
            hor.addView(kol_tw);
            //ll.setMinimumWidth(422);
            vert.addView(hor);
            return vert;
        }
    }

}

