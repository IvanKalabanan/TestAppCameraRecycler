package test.ivacompany.com.testappcamerarecycler.views;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Created by iva on 07.02.17.
 */

public class PhotoTransition extends TransitionSet {

    public PhotoTransition() {
        init();
    }

    /**
     * This constructor allows us to use this transition in XML
     */
    public PhotoTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
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