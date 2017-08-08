package com.tubitv.demo;

import com.tubitv.media.controller.PlayerUIController;
import com.tubitv.media.di.FSMModule;
import com.tubitv.media.di.component.DaggerFsmComonent;
import com.tubitv.media.di.component.FsmComonent;
import com.tubitv.media.fsm.Input;
import com.tubitv.media.fsm.callback.AdInterface;
import com.tubitv.media.fsm.concrete.AdPlayingState;
import com.tubitv.media.fsm.concrete.FinishState;
import com.tubitv.media.fsm.concrete.MakingAdCallState;
import com.tubitv.media.fsm.concrete.MoviePlayingState;
import com.tubitv.media.fsm.concrete.ReceiveAdState;
import com.tubitv.media.fsm.concrete.VastAdInteractionSandBoxState;
import com.tubitv.media.fsm.concrete.VpaidState;
import com.tubitv.media.fsm.concrete.factory.StateFactory;
import com.tubitv.media.fsm.state_machine.FsmPlayer;
import com.tubitv.media.models.AdRetriever;
import com.tubitv.media.models.MediaModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import javax.inject.Inject;

import static junit.framework.Assert.assertTrue;

/**
 * Created by allensun on 8/1/17.
 */
@RunWith(JUnit4.class)
public class ExoPlayerFSMTest {

    FsmPlayer playerFsm;

    @Mock
    MediaModel movieMedia;

    @Mock
    MediaModel adMedia;

    @Mock
    AdRetriever retriever;

    @Mock
    AdInterface adServerInterface;

    @Mock
    PlayerUIController controller;

    @Inject
    StateFactory factory;


    FsmComonent comonent;

    @Before
    public void setup() {
        comonent = DaggerFsmComonent.builder().fSMModule(new FSMModule()).build();
    }

    @Test
    public void testFSMFlowWithVpaid() {


        factory = comonent.getStateFactory();

        playerFsm = new FsmPlayer(factory);
//        playerFsm.setAdMedia(adMedia);
//        playerFsm.setMovieMedia(movieMedia);
//        playerFsm.setAdServerInterface(adServerInterface);
//        playerFsm.setRetriever(retriever);
//        playerFsm.setController(controller);

        playerFsm.transit(Input.MAKE_AD_CALL);

        assertTrue(playerFsm.getCurrentState() instanceof MakingAdCallState);

        playerFsm.transit(Input.AD_RECEIVED);

        assertTrue(playerFsm.getCurrentState() instanceof ReceiveAdState);

        playerFsm.transit(Input.SHOW_ADS);

        assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

        playerFsm.transit(Input.NEXT_AD);

        assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

        playerFsm.transit(Input.AD_CLICK);

        assertTrue(playerFsm.getCurrentState() instanceof VastAdInteractionSandBoxState);

        playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VAST_AD);

        assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

        playerFsm.transit(Input.VPAID_MANIFEST);

        assertTrue(playerFsm.getCurrentState() instanceof VpaidState);

        playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VPAID_AD);

        assertTrue(playerFsm.getCurrentState() instanceof MoviePlayingState);

        playerFsm.transit(Input.MAKE_AD_CALL);

        assertTrue(playerFsm.getCurrentState() instanceof MakingAdCallState);

        playerFsm.transit(Input.EMPTY_AD);

        assertTrue(playerFsm.getCurrentState() instanceof MoviePlayingState);

        playerFsm.transit(Input.MOVIE_FINISH);

        assertTrue(playerFsm.getCurrentState() instanceof FinishState);
    }

    @Test
    public void testFSMFlowWithNoVpaid() {
        factory = comonent.getStateFactory();

        playerFsm = new FsmPlayer(factory);

        for (int i = 0; i < 10; i++) {

            playerFsm.transit(Input.MAKE_AD_CALL);

            assertTrue(playerFsm.getCurrentState() instanceof MakingAdCallState);

            playerFsm.transit(Input.AD_RECEIVED);

            assertTrue(playerFsm.getCurrentState() instanceof ReceiveAdState);

            playerFsm.transit(Input.SHOW_ADS);

            assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

            playerFsm.transit(Input.NEXT_AD);

            assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

            playerFsm.transit(Input.AD_CLICK);

            assertTrue(playerFsm.getCurrentState() instanceof VastAdInteractionSandBoxState);

            playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VAST_AD);

            assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

            playerFsm.transit(Input.AD_CLICK);

            assertTrue(playerFsm.getCurrentState() instanceof VastAdInteractionSandBoxState);

            playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VAST_AD);

            assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

            playerFsm.transit(Input.NEXT_AD);

            assertTrue(playerFsm.getCurrentState() instanceof AdPlayingState);

            playerFsm.transit(Input.VPAID_MANIFEST);

            assertTrue(playerFsm.getCurrentState() instanceof VpaidState);

            playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VPAID_AD);

            assertTrue(playerFsm.getCurrentState() instanceof MoviePlayingState);

            playerFsm.transit(Input.MAKE_AD_CALL);

        }

        playerFsm.transit(Input.EMPTY_AD);

        playerFsm.transit(Input.MOVIE_FINISH);

        assertTrue(playerFsm.getCurrentState() instanceof FinishState);
    }

    @Test
    public void testErrorFlow() {
        testFSMFlowWithNoVpaid();

        playerFsm.transit(Input.AD_CLICK);
        playerFsm.transit(Input.BACK_TO_PLAYER_FROM_VAST_AD);
        playerFsm.transit(Input.AD_FINISH);
        playerFsm.transit(Input.AD_CLICK);


        assertTrue(playerFsm.getCurrentState() instanceof MoviePlayingState);
    }


}