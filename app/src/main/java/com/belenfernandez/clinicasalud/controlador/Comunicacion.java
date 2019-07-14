package com.belenfernandez.clinicasalud.controlador;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.belenfernandez.clinicasalud.interfaces.ManejadorRespuestaJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comunicacion {

    public static void solicitudJSONObject(Context ctx, String url, JSONObject json, final ManejadorRespuestaJSON manejador, final int request) {

        // solicitar. creo el objeto peticion
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // aqui gestiono la respuesta correcta
                        Log.d("XXX","OK: " + response);
                        //Comunicacion recibe la respuesta y procesarRespuetaJSONObject delega el tratamiento de la respuesta
                        manejador.procesarRespuestaJSONObject(response, request);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // aqui gestiono la respuesta si hubo error
                Log.d("XXX","Error: " + error.getMessage());
            }
        });
        //ejecuta la peticion
        RequestQueueSingleton.getInstance(ctx).getRequestQueue().add(jsonObjectRequest);
    }
    public static void solicitudJSONArray(Context ctx, String url, JSONObject json, final ManejadorRespuestaJSON manejador, final int request) {
        // creo el objeto peticion
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // aqui gestiono la respuesta correcta
                        try {
                            // La clave es que aqui se sabe que los datos de la respuesta son un jsonarray
                            // y no un jsonobject
                            // con esta linea se obtiene un jsonarray de un campo de jsonobject
                            JSONArray jsonArray = response.getJSONArray("datos");
                            Log.d("XXX", "OK: " + response);
                            manejador.procesarRespuestaJSONOArray(jsonArray, request);
                        } catch (JSONException js) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // aqui gestiono la respuesta si hubo error
                Log.d("XXX","Error: " + error.getMessage());
            }
        });
        //ejecuta la peticion
        RequestQueueSingleton.getInstance(ctx).getRequestQueue().add(jsonObjectRequest);
    }
}
