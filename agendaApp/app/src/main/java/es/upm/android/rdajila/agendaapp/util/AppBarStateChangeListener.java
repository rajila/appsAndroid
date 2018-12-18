package es.upm.android.rdajila.agendaapp.util;

import android.support.design.widget.AppBarLayout;


/**
 * Link referencia: https://gist.github.com/paulocaldeira17/1fa85d40765245ab26552911acd385ed
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener
{
    // State
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    /**
     * Notifies on state change
     * @param appBarLayout Layout
     * @param state Collapse state
     */
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}