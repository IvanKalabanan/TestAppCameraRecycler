package test.ivacompany.com.testappcamerarecycler.models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by root on 06.02.17.
 */

public class Photo extends RealmObject {
    private long id;
    private String imageUriString;
    @Ignore
    private Uri imageUri;
    private String date;
    private String name;

    public Photo() {}

    public Photo(long id, Uri imageUri, String date, String name) {
        this.id = id;
        this.imageUri = imageUri;
        this.imageUriString = imageUri.toString();
        this.date = date;
        this.name = name;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void convertImageUri() {
        this.imageUri = Uri.parse(this.imageUriString);
    }

    public String getImageUriString() {
        return imageUriString;
    }
}
