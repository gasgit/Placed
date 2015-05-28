package ie.gmit.glen.placed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by glen on 18/02/15.
 *
 *
 * my places adapter to display in the listView each attribute from  each place
 * object in the places list
 *
 *  geo fix -9.0489 53.2719
 *
 */
public class PlacesAdapter extends ArrayAdapter<Places> {

    private Context context;
    private List<Places> viewPlaceList;
    // my adapter to
    public PlacesAdapter(Context context, int resource, List<Places> objects) {
        super(context, resource, objects);
        this.context = context;
        this.viewPlaceList = objects;


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // my view displaying the layout from the item_place3 xml file inserted to each listView entry
        //View view = inflater.inflate(R.layout.item_place, parent, false);
        View view = inflater.inflate(R.layout.item_place, parent, false);


        // my place object to get from placeList
        Places place = viewPlaceList.get(position);

        // my inner views for view

       // TextView lblTitle = (TextView)view.findViewById(R.id.lblTitle);

        TextView tv1 = (TextView) view.findViewById(R.id.lblTitle);
        TextView tv2 = (TextView) view.findViewById(R.id.lblPlace);
        TextView tv3 = (TextView) view.findViewById(R.id.lblPosted);
        TextView tv4 = (TextView) view.findViewById(R.id.txtTitle);
        TextView tv5 = (TextView) view.findViewById(R.id.txtPlace);
        TextView tv6 = (TextView) view.findViewById(R.id.txtPosted);

        ImageView iv = (ImageView) view.findViewById(R.id.imageView);

        // my getting details from place object to display in inner views
       // tv1.setText(place.getId());
        tv1.setText("Title");
        tv2.setText("Place");
        tv3.setText("Posted");
        tv4.setText(place.getTitle());
        tv5.setText(place.getPlace());

        tv6.setText(place.getPosted());

        iv.setImageBitmap(place.getBitmap());

       // return view;
        return view;
    }




}