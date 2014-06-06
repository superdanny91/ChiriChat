package com.ChiriChat.DataAccessObject.DAOWebServer;

import android.util.Log;
import com.ChiriChat.DataAccessObject.InterfacesDAO.IConversacioneDAO;
import com.ChiriChat.model.Contactos;
import com.ChiriChat.model.Conversaciones;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ChiriChatConversacionesDAO implements IConversacioneDAO{

    //Creo el objeto hhtpCLient para acceder a la conexion web.
    private HttpClient httpClient = new DefaultHttpClient();


    @Override
    public Conversaciones insert(Conversaciones dto) throws Exception {
        //Enviamos una peticion post al insert de conversacion.
        HttpPost httpPostNuevoUsuario = new HttpPost("http://chirichatserver.noip.me:85/ws/CreateConversacion");

        //Creo el objeto Jason con los datos del contacto que se registra en la app.
        JSONObject newConver = new JSONObject();
        Log.d("","-.--------------------------------------------------------");
        try {
            newConver.put("nombre", dto.getNombre());
            newConver.put("owner", dto.getNombre());
            ArrayList<Contactos> contactos = dto.getContactos();
            JSONArray JSONContactos = new JSONArray();
            for (int i = 0; i <contactos.size() ; i++) {
                Contactos c = contactos.get(i);
                JSONObject j = new JSONObject(c.toString());
                JSONContactos.put(i,j);
            }
            newConver.put("participantes", JSONContactos);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        List parametros = new ArrayList();
        //Añade a la peticion post el parametro json, que contiene los datos a insertar.(json)
        parametros.add(new BasicNameValuePair("json", newConver.toString()));
        Log.d("JSON de conversacion", newConver.toString());

        //Objeto para poder obtener respuesta del server
        HttpResponse response = httpClient.execute(httpPostNuevoUsuario);
        //Obtenemos el codigo de la respuesta
        int respuesta = response.getStatusLine().getStatusCode();
        Log.w("Respueta Insertar conversacion server", "" + respuesta);
        //Si respuesta 200 devuelvo mi conversacion , si no devolvere null
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

            Conversaciones conver = new Conversaciones(jsonRecibido);

            return conver;

        }

        return null;
    }

    @Override
    public boolean update(Conversaciones dto) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Conversaciones dto) throws Exception {
        return false;
    }

    @Override
    public Conversaciones read(int id) throws Exception {
        return null;
    }

    @Override
    public List<Conversaciones> getAll() throws Exception {
        return null;
    }

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
