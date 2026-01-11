package biblio;

import java.sql.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseService {

    private Connection connection;

    // Constructeur : ouvre la connexion à la base MySQL
    public DatabaseService() throws Exception {
        // Charger le driver JDBC MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connexion à la base (à adapter si ton user / mdp changent)
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/biblio",
                "root",
                ""  
        );
    }

    // Charger tous les livres depuis MySQL
    public List<Livre> chargerDepuisBD() throws Exception {

        List<Livre> livres = new ArrayList<>();//Liste vide qui va contenir les livres.
        String sql = "SELECT * FROM livre";

        PreparedStatement ps = connection.prepareStatement(sql);//→ Préparer l’exécution.
        ResultSet rs = ps.executeQuery();//Exécute le SELECT et récupère le résultat.
       //Boucle sur chaque ligne de la table.

     //À l’intérieur : création d’un new Livre(...),ajout à la liste.
        while (rs.next()) {//C’est l’objet que renvoie JDBC quand on exécute une requête SQL SELECT.
            livres.add(new Livre(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getInt("annee"),
                rs.getBoolean("disponible")
            ));
        }
        rs.close();//rs comme un curseur qui pointe sur chaque ligne du tableau SQL
        ps.close();
        return livres;//Retourne la liste.
    }

    // Export général vers CSV (servira pour le programme C)
    public void exporterCSV(List<Livre> livres, String filename) throws Exception {
        try (FileWriter fw = new FileWriter(filename)) {//ouvrir le fichier en mode ecriture
            for (Livre l : livres) {//Écrit une ligne CSV compatible avec chargerFichier en C
                fw.write(
                    l.id + ";" +
                    l.titre + ";" +
                    l.auteur + ";" +
                    l.annee + ";" +
                    (l.disponible ? 1 : 0) + "\n"
                );
            }
        }
    }

    // Emprunter un livre : disponible -> FALSE
    // Retourne true si 1 livre a été mis à FALSE, false sinon
    public boolean emprunterLivre(int id) throws Exception {

        PreparedStatement ps = connection.prepareStatement(
            "UPDATE livre SET disponible = FALSE " +
            "WHERE id = ? AND disponible = TRUE"
        );
        ps.setInt(1, id);
        int rows = ps.executeUpdate();//nombre de lignes modifiées.
        ps.close();

        return rows > 0; // true si livre existant ET encore disponible
    }

    // Rendre un livre : disponible -> TRUE
    // Retourne true si 1 livre a été mis à TRUE, false sinon
    public boolean rendreLivre(int id) throws Exception {

        PreparedStatement ps = connection.prepareStatement(
            "UPDATE livre SET disponible = TRUE " +
            "WHERE id = ? AND disponible = FALSE"
        );
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        ps.close();

        return rows > 0; // true si livre existant ET actuellement emprunté
    }

    // Récupérer un livre précis (utile pour les CSV d’emprunts / rendus)
    public Livre getLivreParId(int id) throws Exception {

        PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM livre WHERE id = ?"
        );
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        Livre l = null;
        if (rs.next()) {//construire un new livre(...)
            l = new Livre(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getString("auteur"),
                rs.getInt("annee"),
                rs.getBoolean("disponible")
            );
        }
        rs.close();
        ps.close();
        return l;
    }

    //  Fermer proprement la connexion si on  veux
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {//e cest lerreur
            // elle affiche ou lerreur cest produite
            e.printStackTrace();
        }
    }
}
