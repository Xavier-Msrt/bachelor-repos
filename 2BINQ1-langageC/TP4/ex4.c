#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>


// compiler avec le flag -lm pour lier la lib math.h
int main(int argc, char const *argv[])
{
    int h = 0;
    int l = 0;
    int profondeur = 0;
    int b = 0;
    int* histo = NULL;
    srand((unsigned) time(NULL)); // mettre un seed

    int** img = NULL;

    int choix = 0;
    while(choix != 7){
        printf("MENU:\n");
        printf("\t 1) Création d'image avec valeurs aléatoires.\n");
        printf("\t 2) Création d'une image avec valeurs préséfinies.\n");
        printf("\t 3) Affichage de l'image.\n");
        printf("\t 4) Changement de la taille de l'image.\n");
        printf("\t 5) Affichage de l'histogramme de l'image.\n");
        printf("\t 6) Suppression de l'image.\n");
        printf("\t 7) Quitter le programme.\n");
        
        printf("Enter votre choix: ");
        scanf("%d", &choix);

        switch(choix){
            case 1:
                if(img != NULL){
                    printf("error: image charger \n");
                    break;
                }

                printf("Entrer la hauteur: ");
                scanf("%d", &h);
                printf("Entrer la longeur: ");
                scanf("%d", &l);
                printf("Entrer la profondeur: ");
                scanf("%d", &profondeur);


                b = pow(2, profondeur);


                img = (int**) malloc(h*sizeof(int*));
                if(img == NULL) exit(1); // check if allowed


                for (int i = 0; i < h; ++i)
                {
                    img[i] = (int*) malloc(l*sizeof(int));
                    if(img[i] == NULL) exit(1);

                    for (int j = 0; j < l; ++j)
                    {
                        img[i][j] = rand() % b;  
                    }
                }
 
                
                break;
            case 2:
                if(img != NULL){
                    printf("error: image charger \n");
                    break;
                }

                printf("Entrer la hauteur: ");
                scanf("%d", &h);
                printf("Entrer la longeur: ");
                scanf("%d", &l);
                printf("Entrer la profondeur: ");
                scanf("%d", &profondeur);


                b = pow(2, profondeur);


                img = (int**) malloc(h*sizeof(int*));
                if(img == NULL) exit(1);


                for (int i = 0; i < h; ++i)
                {
                    img[i] = (int*) malloc(l*sizeof(int));
                    if(img[i] == NULL) exit(1);
                    
                    for (int j = 0; j < l; ++j)
                    {
                        if(j+1 > b) exit(1);
                        img[i][j] = j+1;  
                    }
                }
             



                break;  
            case 3:
                if(img == NULL){
                    printf("error: pas d'image charger \n");
                    break;
                }

                for (int i = 0; i < h; ++i)
                {
                    for (int j = 0; j < l; ++j)
                    {
                      printf("%10d ", img[i][j]);  
                    }
                    printf("\n");
                }  
                
                break;
            case 4: 
                if(img == NULL){
                    printf("erreur: Aucun image\n");
                    break;
                }
                int current_h = 0;
                int current_l = 0;

                printf("Entrer la nouvelle hauteur: ");
                scanf("%d", &current_h);
                printf("Entrer la nouvelle longeur: ");
                scanf("%d", &current_l);

                if((current_h > h && current_l < l )
                    || ( current_h < h && current_l > l ) ){
                    printf("erreur: impossible d'agrande et raptisir");
                } 

                if(current_l > l){ 
                    //bigger
                    printf("C'est bigger");
                    img = (int**) realloc(img, current_h*sizeof(int*));

                    for (int i = 0; i < current_h; ++i)
                    {
                        img[i] = (int*) realloc(img[i], current_l*sizeof(int)); 

                        if(i >= h ){ // new ligne at 0
                            for (int j = 0; j < current_l; ++j)
                            {
                                img[i][j] = 0;
                            }
                        }else{ // put 0 in end of ligne (because malloc) 
                           for (int j = l; j <= current_l; ++j)
                            {
                               img[i][j] = 0;
                            } 
                        }
                        
                    }

                }else{ 
                    //cropped
                    printf("C'est crop");

                    for (int i = 0; i < h; ++i)
                    {
                        if(current_h <= i){
                            free(img[i]);
                            continue;
                        } 

                        img[i] = (int*) realloc(img[i], current_l*sizeof(int));
                        
                    }

                    img = (int**) realloc(img, current_h*sizeof(int*));
  
                }

                // update new size
                h = current_h;
                l = current_l;
                break;
            case 5:

                histo = (int*) malloc( b * sizeof(int) );
                if(histo == NULL) exit(1);
                //clear memory
                for (int i = 0; i < b; ++i)
                {
                    histo[i] = 0;
                }


                int somme = 0;
                for (int i = 0; i < h; ++i)
                {
                    for (int j = 0; j < l; ++j)
                    {
                        histo[img[i][j]] += 1;
                    }
                }

                for (int i = 0; i < b; ++i)
                {
                   if(histo[i] != 0){
                    printf("\t #pixel de valeurs %d: %d \n", i, histo[i]);
                    somme += histo[i];
                   }
                    
                }
                printf("Nombre de pixels dans l'image: %d \n", somme);

                free(histo);
                histo = NULL;

                break;
            case 6:
                if(histo != NULL){
                    free(histo); 
                }
                if(img != NULL){
                    for (int i = 0; i < h; ++i)
                    {
                        free(img[i]);
                    }
                    free(img);
                }
                img = NULL;
                histo = NULL;

                break;
            case 7:
                //free
                if(histo != NULL){
                    free(histo); 
                }
                
               if(img != NULL){
                    for (int i = 0; i < h; ++i)
                    {
                        free(img[i]);
                    }
                    free(img);
                }
                printf("Sortie du programme \n");
                exit(0);
                break;
        }
    }
    return 0;
}