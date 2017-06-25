package jmediainspector.helpers.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jmediainspector.config.Search;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.AbstractInterface;
import jmediainspector.helpers.search.types.interfaces.SearchCriteriaListener;
import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

/**
 * SearchHelper handle the right panel with a related search in the XML config file.
 *
 * @author Cha
 */
public class SearchHelper implements SearchCriteriaListener {

    @NonNull
    private final Map<@NonNull SearchTypeEnum, LinkedPanel> linkedPanelMap = new LinkedHashMap<>();
    @NonNull
    private final ListView<LinkedPanel> listLeftPanel = new ListView<>();
    private final AnchorPane leftPane;
    private final AnchorPane rightPane;
    private @NonNull final Search search;
    @NonNull
    private final List<@NonNull AbstractInterface> filtersInterfaceList = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param search
     * @param leftPane
     * @param rightPane
     */
    public SearchHelper(@NonNull final Search search, @NonNull final AnchorPane leftPane, @NonNull final AnchorPane rightPane) {
        this.leftPane = leftPane;
        this.rightPane = rightPane;
        this.search = search;

        for (final SearchTypeEnum type : SearchTypeEnum.values()) {
            final SearchPanelInterface searchPanelInterface = type.getSearchPanelInterface();
            final GridPane gridPane = createGridPane();
            final LinkedPanel linkedPanel = new LinkedPanel(type, searchPanelInterface, gridPane);
            this.linkedPanelMap.put(type, linkedPanel);
        }

        initLeftPanel();
        initRightPanels();
        // Simulate a click on general
        final LinkedPanel generalLinkedPanel = this.linkedPanelMap.get(SearchTypeEnum.GENERAL);
        generalLinkedPanel.rightNode.setVisible(true);

        final ColumnConstraints columnConstraints = new ColumnConstraints(10.0, 812.0, 812.0);
        columnConstraints.setHgrow(Priority.SOMETIMES);
    }

    private void initRightPanels() {
        for (final LinkedPanel linkedPanel : this.linkedPanelMap.values()) {
            this.rightPane.getChildren().add(linkedPanel.getRightNode());
        }
    }

    private void initLeftPanel() {
        final ObservableList<LinkedPanel> observableList = FXCollections.observableArrayList(this.linkedPanelMap.values());
        this.listLeftPanel.setItems(observableList);
        this.listLeftPanel.setCellFactory(p -> new PanelListCell());

        this.listLeftPanel.setOnMouseClicked(event -> {
            // Set all right panels to not visible
            for (final LinkedPanel linkedPanel : observableList) {
                linkedPanel.rightNode.setVisible(false);
            }

            // set current right panel to visible
            final LinkedPanel currentLinkedPanel = SearchHelper.this.listLeftPanel.getSelectionModel().getSelectedItem();
            currentLinkedPanel.rightNode.setVisible(true);
        });

        this.leftPane.getChildren().add(this.listLeftPanel);
    }

    @NonNull
    private GridPane createGridPane() {
        final GridPane result = new GridPane();
        result.setPrefHeight(92.0);
        result.setPrefWidth(812.0);
        result.setHgap(2.0);
        result.setVgap(3.0);
        result.setVisible(false);
        return result;
    }

    public class LinkedPanel {

        private @NonNull final Node rightNode;
        private @NonNull final SearchPanelInterface leftNode;
        private @NonNull final SearchTypeEnum searchTypeEnum;

        /**
         * Constructor.
         *
         * @param searchTypeEnum type of the linked panel
         * @param leftSearchPanelInterface left node
         * @param rightNode right node
         */
        public LinkedPanel(@NonNull final SearchTypeEnum searchTypeEnum, @NonNull final SearchPanelInterface leftSearchPanelInterface, @NonNull final Node rightNode) {
            this.searchTypeEnum = searchTypeEnum;
            this.leftNode = leftSearchPanelInterface;
            this.rightNode = rightNode;
        }

