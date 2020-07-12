# Coding Challenge

## Algorithmus

Bei dem beschriebenen Problem handelt es sich um eine Instanz des "Traveling Salesman Problems" (TSP). Genau genommen ist die Instanz symmetrisch, daher gibt es mindestens zwei optimale Lösungen. Das zugehörige Entscheidungsproblem ist bekanntermaßen **NP**-vollständig, also ist es nur genau dann effizient lösbar, falls **P** = **NP** gilt. Lösbar ist das Problem natürlich dennoch mit exponentiellem Zeitaufwand, beispielsweise durch den Held-Karp-Algorithmus. Der Algorithmus basiert auf ["dynamic programming"](https://en.wikipedia.org/wiki/Dynamic_programming) und liefert stets optimale Ergebnisse. Er ist relativ einfach und verständlich zu implementieren, und kann leicht um Parallelität erweitert werden, um schneller zum gewünschten Ergebnis zu gelangen.

Die notwendigen Mengenoperation (Vereinigung, Schnitt, Komplement) wurden durch Bit-Operationen auf 32-Bit-Zahlen implementiert.

## Code

Der Code befindet sich im Verzeichnis [src](src/).

Zum Kompilieren und Ausführen reicht es, die folgenden Befehle im
Hauptverzeichnis des Repositories auszuführen (JDK 8 oder neuer wird
benötigt):

```sh
mkdir -p bin
javac -d bin -sourcepath src src/tspdemo/TspDemo.java
java -classpath bin tspdemo.TspDemo <msg_standorte_deutschland.csv
```

## Ergebnis

```
                 Ismaning/München (Hauptsitz)
 ->  135.91km -> Passau
 ->  253.98km -> Chemnitz
 ->  148.09km -> Görlitz
 ->  196.77km -> Berlin
 ->  190.71km -> Braunschweig
 ->   51.76km -> Hannover
 ->  136.38km -> Hamburg
 ->  135.40km -> Schortens/Wilhelmshaven
 ->  120.49km -> Lingen (Ems)
 ->   64.29km -> Münster
 ->   71.18km -> Essen
 ->   24.55km -> Düsseldorf
 ->   43.93km -> Köln/Hürth
 ->  143.88km -> Frankfurt
 ->   93.74km -> Walldorf
 ->   29.38km -> Bretten
 ->  104.46km -> St. Georgen
 ->   88.30km -> Stuttgart
 ->  158.01km -> Nürnberg
 ->   76.93km -> Ingolstadt
 ->   65.28km -> Ismaning/München (Hauptsitz)
Total: 2333.41km
```
