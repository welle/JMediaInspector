package jmediainspector.helpers.search.enums;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.interfaces.SearchPanelInterface;
import jmediainspector.helpers.search.video.SearchVideoHelper;

public enum Type {
    /**
     * General.
     */
    GENERAL(new SearchVideoHelper()),

    /**
     * Video.
     */
    VIDEO(new SearchVideoHelper()),

    /**
     * Audio.
     */
    AUDIO(new SearchVideoHelper()),

    /**
     * Test.
     */
    TEXT(new SearchVideoHelper());

    private @NonNull SearchPanelInterface searchPanelInterface;

    Type(@NonNull final SearchPanelInterface searchPanelInterface) {
        this.searchPanelInterface = searchPanelInterface;
    }

    public SearchPanelInterface getSearchPanelInterface() {
        return this.searchPanelInterface;
    }
}
