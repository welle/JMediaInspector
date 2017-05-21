package jmediainspector.helpers.search.video;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jmediainspector.helpers.search.interfaces.SearchPanelInterface;

public class SearchVideoHelper implements SearchPanelInterface {

    @Override
    public Node getLeftPanelItem() {
        final Image image = new Image(getClass().getResourceAsStream("/images/audio.png"));
        final Label result = new Label("Search", new ImageView(image));

        return result;
    }
}
