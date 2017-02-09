package test.ivacompany.com.testappcamerarecycler.views;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by iva on 07.02.17.
 */

public class PhotoTransition extends TransitionSet {

    public PhotoTransition() {
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
        setDuration(250);
    }
}