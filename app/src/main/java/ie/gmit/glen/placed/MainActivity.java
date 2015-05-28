package ie.gmit.glen.placed;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import java.util.List;
/*
*  geo fix -9.0489 53.2719
*
* */

public class MainActivity extends AppCompatActivity {
    // create an instance of dbHandler
    PlacesDbHandler fav = new PlacesDbHandler( this,null,null,1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // progress = new ProgressDialog(this);

        addListenerOnButton();


    }

    /***********************************     add listener            **********************************************/

    // my method for button listener keeping the onCreate freed up
    public void addListenerOnButton()
    {


        // my button to display results from server
        final Button btnListView = (Button) findViewById(R.id.btnDisplay);
        btnListView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {



                Intent i = new Intent(getBaseContext(), DisplayActivity.class);
                startActivity(i);

            }
        });


        /***********************************    button to create new entry            **********************************************/


        // my button to create new entry
        final Button btnCreate = (Button) findViewById(R.id.btnCreatePlace);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent i = new Intent(getBaseContext(), CreatePlace.class);
                startActivity(i);



            }
        });

        /***********************************    button to about page            **********************************************/


        final Button btnButton = (Button) findViewById(R.id.btnAbout);
        btnButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent i = new Intent(getBaseContext(), About.class);
                startActivity(i);



            }
        });

        /***********************************     button to favourites             **********************************************/



        // my button to show favourites, open the favourites listView
        final Button btnFAv = (Button) findViewById(R.id.btnFav);
        btnFAv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                if(PlacedGlobal.favouritesAdapter != null) {
                    PlacedGlobal.favouritesAdapter.clear();
                }
                Intent i = new Intent(getBaseContext(), Favourites.class);
                startActivity(i);
                new readDB().execute();





            }
        });

    }


    /***********************************     async method to read db           **********************************************/


    // doing in background task to query the db in PlaceDbHandler which will return
    // a list of place objects for favourites
    class  readDB extends AsyncTask<String, String, List<Places>>
    {

        @Override
        protected List<Places> doInBackground(String... params) {


            // call the getFavourites method on the PlacedDnHandler
            fav.getFavourites();

            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /***********************************   switch statement for geoFence menu           **********************************************/

    // select which distance to return queries from db
    // sets the global geoFence variable for the select statement in the displayActivity
    // opens toast to infom the user
    @Override
     public boolean onOptionsItemSelected (MenuItem item)
        {

            switch (item.getItemId()) {
                case R.id.oneKm: {

                    PlacedGlobal.geoFence = 1;
                    Toast.makeText(getApplicationContext(), "GeoFenced to 1 km ", Toast.LENGTH_LONG).show();

                    return true;
                }

                case R.id.twoKm: {

                    PlacedGlobal.geoFence = 2;
                    Toast.makeText(getBaseContext(), "GeoFenced to 2 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }

                case R.id.fiveKm: {

                    PlacedGlobal.geoFence = 5;
                    Toast.makeText(getBaseContext(), "GeoFenced to 5 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.tenKm: {

                    PlacedGlobal.geoFence = 10;
                    Toast.makeText(getBaseContext(), "GeoFenced to 10 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }

                case R.id.twentyKm: {

                    PlacedGlobal.geoFence = 20;
                    Toast.makeText(getBaseContext(), "GeoFenced to 20 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }

                case R.id.fiftyKm: {

                    PlacedGlobal.geoFence = 50;
                    Toast.makeText(getBaseContext(), "GeoFenced to 50 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }

                case R.id.twoHunKm: {

                    PlacedGlobal.geoFence = 200;
                    Toast.makeText(getBaseContext(), "GeoFenced to 100 km ", Toast.LENGTH_SHORT).show();
                    return true;
                }


                default:
                    break;
            }
            return super.onOptionsItemSelected(item);


        }

    // disable the back button as its the main activity
    @Override
    public void onBackPressed()
    {

        //disable back button
    }


}
