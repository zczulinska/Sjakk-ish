package Chess;

public interface ChessRules {


    void PlayerTurnChange();
    
    Boolean Move(Position position, Position newposition);

    Boolean isGameOver();

    String getWinner();

    Board getBoard();

    String getTurn();

    void erSjakkMatt();

    
}
