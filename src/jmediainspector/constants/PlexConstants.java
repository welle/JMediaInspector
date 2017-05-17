package jmediainspector.constants;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Plex constants.
 *
 * @author Cha
 */
public final class PlexConstants {

    /**
     * Correspondence between section_type in library_sections in Plex database.
     *
     * @author Cha
     */
    public enum SECTION_TYPE {

        /**
         * Movies
         */
        MOVIES(1, "Movies"),

        /**
         * TV Shows.
         */
        TV_SHOWS(2, "TV Shows");

        @NonNull
        private final String directoryName;
        @NonNull
        private final Integer value;

        private SECTION_TYPE(final int value, @NonNull final String directoryName) {
            this.value = Integer.valueOf(value);
            this.directoryName = directoryName;
        }

        /**
         * Get related section type.
         *
         * @param sectionTypeValue
         * @return related section type
         */
        @Nullable
        public static SECTION_TYPE getSectionType(@Nullable final Integer sectionTypeValue) {
            SECTION_TYPE result = null;
            for (final SECTION_TYPE sectionType : values()) {
                if (sectionType.value == sectionTypeValue) {
                    result = sectionType;
                    break;
                }
            }
            return result;
        }

        /**
         * @return the directoryName
         */
        @NonNull
        public final String getDirectoryName() {
            return this.directoryName;
        }

        /**
         * @return the value
         */
        @NonNull
        public final Integer getValue() {
            return this.value;
        }
    }
}
