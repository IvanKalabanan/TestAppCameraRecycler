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

    public static String getTransitionName(long id) {
        return "photoTrans" + id;
    }

}
