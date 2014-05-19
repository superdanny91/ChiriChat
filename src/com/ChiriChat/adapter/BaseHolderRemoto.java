package com.ChiriChat.adapter;/**
 * Created by neosistec on 19/05/2014.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ChiriChat.R;
import com.ChiriChat.model.Mensajes;

/**
 * @author Danny Riofrio Jimenez
 */
public class BaseHolderRemoto implements BaseHolder{

    private ImageView image;
    private TextView textView;

    public BaseHolderRemoto(View v) {
        getView(v);
    }

    @Override
    public void getView(View v) {

        image = (ImageView) v.findViewById(R.id.imagenRemoto);
        textView = (TextView) v.findViewById(R.id.MensajeRemoto);

    }

    @Override
    public void serContent(Object o) {
        Mensajes mensaje = (Mensajes) o;
        //si los contactos tiene imagen editarla aqui
        textView.setText(mensaje.toString());

    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}

