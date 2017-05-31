package jmediainspector.helpers.search.enums;

import org.eclipse.jdt.annotation.NonNull;

import jmediainspector.helpers.search.types.audio.SearchAudioHelper;
import jmediainspector.helpers.search.types.general.SearchGeneralHelper;
import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;
import jmediainspector.helpers.search.types.text.SearchTextHelper;
import jmediainspector.helpers.search.types.video.SearchVideoHelper;

/**
 * Search Type.
 *
 * @author Cha
 */
public enum SearchTypeEnum {
    /**
     * General.
     */
    GENERAL(new SearchGeneralHelper()),

    /**
     * Video.
     */
    VIDEO(new SearchVideoHelper()),

    /**
     * Audio.
     */
    AUDIO(new SearchAudioHelper()),

    /**
     * Test.
     */
    TEXT(new SearchTextHelper());

    private @NonNull SearchPanelInterface searchPanelInterface;

    SearchTypeEnum(@NonNull final SearchPanelInterface searchPanelInterface) {
        this.searchPanelInterface = searchPanelInterface;
    }

    @NonNull
    public SearchPanelInterface getSearchPanelInterface() {
        return this.searchPanelInterface;
    }
}
