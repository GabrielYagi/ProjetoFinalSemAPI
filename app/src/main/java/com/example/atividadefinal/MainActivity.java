package com.example.atividadefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.atividadefinal.CustomAdapter.CustomAdapter;
import com.example.atividadefinal.Model.Player;
import com.example.atividadefinal.MyHelper.MyHelper;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MainActivity extends AppCompatActivity {


    Realm realm;
    EditText editName;
    Button btnSave;

    ListView listView;
    MyHelper helper;
    RealmChangeListener realmChangeListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variáveis
        realm = Realm.getDefaultInstance();
        editName = findViewById(R.id.edit_name);
        btnSave = findViewById(R.id.btn_save);
        listView = findViewById(R.id.lv);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });

        helper = new MyHelper(realm);
        helper.selectFromDB();

        CustomAdapter adapter = new CustomAdapter(this, helper.justRefresh());
        listView.setAdapter(adapter);

        Refresh();

    }
    //Salva no Banco
    private void saveData(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                //Number maxId = bgRealm.where(Player.class).max("player_id");

                //int newKey = (maxId == null) ? 1 : maxId.intValue()+1;


                                                               // , newKey
                Player players = bgRealm.createObject(Player.class);

                players.setPlayer_id(UUID.randomUUID().toString());
                players.setPlayer_name(editName.getText().toString());
                players.setPlayer_level("10");


            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Se der tudo certo
                Toast.makeText(MainActivity.this, "Sucesso",Toast.LENGTH_LONG).show();


            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Caso ocorra alagum erro
                Toast.makeText(MainActivity.this, "Erro",Toast.LENGTH_LONG).show();

            }
        });


    }

    private void Refresh(){

        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, helper.justRefresh());
                listView.setAdapter(adapter);
            }
        };
        realm.addChangeListener(realmChangeListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
