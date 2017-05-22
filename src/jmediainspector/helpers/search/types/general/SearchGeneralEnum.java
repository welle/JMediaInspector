package jmediainspector.helpers.search.types.general;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

public enum SearchGeneralEnum {
    /**
     * General.
     */
    GENERAL(new SearchGeneralHelper()),

    /**
     * Video.
     */
    VIDEO(new SearchGeneralHelper()),

    /**
     * Audio.
     */
    AUDIO(new SearchGeneralHelper()),

    /**
     * Test.
     */
    TEXT(new SearchGeneralHelper());

    private @NonNull SearchPanelInterface searchPanelInterface;

    SearchGeneralEnum(@NonNull final SearchPanelInterface searchPanelInterface) {
        this.searchPanelInterface = searchPanelInterface;
    }

    public SearchPanelInterface getSearchPanelInterface() {
        return this.searchPanelInterface;
    }
}
