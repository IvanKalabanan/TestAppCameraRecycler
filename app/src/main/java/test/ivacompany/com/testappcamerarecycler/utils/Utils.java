package test.ivacompany.com.testappcamerarecycler.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import test.ivacompany.com.testappcamerarecycler.TestApp;
import test.ivacompany.com.testappcamerarecycler.models.Photo;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 06.02.17.
 */

public class Utils {

    private static Realm realm;

    public static void initRealm() {
        if (realm == null) {
            realm.init(TestApp.getAppContext());
            realm = Realm.getDefaultInstance();
        }
    }

    public static List<Photo> readFromDB() {
        List<Photo> list = new ArrayList<>();
        RealmResults<Photo> results = realm.where(Photo.class).findAll();
        for (Photo e : results) {
            e.convertImageUri();
            list.add(e);
        }
        return list;
    }

    public static void addToDB(Photo photo) {
        realm.beginTransaction();
        realm.copyToRealm(photo);
        realm.commitTransaction();
    }

    public static void removeFromRealm(long id) {
        realm.beginTransaction();
        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
        result.convertImageUri();
        Log.d("ff", "isDelete - " + new File(result.getImageUri().getPath()).delete());
        result.deleteFromRealm();
        realm.commitTransaction();
    }

    public static Realm getRealm() {
        return realm;
    }

    public static Photo getPhoto(long id) {
        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
        result.convertImageUri();
        return result;
    }

    public static String getTransitionName(long id){
        return "photoTrans" + id;
    }

}
