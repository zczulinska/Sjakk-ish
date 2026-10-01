# Sjakk-link

Sjakk for to spillere, laget i Java med JavaFX. Navnet står for «sjakk klink»: full sjakk med alle de vanlige reglene.

Prosjektet startet som **Sjakk-ish**, en forenklet sjakkversjon laget individuelt i emnet TDT4100 Objektorientert programmering ved NTNU, våren 2026. Den versjonen er lagret i release `v1.0-sjakk-ish`. 

**Sjakk-link** er laget for å videreutvikle **Sjakk-ish**, men også for å utforske agentisk programmering. Refleksjoner om en slik framgangsmåte er innkludert til slutt. 

![Skjermbilde av Sjakk-link: svart konge står i sjakk og er markert i rødt. Kongen er valgt, og det eneste lovlige kongetrekket er markert i grønt](docs/skjermbilde.png)

## Om spillet

To spillere spiller mot hverandre på samme maskin. Hvit begynner, og spillet følger de vanlige sjakkreglene:

- **Sjakk:** Et trekk er ulovlig hvis det lar din egen konge stå i sjakk. Står du i sjakk, må du komme deg ut.
- **Sjakkmatt:** Står motstanderen i sjakk uten lovlige trekk, har du vunnet.
- **Patt:** Har motstanderen ingen lovlige trekk, men står ikke i sjakk, blir det remis.
- **Rokade:** Kort og lang rokade, når verken konge eller tårn har flyttet, rutene mellom er tomme, og kongen ikke står i, går over eller lander i sjakk.
- **En passant:** En bonde kan slå en motstanderbonde som akkurat har gått to steg forbi den, men bare i trekket rett etter.
- **Bondeforvandling:** En bonde som når siste rad, blir til dronning, tårn, løper eller springer. Spilleren velger selv.

Andre remisregler (50-trekksregelen, trekkgjentakelse og utilstrekkelig materiale) er bevisst utelatt. Står det for eksempel bare to konger igjen, kan spillerne starte et nytt parti med knappen.

## Funksjoner

- Grafisk sjakkbrett i JavaFX, sett fra hvits side
- Bare brikkene til den som har tur kan velges, og bare lovlige trekk markeres i grønt
- Status over brettet viser hvem sin tur det er og om kongen står i sjakk, og kongens rute farges rød
- Resultatet vises når spillet er over: hvem som vant ved sjakkmatt, eller remis ved patt
- Dialog for å velge brikke ved bondeforvandling
- Knapp for nytt parti
- Alle trekk lagres fortløpende til fil med standard sjakkoordinater (for eksempel `E2->E4`, eller `E7->E8=D` ved bondeforvandling), én fil per spiller, og vises i en liste ved siden av brettet

## Oppbygning

- `Brikke` er en abstrakt klasse, og hver brikketype (`Pawn`, `Rook`, `Horse`, `Bishop`, `Queen`, `King`) er en subklasse som selv regner ut hvordan den kan bevege seg.
- `Board` holder styr på brettets 64 ruter, utfører trekk og kan svare på om en rute er angrepet og om en konge står i sjakk.
- `ChessGame` implementerer grensesnittet `ChessRules` og styrer spillets gang. Den bestemmer hvem sin tur det er og hvilke trekk som er lovlige, fjerner trekk som setter egen konge i sjakk, håndterer rokade, en passant og bondeforvandling, og avgjør sjakkmatt og patt.
- `Position` validerer at en posisjon ligger på brettet.
- `ChessFileHandler` gjør om posisjoner til sjakkoordinater og skriver trekkene til fil.
- `ChessController` kobler modellen sammen med brukergrensesnittet (FXML).

## Testing

Prosjektet har 56 enhetstester skrevet med JUnit 5 (`src/test/java/Chess/ChessTest.java`). De dekker:

- startoppstillingen, turbytte og lagring av trekk til fil
- trekkene til hver brikketype
- sjakk, bundne brikker, og at kongen ikke kan gå inn i sjakk
- sjakkmatt (narrematt) og patt
- rokade, inkludert alle tilfellene der rokade ikke er lov
- en passant, inkludert en passant som ville blottlagt egen konge
- bondeforvandling til alle brikketyper

Mange av testene setter opp en egen stilling på et tomt brett (`Board.tomtBrett()`) i stedet for å spille seg fram dit fra start.

## Kjøre prosjektet

Krever Java 21 og Maven.

```bash
mvn javafx:run
```

Kjøre testene:

```bash
mvn test
```

Appen kan også startes ved å kjøre `ChessApp.java` direkte fra VS Code.

## Refleksjoner og Observasjoner

Sjakk-link ble kodet agentisk med Claude Code. Først ba jeg Claude lage en CLAUDE.md-fil som et sammendrag av prosjektet jeg hadde fra før. Deretter ba jeg Claude lage en PLAN.md-fil som den skulle følge. Dette var for at den ikke skulle lage mange forandringer uten min kontroll, og for at jeg skulle være sikker på at den forstod hva jeg ønsket. 

Underveis under kodingen kom KI selv med forslag som den implementerte, dette står under i PLAN.md under AI idéer. 

Å kode på denne måten er mye mer effektivt, men mindre lærerrikt for en som skal lære seg å kode såklart. 

Det interessante var at selv om KI lagde hele planen og koden, så ba jeg den tilsutt å se over hele koden og om alt er på plass. KI fant feil og forbedringer som den gjorde én siste gang før jeg sa meg ferdig. 
Dette var feil som rydding, niché tilfeller osv. 

---

Laget av Zofia Czulinska
