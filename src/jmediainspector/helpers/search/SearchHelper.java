package jmediainspector.helpers.search;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import jmediainspector.helpers.search.enums.SearchTypeEnum;
import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

public class SearchHelper {

    private final Map<SearchTypeEnum, Node> leftPanelsMap = new HashMap<>();
    private ListView<Node> listLeftPanel = new ListView<>();

    public SearchHelper() {
        for (final SearchTypeEnum type : SearchTypeEnum.values()) {
            final Map<Node, Node> value = new HashMap<>();
            final SearchPanelInterface searchPanelInterface = type.getSearchPanelInterface();
            value.put(searchPanelInterface.getLeftPanelItem(), searchPanelInterface.getLeftPanelItem());
            this.leftPanelsMap.put(type, searchPanelInterface.getLeftPanelItem());
        }
        final ObservableList<Node> list = FXCollections.observableArrayList(this.leftPanelsMap.values());
        this.listLeftPanel = new ListView<>(list);
        this.listLeftPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(final MouseEvent event) {
                System.out.println("clicked on " + SearchHelper.this.listLeftPanel.getSelectionModel().getSelectedItem());
            }
        });
    }

    @NonNull
    public ListView<Node> getLeftPanel() {
        return this.listLeftPanel;
    }

    // set on click on left panel

    // set right panels visible or not

    // etc
}
