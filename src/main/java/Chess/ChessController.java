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
    }

    private void tegnBrett() {
        chessBoard.getChildren().clear();

        for (int rad = 0; rad < 8; rad++) {
            for (int kolonne = 0; kolonne <8 ; kolonne++) {

                Position pos = new Position(rad, kolonne); //logisk rute
                StackPane rute = new StackPane(); //visuell rute, kan legge elementer oppå hverandre

                Rectangle bakgrunn = new Rectangle( 80,80);

                if (erMarkert(pos)) {
                    bakgrunn.setFill(Color.YELLOWGREEN);
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
        if (brikke != null) { 
            // hvis ruten faktisk har en brikke
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
                valgtPosisjon = null;
                if (game.gameOver) {
            visGameOverTekst();
}
            } else if (brikke != null){
                valgtPosisjon = pos;
            } else { valgtPosisjon = null;}
    }
    }

    tegnBrett(); 
    // tegn hele brettet på nytt så markeringene oppdateres på skjermen
}

private boolean erMarkert(Position pos) {
    if (valgtPosisjon == null) {
        // hvis ingen brikke er valgt, skal ingen ruter markeres
        return false;
    }

    Brikke valgtBrikke = game.getBoard().getBrikke(valgtPosisjon); 
    // henter brikken som står på den valgte ruten

    if (valgtBrikke == null) {
        // sikkerhetssjekk: hvis valgt rute ikke har brikke likevel
        return false;
    }

    valgtBrikke.lovligetrekk(game.getBoard()); 
    // ber brikken regne ut hvilke trekk som er lovlige

    List<Position> trekk = valgtBrikke.getlovligetrekk(); 
    // henter lista med lovlige ruter

    return trekk.contains(pos); 
    // returnerer true hvis denne ruten er en av de lovlige trekkene
}

private void visGameOverTekst() {
    String vinner = game.getTurn().equals("W") ? "Svart vant" : "Hvit vant";
    winnerLabel.setText(vinner);
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
