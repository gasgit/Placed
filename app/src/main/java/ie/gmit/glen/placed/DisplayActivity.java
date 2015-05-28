package ie.gmit.glen.placed;

/*
* my activity to display the results of the call to the server
*
*  geo fix -9.074702 53.262621
* */


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Looper;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


public class DisplayActivity extends ListActivity implements LocationListener
{

    // public list places for listview
    public List<Places> dislplayPlacesList;
    // create single place object to store data for display singe activity
    public static Places singlePlace = new Places();
    // breate bitmap to store image streamed from server
    public static Bitmap myBmp;
    // get location off providers
    private LocationManager locationManager;
    private Location  location;
    private String provider;
    // global lat/lng variables
    public float lat;
    public float lng;


    // create latlng JSONObj to create json string to post location to server with current location
    // to query db using geofence to return results within selected area
    public JSONObject latLngJObj = new JSONObject();


    // test using the emulator
    private static final String MY_SERVER_IMAGES = "http://10.0.2.2:5000/static/img/";
    public static final String GET_PLACES = "http://10.0.2.2:5000/getPlaces/";
    public static final String POST_LATLNG = "http://10.0.2.2:5000/postLatLng/";

    // test using device - check ip address
//     private static final String MY_SERVER_IMAGES = "http://192.168.1.101:5000/static/img/";
//     public static final String GET_PLACES = "http://192.168.1.101:5000/getPlaces/";
//     public static final String POST_LATLNG = "http://192.168.1.101:5000/postLatLng/";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        /**************************  Location Manager ****************************************/



        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> uses default
        Criteria criteria = new Criteria();
        // gets provider from lm
        provider = locationManager.getBestProvider(criteria, false);
        // get location from lm
        location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null)
        {
            // pass location on to onLocation changed method
            onLocationChanged(location);
        }

        // start check background task
        new check().execute();
        // check if device is connected
        isOnLine();

    }


    @Override
    protected void onResume() {
        super.onResume();
        // get location updates
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }


    // get lat, lng from location into variables
    @Override
    public void onLocationChanged(Location location) {
         lat = (float) location.getLatitude();
         lng = (float) location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /*****************************     End Location     *****************************************/

    /*****************************     check if online      *************************************/


    // check device is connected, if not inform user with toast
    protected boolean isOnLine()
    {
        // create cm to check if device is online, if not inform user with toast
        ConnectivityManager myCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = myCM.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Not online at moment :(", Toast.LENGTH_LONG).show();
            return false;

        }

    }

    /*****************************     end if online      *************************************/





    /*****************************     post to server      *************************************/


    // post current location and query db for entries within n kms
    private HttpResponse post() throws IOException
    {

        HttpResponse httpresponse = null;
        try {
            // create httpclient
            HttpClient httpclient = new DefaultHttpClient();
            // set the address for post
            HttpPost httppost = new HttpPost(POST_LATLNG);
            // select statement using the  Haversine formula to calculate the great-circle distance between two points
            // using lat and lng in formula, using the geoFence variable to set distance required around and limiting to 20 results
            String str = " SELECT *, ( 6371 * acos( cos( radians(" + String.valueOf(lat) + ") ) * cos( radians( lat ) ) "
                    + "* cos( radians( lng ) - radians(" + String.valueOf(lng) + ") ) + sin( radians(" + String.valueOf(lat)
                    + " ) ) * sin( radians( lat ) ) ) ) AS distance FROM places "
                    + "HAVING distance < " + String.valueOf(PlacedGlobal.geoFence)
                    + " ORDER BY distance LIMIT 30 ";
            // creating json string by adding header and query to json object
            latLngJObj.put("data",str);
            Log.d("Select ",str);

            // add latLngJObj to string to the string entity
            StringEntity se = new StringEntity(latLngJObj.toString());
            // setting content header for string entity
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //posting se
            httppost.setEntity(se);

            // post
            httpresponse = httpclient.execute(httppost);

        } catch (IOException e) {

            cannotConnectServer();

            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return httpresponse;




    }

    /*****************************     end post to server      *************************************/





    /*****************************    make connection call      *************************************/


    // check if i have content from server, if not inform user. Connection timeout is in HTTPManager
    // and response is quick for now
    private void  makeConnections()
    {
        try
        {
            post();

            requestData(GET_PLACES);


        }
        catch (Exception e)
        {

            cannotConnectServer();

            //  Toast.makeText(getApplicationContext(), "Cannot connect to the server :( ", Toast.LENGTH_LONG).show();

        }
    }

    /*****************************  end  make connection call      *************************************/




    /*****************************   background task to make connection      *************************************/


    // start on background process
    class check extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {


            makeConnections();
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }


    /*****************************   background task to make connection      ************************/



    /*****************************   cannot connect thread     *************************************/


    // runOnUiThread to push toast to ui, called from a try/catch of method post() which is on a background thread
    // toast cannot be called on background thread. Thread sleep set to 2000(approx Toast.LENGTH_SHORT)
    private void cannotConnectServer() {

        new Thread() {
            public void run()
            {

                try
                {
                    runOnUiThread(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Connot connect to server :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Thread.sleep(2000);

                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);


                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                }

        }.start();
    }

    /*****************************  end cannot connect thread     *************************************/




    // not used here
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display, menu);
        return super.onCreateOptionsMenu(menu);


    }



    // not used here
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


       return super.onOptionsItemSelected(item);

    }





    /*****************************   update display with data     *************************************/



    // method to call view adapter and add to list adapter
    protected void updateDisplay()
    {

       PlacedGlobal.placesAdapter = new PlacesAdapter(this, R.layout.item_place3, dislplayPlacesList);
        setListAdapter(PlacedGlobal.placesAdapter);

    }


    /*****************************   end update display with data     *************************************/



    /*****************************   create new instance getPlaces    *************************************/


    //  method to create new getPlaces instance
    private void requestData(String myString)
    {
        // create new myGetPlaces object
        myGetPlaces getPlaces = new myGetPlaces();

        getPlaces.execute(myString);

    }


    /*****************************   end new instance getPlaces    *************************************/


    /*****************************   getPlaces  async task,   ******************************************/


    // method to make calls to HTTPManager class for json content, Parsed by PlacesJSONParser class and added
    // to the placeList, iteration over placeList allows me to get the imageUrl to request image from img folder
    // on server and add to  each place object on placeList
    private class myGetPlaces extends AsyncTask<String, String, List<Places>> {


        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected List<Places> doInBackground(String... params)
        {
            // gets json string from server returning query from     datbase
            // HttpManager connects,gets and parses to string,
            String content = HttpManager.getData(params[0]);


            try
            {   // populate displayPlaceList by parsing content with PlacesJSONParser
                dislplayPlacesList = PlacesJSONParser.parseFeed(content);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }

            // iterate over and placeList and add image to each object
                for (Places place : dislplayPlacesList) {

                    // request images from server using url to img folder and appending the img name from object
                    String imgUrl = MY_SERVER_IMAGES + place.getPhoto();

                    InputStream myIs = null;
                    try
                    {   // stream contents of imgUrl
                        myIs = (InputStream) new URL(imgUrl).getContent();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    // convert inputstream to bitmap
                    myBmp = BitmapFactory.decodeStream(myIs);

                    // when iinput stream is null, close
                    if (myIs == null) {
                        try {
                            myIs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    // add bitmap to each place object
                    place.setBitmap(myBmp);


                }
            // return completed list of objects
            return dislplayPlacesList;


        }

        // check list not empty and update listAdapter
        @Override
        protected void onPostExecute(List<Places> result)
        {
            if(dislplayPlacesList != null) {

                updateDisplay();
            }
            else
            {

                Log.d("myMessage", "Cannot connect to server");



            }

        }
    }


    /*****************************  end getPlaces  async task,   ***************************************/



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        // create single object place,  get position in placeList and get data
       // Places place = placesList.get(position);
        singlePlace = dislplayPlacesList.get(position);
        // call activity to display single entry
        Intent displaySinglePlace = new Intent(getBaseContext(),DisplaySingleActivity.class);
        startActivity(displaySinglePlace);


    }


    // terurn to main activity
    @Override
    public void onBackPressed()
    {

        if(dislplayPlacesList != null) {
            PlacedGlobal.placesAdapter.clear();
        }

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);


    }






}
