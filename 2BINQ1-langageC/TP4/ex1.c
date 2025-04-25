#include <stdio.h>
#include <stdlib.h>

int main(int argc, char const *argv[])
{
    int h = 0;
    int l = 0;

    printf("Hauteur = ");
    scanf("%d", &h);
    printf("Largeux = ");
    scanf("%d", &l);


    char** tab = (char**) malloc(h*sizeof(char*));
    if(tab == NULL) exit(1);

    for (int i = 0; i < h; ++i)
    {
        tab[i] = (char*) malloc(h*sizeof(char));
        if(tab[i] == NULL) exit(1);

        for (int j = 0; j < l; ++j)
        {
            tab[i][j] = '.';
        }
    }

    for (int i = 0; i < h; ++i)
    {
        for (int j = 0; j < l; ++j)
        {
            printf("%3c ", tab[i][j]);
        }
        printf("\n");
    }


    char player = 'x';
    int choix = 1;

    while (choix != 0) {
        printf("Colonne joueur %c ? ", player);
        scanf("%d", &choix);
        if(choix == 0) break;

        for (int i = h-1; i >= 0; --i)
        {
            if(tab[i][choix-1] == '.'){
                tab[i][choix-1] = player;
                break;
            }
        }

        for (int i = 0; i < h; ++i)
        {
            for (int j = 0; j < l; ++j)
            {
                printf("%3c ", tab[i][j]);
            }
            printf("\n");
        }
        player = (player == 'x' ? 'o': 'x');
    }

    
    

    




    // free memory
    for (int i = 0; i < h; ++i)
    {
        free(tab[i]);
    }
    free(tab);

    return 0;
}