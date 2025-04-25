#include <stdio.h>
#include <stdlib.h>

int main(int argc, char const *argv[])
{
    int n = 0;
    scanf("%d", &n);



    int *tab = malloc(n*sizeof(int));

    if(tab == NULL)
        exit(1);


    int bigCount = 0;
    int other = 0;

    for (int i = 0; i < n; ++i)
    {
        scanf("%d", &tab[i]);
        if(tab[i] >= 0 )
            bigCount++;
        else
            other++;
    }




    int *tabBig = malloc(bigCount*sizeof(int));
    if(tabBig == NULL)
        exit(1);

    int *tabOther = malloc(other*sizeof(int));
    if(tabOther == NULL)
        exit(1);

    int indexBigIndex = 0;
    int indexOutherIndex = 0;
    for (int i = 0; i < n; ++i)
    {
        if(tab[i] >= 0 ){
           tabBig[indexBigIndex] = tab[i];
            indexBigIndex++; 
        }else{
            tabOther[indexOutherIndex] = tab[i];
            indexOutherIndex++; 
        }
            
    }

    free(tab);

    // affichage des 2 tableau
    for (int i = 0; i < bigCount; ++i)
    {
        printf("%d, ", tabBig[i]);
    }

    printf("\n");

     for (int i = 0; i < other; ++i)
    {
        printf("%d, ", tabOther[i]);
    }
    printf("\n");

    
    free(tabBig);
    free(tabOther);




    return 0;
}