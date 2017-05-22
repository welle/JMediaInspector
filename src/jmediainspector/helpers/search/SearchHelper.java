package jmediainspector.helpers.search;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

public class SearchHelper {

    private final Map<SearchTypeEnum, LinkedPanel> linkedPanelMap = new LinkedHashMap<>();
    private final ListView<LinkedPanel> listLeftPanel = new ListView<>();
    private final AnchorPane leftPane;
    private final AnchorPane rightPane;

    /**
     * Constructor.
     *
     * @param leftPane
     * @param rightPane
     */
    public SearchHelper(final AnchorPane leftPane, final AnchorPane rightPane) {
        this.leftPane = leftPane;
        this.rightPane = rightPane;

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

        this.listLeftPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                // Set all right panels to not visible
                for (final LinkedPanel linkedPanel : observableList) {
                    linkedPanel.rightNode.setVisible(false);
                }

                // set current right panel to visible
                final LinkedPanel currentLinkedPanel = SearchHelper.this.listLeftPanel.getSelectionModel().getSelectedItem();
                currentLinkedPanel.rightNode.setVisible(true);
            }
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

    private class LinkedPanel {

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

    @NonNull
    public Node getRightPane(@NonNull final SearchTypeEnum type) {
        return this.linkedPanelMap.get(type).getRightNode();
    }
}
