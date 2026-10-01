package Chess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class ChessFileHandler {

    private final Path filHvit = Path.of("data", "Trekkhvit.txt");
    private final Path filSvart = Path.of("data", "Trekksvart.txt");

    public void skrivTrekk(Position fra, Position til, String color) throws IOException {
        skrivTrekk(fra, til, color, null);
    }

    public void skrivTrekk(Position fra, Position til, String color, String forvandling) throws IOException { //forvandling er null hvis trekket ikke er en bondeforvandling
        Files.createDirectories(filHvit.getParent());

        Path fil;
        if ("W".equals(color)) {
            fil = filHvit;
        } else {
            fil = filSvart;
        }

        Files.writeString(
            fil,
            fraPosisjontilTekst(fra, til) + forvandlingTekst(forvandling) + System.lineSeparator(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    public void nullstillFiler() throws IOException {
        Files.createDirectories(filHvit.getParent());
        Files.writeString(filHvit, "");
        Files.writeString(filSvart, "");
    }

    public String finnPosisjon(Position pos){ //standard notasjon: kolonne 0 er A, row 0 (hvits bakerste rad) er 1
        String col = String.valueOf((char) ('A' + pos.getcol()));
        String row = String.valueOf(pos.getrow() + 1);
        return col+row;
    }

    private String forvandlingTekst(String forvandling){ //norske bokstaver: =D, =T, =L eller =S
        if(forvandling == null){
            return "";
        }
        switch(forvandling){
            case "Queen": return "=D";
            case "Rook": return "=T";
            case "Bishop": return "=L";
            case "Horse": return "=S";
            default: return "";
        }
    }

    private String fraPosisjontilTekst(Position fra, Position til){
        return finnPosisjon(fra) + "->"+ finnPosisjon(til);
    }


}