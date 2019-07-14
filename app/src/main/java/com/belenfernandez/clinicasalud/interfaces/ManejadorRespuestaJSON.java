//interface que implementaran todas las activities que hagan una llamada a volley
package com.belenfernandez.clinicasalud.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ManejadorRespuestaJSON {

    void procesarRespuestaJSONObject(JSONObject response, int request);
    void procesarRespuestaJSONOArray(JSONArray response, int request);

}
