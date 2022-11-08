package com.example.turisteo.firebase;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

// Para enviar y recibir notificacion con Firebase se usa Cloud Messaging.
// Tutorial completo: https://www.youtube.com/watch?v=kZJhB7LlcOU
// 1) Se añade la dependencia en el Gradle: implementation 'com.google.firebase:firebase-messaging:23.1.0'
// 2) Se añade en el Manifest el service de notificaciones (en android:name=".firebase.MyFirebaseMessagingService" es el nombre de esta clase).
// 3) Se crea esta clase que extienda de FirebaseMessagingService y ya esta listo para recibir notificaciones en segundo plano.
// 4) Si se quiere recibir notificaciones en primer plano se realiza lo que se explica abajo en esta clase.
// Tener en cuenta que algunas veces demora unos minutos en llegar la notificación, al menos eso note las veces que probe.

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Las notificaciones por defecto llegan en segundo plano (cuando tenemos la app minimizada) y aparecen en la barra de notificaciones,
    // asi que si el usuario tiene la app abierta cuando llega una notificacion (app en primer plano) es necesario manejarla desde codigo.
    // Para eso sobreescribimos la clase onMessageReceived y mostramos la notificacion recibida en un Toast.
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null) {
            Toast.makeText(this, message.getNotification().getBody(), Toast.LENGTH_LONG).show();
        }
    }
}
