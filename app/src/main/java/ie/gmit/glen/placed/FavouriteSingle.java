package ie.gmit.glen.placed;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FavouriteSingle extends AppCompatActivity {


    // creaate new object to
    public  Places placeTOdb = new Places();
    PlacesDbHandler fav = new PlacesDbHandler( this,null,null,1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_single);

        // textViews as labels
        TextView idLbl = (TextView)findViewById(R.id.lblIdFav);
        idLbl.setText("Id: ");
        TextView titleLbl = (TextView)findViewById(R.id.lblTitleFav);
        titleLbl.setText("Title: ");
        TextView placeLbl = (TextView)findViewById(R.id.lblPlaceFav);
        placeLbl.setText("Place:");
        TextView commentLbl =(TextView)findViewById( R.id.lblCommentsFav);
        commentLbl.setText("Comments:");
        TextView latLbl = (TextView)findViewById(R.id.lblLatFav);
        latLbl.setText("Latitude:");
        TextView lngLbl = (TextView)findViewById(R.id.lblLngFav);
        lngLbl.setText("Longitude");
        TextView postedLbl = (TextView)findViewById(R.id.lblPostedFav);
        postedLbl.setText("Posted");



        // textViews as content
        TextView tv1 = (TextView) findViewById(R.id.lrgTxIdFav);
        TextView tv2 = (TextView) findViewById(R.id.lrgTxTitleFav);
        TextView tv3 = (TextView) findViewById(R.id.lrgTxPlacesFav);
        TextView tv4 = (TextView) findViewById(R.id.lrgTxCommentsFav);
        TextView tv5 = (TextView) findViewById(R.id.lrgTxLatFav);
        TextView tv6 = (TextView) findViewById(R.id.lrgTxLngFav);
        TextView tv7 = (TextView) findViewById(R.id.lrgTxtPostedFav);
       // ImageView iv = (ImageView)findViewById(R.id.lrgImageViewFav);

        // set textview with contents from singleplaceFav object, selectet from listview in favourites
        tv1.setText(Favourites.singlePlaceFav.getId());
        tv2.setText(Favourites.singlePlaceFav.getTitle());
        tv3.setText(Favourites.singlePlaceFav.getPlace());
        tv4.setText(Favourites.singlePlaceFav.getComments());
        tv5.setText(Favourites.singlePlaceFav.getLat());
        tv6.setText(Favourites.singlePlaceFav.getLng());
        tv7.setText(Favourites.singlePlaceFav.getPosted());


        // my button to display results from server
        final Button buttonMap = (Button) findViewById(R.id.buttonMapFav);
        buttonMap.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Toast.makeText(getApplicationContext(), "Opening map at " + Favourites.singlePlaceFav.getPlace().toString(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(i);


            }
        });


        // button to delete selected single fav displayed from Globals.favList from db
        final Button btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                deletePlace();

                Toast.makeText(getApplicationContext(), "Deleted from Favourites "  , Toast.LENGTH_LONG).show();

            }
        });


    }
    // delete place method, dbHandler instance,Calls on method to delete entry from db
    // in dbHandler based on id of singlePlaceFav object
    public void deletePlace() {


        PlacesDbHandler dbHandler = new PlacesDbHandler(this, null, null, 1);

        // pass id from object
        dbHandler.deletePlace(Favourites.singlePlaceFav.getId());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_single, menu);
        return true;
    }
    // menu to delete or display favourites
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            // call delete method
            case R.id.delete:
            {
                deletePlace();
                Toast.makeText(getBaseContext(), "Deleted from device", Toast.LENGTH_SHORT).show();
                return true;
            }
            // call to the get favourite method to refresh
            case R.id.favourites:
            {
                // call in instance of dbHandler to call get favs
                PlacesDbHandler fav = new PlacesDbHandler(this,null,null,1);
                fav.getFavourites();

                if (PlacedGlobal.favouritesAdapter.getCount() == 0)
                {
                    Toast.makeText(getBaseContext(), "No favourites to display", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Intent i = new Intent(getBaseContext(), Favourites.class);
                    startActivity(i);
                    Toast.makeText(getBaseContext(), "Displaying favourites", Toast.LENGTH_SHORT).show();


                }



//                try {
//
//                } catch (Exception e) {
//
//
//                }
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed()
    {


        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);

    }
}
