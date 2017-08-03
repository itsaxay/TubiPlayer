package com.tubitv.media.fsm.concrete;

import com.google.android.exoplayer2.ExoPlayer;
import com.tubitv.media.fsm.BaseState;
import com.tubitv.media.fsm.Input;
import com.tubitv.media.fsm.State;
import com.tubitv.media.fsm.concrete.factory.StateFactory;

/**
 * Created by allensun on 7/31/17.
 */
public class AdPlayingState extends BaseState {


    @Override
    public State transformToState(Input input, StateFactory factory) {

        switch (input) {

            case NEXT_AD:
                return  factory.createState(AdPlayingState.class);

            case AD_CLICK:
                return factory.createState(VastAdInteractionSandBoxState.class);

            case AD_FINISH:
                return factory.createState(MoviePlayingState.class);

            case VPAID_MANIFEST:
                return factory.createState(VpaidState.class);

        }
        return null;
    }

    @Override
    public void updatePlayer(ExoPlayer contentPlayer, ExoPlayer adPlayer) {

    }

    @Override
    public void prepareWorkLoad() {

    }

}