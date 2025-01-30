package pl.zespolowy.view_controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.zespolowy.Language.*;
import pl.zespolowy.Words.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MainViewController {



    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab themesLanguagesTab;
    @FXML
    private Tab themesWordsTab;
    @FXML
    private Tab resultsTab;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/lpabn.png"));

        // Tworzenie ImageView
        ImageView imageView = new ImageView(image);

        // Ustawienia ImageView
        imageView.setFitWidth(anchorPane.getWidth());
        imageView.setFitHeight(anchorPane.getHeight());
        imageView.setPreserveRatio(true);


        // Dodanie ImageView do AnchorPane
        anchorPane.getChildren().add(imageView);
    }

}
