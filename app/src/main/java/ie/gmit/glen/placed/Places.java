package ie.gmit.glen.placed;

import android.graphics.Bitmap;



/**
 * Created by glen on 07/02/15.
 *
 * my Class to create place objects for application getters/setters
 */
public class Places {

    private String id;
    private String title;
    private String place;
    private String comments;
    private String lat;
    private String lng;
    private String photo;
    private String posted;
    private String image;
    private Bitmap bitmap;




    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Places()
    {

    }




}