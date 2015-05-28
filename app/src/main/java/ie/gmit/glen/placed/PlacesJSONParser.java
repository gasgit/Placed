package ie.gmit.glen.placed;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by glen on 17/02/15.
 */
public class PlacesJSONParser {




    public static List<Places> parseFeed(String content) {



        try {
            // my new jsonObject from feed
            JSONObject jsonObject = new JSONObject(content);
            // my new json array from jsonObject using places tag/node
            JSONArray ar = jsonObject.getJSONArray("places");
            // my placeList of place objects from parsed JSONArray
            List<Places> placesList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++)
            {

                JSONObject  obj = ar.getJSONObject(i);
                // my place object
                Places place = new Places();

                // my abstraction from JSONObject and insertion to place object
                place.setComments(obj.getString("comments"));
                place.setId(obj.getString("id"));
                place.setLat(obj.getString("lat"));
                place.setLng(obj.getString("lng"));
                place.setPhoto(obj.getString("photo"));
                place.setPlace(obj.getString("place"));
                place.setTitle(obj.getString("title"));
                place.setPosted(obj.getString("posted"));
              // place.setImage(obj.getString("image"));



                // my adding place objects into placeList array
                placesList.add(place);

            }

            return placesList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}