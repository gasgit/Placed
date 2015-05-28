package ie.gmit.glen.placed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class FavouritesAdapter extends ArrayAdapter<Places> {

    private Context context;

    //
    public FavouritesAdapter(Context context, int resource, List<Places> objects) {
        super(context, resource, objects);
        this.context = context;
        PlacedGlobal.favList = objects;

    }


    // create view to display selected item from favourites lis
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // my view displaying the layout from the item_place3 xml file inserted to each listView entry
        //View view = inflater.inflate(R.layout.item_place, parent, false);
        View favView = inflater.inflate(R.layout.item_favourites, parent, false);


        // my place object to get from placeList
        //Places place = favList.get(position);
        Places favPlace = PlacedGlobal.favList.get(position);


        // my inner views for view
        TextView tv1 = (TextView) favView.findViewById(R.id.lblTitleFav);
        TextView tv2 = (TextView) favView.findViewById(R.id.lblPlaceFav);
        TextView tv3 = (TextView) favView.findViewById(R.id.lblPostedFav);
        TextView tv4 = (TextView) favView.findViewById(R.id.txtTitleFav);
        TextView tv5 = (TextView) favView.findViewById(R.id.txtPlaceFav);
        TextView tv6 = (TextView) favView.findViewById(R.id.txtPostedFav);


        //   ImageView iv = (ImageView) view.findViewById(R.id.imageViewFav);
//
//        // my getting details from place object to display in inner views
//        // tv1.setText(place.getId());
        tv1.setText("Title");
        tv2.setText("Place");
        tv3.setText("Posted");
        tv4.setText(favPlace.getTitle());
        tv5.setText(favPlace.getPlace());
        tv6.setText(favPlace.getPosted());
//
//        iv.setImageBitmap(place.getBitmap());


        // return view;
        return favView;



    }
}
