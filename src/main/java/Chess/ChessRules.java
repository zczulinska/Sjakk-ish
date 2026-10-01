package Chess;

import java.util.List;

public interface ChessRules {


    void PlayerTurnChange();
    
    Boolean Move(Position position, Position newposition);

    List<Position> lovligeTrekk(Position fra);

    Boolean isGameOver();

    String getWinner();

    Board getBoard();

    String getTurn();

    void erSjakkMatt();

    
}
