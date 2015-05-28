package ie.gmit.glen.placed;


/*
* my activity to create a new place entry by the user to save to the server
*     geo fix -9.074702 53.262621
*
*
 */

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;

import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;


public class CreatePlace extends AppCompatActivity implements LocationListener{


    // test with emulator, 10.0.2.2 as android emululated device uses 127.0.0.1

    public static final String POST_PLACES = "http://10.0.2.2:5000/postPlaces/";
    public static final String POST_IMAGE = "http://10.0.2.2:5000/postImage/";


   // test on device - check ip address of laptop allow for firewall

//    public static final String POST_PLACES = "http://192.168.1.101:5000/postPlaces/";
//    public static final String POST_IMAGE = "http://192.168.1.101:5000/postImage/";



    private static final int IMAGE_CAPTURE_REQUEST = 1;
    // create new place object to create json string for db on post
    public Places newPlace = new Places();
    // create temp object ingPlaces to create json string to process img on webserver to static/img folder
    public Places imgPlaces = new Places();

   // public Places latLng = new Places();

    // public lat/lng variables
    public float lat;
    public float lng;
    // public string
    public String base64_image_str;
    // public byte[]
    public byte[] myByteArray;

    // public json objects for post creation
    public JSONObject newPlacesJObj = new JSONObject();
    public JSONObject newImageJObj = new JSONObject();
   // public JSONObject pubJObj3 = new JSONObject();


    // my editTexts and TextViews declared
    private TextView txtLat;
    private TextView txtLng;
    private ImageView biv;
    private EditText edTitle;
    private EditText edPlace;
    private EditText edComm;



