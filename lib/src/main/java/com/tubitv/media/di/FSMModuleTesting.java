package com.tubitv.media.di;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.tubitv.media.controller.PlayerComponentController;
import com.tubitv.media.controller.PlayerUIController;
import com.tubitv.media.di.annotation.ActicityScope;
import com.tubitv.media.fsm.callback.AdInterface;
import com.tubitv.media.fsm.callback.RetrieveAdCallback;
import com.tubitv.media.fsm.concrete.MoviePlayingState;
import com.tubitv.media.fsm.concrete.factory.StateFactory;
import com.tubitv.media.fsm.listener.AdPlayingMonitor;
import com.tubitv.media.fsm.listener.CuePointMonitor;
import com.tubitv.media.fsm.state_machine.FsmPlayer;
import com.tubitv.media.helpers.Constants;
import com.tubitv.media.models.AdMediaModel;
import com.tubitv.media.models.AdRetriever;
import com.tubitv.media.models.MediaModel;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by allensun on 8/7/17.
 * on Tubitv.com, allengotstuff@gmail.com
 */
@Module
public class FSMModuleTesting {

    private SimpleExoPlayer mainPlayer;

    private SimpleExoPlayer adPlayer;

    private WebView webView;

    private View rootView;

    public FSMModuleTesting(@Nullable SimpleExoPlayer mainPlayer, @Nullable SimpleExoPlayer adPlayer, @Nullable WebView webView, @Nullable View rootView) {
        this.mainPlayer = mainPlayer;
        this.adPlayer = adPlayer;
        this.webView = webView;
        this.rootView = rootView;
    }

    @ActicityScope
    @Provides
    StateFactory provideStateFactory() {
        return new StateFactory();
    }

    @ActicityScope
    @Provides
    FsmPlayer provideFsmPlayer(StateFactory factory) {
        return new FsmPlayer(factory){
            @Override
            public Class initializeState() {
                return MoviePlayingState.class;
            }
        };
    }

    @ActicityScope
    @Provides
    PlayerUIController provideController() {
        return new PlayerUIController(mainPlayer, adPlayer, webView, rootView);
    }

    @ActicityScope
    @Provides
    PlayerComponentController provideComponentController(){
        return new PlayerComponentController(null,null,null);
    }

    @ActicityScope
    @Provides
    AdRetriever provideAdRetriever() {
        return new AdRetriever();
    }

    @ActicityScope
    @Provides
    AdPlayingMonitor provideAdPlayingMonitor(FsmPlayer player) {
        return new AdPlayingMonitor(player);
    }

    @ActicityScope
    @Provides
    CuePointMonitor provideCuePointMonitor(FsmPlayer fsmPlayer) {

        CuePointMonitor cuePointMonitor = new CuePointMonitor(fsmPlayer) {
            @Override
            public int networkingAhead() {
                return 5000;
            }
        };

        cuePointMonitor.setQuePoints(new int[]{20000, 60000, 120000, 1800000});

        return cuePointMonitor;
    }

    @ActicityScope
    @Provides
    AdMediaModel provideAdMediaModel() {
        MediaModel adsMediaModel = MediaModel.ad("http://c13.adrise.tv/ads/transcodes/004130/1050072/v0617070213-640x360-SD-,764,1057,k.mp4.m3u8",
                "https://github.com/stoyand", false);

        final List<MediaModel> list = new ArrayList<>();
        list.add(adsMediaModel);

        AdMediaModel adMediaModel = new AdMediaModel(list) {
            @Nullable
            @Override
            public MediaModel nextAD() {
                return list != null && list.size() > 0 ? list.get(0) : null;
            }
        };

        return adMediaModel;
    }

    @ActicityScope
    @Provides
    AdInterface provideAdInterface() {

        // using the fake generated AdMediaModel to do has the returned data.
        return new AdInterface() {
            @Override
            public void fetchAd(AdRetriever retriever, RetrieveAdCallback callback) {
                Log.d(Constants.FSMPLAYER_TESTING, "On ad receive");
                callback.onReceiveAd(provideAdMediaModel());
            }
        };
    }


}