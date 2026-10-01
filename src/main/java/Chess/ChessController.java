package Chess;

import java.util.List;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class ChessController {

    @FXML
private VBox gameOverBox;

@FXML
private Label winnerLabel;

@FXML
private Label statusLabel;
    @FXML
    private GridPane chessBoard;

    private ChessGame game;
    private Position valgtPosisjon = null;

    @FXML
private TextArea whiteMovesArea;

@FXML
private TextArea blackMovesArea;

    @FXML
    public void initialize() {
        game = new ChessGame();
        tegnBrett();
        oppdaterTrekkVisning();
        oppdaterStatus();
    }

    private void tegnBrett() {
        chessBoard.getChildren().clear();
        List<Position> markerte = markerteRuter(); //regnes ut én gang per tegning, ikke for hver rute
        Position kongeISjakk = game.erSjakk() ? game.getBoard().finnKonge(game.getTurn()) : null;

        for (int rad = 0; rad < 8; rad++) {
            for (int kolonne = 0; kolonne <8 ; kolonne++) {

                Position pos = new Position(rad, kolonne); //logisk rute
                StackPane rute = new StackPane(); //visuell rute, kan legge elementer oppå hverandre

                Rectangle bakgrunn = new Rectangle( 80,80);

                if (markerte.contains(pos)) {
                    bakgrunn.setFill(Color.YELLOWGREEN);
                } else if (pos.equals(kongeISjakk)) {
                    bakgrunn.setFill(Color.INDIANRED);
                } else if ((rad + kolonne) % 2 == 0) {
                    bakgrunn.setFill(Color.DARKSLATEGREY);
                } else {
                    bakgrunn.setFill(Color.LIGHTSLATEGRAY);
                }

                rute.getChildren().add(bakgrunn);

                Brikke brikke = game.getBoard().getBrikke(pos);
                if (brikke != null) {
                    Image image = new Image(getClass().getResourceAsStream(brikke.getImagePath()));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(true);

                    rute.getChildren().add(imageView);
                }

                rute.setOnMouseClicked(e -> håndterKlikk(pos)); //det som skjer når ruten blir klikket

                chessBoard.add(rute, kolonne, rad); //legge ruten i riktg kolonne og rad
            }
        }
    }

    private void håndterKlikk(Position pos) {
    Brikke brikke = game.getBoard().getBrikke(pos); 
    // finner ut om det står en brikke på ruten brukeren klikket på

    if (valgtPosisjon == null) { 
        // hvis ingen rute er valgt fra før
        if (erEgenBrikke(brikke)) { 
            // hvis ruten har en brikke som tilhører den som har tur
            valgtPosisjon = pos; 
            // velg denne ruten
        }

    } else {
        // hvis en rute allerede er valgt
        if (valgtPosisjon.equals(pos)) { 
            // hvis brukeren klikker på samme rute igjen
            valgtPosisjon = null; 
            // fjern valget
        } else {
            // hvis brukeren klikker på en annen rute
            boolean flyttet = game.Move(valgtPosisjon, pos);
            if(flyttet){
                oppdaterTrekkVisning();
                oppdaterStatus();
                valgtPosisjon = null;
            } else if (erEgenBrikke(brikke)){
                // ugyldig trekk, men brukeren klikket på en annen av sine egne brikker
                valgtPosisjon = pos;
            } else { valgtPosisjon = null;}
    }
    }

    tegnBrett(); 
    // tegn hele brettet på nytt så markeringene oppdateres på skjermen
}

private boolean erEgenBrikke(Brikke brikke) {
    // bare brikkene til den som har tur kan velges
    return brikke != null && brikke.getColor().equals(game.getTurn());
}

private List<Position> markerteRuter() {
    if (valgtPosisjon == null) {
        // hvis ingen brikke er valgt, skal ingen ruter markeres
        return List.of();
    }

    return game.lovligeTrekk(valgtPosisjon);
    // spillet regner ut hvilke trekk som er lovlige, også at egen konge ikke havner i sjakk
}

private void oppdaterStatus() {
    if (game.isGameOver()) {
        statusLabel.setText("Spillet er over");
        visGameOverTekst();
        return;
    }
    String spiller = game.getTurn().equals("W") ? "Hvit" : "Svart";
    if (game.erSjakk()) {
        statusLabel.setText("Sjakk! " + spiller + " sin tur");
    } else {
        statusLabel.setText(spiller + " sin tur");
    }
}

private void visGameOverTekst() {
    if (game.erPatt()) {
        winnerLabel.setText("Remis (patt)");
    } else {
        String vinner = game.getWinner().equals("W") ? "Hvit" : "Svart";
        winnerLabel.setText(vinner + " vant ved sjakkmatt");
    }
    gameOverBox.setVisible(true);
}

private void oppdaterTrekkVisning() {
    try {
        whiteMovesArea.setText(fileInnhold("data/Trekkhvit.txt"));
        blackMovesArea.setText(fileInnhold("data/Trekksvart.txt"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private String fileInnhold(String filsti) {
    try {
        return java.nio.file.Files.readString(java.nio.file.Path.of(filsti));
    } catch (java.io.IOException e) {
        return "";
    }
}

}
