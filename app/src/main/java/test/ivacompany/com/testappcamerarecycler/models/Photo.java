package test.ivacompany.com.testappcamerarecycler.models;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by root on 06.02.17.
 */

public class Photo extends RealmObject {
    @Ignore
    private Bitmap image;
    private byte[] byteImage;
    private String date;
    private String name;

    public Photo() {}

    public Photo(Bitmap image, String date, String name) {
        this.image = image;
        this.byteImage = toByte(image);
        this.date = date;
        this.name = name;
    }

    private byte[] toByte(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }
}
