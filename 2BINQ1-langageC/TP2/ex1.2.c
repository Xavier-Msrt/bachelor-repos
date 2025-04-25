#include <stdio.h>
#include <stdlib.h>

#define SIZE 100
#define ALPH 26
int main(int argc, char const *argv[])
{

    // lecture nb lignes t colonnes
    int l, c;
    printf("Nombre de ligne: ");
    scanf("%d", &l); 
    printf("Nombre de colonne: ");
    scanf("%d", &c); 
    

    // alphabet en boucle sur ligne
    char mat1[SIZE][SIZE] = {0};

    char currentLetter = 'A';
    for (int i = 0; i < l; ++i)
    {
        for (int j = 0; j < c; ++j)
        {
            mat1[i][j] = currentLetter;
            currentLetter == 'Z' ? currentLetter = 'A': currentLetter++;
        }  
    }


    // afficher
    for (int i = 0; i < l; ++i)
    {
        for (int j = 0; j < c; ++j)
        {
            printf("%c ", mat1[i][j]);
        }
        printf("\n");
    }

    printf("\n");


    // alphabet en boucle sur colonne
    char mat2[SIZE][SIZE] = {0};
    currentLetter = 'A';
    for (int i = 0; i < c; ++i)
    {
        for (int j = 0; j < l; ++j)
        {
            mat2[j][i] = currentLetter;
            currentLetter == 'Z' ? currentLetter = 'A': currentLetter++;
        }
    }


    // afficher
    for (int i = 0; i < l; ++i)
    {
        for (int j = 0; j < c; ++j)
        {
            printf("%c ", mat2[i][j]);
        }
        printf("\n");
    }

     printf("\n");





    // aléatoire lettre sur ligne
    int min = 65;
    int max = 90;
       
    char mat3[SIZE][SIZE] = {0};

    for (int i = 0; i < l; ++i)
    {
        for (int j = 0; j < c; ++j)
        {

            mat3[i][j] = min + (int)(rand()/(RAND_MAX+1.0)*(max-min+1));
        }  
    }




    // afficher
    for (int i = 0; i < l; ++i)
    {
        for (int j = 0; j < c; ++j)
        {
            printf("%c ", mat3[i][j]);
        }
        printf("\n");
    }

     printf("\n");


     // exc 4

    
    char car = 'A';
    printf("          ");// for format
    while(car != 'Z'+1){
        printf("%c ", car);
        car++;
    }
    printf("\n");

    for (int i = 0; i < l; ++i)
    {
        // comptage
        int chatTab[ALPH] = {0};
        for (int j = 0; j < c; ++j)
        {
             chatTab[mat3[i][j]-'A']++;
        }

        //affichage ligne
        printf("Ligne %d : ", i+1);
        for (int k = 0; k < ALPH; ++k)
        {
             printf("%d ", chatTab[k]);
        }
         printf("\n");
    }




    return 0;
}