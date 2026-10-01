# CLAUDE.md

## Prosjektets historie og mål

- Prosjektet het tidligere **Sjakk-ish**: en forenklet sjakkversjon (ingen sjakk/sjakkmatt, rokade, en passant eller bondeforvandling; spillet vinnes ved å slå kongen). Denne versjonen er lagret i release/tag `v1.0-sjakk-ish`.
- Nå skal det gjøres om til **Sjakk-link** (står for «sjakk klink»), som skal være **full sjakk** med alle reglene.
- README.md beskriver fortsatt Sjakk-ish og oppdateres til slutt (steg 12 i `PLAN.md`).

## Arbeidsregler

- **Kjør `mvn test` etter hver endring.** Ikke gå videre før testene er grønne.
- **Jobb i små steg.** Én regel eller én refaktorering om gangen.
- **Forklar hva du har endret** etter hvert steg: hvilke filer, hva og hvorfor.
- Følg planen i `PLAN.md`, og kryss av steget (`- [x]`) når det er ferdig og testene er grønne.
- Skriv på norsk (kommentarer, forklaringer), i tråd med eksisterende kode.

## Bygg og kjøring

Krever Java 21 og Maven.

```bash
mvn test          # kjør testene (JUnit 5)
mvn javafx:run    # start appen
```

Mappene `data/` (trekkfiler) og `target/` er genererte og skal ikke committes.

## Oppbygning

All kode ligger i pakken `Chess` (`src/main/java/Chess/`), modulen heter `TDT4100_project` (`module-info.java`).

| Klasse | Ansvar |
|---|---|
| `Brikke` | Abstrakt superklasse for alle brikker: farge (`"W"`/`"B"`), posisjon, `lovligetrekk(Board)`, `getlovligetrekk()`, `getImagePath()`, `setBrukt()`. |
| `Pawn`, `Rook`, `Horse` (springer), `Bishop`, `Queen`, `King` | Hver subklasse regner ut sine egne pseudo-lovlige trekk i en egen liste `muligetrekk`. |
| `Board` | 8×8-array `Brikke[][]`, setter opp startstillingen, `getBrikke(Position)` og `movePiece(fra, til)`. |
| `Position` | Uforanderlig rad/kolonne (0–7). Kaster `IllegalArgumentException` utenfor brettet. Har `equals`/`hashCode`. |
| `ChessRules` | Grensesnitt for spillogikken. |
| `ChessGame` | Implementerer `ChessRules`: tur, validering og utføring av trekk (`Move`), lovlige trekk (`lovligeTrekk`), sjakk, sjakkmatt og patt, vinner, logging til fil. |
| `ChessFileHandler` | Gjør posisjoner om til koordinater (`finnPosisjon`) og skriver trekk til `data/Trekkhvit.txt` og `data/Trekksvart.txt`. Filene nullstilles når et nytt `ChessGame` lages. |
| `ChessController` | JavaFX-kontroller for `ChessApp.fxml`: tegner brettet, håndterer klikk, markerer lovlige trekk og viser trekklister og vinner. |
| `ChessApp` | JavaFX-oppstart. |

Ressurser (FXML og brikkebilder) ligger i `src/main/resources/Chess/`. Testene ligger i `src/test/java/Chess/ChessTest.java`.

## Konvensjoner og særegenheter å kjenne til

- **Koordinater:** `row 0` er hvit bakerste rad (vises øverst i GUI), `row 7` er svart. Hvite bønder går mot økende `row`. `finnPosisjon` mapper `row 0 → "8"` og `col 0 → "A"`, så hvite trekk logges med svarts rader (f.eks. hvit springer `B8->C6`). Dette er ikke standard notasjon; testen `move_writeToFile` avhenger av det.
- **Trekkgenerering:** brikkene lager posisjoner med `new Position(...)` og fanger `IllegalArgumentException` for å hoppe over ruter utenfor brettet.
- **`setBrukt()`** brukes av `Pawn` (dobbelsteg) og av `King` og `Rook` (rokade). Andre brikker har tomme implementasjoner.
- **En passant:** `Board` husker ruten en bonde hoppet over (`enPassantRute`), og `ChessGame.Move` setter den etter hvert trekk. `Pawn.erEnPassant` avgjør om et slag er en passant, og `ChessGame.slåttRute` finner bonden som skal fjernes, både ved selve trekket og når trekket prøves i sjakksjekken.
- **Rokade** genereres i `ChessGame.rokadeTrekk`, ikke i `King.lovligetrekk`. Ellers ville angrepssjekken (`Board.erAngrepet`) kalt seg selv i det uendelige.
- **`Brikke.getlovligetrekk`-feltet** i superklassen brukes ikke; alle subklasser overstyrer `getlovligetrekk()`.
- **Spillslutt:** etter hvert trekk sjekker `ChessGame` om den som har tur har lovlige trekk. Ingen trekk og i sjakk gir sjakkmatt, ingen trekk uten sjakk gir patt (remis, `getWinner()` er `null`).
- **Kontrolleren** kaller `lovligetrekk` direkte for å markere ruter. Når trekkvalidering (f.eks. at egen konge ikke kan stå i sjakk) flyttes inn i `ChessGame`, må markeringen bruke samme logikk.
- `ChessGame.gameOver` og `fileHandler` er `public` og brukes direkte av kontrolleren.

## Det som mangler for full sjakk

1. Sjakk: et trekk er ulovlig hvis det etterlater egen konge i sjakk
2. Sjakkmatt og patt som avslutter spillet (i stedet for at kongen slås)
3. Rokade (kort og lang)
4. En passant
5. Bondeforvandling

Andre remisregler enn patt (50-trekksregelen, trekkgjentakelse, utilstrekkelig materiale) skal ikke implementeres.
