package com.tubitv.media.fsm.concrete;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebView;

import com.google.android.exoplayer2.ExoPlayer;
import com.tubitv.media.controller.PlayerComponentController;
import com.tubitv.media.controller.PlayerUIController;
import com.tubitv.media.fsm.BaseState;
import com.tubitv.media.fsm.Input;
import com.tubitv.media.fsm.State;
import com.tubitv.media.fsm.concrete.factory.StateFactory;
import com.tubitv.media.fsm.state_machine.FsmPlayer;
import com.tubitv.media.helpers.Constants;
import com.tubitv.media.models.AdMediaModel;
import com.tubitv.media.models.MediaModel;
import com.tubitv.media.models.VpaidClient;
import com.tubitv.media.utilities.ExoPlayerLogger;

/**
 * Created by allensun on 8/1/17.
 */
public class VpaidState extends BaseState {

    @Override
    public State transformToState(Input input, StateFactory factory) {

        switch (input) {
            case VPAID_FINISH:
                return factory.createState(MoviePlayingState.class);

            case VPAID_MANIFEST:
                return factory.createState(VpaidState.class);

            case NEXT_VPAID:
                return factory.createState(VpaidState.class);
        }
        return null;
    }

    //TODO: API level lower that certain, will disable vpaid.
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void performWorkAndupdatePlayerUI(@Nullable FsmPlayer fsmPlayer, @NonNull PlayerUIController controller, @NonNull PlayerComponentController componentController, @NonNull MediaModel movieMedia, @Nullable AdMediaModel adMedia) {
        if (isNull(fsmPlayer, controller, componentController, movieMedia, adMedia)) {
            return;
        }

        pausePlayerAndSHowVpaid(controller, componentController, fsmPlayer);
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private void pausePlayerAndSHowVpaid(PlayerUIController controller, PlayerComponentController componentController, FsmPlayer fsmPlayer) {

        ExoPlayer moviePlayer = controller.getContentPlayer();

        if (moviePlayer != null && moviePlayer.getPlayWhenReady()) {
            moviePlayer.setPlayWhenReady(false);
        }

        ExoPlayer adPlayer = controller.getContentPlayer();
        if (adPlayer != null && adPlayer.getPlayWhenReady()) {
            adPlayer.setPlayWhenReady(false);
        }

        VpaidClient client = componentController.getVpaidClient();

        if (client != null) {

            client.init();

            controller.getExoPlayerView().setVisibility(View.INVISIBLE);

            WebView vpaidWebView = controller.getVpaidWebView();


            vpaidWebView.setVisibility(View.VISIBLE);
            vpaidWebView.bringToFront();
            vpaidWebView.invalidate();

            vpaidWebView.addJavascriptInterface(client, "TubiNativeJSInterface");
            vpaidWebView.loadUrl("https://s3-us-west-1.amazonaws.com/tubi-vpaid/index.html");
        }else{
            ExoPlayerLogger.w(Constants.FSMPLAYER_TESTING, "VpaidClient is null");
        }

    }


}
