package jmediainspector.helpers.search.types.interfaces;

import org.eclipse.jdt.annotation.NonNull;

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
public abstract class AbstractInterface {

    /**
     * Linked criteria.
     */
    @NonNull
    protected final Criteria criteria;
    private JavaBeanBooleanProperty selectedProperty = null;
    @NonNull
    private final CheckBox selectedCheckBox = new CheckBox();
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

        try {
            this.selectedProperty = JavaBeanBooleanPropertyBuilder.create().bean(this.criteria).name("selected").build();
        } catch (final NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.selectedCheckBox.selectedProperty().bindBidirectional(this.selectedProperty);
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
        this.rightPane.add(deleteButton, 3, 0);

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

    public abstract void handleEvent(final SearchHelper searchHelper, @NonNull AbstractSearchCriteriaController abstractSearchCriteriaController);

    /**
     * Get selected checkbox.
     *
     * @return pane containing choices to be includes in the screen.
     */
    @NonNull
    public Node getSelectedCheckBox() {
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
}
