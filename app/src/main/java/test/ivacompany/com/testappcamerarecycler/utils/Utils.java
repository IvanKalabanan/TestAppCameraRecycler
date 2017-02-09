package test.ivacompany.com.testappcamerarecycler.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import test.ivacompany.com.testappcamerarecycler.models.Photo;

/**
 * Created by root on 06.02.17.
 */

public class Utils {

    private static final String TAG = "Utils";

    private static Realm realm;

    public static void initRealm(Context context) {
        if (realm == null) {
            realm.init(context);
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
        Log.d(TAG, "isDelete - " + new File(result.getImageUri().getPath()).delete());
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

    public static String getTransitionName(long id) {
        return "photoTrans" + id;
    }

}
