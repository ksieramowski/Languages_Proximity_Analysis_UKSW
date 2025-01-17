package pl.zespolowy;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.javafx.FxGraphRenderer;
import pl.zespolowy.Business.Algorithm.*;
import pl.zespolowy.Controllers.MainSceneController;
import pl.zespolowy.Translation.DeepLTranslator;
import pl.zespolowy.Translation.Translator;
import pl.zespolowy.Words.WordSet;
import pl.zespolowy.graphs.ProximityGraphs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-scene.fxml"));
//        Parent root = fxmlLoader.load();
//        MainSceneController controller = fxmlLoader.getController();
//
//        Translator translator = new DeepLTranslator();
//        controller.setTranslator(translator);
//
//        Scene scene = new Scene(root, 800, 600);

        AppConfig.read();


        WordSetsTranslation wst = new WordSetsTranslation();
        WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
        LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
        languageProximity.countProximityAndFillLPRClasses();
        WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity, wst);

        ProximityGraphs proximityGraphs = new ProximityGraphs(wordsProximityNormalizer.getFinalResult(),
                                                              wordsProximityNormalizer.getResultByTopic());

        SingleGraph mainGraph = proximityGraphs.getOverallLanguageProximityGraph();
        SingleGraph themeGraph = proximityGraphs.getThemesLanguageProximityGraphs();

        HBox hBox = new HBox();

        // Overall
        FxViewer view1 = new FxViewer(mainGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view1.enableAutoLayout();
        FxViewPanel mainPanel = (FxViewPanel) view1.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        hBox.setHgrow(mainPanel, Priority.ALWAYS);
        hBox.setStyle("-fx-background-color: lightblue;");
        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
        hBox.getChildren().addAll(mainPanel);

        // Themes
        FxViewer view2 = new FxViewer(themeGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        view2.enableAutoLayout();
        FxViewPanel themePanel = (FxViewPanel) view2.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(themePanel);
        hBox.getChildren().addAll(stackPane);

        themePanel.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                // Reset the camera to the default view
                themePanel.getCamera().resetView();
                return;
            }

            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            Point3 graphCoordinates = themePanel.getCamera().transformPxToGu(x, y);

            themePanel.getCamera().setViewCenter(graphCoordinates.x, graphCoordinates.y, 0);

            // Zoom in
            themePanel.getCamera().setViewPercent(0.3);
        });


        Scene scene = new Scene(hBox, 1280, 720);

        stage.setTitle("Windows");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {



        launch();
    }
}