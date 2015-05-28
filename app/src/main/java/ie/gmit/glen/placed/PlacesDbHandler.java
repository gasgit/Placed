package ie.gmit.glen.placed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by glen on 15/04/15.
 */
public class PlacesDbHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "placesDB.db";
    public static final String TABLE_PLACES = "places";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_COMMENTS ="comments";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_POSTED = "posted";
   // public static final Bitmap COLUMN_BITMAP = bitmap;

    String CREATE_NEW_TABLE = "CREATE TABLE " +
            TABLE_PLACES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_PLACE + " TEXT,"
            + COLUMN_COMMENTS + " TEXT,"
            + COLUMN_LAT + " TEXT,"
            + COLUMN_LNG + " TEXT,"
            + COLUMN_PHOTO +" TEXT,"
            + COLUMN_POSTED + " TEXT" +")";

//    public PlacesDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    public PlacesDbHandler(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(CREATE_NEW_TABLE);
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);

    }


    public void addPlace(Places place4Db) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, place4Db.getTitle());
        values.put(COLUMN_PLACE,place4Db.getPlace());
        values.put(COLUMN_COMMENTS, place4Db.getComments());
        values.put(COLUMN_LAT, place4Db.getLat());
        values.put(COLUMN_LNG, place4Db.getLng());
        values.put(COLUMN_PHOTO, place4Db.getPhoto());
        values.put(COLUMN_POSTED, place4Db.getPosted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PLACES, null, values);
        db.close();
    }

    public List<Places> getFavourites()
    {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Places favPlace = new Places();

                favPlace.setId(cursor.getString(0));
                favPlace.setTitle(cursor.getString(1));
                favPlace.setPlace(cursor.getString(2));
                favPlace.setComments(cursor.getString(3));
                favPlace.setLat(cursor.getString(4));
                favPlace.setLng(cursor.getString(5));
                favPlace.setPhoto(cursor.getString(6));
                favPlace.setPosted(cursor.getString(7));




                // Adding newPlace to list
                PlacedGlobal.favList.add(favPlace);

            } while (cursor.moveToNext());
        }

        // return contact list



        return PlacedGlobal.favList;
    };


    //delete here

    public boolean deletePlace(String id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PLACES + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Places place = new Places();

        if (cursor.moveToFirst()) {
            place.setId((cursor.getString(0)));
            db.delete(TABLE_PLACES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(place.getId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
