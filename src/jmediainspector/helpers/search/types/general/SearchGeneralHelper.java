package jmediainspector.helpers.search.types.general;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jmediainspector.helpers.search.types.interfaces.SearchPanelInterface;

public class SearchGeneralHelper implements SearchPanelInterface {

    @Override
    public Node getLeftPanelItem() {
        final Image image = new Image(getClass().getResourceAsStream("/images/general.png"), 25, 25, true, true);
        final ImageView graphic = new ImageView(image);
        final Label result = new Label("General", graphic);

        return result;
    }
}