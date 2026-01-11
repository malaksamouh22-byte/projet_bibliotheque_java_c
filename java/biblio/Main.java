package biblio;

import java.util.*;//permet d’utiliser Scanner, List, etc.
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseService db = new DatabaseService();//Ouverture de la connexion MySQL
            CExecutor exec = new CExecutor();//Prépare l’exécution du programme C (traitement2.exe)
            Scanner sc = new Scanner(System.in);//Pour lire les choix de l’utilisateur.
            //affichage du menu
            System.out.println("=== MENU BIBLIOTHEQUE ===");
            System.out.println("1. Voir la liste des livres");
            System.out.println("2. Emprunter un livre");
            System.out.println("3. Générer fichiers via C");
            System.out.println("4. Rendre un livre");
            System.out.print("Choix : ");

            int choix = sc.nextInt();//lit le num choisis par user
            sc.nextLine(); // consomme le retour à la ligne

            switch (choix) {

                case 1: {  // Voir la liste des livres
                    List<Livre> livres = db.chargerDepuisBD();
                    for (Livre l : livres) {
                        System.out.println(
                            l.id + " - " + l.titre + " (" + l.auteur + ") " +
                            (l.disponible ? "[DISPONIBLE]" : "[EMPRUNTÉ]")
                        );
                    }
                    break;
                }

                case 2: {  // Emprunter un livre
                    System.out.print("ID du livre à emprunter : ");
                    int id = sc.nextInt();
                    sc.nextLine(); // retour ligne

                    boolean ok = db.emprunterLivre(id);

                    if (!ok) {
                        System.out.println("❌ Livre introuvable ou déjà emprunté !");
                    } else {
                        System.out.println("✔ Livre emprunté avec succès !");

                        // Infos de la personne
                        System.out.print("Nom de la personne : ");
                        String nom = sc.nextLine();

                        System.out.print("Prénom de la personne : ");
                        String prenom = sc.nextLine();

                        System.out.print("Âge de la personne : ");
                        int age = sc.nextInt();
                        sc.nextLine();

                        Personne p = new Personne(nom, prenom, age);
                        Livre l = db.getLivreParId(id);

                        if (l != null) {
                            // Écriture dans emprunts.csv
                            try (FileWriter fw = new FileWriter("../c/emprunts.csv", true)) {
                                fw.write(p.nom + ";" + p.prenom + ";" + p.age + ";" +
                                         l.id + ";" + l.titre + ";" + l.auteur + ";" +
                                         l.annee + ";EMPRUNT\n");
                            } catch (IOException e) {
                                System.out.println("Erreur écriture emprunts.csv : " + e.getMessage());
                            }
                        }
                    }

                    // Mise à jour du CSV général pour le C
                    List<Livre> livres = db.chargerDepuisBD();
                    db.exporterCSV(livres, "../c/bdd_livres.csv");
                    break;
                }

                case 3: {  // Lancer le programme C
                    List<Livre> livres = db.chargerDepuisBD();
                    db.exporterCSV(livres, "../c/bdd_livres.csv");

                    exec.executerC("../c/bdd_livres.csv");
                    System.out.println("Traitement C terminé !");
                    break;
                }

                case 4: {  // Rendre un livre
                    System.out.print("ID du livre à rendre : ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    boolean ok = db.rendreLivre(id);

                    if (!ok) {
                        System.out.println("❌ Livre introuvable ou déjà disponible !");
                    } else {
                        System.out.println("✔ Livre rendu avec succès !");

                        // Infos de la personne qui rend le livre
                        System.out.print("Nom de la personne : ");
                        String nom = sc.nextLine();

                        System.out.print("Prénom de la personne : ");
                        String prenom = sc.nextLine();

                        System.out.print("Âge de la personne : ");
                        int age = sc.nextInt();
                        sc.nextLine();

                        Personne p = new Personne(nom, prenom, age);
                        Livre l = db.getLivreParId(id);

                        if (l != null) {
                            // Écriture dans livres_rendus.csv
                            try (FileWriter fw = new FileWriter("../c/livres_rendus.csv", true)) {
                                fw.write(p.nom + ";" + p.prenom + ";" + p.age + ";" +
                                         l.id + ";" + l.titre + ";" + l.auteur + ";" +
                                         l.annee + ";RETOUR\n");
                            } catch (IOException e) {
                                System.out.println("Erreur écriture livres_rendus.csv : " + e.getMessage());
                            }
                        }

                        System.out.println("➡ Ajouté dans livres_rendus.csv");
                    }

                    // Mise à jour générale pour le C
                    List<Livre> livres = db.chargerDepuisBD();
                    db.exporterCSV(livres, "../c/bdd_livres.csv");
                    break;
                }

                default:
                    System.out.println("Choix invalide.");
                    break;
            }

        } catch (Exception e) {//= si une erreur survient, je la récupère dans
            e.printStackTrace();//ou et pourquoi il y a une erreur
        }
    }
}
