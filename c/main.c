#include <stdio.h>
#include <stdlib.h>
#include "bib.h"
//argc = nombre d’arguments
//argv = arguments eux-mêmes (argv[1] = fichier CSV envoye par Java)
int main(int argc, char** argv) {
//. Verification si un fichier CSV est passe
    if (argc < 2) {
        printf("Usage : traitement <csv_file>\n");
        return 1;
    }
//taille va recevoir le nombre de livres lus depuis le CSV.
    int taille = 0;

    printf("Chargement du fichier CSV...\n");
    Livre* livres = chargerFichier(argv[1], &taille);
    printf("%d livres charges.\n", taille);//on charge la base dans la memoire

    int choix;
    printf("\n=== MENU TRAITEMENT C ===\n");
    printf("1. Generer fichiers (disponibles/empruntes + livres_java.txt)\n");
    printf("2. Rechercher par mot-cle (titre/auteur)\n");
    printf("3. Filtrer par annee minimale\n");
    printf("Votre choix : ");
    scanf("%d", &choix);

    if (choix == 1) {
        printf("Generation des fichiers...\n");
        genererFichiers(livres, taille);
        printf("Fichiers generes avec succès !\n");
    } else if (choix == 2) {
        char mot[100];
        printf("Mot-cle (sans espaces) : ");
        scanf("%99s", mot);
        rechercherParMotCle(livres, taille, mot);
    } else if (choix == 3) {
        int anneeMin;
        printf("Annee minimale : ");
        scanf("%d", &anneeMin);
        filtrerParAnneeMin(livres, taille, anneeMin);
    } else {
        printf("Choix invalide.\n");
    }

    free(livres);
    return 0;
}
