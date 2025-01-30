package pl.zespolowy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
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

        AppConfig.read();
        stage.setResizable(false);
        stage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("/lpalogo.png"))
        );
        Scene scene = new Scene(root, 600,400);

        stage.setTitle("Language Proximity Analysis");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {

        launch();
    }
}