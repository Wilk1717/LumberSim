# Symulacja dynamiki ekosystemu leśnego

Projekt napisany w języku Java z wykorzystaniem biblioteki Swing do renderowania interfejsu graficznego. 
Głównym tematem jest symulacja ekosystemu leśnego. Zaimplementowane zostały zaawansowane interakcje fizyczne i ekonomiczne między różnymi typami agentów (drwale, strażnicy leśni) oraz samym środowiskiem (mechanika odrastania lasu, zapętlona topologia torusa).

---

## Quick Start

Projekt wykorzystuje system budowania Gradle.

**Najpierw należy sklonować repozytorium z serwisu GitHub:**
`git clone https://github.com/Wilk1717/LumberSim.git`
`cd LumberSim`

**W głównym katalogu projektu należy wywołać komendę:**
* **Windows:** `gradlew.bat run`
* **Linux / macOS:** `./gradlew run`

---

## Sample Run

Po pomyślnym uruchomieniu programu, na ekranie natychmiast pojawi się główne okno aplikacji, a symulacja rozpocznie się automatycznie.

### Inicjalizacja środowiska
Plansza generuje się z określonym w parametrach startowym procentem zalesienia. 

### Aktywność Agentów na mapie
* **Ekologiczni drwale (żółci):** Szukają najbliższych drzew, ścinają je pojedynczo i pozwalają lasowi na naturalne, szybkie odrastanie.
* **Chciwi drwale (czerwoni):** Skanują obszar w swoim polu widzenia, poszukując miejsc z największą ilością drzew. Wykonują wycinkę obszarową, co często prowadzi do powstawania martwych polan bez szans na odrośnięcie.
* **Strażnicy leśni (niebiescy):** Patrolują mapę, jeśli namierzą Chciwego drwala, nakładają na niego mandat i cooldown na jego ruch na określoną ilość ticków.

### Panel Statystyk
W dolnej części ekranu na bieżąco aktualizowany jest panel telemetrii, na którym na żywo wyświetlane są informacje o:
* Aktualnej populacji poszczególnych frakcji.
* Średnim zgromadzonym majątku obu grup drwali.
* Ogólnym procencie zalesienia planszy.
