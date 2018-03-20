package anoh.kobenan.tp5.view;

import anoh.kobenan.tp5.model.IterateurMots;
import anoh.kobenan.tp5.model.MotsCroisesTP5;
import anoh.kobenan.tp5.model.db.ChargerGrille;
import anoh.kobenan.tp5.util.ControlsArray;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Created by 18018881 on 22/02/2018.
 */
public class ControllerTP5 {

    private MotsCroisesTP5 mc;
    private ControlsArray<TextField> gridSnap;
    private Direction curDirection = Direction.HORIZONTAL;
    private TextField currentField = null;

    @FXML
    private GridPane monGridPane;

    @FXML
    public void initialize() {
        ChargerGrille loader = new ChargerGrille();
        try {
            this.mc = loader.extraireGrille(ChargerGrille.CHOIX_GRILLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setUp();
    }

    public void setUp() {
        TextField modele = (TextField) this.monGridPane.getChildren().get(0);
        this.monGridPane.getChildren().clear();

        for (int lig = 1; lig <= this.mc.getHauteur(); ++lig) {
            for (int col = 1; col <= this.mc.getLargeur(); ++col) {
                if (!this.mc.estCaseNoire(lig, col)) {
                    TextField tf = new TextField();
                    tf.setAlignment(Pos.CENTER);
                    tf.setPrefWidth(modele.getPrefWidth());
                    tf.setPrefHeight(modele.getPrefHeight());

                    for (Object cle : modele.getProperties().keySet()) {
                        tf.getProperties().put(cle, modele.getProperties().get(cle));
                    }

                    this.monGridPane.add(tf, col - 1, lig - 1);
                }
            }
        }

        this.settings();
    }

    /**
     * Configuration de la grille des mots croisés.
     * Ajoute les bindings et des écoutes d'évènement sur chaque text field de la grille.
     */
    private void settings() {
        // Initialisation de gridSnap
        this.gridSnap = new ControlsArray<>(this.mc.getHauteur(), this.mc.getLargeur());

        for (Node n : this.monGridPane.getChildren()) {
            if (n instanceof TextField) {
                TextField tf = (TextField) n;
                int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
                int col = ((int) n.getProperties().get("gridpane-column")) + 1;

                tf.textProperty().bindBidirectional(this.mc.propositionProperty(lig, col));
                tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.isEmpty()) {
                        this.currentField.getStyleClass().remove("has-error");
                        this.currentField.getStyleClass().remove("is-success");
                        ScaleTransition transition = new ScaleTransition(Duration.millis(500), tf);
                        // From 0%
                        transition.setFromX(0.0);
                        transition.setFromY(0.0);
                        // To 100%
                        transition.setToX(1.0);
                        transition.setToY(1.0);
                        transition.setAutoReverse(true);
                        transition.play();
                    }
                });

                tf.lengthProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.intValue() > oldValue.intValue() && tf.getText().length() >= 1)
                        tf.setText(tf.getText().trim().substring(0, 1));

                });

                String defHoriz = this.mc.getDefinition(lig, col, true);
                String defVert = this.mc.getDefinition(lig, col, false);
                if (defHoriz != null && defVert != null)
                    tf.setTooltip(new Tooltip(defHoriz + " / " + defVert));
                else if (defHoriz != null)
                    tf.setTooltip(new Tooltip(defHoriz));
                else if (defVert != null)
                    tf.setTooltip(new Tooltip(defVert));


                tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        currentField = tf;
                        currentField.getStyleClass().add("is-current");
                    }

                    if (oldValue)
                        tf.getStyleClass().remove("is-current");
                });
                tf.setOnMouseClicked(this::clicCase);
                tf.setOnKeyReleased(this::keyPressedCase);
                this.gridSnap.add(lig, col, tf);
            }
        }

        TextField firstField = (TextField) this.monGridPane.getChildren().get(0);
        firstField.requestFocus();
    }

    private void keyPressedCase(KeyEvent e) {
        KeyCode code = e.getCode();
        TextField tf = (TextField) e.getSource();

        if (code.isLetterKey()) {
            this.moveTo(true, tf);
        }

        switch (code) {
            case UP:
                this.curDirection = Direction.VERTICAL;
                this.moveTo(false, tf);
                break;

            case DOWN:
                this.curDirection = Direction.VERTICAL;
                this.moveTo(true, tf);
                break;

            case LEFT:
                this.curDirection = Direction.HORIZONTAL;
                this.moveTo(false, tf);
                break;

            case RIGHT:
                this.curDirection = Direction.HORIZONTAL;
                this.moveTo(true, tf);
                break;

            case ENTER:
                boolean horiz = this.curDirection == Direction.HORIZONTAL;
                int num = horiz
                        ? ((int) ((TextField) e.getSource()).getProperties().get("gridpane-row")) + 1
                        : ((int) ((TextField) e.getSource()).getProperties().get("gridpane-column")) + 1;
                int length = horiz ? this.mc.getLargeur() : this.mc.getHauteur();

                int i = 1;
                IterateurMots it = this.gridSnap.iterator(horiz, num);
                while (it.hasNext() && i <= length) {
                    TextField value = (TextField) it.next();
                    if (!this.mc.estCaseNoire(horiz ? num : i, horiz ? i : num) &&
                            value.getText().equals(this.mc.getSolution(horiz ? num : i, horiz ? i : num) + "")) {
                        value.getStyleClass().add("is-success");
                    } else {
                        value.getStyleClass().add("has-error");
                    }

                    ++i;
                }
                break;

            case BACK_SPACE:
                currentField.setText("");
                this.moveTo(false, currentField);
                break;

            default: // Désactiver le comportement par defaut de toutes les autres touches
        }
    }

    private void moveTo(boolean next, TextField src) {
        int lig = ((int) src.getProperties().get("gridpane-row")) + 1;
        int col = ((int) src.getProperties().get("gridpane-column")) + 1;
        switch (this.curDirection) {
            case HORIZONTAL:
                src = this.gridSnap.getNextNotNullValue(lig, next ? ++col : --col, true, next);
                break;

            case VERTICAL:
                src = this.gridSnap.getNextNotNullValue(next ? ++lig : --lig, col, false, next);
                break;
        }

        src.requestFocus();
    }

    @FXML
    private void clicCase(MouseEvent e) {
        if (e.getButton() == MouseButton.MIDDLE) {
            TextField cas = (TextField) e.getSource();
            int lig = ((int) cas.getProperties().get("gridpane-row")) + 1;
            int col = ((int) cas.getProperties().get("gridpane-column")) + 1;
            cas.getStyleClass().remove("has-error");
            cas.getStyleClass().remove("is-success");
            this.mc.reveler(lig, col);
            moveTo(true, cas);
        }
    }

    public void setMotsCroises(MotsCroisesTP5 mc) {
        this.mc = mc;
    }

    private enum Direction {HORIZONTAL, VERTICAL}
}
