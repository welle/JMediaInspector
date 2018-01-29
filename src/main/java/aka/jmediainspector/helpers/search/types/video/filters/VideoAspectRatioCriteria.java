package aka.jmediainspector.helpers.search.types.video.filters;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNull;

import aka.jmediainspector.config.Criteria;
import aka.jmediainspector.controllers.tabs.AbstractSearchCriteriaController;
import aka.jmediainspector.helpers.search.SearchHelper;
import aka.jmediainspector.helpers.search.commons.ConditionFilter;
import aka.jmediainspector.helpers.search.comparators.AspectRatioComparator;
import aka.jmediainspector.helpers.search.componenttype.AbstractComboboxCriteria;
import aka.jmediainspector.helpers.search.enums.SearchTypeEnum;
import aka.jmediainspector.helpers.search.interfaces.AbstractInterface;
import aka.jmetadata.main.constants.video.AspectRatio;
import aka.jmetadataquery.search.constants.conditions.Operator;
import aka.jmetadataquery.search.operation.interfaces.OperatorSearchInterface;
import aka.jmetadataquery.search.types.video.VideoAspectRatioSearch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Criteria for Audio Compression mode.
 *
 * @author charlottew
 */
public class VideoAspectRatioCriteria extends AbstractComboboxCriteria<AspectRatio> {

    /**
     * Default Constructor.
     */
    public VideoAspectRatioCriteria() {
        // Internal use, do not delete, used in reflection.
    }

    /**
     * Constructor.
     *
     * @param filter Linked Filter
     * @see Criteria
     */
    public VideoAspectRatioCriteria(@NonNull final Criteria filter) {
        super(filter);
    }

    @Override
    @NonNull
    public SearchTypeEnum getType() {
        return SearchTypeEnum.VIDEO;
    }

    @Override
    public @NonNull String getFullName() {
        return "Aspect Ratio";
    }

    @Override
    public void handleEvent(final SearchHelper searchHelper, @NonNull final AbstractSearchCriteriaController abstractSearchCriteriaController) {
        final Criteria filter = abstractSearchCriteriaController.getNewCriteria();
        final VideoAspectRatioCriteria newCriteria = new VideoAspectRatioCriteria(filter);

        searchHelper.addCriteria(newCriteria);
    }

    @Override
    public OperatorSearchInterface getSearch() {
        final Operator operation = getSelectedOperator();
        VideoAspectRatioSearch videoAspectRatioSearch = null;
        final Enum<?> value = getSelectedComboboxEnumValue();
        if (operation != null && value instanceof AspectRatio) {
            final AspectRatio codecEnum = (AspectRatio) value;
            videoAspectRatioSearch = new VideoAspectRatioSearch(operation, codecEnum);
        }
        return videoAspectRatioSearch;
    }

    @Override
    public void init() {
        this.availableTypes = new ArrayList<>();
        this.availableTypes.add(ConditionFilter.EQUALS);
        this.availableTypes.add(ConditionFilter.NOT_EQUALS);

        this.availableValues = new ArrayList<>(Arrays.asList(AspectRatio.values()));
    }

    @Override
    public AbstractInterface<?> getCriteria() {
        return this;
    }

    @Override
    public ComboBox<? extends Enum<?>> getCombobox() {
        final ComboBox<AspectRatio> result = new ComboBox<>();
        final StringConverter<AspectRatio> converter = new StringConverter<AspectRatio>() {
            @Override
            public String toString(final AspectRatio object) {
                final String name = object.getAspectRatioFullName();
                final StringBuilder sb = new StringBuilder();
                sb.append(name);
                final double ratio = object.getAspectRatio();
                if (ratio > 0) {
                    sb.append(" (");
                    sb.append(ratio);
                    sb.append(")");
                }

                return sb.toString();
            }

            @Override
            public AspectRatio fromString(final String string) {
                return null;
            }
        };
        result.setConverter(converter);
        final AspectRatio[] values = AspectRatio.values();
        Arrays.sort(values, AspectRatioComparator.INSTANCE);
        result.getItems().setAll(values);
        return result;
    }
}
