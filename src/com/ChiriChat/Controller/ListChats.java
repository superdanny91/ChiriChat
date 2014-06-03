package com.ChiriChat.Controller;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.ChiriChat.Adapter.myAdapterChats;
import com.ChiriChat.R;
import com.ChiriChat.SQLiteDataBaseModel.BDSQLite;
import com.ChiriChat.SQLiteDataBaseModel.GestionBaseDatosConversaciones;
import com.ChiriChat.model.Contactos;
import com.ChiriChat.model.Conversaciones;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by danny on 31/05/14.
 */
public class ListChats extends Activity {

    private GestionBaseDatosConversaciones GBDConversaciones = new GestionBaseDatosConversaciones();
    private ListView listViewChats;

    private myAdapterChats adapterChats;

    private Menu optionsMenu;
    private ShareActionProvider provider;

    private ArrayList<Conversaciones> allChats = new ArrayList<Conversaciones>();
    private ArrayList<Contactos> usuariosConversacion = new ArrayList<Contactos>();
    private Contactos thisContacto;

    private BDSQLite bd; // Instancia de la base de datos
    private SQLiteDatabase baseDatos; // Instancia de la base de datos escritura
    private SQLiteDatabase baseDatosL;// Instancia de la base de datos lectura
    
    Bundle extra;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_chats);
        bd= new BDSQLite(this, null);
        baseDatos = bd.getWritableDatabase();
        baseDatosL = bd.getReadableDatabase();
        
        listViewChats = (ListView) findViewById(R.id.listView_Chats);

        allChats = GBDConversaciones.recuperarConversaciones(baseDatosL,usuariosConversacion);

        Log.d("Conversaciones Lista",""+allChats.toString());

        adapterChats = new myAdapterChats(this, allChats);

        listViewChats.setAdapter(adapterChats);

        listViewChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        adapterChats.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflate = getMenuInflater();

        inflate.inflate(R.menu.menu_activity_chats, menu);

        provider = (ShareActionProvider) menu.findItem(R.id.menu_share_contactos)
                .getActionProvider();

        provider.setShareIntent(doShare());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_contacts:
                Intent intent = new Intent(this, ListContacts.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                Intent i = new Intent(this, Opciones.class);
                startActivity(i);
                this.finish();
                break;
            case R.id.menuBar_my_perfil:
                openEditPerfil();

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * +
     * Metodo que devolvera un intent para compartir.
     * Lo usa el ActioProvider
     *
     * @return
     */
    public Intent doShare() {
        // populate the share intent with data
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "This is a message for you");
        return intent;
    }


    public void openEditPerfil() {
        Intent i = new Intent(this, EditMyPerfilUser.class);
        startActivity(i);
//        extra.putString(nombre, value);
    }
}