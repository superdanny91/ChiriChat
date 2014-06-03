package com.ChiriChat.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ChiriChat.Language.Language;
import com.ChiriChat.R;
import com.ChiriChat.SQLiteDataBaseModel.BDSQLite;
import com.ChiriChat.SQLiteDataBaseModel.GestionBaseDatosContactos;
import com.ChiriChat.model.Contactos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by danny on 30/05/2014.
 */
public class Register extends Activity {

    private static final String ID_USER = "idUsuario";
    private static final String NOMBRE = "nombre";
    private static final String TELEFONO = "telefono";
    private static final String ESTADO = "estado";
    //
    private static final String LANGUAGE = "language";
    private Locale myLocale;

    private EditText nombre;
    private EditText telefono;
    
    BDSQLite bd;
    private SQLiteDatabase baseDatos;
    private SQLiteDatabase baseDatosL;
    private GestionBaseDatosContactos GBDContactos = new GestionBaseDatosContactos();
    
    private Contactos miContacto= new Contactos();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getLanguage();


        nombre = (EditText) findViewById(R.id.login_usuario_edit);
        telefono = (EditText) findViewById(R.id.login_telefono_edit);
        bd = new BDSQLite(this, null);
        baseDatos = bd.getWritableDatabase();
        baseDatosL = bd.getReadableDatabase();
        
        
        
        miContacto = GBDContactos.devolverMiContacto(baseDatosL);

        if ((miContacto instanceof Contactos) && miContacto.getTelefono() != 0){
            iniciarActividadPrincipal();
        }



    }

    public void registrar(View v){

        if (nombre.getText().length() >= 0 && telefono.getText().length() == 9){

            String nombreUsuario = nombre.getText().toString();
            int telef = Integer.parseInt(telefono.getText().toString());
            //saveUsuario(1,nombe,telef,":)");
            GBDContactos.insertarMiUsuario(baseDatos,nombreUsuario, telef);
            iniciarActividadPrincipal();
        }

    }

//    private void saveUsuario(int idUsuario, String nombre, int telefono, String estado) {
//        SharedPreferences prefs = getSharedPreferences(
//                Register.class.getSimpleName(),
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        Contactos contacto = new Contactos(idUsuario,nombre,estado,telefono);
//
//        JSONObject json = new JSONObject();
//        try {
//            json.put("Id", contacto.getId());
//            json.put("Nombre", contacto.getNombre());
//            json.put("Estado", contacto.getEstado());
//            json.put("Telefono", contacto.getTelefono());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        editor.putString("Usuario", json.toString());
//
//        Log.d("JSON del usuario registrado", json.toString());
//
//        editor.commit();
//    }

    /**
     * Metodo que me devuelve el usuario registrado.
     * El usuario se almaceno en el fichero de propiedades en formato JSON. Primero recuperamos el JSON y a partir
     * de el objeto JSON construimos el objeto.
     * @return
     */
//    private Contactos getUsuario(){
//        Contactos myContacto = null;
//            SharedPreferences prefs = getSharedPreferences(
//                    Register.class.getSimpleName(),
//                    Context.MODE_PRIVATE);
//
//        String json = prefs.getString("Usuario","");
//        JSONObject usuario;
//
//        try {
//            usuario = new JSONObject(json);
//            myContacto = new Contactos(usuario);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (myContacto instanceof Contactos) {
//            Log.d("Contacto recuperardo con JSON",myContacto.toString());
//           return myContacto;
//        }
//
//        return null;
//    }

    /**
     * Metoto que conprueba que ya exista registro del dispositivo
     */
    public void iniciarActividadPrincipal(){

       // Contactos contacto = getUsuario();
    //    if (contacto instanceof Contactos){
            Intent intent = new Intent(this, ListChats.class);
            startActivity(intent);
            this.finish();
    //    }

    }

    public void getLanguage(){
        SharedPreferences prefs = getSharedPreferences(
                Register.class.getSimpleName(),
                Context.MODE_PRIVATE);
        String language = prefs.getString(LANGUAGE,"");

        Log.d("PREFERS", prefs.getAll().toString());
        Log.d("Idioma ---->", language);
//        setLocal(language);
        Language.setLocal(language);
        startActivity(this.getIntent());

    }

    /**
     * Metodo que cambiara el leguage actual de la app
     * @param lang
     */
    private void setLocal(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

}