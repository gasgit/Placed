package ie.gmit.glen.placed;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;


/*
*   geo fix -9.074702 53.262621
*
* */

public class DisplaySingleActivity extends AppCompatActivity {

    public Places placeTOdb = new Places();
    public Places reportObjPlaces = new Places();

    public JSONObject reportObj = new JSONObject();
    public static final String POST_REPORT = "http://10.0.2.2:5000/postReport/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_single);


        // textViews as labels
        TextView idLbl = (TextView)findViewById(R.id.lblId);
        idLbl.setText("Id: ");
        TextView titleLbl = (TextView)findViewById(R.id.lblTitle);
        titleLbl.setText("Title: ");
        TextView placeLbl = (TextView)findViewById(R.id.lblPlace);
        placeLbl.setText("Place:");
        TextView commentLbl =(TextView)findViewById( R.id.lblComments);
        commentLbl.setText("Comments:");
        TextView latLbl = (TextView)findViewById(R.id.lblLat);
        latLbl.setText("Latitude:");
        TextView lngLbl = (TextView)findViewById(R.id.lblLng);
        lngLbl.setText("Longitude");
        TextView postedLbl = (TextView)findViewById(R.id.lblPosted);
        postedLbl.setText("Posted");



        // textViews as content
        TextView tv1 = (TextView) findViewById(R.id.lrgTxId);
        TextView tv2 = (TextView) findViewById(R.id.lrgTxTitle);
        TextView tv3 = (TextView) findViewById(R.id.lrgTxPlaces);
        TextView tv4 = (TextView) findViewById(R.id.lrgTxComments);
        TextView tv5 = (TextView) findViewById(R.id.lrgTxLat);
        TextView tv6 = (TextView) findViewById(R.id.lrgTxLng);
        TextView tv7 = (TextView) findViewById(R.id.lrgTxtPosted);
        ImageView iv = (ImageView)findViewById(R.id.lrgImageView);

        tv1.setText(DisplayActivity.singlePlace.getId());
        tv2.setText(DisplayActivity.singlePlace.getTitle());
        tv3.setText(DisplayActivity.singlePlace.getPlace());
        tv4.setText(DisplayActivity.singlePlace.getComments());
        tv5.setText(DisplayActivity.singlePlace.getLat());
        tv6.setText(DisplayActivity.singlePlace.getLng());
        tv7.setText(DisplayActivity.singlePlace.getPosted());
        iv.setImageBitmap(DisplayActivity.singlePlace.getBitmap());


        placeTOdb.setTitle(tv2.getText().toString());
        placeTOdb.setPlace(tv3.getText().toString());
        placeTOdb.setComments(tv4.getText().toString());
        placeTOdb.setLat(tv5.getText().toString());
        placeTOdb.setLng(tv6.getText().toString());
        placeTOdb.setPhoto(DisplayActivity.singlePlace.getPhoto());
        placeTOdb.setPosted(tv7.getText().toString());


        reportObjPlaces.setId(DisplayActivity.singlePlace.getId());
        reportObjPlaces.setComments(DisplayActivity.singlePlace.getComments());
        reportObjPlaces.setPhoto(DisplayActivity.singlePlace.getPhoto());



        Log.d("Here", DisplayActivity.singlePlace.getId().toString());

        // my button to display results from server
        final Button buttonMap = (Button) findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Opening map at " + DisplayActivity.singlePlace.getPlace().toString(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(i);


            }
        });
        // button to add selected single displayed from placeList to db as favorites onto device
        final Button btnFav = (Button) findViewById(R.id.btnFav);
        btnFav.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                newPlace();

                Toast.makeText(getApplicationContext(), "Saved to Favourites "  , Toast.LENGTH_LONG).show();

            }
        });



        final Button btnRep = (Button) findViewById(R.id.btnReport);
        btnRep.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                createJsonReport(reportObjPlaces);

                Toast.makeText(getApplicationContext(), "Reported to Administrator "  , Toast.LENGTH_LONG).show();
                new postReport().execute();

            }
        });

    }

    public void newPlace () {


        PlacesDbHandler dbHandler = new PlacesDbHandler(this, null, null, 1);


        dbHandler.addPlace(placeTOdb);

    }

    public  String createJsonReport(Places reportObjPlaces)
    {

        try {


            reportObj.put("id", reportObjPlaces.getId());
            reportObj.put("comments", reportObjPlaces.getComments());
            reportObj.put("photo", reportObjPlaces.getPhoto());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    //  post method called in postReportIssue Async, posts content to db
    private HttpResponse postReportIssue() throws IOException
    {

        HttpResponse httpresponse = null;
        try {
            // new instance of httpclient
            HttpClient httpclient = new DefaultHttpClient();
            //
            HttpPost httppost = new HttpPost(POST_REPORT);
            // create json string for posting  images
            StringEntity se = new StringEntity(reportObj.toString());

            //Log.d("Posting Object" ,reportObj.toString());
            // set content header as json for server
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            // post string entity
            httppost.setEntity(se);
            Log.d("Reported " , reportObj.toString());

            httpresponse = httpclient.execute(httppost);
        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Post Unsuccessful :( ", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }


        return httpresponse;


    }

    class postReport extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            try {
                postReportIssue();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_single, menu);
        return true;


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
            {
                newPlace();
                Toast.makeText(getApplicationContext(), "Saved to Favourites "  , Toast.LENGTH_LONG).show();
                return true;
            }


            case R.id.favourites:
            {

               PlacesDbHandler fav = new PlacesDbHandler(this,null,null,1);


                fav.getFavourites();

                try {
                    Intent i = new Intent(getBaseContext(), Favourites.class);
                    startActivity(i);
                } catch (Exception e) {

                    Toast.makeText(getBaseContext(), "Cannot Display favourites", Toast.LENGTH_SHORT).show();
                    // e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), "Displaying favourites", Toast.LENGTH_SHORT).show();
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
