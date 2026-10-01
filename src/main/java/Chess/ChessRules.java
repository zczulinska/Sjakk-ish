package Chess;

import java.util.List;

public interface ChessRules {


    void PlayerTurnChange();
    
    Boolean Move(Position position, Position newposition);

    Boolean Move(Position position, Position newposition, String forvandling);

    boolean erBondeforvandling(Position fra, Position til);

    List<Position> lovligeTrekk(Position fra);

    Boolean isGameOver();

    String getWinner();

    Board getBoard();

    String getTurn();

    Boolean erSjakk();

    Boolean erSjakkMatt();

    Boolean erPatt();

    
}
