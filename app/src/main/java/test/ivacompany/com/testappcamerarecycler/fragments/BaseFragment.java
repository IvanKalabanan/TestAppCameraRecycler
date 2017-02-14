package test.ivacompany.com.testappcamerarecycler.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import test.ivacompany.com.testappcamerarecycler.models.Photo;
import test.ivacompany.com.testappcamerarecycler.utils.Constants;

/**
 * Created by iva on 14.02.17.
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected Realm realm;

    public List<Photo> readFromDB() {
        List<Photo> list = new ArrayList<>();
        RealmResults<Photo> results = realm.where(Photo.class).findAll();
        for (Photo e : results) {
            e.convertImageUri();
            list.add(e);
        }
        return list;
    }

    public void addToDB(Photo photo) {
        realm.beginTransaction();
        realm.copyToRealm(photo);
        realm.commitTransaction();
    }

    public void removeFromRealm(long id) {
        realm.beginTransaction();
        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
        result.convertImageUri();
        Log.d(TAG, "isDelete - " + new File(result.getImageUri().getPath()).delete());
        result.deleteFromRealm();
        realm.commitTransaction();
    }

    public Photo getPhoto(long id) {
        Photo result = realm.where(Photo.class).equalTo(Constants.ID, id).findFirst();
        result.convertImageUri();
        return result;
    }
}
