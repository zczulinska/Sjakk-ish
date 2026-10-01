# Plan: fra Sjakk-ish til Sjakk-link

Målet er full sjakk med sjakk, sjakkmatt, patt, rokade, en passant og bondeforvandling. Spillet vinnes ved sjakkmatt, ikke ved å slå kongen.

Etter hvert steg kjøres `mvn test`, og det forklares hva som er endret. Steget krysses av når det er ferdig og testene er grønne.

## Om de eksisterende testene

Ingen av de 9 eksisterende testene tester at spillet vinnes ved å slå kongen, eller andre regler som forsvinner. Alle trekkene i testene er også lovlige i full sjakk, så de skal passere uendret.

To tester må endres i steg 11 (standard notasjon):
- `positionToCoordinate` forventer at rad 0 skrives som «8».
- `move_writeToFile` forventer at et hvitt springertrekk logges som `B8->C6`.

Med standard notasjon blir det `B1->C3`. Testene ville da sjekke en notasjon vi bevisst har gått bort fra, og det er grunnen til at de må endres.

## Steg

- [x] **Steg 0: Navnebytte og opprydding** (ingen logikk)
  - Bytte Sjakk-ish til Sjakk-link i `pom.xml` og i vindustittelen i `ChessApp`.
  - Legge til `.gitignore` for `data/` og `target/`.

- [x] **Steg 1: Gjøre det mulig å teste egne stillinger**
  - `Board` får en måte å lage et tomt brett på og sette ut enkeltbrikker. `ChessGame` kan startes med et ferdig brett.
  - Uten dette må hver test spille seg fram til stillingen trekk for trekk.
  - Tester: tomt brett og utsetting av brikker.

- [x] **Steg 2: Angrepssjekk**
  - `Board` får en metode som sier om en rute er angrepet av en gitt farge. Den brukes for å finne ut om kongen står i sjakk.
  - Bonden behandles spesielt. Den angriper skrått også når ruten er tom, mens `lovligetrekk` bare legger til skrå trekk når det står en motstander der.
  - Tester: tårn, løper, springer, bonde og konge gir sjakk, og en brikke i veien blokkerer.

- [ ] **Steg 3: Trekk som setter egen konge i sjakk blir ulovlige**
  - `ChessGame` får `lovligeTrekk(Position)`. Den prøver hvert trekk midlertidig på brettet og fjerner trekk der egen konge står i sjakk etterpå.
  - `Move` bruker denne metoden.
  - Tester: en bundet brikke kan ikke flytte, en konge i sjakk må komme seg ut, og kongen kan ikke gå inn i sjakk.

- [ ] **Steg 4: Grafikken markerer bare lovlige trekk**
  - `ChessController` bruker `game.lovligeTrekk` i stedet for å spørre brikken direkte.

- [ ] **Steg 5: Sjakkmatt og patt avslutter spillet**
  - Etter hvert trekk sjekkes det om motstanderen har lovlige trekk igjen:
    - Ingen trekk og står i sjakk: sjakkmatt, og den som trakk vinner.
    - Ingen trekk og står ikke i sjakk: patt, og det blir remis.
  - `erSjakkMatt()`, som teller konger, fjernes. `ChessRules` oppdateres, for eksempel med `erSjakk()` og en måte å se resultatet på.
  - Tester: narrematt (de fire trekkene f3, e5, g4, Dh4#), en kjent pattstilling, og at sjakk alene ikke avslutter spillet.

- [ ] **Steg 6: Vise resultatet i grafikken**
  - Vise «Sjakk!» når kongen står i sjakk.
  - Vise «Hvit/Svart vant ved sjakkmatt» eller «Remis (patt)», lest fra `ChessGame` i stedet for å utledes fra hvem sin tur det er.

- [ ] **Steg 7: Rokade**
  - `King` og `Rook` får ekte `setBrukt()`.
  - Kongen kan rokere når:
    - verken konge eller tårn har flyttet,
    - rutene mellom dem er tomme,
    - kongen ikke står i sjakk,
    - kongen ikke passerer eller lander på en angrepet rute.
  - Når kongen rokerer, flytter tårnet med.
  - Rokadetrekkene holdes utenfor angrepssjekken, så de to sjekkene ikke kaller hverandre i det uendelige.
  - Tester: kort og lang rokade, og at rokade nektes i hvert av tilfellene over.

- [ ] **Steg 8: En passant**
  - `Board` husker hvilken rute som kan slås en passant etter et dobbeltsteg, og nullstiller den etter neste trekk.
  - Når en bonde slår en passant, fjernes den slåtte bonden.
  - Tester: lovlig en passant, at muligheten forsvinner etter ett trekk, og en passant som ville blottlagt egen konge.

- [ ] **Steg 9: Bondeforvandling i modellen**
  - `Move` får en variant der man velger brikke. Vanlig `Move` gir dronning.
  - Tester: forvandling til dronning og til springer, og forvandling der bonden slår en brikke.

- [ ] **Steg 10: Bondeforvandling i grafikken**
  - En dialog lar spilleren velge mellom dronning, tårn, løper og springer.

- [ ] **Steg 11: Standard notasjon**
  - `finnPosisjon` endres slik at hvit står på rad 1 og 2. De to testene nevnt over oppdateres.

- [ ] **Steg 12: Oppdatere README**
  - README beskriver Sjakk-link og de nye reglene.

## Avklaringer

1. **Bondeforvandling:** Spilleren velger brikke selv (steg 10).
2. **Standard notasjon:** Tas med (steg 11).
3. **Remis:** Bare patt. 50-trekksregelen, trekkgjentakelse og utilstrekkelig materiale holdes utenfor.
