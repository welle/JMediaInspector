package jmediainspector.helpers.search.types.audio;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;
import jmediainspector.helpers.search.types.text.SearchTextHelper;

public enum SearchAudioEnum {
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

    SearchAudioEnum(@NonNull final SearchPanelInterface searchPanelInterface) {
        this.searchPanelInterface = searchPanelInterface;
    }

    public SearchPanelInterface getSearchPanelInterface() {
        return this.searchPanelInterface;
    }
}
