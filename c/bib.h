#ifndef BIB_H
#define BIB_H
//empeche que le fichier soit iclus  deux fois par erreur .
//C’est une garde d’inclusion : ça évite que le fichier soit inclus plusieurs fois par erreur (et créer des conflits).

// Structure représentant un livre
typedef struct {
    int id;
    char titre[100];
    char auteur[100];
    int annee;
    int disponible; // 1 = disponible, 0 = emprunté
} Livre;
//Déclarer les fonctions pour pouvoir les utiliser ailleurs
//les prototypes
// Charge un fichier CSV dans un tableau de Livre (allocation dynamique)
Livre* chargerFichier(const char* filename, int* taille);

// Génère les fichiers de base (disponibles.csv, empruntes.csv, livres_java.txt)
void genererFichiers(Livre* livres, int taille);

// Recherche par mot-clé dans titre ou auteur, génère recherche.csv
void rechercherParMotCle(Livre* livres, int taille, const char* motCle);

// Filtre par année minimale, génère filtre_annee.csv
void filtrerParAnneeMin(Livre* livres, int taille, int anneeMin);

#endif
//Ferme la garde d’inclusion #ifndef.