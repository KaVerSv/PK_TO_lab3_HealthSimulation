# Public Health Application

## Opis
Projekt Symulacji rozprzestrzeniania się choroby w populacji. Projekt wykorzystuje różne wzorce projektowe, takie jak stan do śledzenia stanu zdrowia danej osoby, obsługujemy takie stany jak zdrowy,
postępująca infekcja i zarażony. Ludzie stają się zarażeni kiedy przebywają przez określony czas w określonej odległości od osoby chorej, infekcja rozwija się określony czas z określonego przedziału, po upływie
danego czasu osoba zdrowieje zyskując odporność. Dany krok symulacji można zapisać kożystając z wzorca projektowego Memento (Pamiątka). Projekt wykrzystuje interfejs graficzny java swing. Do symualcji ruchu
ludzi wykorzystano projekt wektorów z lab2.

## Wymagania
Aby uruchomić projekt, wymagane jest:
- Java Development Kit (JDK) w wersji 8 lub nowszej
- Środowisko Eclipse lub IntelliJ IDEA (opcjonalnie)

## Funkcje
- Symulacja ruchu ludzi w określonym środowisku.
- Śledzenie stanu zdrowia ludzi.
- Możliwość zapisu i wczytywania stanu symulacji.

## Użycie
Po uruchomieniu projektu, należy:

Uruchom symulację za pomocą przycisku "Start".
Obserwuj ruch ludzi i ich interakcje.
Możesz zatrzymać symulację w dowolnym momencie za pomocą przycisku "Stop".

## Wzorce Projektowe
Projekt wykorzystuje następujące wzorce projektowe:
- Pamiątka (Memento): Do zapisywania i przywracania stanu symulacji.
- Stan (State): Do zarządzania stanem zdrowia ludzi.
- Singleton: Do przechowywania populacji oraz postępujących infekcji
