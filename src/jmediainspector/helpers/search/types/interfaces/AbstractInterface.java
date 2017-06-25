package jmediainspector.helpers.search.types.interfaces;

import java.util.logging.Level;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.healthmarketscience.sqlbuilder.BinaryCondition;

import aka.jmetadataquery.main.types.search.operation.interfaces.OperatorSearchInterface;
import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import jmediainspector.config.Criteria;
import jmediainspector.config.ObjectFactory;
import jmediainspector.context.ApplicationContext;
import jmediainspector.controllers.AbstractSearchCriteriaController;
import jmediainspector.helpers.search.SearchHelper;
import jmediainspector.helpers.search.enums.SearchTypeEnum;

/**
 * <filter selected="true">
 * <type>EQUALS</type>
 * <value>R_1080</value>
 * </filter>
 *
 * @author Cha
 */
public abstract class AbstractInterface<T extends Enum<?>> {

    /**
     * Linked criteria.
     */
    @NonNull
    protected final Criteria criteria;
    private JavaBeanBooleanProperty selectedProperty = null;
    private JavaBeanBooleanProperty requiredProperty = null;
    @NonNull
    private final CheckBox selectedCheckBox = new CheckBox("Use");
    @NonNull
    private final CheckBox requiredCheckBox = new CheckBox("Required");
    /**
     * Right pane.
     */
    @NonNull
    public final GridPane rightPane = new GridPane();
    private SearchCriteriaListener searchCriteriaListener;

    /**
     * Default constructor.
     */
    public AbstractInterface() {
        final Criteria newCriteria = (new ObjectFactory()).createCriteria();
        assert newCriteria != null;
        this.criteria = newCriteria;
    }

    /**
     * Constructor.
     *
     * @param criteria Linked Criteria
     * @see Criteria
     */
    public AbstractInterface(@NonNull final Criteria criteria) {
        this.criteria = criteria;
        this.rightPane.setHgap(2);
        this.rightPane.setVgap(3);
        this.rightPane.setMaxWidth(Double.MAX_VALUE);

        this.rightPane.add(getSelectedCheckBox(), 0, 0);
        GridPane.setValignment(getSelectedCheckBox(), VPos.TOP);
        this.rightPane.add(getRequiredCheckBox(), 1, 0);
        GridPane.setValignment(getRequiredCheckBox(), VPos.TOP);

        try {
            this.selectedProperty = JavaBeanBooleanPropertyBuilder.create().bean(this.criteria).name("selected").build();
            this.requiredProperty = JavaBeanBooleanPropertyBuilder.create().bean(this.criteria).name("required").build();
        } catch (final NoSuchMethodException e) {
            ApplicationContext.getInstance().getLogger().logp(Level.SEVERE, "AbstractInterface", "AbstractInterface", e.getMessage(), e);
        }
        this.selectedCheckBox.selectedProperty().bindBidirectional(this.selectedProperty);
        this.requiredCheckBox.selectedProperty().bindBidirectional(this.requiredProperty);
    }

    /**
     * Get pane containing choices.
     *
     * @return pane containing choices to be includes in the screen.
     */
    @NonNull
    public Node getRightPaneChoices() {
        final Button deleteButton = new Button("X");
        GridPane.setValignment(deleteButton, VPos.TOP);
        GridPane.setHalignment(deleteButton, HPos.RIGHT);
        deleteButton.setOnAction(e -> delete());
        this.rightPane.add(deleteButton, 4, 0);

        return this.rightPane;
    }

    private void delete() {
        this.searchCriteriaListener.delete(this);
    }

    /**
     * Get pane Type.
     *
     * @return pane type.
     * @see SearchTypeEnum
     */
    @NonNull
    public abstract SearchTypeEnum getType();

    /**
     * Get name.
     *
     * @return name.
     */
    @NonNull
    public abstract String getFullName();

    /**
     * Handle event.
     *
     * @param searchHelper
     * @param abstractSearchCriteriaController
     */
    public abstract void handleEvent(final SearchHelper searchHelper, @NonNull AbstractSearchCriteriaController abstractSearchCriteriaController);

    /**
     * Get required checkbox.
     *
     * @return required checkbox.
     */
    @NonNull
    public CheckBox getRequiredCheckBox() {
        return this.requiredCheckBox;
    }

    /**
     * Get selected checkbox.
     *
     * @return selected checbox.
     */
    @NonNull
    public CheckBox getSelectedCheckBox() {
        return this.selectedCheckBox;
    }

    /**
     * Set the listener.
     *
     * @param searchCriteriaListener
     */
    public void setListener(@NonNull final SearchCriteriaListener searchCriteriaListener) {
        this.searchCriteriaListener = searchCriteriaListener;
    }

    /**
     * @return search
     */
    public abstract OperatorSearchInterface getSearch();

    /**
     * Get condition operator.
     *
     * @return operator
     */
    public abstract BinaryCondition.Op getSelectedOperator();

    /**
     * Get condition operator.
     *
     * @return operator
     */
    public abstract Enum<?> getSelectedEnumValue();

    /**
     * Initialization.
     */
    public abstract void init();

    /**
     * Get linked criteria.
     *
     * @return Linked criteria
     */
    @Nullable
    public abstract AbstractInterface<?> getCriteria();
}