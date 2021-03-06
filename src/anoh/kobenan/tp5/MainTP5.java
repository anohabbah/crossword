package anoh.kobenan.tp5;

import anoh.kobenan.tp5.view.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainTP5 extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            primaryStage.setTitle("TP5 - Mots Croisés [Anoh & Kobenan]");
            primaryStage.setResizable(false);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainTP5.class.getResource("view/Principal.fxml"));
            BorderPane root = loader.load();
            PrincipalController rootController = loader.getController();

            FXMLLoader l = new FXMLLoader();
            l.setLocation(MainTP5.class.getResource("view/VueTP5.fxml"));
            BorderPane grille = l.load();

            rootController.setGrilleController(l.getController());

            root.setCenter(grille);

            Scene scene = new Scene(root, 550, 650);

            // Ctrl + W, ferme la fenetre
            final KeyCombination comb = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
                if (comb.match(event))
                    System.exit(0);
            });

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
