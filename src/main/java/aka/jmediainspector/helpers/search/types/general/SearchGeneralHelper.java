package aka.jmediainspector.helpers.search.types.general;

import aka.jmediainspector.helpers.search.interfaces.SearchPanelInterface;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchGeneralHelper implements SearchPanelInterface {

    @Override
    public Label getLeftPanelItem() {
        final Image image = new Image(getClass().getResourceAsStream("/images/general.png"), 25, 25, true, true);
        final ImageView graphic = new ImageView(image);
        final Label result = new Label("General", graphic);

        return result;
    }
}
