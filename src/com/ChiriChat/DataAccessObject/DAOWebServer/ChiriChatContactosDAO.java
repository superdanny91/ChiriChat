/*
* Copyright (C) 2014 Alejandro Moreno Jimenez | alejandroooo887@gmail.com
*					 Danny Riofrio Jimenez | superdanny.91@gmail.com
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>. *
*/


package com.ChiriChat.DataAccessObject.DAOWebServer;

import android.util.Log;
import com.ChiriChat.DataAccessObject.InterfacesDAO.IContactosDAO;
import com.ChiriChat.model.Contactos;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasde que extiendo de IContactosDAO, tendremos en ella los metodos CRUD y lo que
 * nos hayamos declarado el la interfaz.
 */
public class ChiriChatContactosDAO implements IContactosDAO {
    //Creo el objeto hhtpCLient para acceder a la conexion web.
    private HttpClient httpClient = new DefaultHttpClient();

    /**
     * Inserta un Contactoo en el servidor a traves de una petidion POST, crea el objeto JSON
     * para enviarlo, recupera el codigo del response, y si ha tenido exito la operacion
     * se recupera lo que el servidor devuelve, contruyo el objeto Contacto y lo devuelvo, si
     * ha avido algun error devolvera un null.
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public Contactos insert(Contactos dto) throws Exception {

        Contactos c = null;

        //Enviamos una peticion post al insert del usuario.
        HttpPost httpPostNuevoUsuario = new HttpPost("http://chirichatserver.noip.me:85/ws/insertUsuario");

        //Creo el objeto Jason con los datos del contacto que se registra en la app.
        JSONObject newUsuario = new JSONObject();
        try {
            newUsuario.put("nombre", dto.getNombre());
            newUsuario.put("telefono", dto.getTelefono());
            newUsuario.put("estado", dto.getEstado());
            newUsuario.put("id_gcm", dto.getIdgcm());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List parametros = new ArrayList();
        //Añade a la peticion post el parametro json, que contiene los datos a insertar.(json)
        parametros.add(new BasicNameValuePair("json", newUsuario.toString()));

        Log.d("JSON de insert usaurio DAO", newUsuario.toString());
        try {
            //Creamos la entidad con los datos que le hemos pasado
            httpPostNuevoUsuario.setEntity(new UrlEncodedFormEntity(parametros));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Objeto para poder obtener respuesta del server
        HttpResponse response = httpClient.execute(httpPostNuevoUsuario);
        //Obtenemos el codigo de la respuesta
        int respuesta = response.getStatusLine().getStatusCode();
        Log.w("Respueta", "" + respuesta);
        //Si respuesta 200 devuelvo mi usuario , si no devolvere null
        if (respuesta == 200) {


            //Nos conectamos para recibir los datos de respuesta
            HttpEntity entity = response.getEntity();
            //Creamos el InputStream
            InputStream is = entity.getContent();
            //Leemos el inputStream
            String temp = StreamToString(is);
            //Creamos el JSON con la cadena del inputStream
            Log.d("Cadena JSON", temp.toString());
            JSONObject jsonRecibido = new JSONObject(temp);

            Log.d("InputStreamReader", temp.toString());
            Log.d("JSON ==>", jsonRecibido.toString());

             c = new Contactos(jsonRecibido);


        }

        return c;

    }

    @Override
    public boolean update(Contactos dto) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Contactos dto) throws Exception {
        return false;
    }

    @Override
    public Contactos read(int id) throws Exception {
        return null;
    }

    /**
     * Metodo que recuperara todos los usuario del web-service, contruira los objetos
     * contactos y los almacenara en una lista, que sera la que devuelva.
     * @return
     * @throws Exception
     */
    @Override
    public List<Contactos> getAll() throws Exception {

        List<Contactos> allContacts = new ArrayList<Contactos>();

        //Enviamos una peticion post al insert del usuario.
        HttpPost httpPostNuevoUsuario = new HttpPost("http://chirichatserver.noip.me:85/ws/usuarios");


        try {
            HttpResponse response = httpClient.execute(httpPostNuevoUsuario);

            int respuesta = response.getStatusLine().getStatusCode();

            Log.d("=>>>>reponse", String.valueOf(respuesta));

            if (respuesta == 200) {


                //Nos conectamos para recibir los datos de respuesta
                HttpEntity entity = response.getEntity();
                //Creamos el InputStream
                InputStream is = entity.getContent();
                //Leemos el inputStream
                String temp = StreamToString(is);
                //Creamos el JSON con la cadena del inputStream
                Log.d("Cadena JSON", temp.toString());
                JSONArray jsonRecibido = new JSONArray(temp);

                Log.d("InputStreamReader", temp.toString());
                Log.d("JSON ==>", jsonRecibido.toString());

                for (int i = 0; i < jsonRecibido.length(); i++) {
                    Log.d("Item de la array", jsonRecibido.get(i).toString());

                    JSONObject jo = (JSONObject) jsonRecibido.get(i);

                    Contactos us = new Contactos(jo);

                    Log.d("Datos del usuario", us.toString());

                    allContacts.add(us);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return allContacts;
    }

    /**
     * Metodo que llamara al metodo @see getAll() y eliminara nuestro contacto de la lista,
     * devolvera la lista de contactos sin nuestro contacto.
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public List getAllSinMiContacto(Contactos dto) throws Exception {
        List<Contactos> contactos;

        contactos = getAll();
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getTelefono() == dto.getTelefono()) {
                Log.d("Contacto eliminado", contactos.get(i).toString());
                contactos.remove(i);
            }
        }


        return contactos;
    }

    /**
     * Metoto al que se le pasara un InputStream y devolera una cadena.
     * Este metodo es llamado para tratar las respuesta del servidor.
     * @param is
     * @return
     */
    @Override
    public String StreamToString(InputStream is) {
        //Creamos el Buffer
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            //Bucle para leer todas las lineas
            //En este ejemplo al ser solo 1 la respuesta
            //Pues no hara falta
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //retornamos el codigo limpio
        return sb.toString();
    }


}
