#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bib.h"


//Normalisation du texte (accents / majuscules)

char toLowerNoAccent(char c) {
    // Convertir majuscules → minuscules
    if (c >= 'A' && c <= 'Z') c += 32;

    // Nettoyage accent / UTF-8 cassé (Windows console),On convertit en unsigned char pour bien gérer certains codes UTF-8.
    unsigned char uc = (unsigned char)c;

    switch (uc) {
        case 0xC3: return 'a'; // caractères UTF-8 mal lus
        case 0xA9: return 'e'; // é
        case 0xA0: return 'a'; // à
        case 0xA8: return 'e'; // è
        case 0xAA: return 'e'; 
    }

    return c;
}

void normalize(char* s) {
    for (int i = 0; s[i] != '\0'; i++) {
        s[i] = toLowerNoAccent(s[i]);
    }
}


// ======== 2) CHARGEMENT DU FICHIER CSV ========
//Retourne un pointeur vers un tableau dynamique de livres
//◼ taille est un OUTPUT : nb total de livres lus.

Livre* chargerFichier(const char* filename, int* taille) {
    FILE* f = fopen(filename, "r");
    if (!f) {
        printf("Erreur ouverture CSV\n");//Ouvre le CSV en lecture. //Si erreur → message + arrêt du programme.
        exit(1);
    }

    Livre* livres = malloc(1000 * sizeof(Livre)); //livres est un pointeur vers la première case du tableau
    //  réserve de la mémoire pour 1000 livres (taille max). allocation dynamique
    if (!livres) {
        printf("Erreur allocation mémoire\n");
        fclose(f);//fermeture du fichier
        exit(1);//arret du prog
    }
//count → compteur du nombre de livres lus.
// L → structure temporaire dans laquelle on lit une ligne du fichier.
    int count = 0;
    while (!feof(f)) {//endof file elle sert de verifier si on a dj tente de lire apres la fin fu fivhier
        Livre L;//structure temp
        if (fscanf(f, "%d;%99[^;];%99[^;];%d;%d\n",
                   &L.id, L.titre, L.auteur, &L.annee, &L.disponible) == 5) {
            livres[count++] = L;
        }
    }

    fclose(f);
    *taille = count;//enregistre la taille de nombre de livres lus// et on retourne le ponteur vers la tavleau de livres
    return livres;
}

//
// ======== 3) GÉNÉRATION DES FICHIERS ========
//

void genererFichiers(Livre* livres, int taille) {
    FILE* f1 = fopen("disponibles.csv", "w");
    FILE* f2 = fopen("empruntes.csv", "w");
    FILE* f3 = fopen("livres_java.txt", "w");

    if (!f1 || !f2 || !f3) {
        printf("Erreur création fichiers de sortie\n");
        if (f1) fclose(f1);
        if (f2) fclose(f2);
        if (f3) fclose(f3);
        return;
    }

    for (int i = 0; i < taille; i++) {

        if (livres[i].disponible == 1) {
            fprintf(f1, "%d;%s;%s;%d;1\n",
                    livres[i].id, livres[i].titre,
                    livres[i].auteur, livres[i].annee);
        } else {
            fprintf(f2, "%d;%s;%s;%d;0\n",
                    livres[i].id, livres[i].titre,
                    livres[i].auteur, livres[i].annee);
        }

        fprintf(f3, "new Livre(%d, \"%s\", \"%s\", %d, %d);\n",
                livres[i].id, livres[i].titre,
                livres[i].auteur, livres[i].annee, livres[i].disponible);
    }

    fclose(f1);
    fclose(f2);
    fclose(f3);
}

//
// ======== 4) RECHERCHE PAR MOT-CLÉ (titre/auteur) ========
//

void rechercherParMotCle(Livre* livres, int taille, const char* motCle) {
    FILE* f = fopen("recherche.csv", "w");
    if (!f) {
        printf("Erreur création fichier recherche.csv\n");
        return;
    }

    char motNorm[100];
    strcpy(motNorm, motCle);
    normalize(motNorm);

    int found = 0;

    for (int i = 0; i < taille; i++) {
        char titreNorm[100], auteurNorm[100];

        strcpy(titreNorm, livres[i].titre);
        strcpy(auteurNorm, livres[i].auteur);

        normalize(titreNorm);
        normalize(auteurNorm);

        if (strstr(titreNorm, motNorm) || strstr(auteurNorm, motNorm)) {// si le mot cle apparait dans titre ou auteur on le declare comme trouve 

            printf("-> %d | %s | %s | %d | %s\n",
                   livres[i].id,
                   livres[i].titre,
                   livres[i].auteur,
                   livres[i].annee,
                   livres[i].disponible ? "dispo" : "emprunté");

            fprintf(f, "%d;%s;%s;%d;%d\n",
                    livres[i].id,
                    livres[i].titre,
                    livres[i].auteur,
                    livres[i].annee,
                    livres[i].disponible);

            found = 1;
        }
    }

    if (!found) {
        printf("Aucun livre trouvé pour le mot clé '%s'\n", motCle);
    } else {
        printf("Résultats enregistrés dans recherche.csv\n");
    }

    fclose(f);
}

//
// ======== 5) FILTRER PAR ANNÉE MINIMALE ========
//

void filtrerParAnneeMin(Livre* livres, int taille, int anneeMin) {
    FILE* f = fopen("filtre_annee.csv", "w");
    if (!f) {
        printf("Erreur création fichier filtre_annee.csv\n");
        return;
    }

    int found = 0;

    for (int i = 0; i < taille; i++) {
        if (livres[i].annee >= anneeMin) {

            printf("-> %d | %s | %s | %d | %s\n",
                   livres[i].id,
                   livres[i].titre,
                   livres[i].auteur,
                   livres[i].annee,
                   livres[i].disponible ? "dispo" : "emprunté");

            fprintf(f, "%d;%s;%s;%d;%d\n",
                    livres[i].id,
                    livres[i].titre,
                    livres[i].auteur,
                    livres[i].annee,
                    livres[i].disponible);

            found = 1;
        }
    }

    if (!found) {
        printf("Aucun livre avec annee >= %d\n", anneeMin);
    } else {
        printf("Résultats enregistrés dans filtre_annee.csv\n");
    }

    fclose(f);
}
