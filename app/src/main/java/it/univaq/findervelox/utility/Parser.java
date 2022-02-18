package it.univaq.findervelox.utility;

import org.json.JSONException;
import org.json.JSONObject;

import it.univaq.findervelox.model.Autovelox;

public class Parser {

    public static Autovelox parseAutovelox(JSONObject json) {

        /*
        [{
            "id": 1,
            "name": "Leanne Graham",
            "username": "Bret",
            "email": "Sincere@april.biz",
            "address": {
              "street": "Kulas Light",
              "suite": "Apt. 556",
              "city": "Gwenborough",
              "zipcode": "92998-3874",
              "geo": {
                "lat": "-37.3159",
                "lng": "81.1496"
              }
            },
            "phone": "1-770-736-8031 x56442",
            "website": "hildegard.org",
            "company": {
              "name": "Romaguera-Crona",
              "catchPhrase": "Multi-layered client-server neural-net",
              "bs": "harness real-time e-markets"
            }
          }
         */
        try {
            Autovelox autovelox = new Autovelox();
            autovelox.setId(Long.parseLong(json.getString("cidentificatore_in_openstreet")));
            autovelox.setCity(json.getString("ccomune"));
            autovelox.setProvince(json.getString("cprovincia"));
            autovelox.setRegion(json.getString("cregione"));
            autovelox.setDateTimeInsertion(Long.parseLong(json.getString("cdata_e_ora_inserimento")));
            try {
                autovelox.setLatitude(json.getDouble("clatitudine"));
                autovelox.setLongitude(json.getDouble("clongitudine"));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return autovelox;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