    private final int GALLERY_REQUEST_CODE = 2222;
    // make public selected image location, provider,location manager,location and pictureObject
    private  Uri selectedImageUri;
    private String provider;
    public  LocationManager locationManager;
    private Location  location;
    private Bitmap pictureObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_place);
        //
        addListenerOnButton();

        //***************************    edit texts  and textViews *********************************

        edTitle = (EditText) findViewById(R.id.edTxtTitle);
        edPlace = (EditText) findViewById(R.id.edTxtPlace);
        edComm = (EditText) findViewById(R.id.edTxtComment);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLng = (TextView) findViewById(R.id.txtLng);



        //***************************    locationListener   ****************************************
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // get all availible providers
        locationManager.getAllProviders();

        //contains parameters for the location manager to choose the appropriate provider and parameters to compute the location
        Criteria criteria = new Criteria();
        // get provider that best suits
        provider = locationManager.getBestProvider(criteria, false);
        // get last known fix on location from provider
        location = locationManager.getLastKnownLocation(provider);



        // Initialize the location fields
        if (location != null)
        {
            //System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }
        else
        {   // warning messages for location
            txtLat.setText("Location not available");
            txtLng.setText("Location not available");

            // inform user to enable location
            Toast.makeText(getApplicationContext(), "Location not enabled on device:(" + "\n\t\tEnable in settings", Toast.LENGTH_LONG).show();

        }



    }
    // update location on activity resume
    @Override
    protected void onResume()
    {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    // Remove the location listener updates when Activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    // store lat, lng as well as displaying
    @Override
    public void onLocationChanged(Location location)
    {
        lat = (float)location.getLatitude();
        lng = (float)location.getLongitude();
        txtLat.setText(Float.valueOf(lat).toString());
        txtLng.setText(Float.valueOf(lng).toString());


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    // asking system for access to the gallery for images

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // to display selected image
        biv = (ImageView)findViewById(R.id.backGroundImageView);
        biv.setImageResource(R.drawable.ic_launcher);

        // To Handle Gallery Result
        if (data != null && requestCode == GALLERY_REQUEST_CODE)
        {


                // address of selected image
                selectedImageUri = data.getData();
                //Log.d("Hash Code", String.valueOf(selectedImageUri.hashCode()));
                String [] fileColumn = { MediaStore.Images.Media.DATA };
                // cursor gets url of images on device
                Cursor imageCursor = getContentResolver().query(selectedImageUri,
                        fileColumn, null, null, null);
                imageCursor.moveToFirst();


                int fileColumnIndex = imageCursor.getColumnIndex(fileColumn[0]);
                String picturePath = imageCursor.getString(fileColumnIndex);
                imageCursor.close();

                // decode streamed image
                pictureObject = BitmapFactory.decodeFile(picturePath);

                // resize image before compressing for http post
                pictureObject = Bitmap.createScaledBitmap(pictureObject, 200, 200, true);

                // add image url to new place object
                newPlace.setPhoto(picturePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // compress
                pictureObject.compress(Bitmap.CompressFormat.JPEG, 60, baos);

                myByteArray = baos.toByteArray();


                biv.setImageBitmap(pictureObject.createScaledBitmap(pictureObject,120,120,false));

                //convert byte array  to Base64

                 encode64(myByteArray);

        }
    }


    // encoding byte[] to base64
    private void encode64(byte [] myByteArray)
    {
        //convert picture object to Base64
        base64_image_str = Base64.encodeToString(myByteArray, Base64.DEFAULT);
    }


    // my method to create json object and string from newPlace object
    public  String createJsonNewPlace(Places newPlace)
    {

        try {

            newPlacesJObj.put("title",newPlace.getTitle());
            newPlacesJObj.put("place",newPlace.getPlace());
            newPlacesJObj.put("comments", newPlace.getComments());
            newPlacesJObj.put("lat", newPlace.getLat());
            newPlacesJObj.put("lng",newPlace.getLng());
            newPlacesJObj.put("photo", newPlace.getPhoto());




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    // create object imgPlaces
    // create json object to create json string for image post
    // create json string with photoPost and base64 string for fast network transfer
    // string decoded on server, id added and placed in img folder
    public  String createJsonImages(Places imgPlaces)
    {

        try {



            String photoPost = newPlace.getPhoto();
            imgPlaces.setPhoto(photoPost);
            imgPlaces.setImage(base64_image_str);

            newImageJObj.put("photoPost", imgPlaces.getPhoto());

            newImageJObj.put("imagePost", imgPlaces.getImage());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //  post method called in postPlaces Async, posts content to db
    private HttpResponse post() throws IOException
    {

        HttpResponse httpresponse = null;
        try {
            // new instance of httpclient
            HttpClient httpclient = new DefaultHttpClient();
            //
            HttpPost httppost = new HttpPost(POST_PLACES);
            // create json string for posting  images
            StringEntity se = new StringEntity(newPlacesJObj.toString());
            // set content header as json for server
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            // post string entity
            httppost.setEntity(se);

            httpresponse = httpclient.execute(httppost);
        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Post Unsuccessful :( ", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }


        return httpresponse;


    }

    // posts image to server, image decoded, id added and written to ing folder
    private HttpResponse postPicHttp() throws IOException
    {

        HttpResponse httpresponse = null;
        try {

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(POST_IMAGE);

        StringEntity se = new StringEntity(newImageJObj.toString());

        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        httppost.setEntity(se);

         httpresponse = httpclient.execute(httppost);

        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Post Unsuccessful :( ", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

        return httpresponse;




    }

    //***************************    locationListener   *****************************************



    //***************************    post image async    *****************************************

    class  postImage extends AsyncTask<String, String, String>
    {
        // create background job off ui
        @Override
        protected String doInBackground(String... params)
        {
            try {
                postPicHttp();
            } catch (IOException e)
            {
                e.printStackTrace();
            }return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }


    // my post places async task, background job off ui
    class postPlaces extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            try {
                post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // my method for button listener keeping the on create freed up
    public void addListenerOnButton()
    {
        //***************************   button create   ****************************************
        // my button to get text from EditTexts and set to myPlace object
        // location manager to get lat lng
        Button btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {


                // set to newplace object from editTexts
                newPlace.setTitle(edTitle.getText().toString());
                newPlace.setPlace(edPlace.getText().toString());
                newPlace.setComments(edComm.getText().toString());
                newPlace.setLat(String.valueOf(lat));
                newPlace.setLng(String.valueOf(lng));

                // replacing the local address of the image
                // get hashcode for unique to device, append .jpg for image name
                newPlace.setPhoto(String.valueOf(selectedImageUri.hashCode()) + ".jpg");

                // add to json objects for json string creation
                createJsonNewPlace(newPlace);
                createJsonImages(imgPlaces);

                // call background methods
                new postPlaces().execute();
                new postImage().execute();
                // clear textfields and imageview
                edTitle.setText("");
                edComm.setText("");
                edPlace.setText("");
                biv.setImageBitmap(null);



            }
        });// end create button

        //**************************^^  button create   ^^****************************************



        //***************************  button select image  **********************************************

        // my button to select image from gallery to add to post
        final Button btnChooseImage = (Button) findViewById(R.id.btnChooseImg);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);



            }
        });
        //*************************^^  button select image  ^^********************************************


        //*************************VV  button take image  VV********************************************


        // my button to create use the camera to take new picture
        final Button btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                // let camera intent do its thing, ill replace the image id with hashcode
                // system creates the location id on device
                String imageFileName= UUID.randomUUID().toString() + ".jpg";
                ContentValues contentValues=new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE,imageFileName);
                Uri mImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT,mImageURI);
                try {
                    startActivityForResult(i,IMAGE_CAPTURE_REQUEST);
                }
                catch (  ActivityNotFoundException e) {
                    Log.d("Mo Camera","Could not start a Camera Intent");
                }

            }
        });
        //*************************^^  button take image  ^^********************************************



    }

    @Override
    public void onBackPressed()
    {

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }




}



