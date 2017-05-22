package jmediainspector.helpers.search.types.text;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

public enum SearchTextEnum {
    /**
     * General.
     */
    GENERAL(new SearchTextHelper()),

    /**
     * Video.
     */
    VIDEO(new SearchTextHelper()),

    /**
     * Audio.
     */
    AUDIO(new SearchTextHelper()),

    /**
     * Test.
     */
    TEXT(new SearchTextHelper());

    private @NonNull SearchPanelInterface searchPanelInterface;

    SearchTextEnum(@NonNull final SearchPanelInterface searchPanelInterface) {
        this.searchPanelInterface = searchPanelInterface;
    }

    public SearchPanelInterface getSearchPanelInterface() {
        return this.searchPanelInterface;
    }
}
