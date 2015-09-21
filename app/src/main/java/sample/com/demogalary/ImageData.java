package sample.com.demogalary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Amit on 9/14/2015.
 */
public class ImageData {
    String location;
    String coordinates;
    Bitmap imgBitmap;

    public String getlocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Bitmap getImgResId() {
        return imgBitmap;
    }

    public void setImgResId(byte[] image) {
        this.imgBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
