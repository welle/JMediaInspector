package aka.jmediainspector.helpers.search.types.text;

import aka.jmediainspector.helpers.search.interfaces.SearchPanelInterface;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchTextHelper implements SearchPanelInterface {

    @Override
    public Label getLeftPanelItem() {
        final Image image = new Image(getClass().getResourceAsStream("/images/text.png"), 25, 25, true, true);
        final ImageView graphic = new ImageView(image);
        final Label result = new Label("Text", graphic);

        return result;
    }
}
