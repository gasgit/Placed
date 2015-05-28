package ie.gmit.glen.placed;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class Favourites extends ListActivity {


    // create singlePlaceFav object to store content for display favourite single
    public static Places singlePlaceFav = new Places();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        updateDisplay();
    }

    protected void updateDisplay()
    {
        // clear list adapter to prevent duplication
        setListAdapter(null);
        // create favourites adapter and add PlacedGlobal favlist, favlist created in
        // PlacesDbHandler from query to on device sqlite db. myGlobals created to store content that
        // needs to pass between activities.
        PlacedGlobal.favouritesAdapter = new FavouritesAdapter(this, R.layout.item_favourites,  PlacedGlobal.favList);
        setListAdapter(PlacedGlobal.favouritesAdapter);

    }
    // select item on list
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // create single object place,  get position in placeList and get data
        singlePlaceFav = PlacedGlobal.favList.get(position);
        // call activity to display single entry
        Intent displaySinglePlaceFav = new Intent(getBaseContext(),FavouriteSingle.class);

        // start displatSinglePlaceFav activity to display the selected from list
        startActivity(displaySinglePlaceFav);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // return to main activity/menu
    @Override
    public void onBackPressed()
    {

        if(PlacedGlobal.favList != null) {
            PlacedGlobal.favouritesAdapter.clear();
        }

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }
}
