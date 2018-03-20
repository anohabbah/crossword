package anoh.kobenan.tp5.view;

import anoh.kobenan.tp5.MainTP5;
import anoh.kobenan.tp5.model.MotsCroisesTP5;
import anoh.kobenan.tp5.model.db.ChargerGrille;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.Map;

public class PrincipalController {

    private MainTP5 app;
    ChargerGrille grille;
    MotsCroisesTP5 mc;

    @FXML
    public MenuItem exit;

    @FXML
    private Menu list;

    @FXML
    private MenuItem random;

    @FXML
    public void initialize() {
        this.grille = new ChargerGrille();
        Map<Integer, String> jeux = this.grille.ListeJeu();

        for (Integer cle : jeux.keySet()) {
            String nomJeu = jeux.get(cle);
            MenuItem item = new MenuItem(nomJeu);

            item.setOnAction(event -> this.genererGrille(cle));

            this.list.getItems().add(item);
        }
    }

    @FXML
    public void exit() {
        System.exit(1);
    }

    @FXML
    public void random() throws Exception {
        int numGrille = 1 + (int) (Math.random() * 11);
        this.genererGrille(numGrille);
    }

    private void genererGrille(int numGrille) {
        try {
            this.mc = this.grille.extraireGrille(numGrille);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ControllerTP5 controller = this.app.getGrilleController();
        controller.setMotsCroises(this.mc);
        controller.setUp();

    }

    public void setMainApp(MainTP5 app) {
        this.app = app;
    }
}
