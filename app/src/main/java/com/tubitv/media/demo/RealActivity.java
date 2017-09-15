package com.tubitv.media.demo;

import com.tubitv.media.activities.DoubleViewTubiPlayerActivity;
import com.tubitv.media.demo.di.DaggerFsmComonentReal;
import com.tubitv.media.demo.di.FSMModuleReal;

/**
 * Created by allensun on 8/29/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
public class RealActivity extends DoubleViewTubiPlayerActivity {

    @Override
    protected void injectDependency() {
        DaggerFsmComonentReal.builder().fSMModuleReal(new FSMModuleReal(vpaidWebView,mTubiPlayerView)).build().inject(this);
    }
}
