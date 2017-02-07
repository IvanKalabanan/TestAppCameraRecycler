package test.ivacompany.com.testappcamerarecycler;

import android.app.Application;
import android.content.Context;

/**
 * Created by root on 06.02.17.
 */

public class TestApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }


    public static Context getAppContext() {
        return context;
    }
}
