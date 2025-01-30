package pl.zespolowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.zespolowy.Business.Algorithm.*;
import pl.zespolowy.Translation.DeepLTranslator;
import pl.zespolowy.Translation.Translator;
import pl.zespolowy.view_controllers.MainViewController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchFieldException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainView.fxml"));
        Parent root = fxmlLoader.load();
        MainViewController controller = fxmlLoader.getController();

        Translator translator = new DeepLTranslator();


        AppConfig.read();
//
//
//        WordSetsTranslation wst = new WordSetsTranslation();
//        WordSetsRegrouper wordSetsRegroup = new WordSetsRegrouper(wst);
//        LanguageSimilarityCalculator languageProximity = new LanguageSimilarityCalculator(wordSetsRegroup);
//        languageProximity.countProximityAndFillLPRClasses();
//        WordsProximityNormalizer wordsProximityNormalizer = new WordsProximityNormalizer(languageProximity, wst);
//
//        ProximityGraphs proximityGraphs = new ProximityGraphs(wordsProximityNormalizer.getFinalResult(),
//                                                              wordsProximityNormalizer.getResultByTopic());
//
//        SingleGraph mainGraph = proximityGraphs.getOverallLanguageProximityGraph();
//        SingleGraph themeGraph = proximityGraphs.getThemesLanguageProximityGraphs();
//
//
//        // Overall
//        FxViewer view1 = new FxViewer(mainGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        view1.enableAutoLayout();
//        FxViewPanel mainPanel = (FxViewPanel) view1.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
//
//        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
//
//        // Themes
//        FxViewer view2 = new FxViewer(themeGraph, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//        view2.enableAutoLayout();
//        FxViewPanel themePanel = (FxViewPanel) view2.addView(FxViewer.DEFAULT_VIEW_ID, new FxGraphRenderer());
//        //FxDefaultView panel = (FxDefaultView) view.addDefaultView(false);
//
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().addAll(themePanel);
//
//        HBox hBox = new HBox();
//        hBox.setStyle("-fx-background-color: lightblue;");
//        hBox.getChildren().addAll(mainPanel);
//        HBox.setHgrow(mainPanel, Priority.ALWAYS);
//        hBox.getChildren().addAll(stackPane);
//        HBox.setHgrow(stackPane, Priority.ALWAYS);
//
//        themePanel.setOnMouseClicked(mouseEvent -> {
//            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                // Reset the camera to the default view
//                themePanel.getCamera().resetView();
//                return;
//            }
//
//            double x = mouseEvent.getX();
//            double y = mouseEvent.getY();
//
//            Point3 graphCoordinates = themePanel.getCamera().transformPxToGu(x, y);
//
//            themePanel.getCamera().setViewCenter(graphCoordinates.x, graphCoordinates.y, 0);
//
//            // Zoom in
//            themePanel.getCamera().setViewPercent(0.3);
//        });

        Scene scene = new Scene(root, 600,400);
        //Scene scene = new Scene(hBox, 1280, 720);

        stage.setTitle("Language Proximity Analysis");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {

        launch();
    }
}