        /**
         * @return the searchTypeEnum
         */
        @NonNull
        public final SearchTypeEnum getSearchTypeEnum() {
            return this.searchTypeEnum;
        }

        /**
         * @return the rightNode
         */
        @NonNull
        public final Node getRightNode() {
            return this.rightNode;
        }

        /**
         * @return the leftNode
         */
        @NonNull
        public final SearchPanelInterface getLeftSearchPanelInterface() {
            return this.leftNode;
        }
    }

    private final class PanelListCell extends ListCell<LinkedPanel> {

        @Override
        protected void updateItem(final LinkedPanel item, final boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setGraphic(item.getLeftSearchPanelInterface().getLeftPanelItem());
            }
        }
    }

    private void setSelectedPanels(@NonNull final SearchTypeEnum type) {
        final LinkedPanel selectedLinkedPanel = this.linkedPanelMap.get(type);
        final List<LinkedPanel> observableList = this.listLeftPanel.getItems();
        // Set all right panels to not visible
        for (final LinkedPanel linkedPanel : observableList) {
            linkedPanel.rightNode.setVisible(false);
        }

        // set current right panel to visible
        selectedLinkedPanel.rightNode.setVisible(true);
    }

    /**
     * Add Search Criteria into left and right panels.
     *
     * @param filtersInterface
     */
    public void addCriteria(@NonNull final AbstractInterface filtersInterface) {
        final GridPane rightGridPane = (GridPane) this.linkedPanelMap.get(filtersInterface.getType()).getRightNode();
        rightGridPane.setHgap(10); // horizontal gap in pixels => that's what you are asking for
        rightGridPane.setVgap(5); // vertical gap in pixels
        rightGridPane.setPadding(new Insets(10, 10, 10, 10)); // margins around the whole grid
        final int nextIndex = getMaxRows(rightGridPane) + 1;
        rightGridPane.addRow(nextIndex, filtersInterface.getRightPaneChoices());

        setSelectedPanels(filtersInterface.getType());
        filtersInterface.setListener(this);
        this.filtersInterfaceList.add(filtersInterface);
    }

    private int getMaxRows(@NonNull final GridPane gridPane) {
        final int maxIndex = gridPane.getChildren().stream().mapToInt(n -> {
            final Integer row = GridPane.getRowIndex(n);

            // default values are 0 / 1 respectively
            return (row == null ? 0 : row.intValue());
        }).max().orElse(-1);

        return maxIndex;
    }

    @Override
    public void delete(@NonNull final AbstractInterface filtersInterface) {
        final GridPane rightGridPane = (GridPane) this.linkedPanelMap.get(filtersInterface.getType()).getRightNode();
        final Integer index = GridPane.getRowIndex(filtersInterface.getRightPaneChoices());
        if (index != null) {
            deleteRow(rightGridPane, index.intValue());
            this.filtersInterfaceList.remove(filtersInterface);
        }
    }

    private void deleteRow(@NonNull final GridPane grid, final int row) {
        final Set<Node> deleteNodes = new HashSet<>();
        for (final Node child : grid.getChildren()) {
            // get index from child
            final Integer rowIndex = GridPane.getRowIndex(child);

            // handle null values for index=0
            final int r = rowIndex == null ? 0 : rowIndex.intValue();

            if (r > row) {
                // decrement rows for rows after the deleted row
                GridPane.setRowIndex(child, Integer.valueOf(r - 1));
            } else if (r == row) {
                // collect matching rows for deletion
                deleteNodes.add(child);
            }
        }

        // remove nodes from row
        grid.getChildren().removeAll(deleteNodes);
    }

    /**
     * Get existing filter list in the search.
     *
     * @return filters list
     */
    @NonNull
    public List<@NonNull AbstractInterface> getFiltersList() {
        return this.filtersInterfaceList;
    }

    /**
     * Get linked map.
     *
     * @return linked map
     */
    @NonNull
    public Map<@NonNull SearchTypeEnum, LinkedPanel> getLinkedPanelMap() {
        return this.linkedPanelMap;
    }
}
