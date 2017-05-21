package jmediainspector.controllers;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jmediainspector.listeners.ApplicationConfigurationsListener;

public class SearchCriteriaControler extends AnchorPane implements ApplicationConfigurationsListener {

    @NonNull
    private final static Logger LOGGER = Logger.getLogger(PlexToolsTabControler.class.getName());

    @FXML
    private void addVideoResolutionCriteria() {

    }

    @Override
    public void onChanges() {
        // reload file
    }
}
