package Chess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;


public class ChessFileHandler {

    private final Path filHvit = Path.of("data", "Trekkhvit.txt");
    private final Path filSvart = Path.of("data", "Trekksvart.txt");

    public void skrivTrekk(Position fra, Position til, String color) throws IOException {
        Files.createDirectories(filHvit.getParent());

        Path fil;
        if ("W".equals(color)) {
            fil = filHvit;
        } else {
            fil = filSvart;
        }

        Files.writeString(
            fil,
            fraPosisjontilTekst(fra, til) + System.lineSeparator(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    public String lesTrekk(Path fil){
        StringBuilder ut = new StringBuilder();
        try{
            List<String> innhold = Files.readAllLines(fil);
            for(String line : innhold){
                ut.append(line).append("\n");
            }
            return ut.toString();}
        catch(IOException e){
            e.printStackTrace();
            return "";}
        
    }
    
    public void nullstillFiler() throws IOException {
        Files.createDirectories(filHvit.getParent());
        Files.writeString(filHvit, "");
        Files.writeString(filSvart, "");
    }

    public String finnPosisjon(Position pos){
        String row = "";
        String col = "";
        if(pos.getcol()==0){col = "A";}
        if(pos.getcol()==1){col = "B";}
        if(pos.getcol()==2){col = "C";}
        if(pos.getcol()==3){col = "D";}
        if(pos.getcol()==4){col = "E";}
        if(pos.getcol()==5){col = "F";}
        if(pos.getcol()==6){col = "G";}
        if(pos.getcol()==7){col = "H";}
    
        if(pos.getrow() == 0){row = "8";}
        if(pos.getrow() == 1){row = "7";}
        if(pos.getrow() == 2){row = "6";}
        if(pos.getrow() == 3){row = "5";}
        if(pos.getrow() == 4){row = "4";}
        if(pos.getrow() == 5){row = "3";}
        if(pos.getrow() == 6){row = "2";}
        if(pos.getrow() == 7){row = "1";}

        return col+row;
    }

    private String fraPosisjontilTekst(Position fra, Position til){
        return finnPosisjon(fra) + "->"+ finnPosisjon(til);
    }


}