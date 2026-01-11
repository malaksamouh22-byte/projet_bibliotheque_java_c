# 📚 Projet Bibliothèque — Java, C et MySQL

Ce projet est une application hybride qui combine Java, C et MySQL pour gérer une bibliothèque.
Il respecte les exigences suivantes :
- gestion d’une base de données
- traitements en langage C avec structures et pointeurs
- exécution du programme C à partir de Java

---

## 🧩 Architecture générale

Java est utilisé comme interface utilisateur et comme gestionnaire de base de données.
C est utilisé pour le traitement avancé des fichiers.

Le flux de fonctionnement est le suivant :

1. Java lit les données depuis MySQL
2. Java exporte les livres dans un fichier CSV
3. Java compile et exécute le programme C
4. C lit le CSV, traite les données et génère de nouveaux fichiers

---

## 🗄️ Base de données MySQL

Table : `livre`

| Champ | Type |
|------|------|
| id | INT |
| titre | VARCHAR |
| auteur | VARCHAR |
| annee | INT |
| disponible | BOOLEAN |

---

## ☕ Partie Java

Fonctionnalités :
- afficher la liste des livres
- emprunter un livre
- rendre un livre
- exporter la base MySQL en CSV
- lancer le programme C

Classes principales :
- Main.java → menu utilisateur
- DatabaseService.java → accès MySQL
- Livre.java → structure objet d’un livre
- CExecutor.java → compilation et exécution du C

---

## ⚙️ Partie C

Fonctionnalités :
- chargement du fichier CSV
- utilisation de structures `Livre`
- allocation dynamique avec `malloc`
- recherche par mot-clé
- filtrage par année
- génération de nouveaux fichiers :
  - disponibles.csv
  - empruntes.csv
  - livres_java.txt

Fichiers :
- bib.h → définition de la structure et prototypes
- bib.c → fonctions de traitement
- main.c → menu C

---

## ▶️ Exécution

### Compilation Java :
javac -cp ".;lib/mysql-connector-j.jar" java/biblio/*.java
### Lancement :
java -cp ".;lib/mysql-connector-j.jar" biblio.Main

---

## 🎯 Objectifs pédagogiques

Ce projet montre :
- l’utilisation des bases de données avec Java
- la manipulation de fichiers en C
- l’utilisation des pointeurs et structures
- la communication entre deux langages
- l’automatisation de la compilation et de l’exécution

Projet réalisé dans le cadre d’un travail académique.

