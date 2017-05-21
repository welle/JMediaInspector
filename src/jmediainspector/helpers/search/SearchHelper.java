package jmediainspector.helpers.search;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import jmediainspector.helpers.search.enums.Type;
import jmediainspector.helpers.search.interfaces.SearchPanelInterface;

public class SearchHelper {

    public Map<Type, Map<Node, Node>> leftRightPanelsMap = new HashMap<>();

    public SearchHelper() {
        for (final Type type : Type.values()) {
            final Map<Node, Node> value = new HashMap<>();
            final SearchPanelInterface searchPanelInterface = type.getSearchPanelInterface();
            value.put(searchPanelInterface.getLeftPanelItem(), searchPanelInterface.getLeftPanelItem());
            this.leftRightPanelsMap.put(type, value);
        }
    }

    // set on click on left panel

    // set right panels visible or not

    // etc
}
