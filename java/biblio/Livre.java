package biblio;
public class Livre {
    public int id;
    public String titre;
    public String auteur;
    public int annee;
    public boolean disponible;

    public Livre(int id, String titre, String auteur, int annee, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.annee = annee;
        this.disponible = disponible;
    }
}
