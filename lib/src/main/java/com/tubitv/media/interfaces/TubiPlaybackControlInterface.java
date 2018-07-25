package com.tubitv.media.interfaces;

/**
 * Created by stoyan tubi_tv_quality_on 4/27/17.
 */
public interface TubiPlaybackControlInterface {

    //action control
    void triggerSubtitlesToggle(boolean enabled);

    void triggerQualityTrackToggle();

    void seekBy(long millisecond);

    void seekTo(long millisecond);

    void triggerPlayOrPause(boolean setPlay);

    //display control
    String getCurrentVideoName();

    boolean videoReadyToPlay();

    boolean isCurrentVideoAd();

    long currentDuration();

    long currentProgressPosition();

    long currentBufferPosition();

}
