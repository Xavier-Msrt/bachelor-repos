#include <stdio.h>
#include <stdlib.h>

#define MAX_ETU 50

int main(int argc, char const *argv[])
{
    int size = 0;
    float currentValue = 0;
    float avg = 0;
    float tab[MAX_ETU] = {0};

    while( size < MAX_ETU && scanf("%f", &currentValue) != EOF){ // attention verifier que la taill ne dépasse pas la palce memoire
        avg += currentValue;
        tab[size] = currentValue;
        size++;
    }


    avg /= size; 
    printf("Moyenne = %f \n", avg);

    printf("Exart des etudiant par rapport a la moyenner: \n");
    for (int i = 0; i < size; ++i)
    {
        float calc = (float)tab[i]-avg;
        printf("  Etudiant %d: %f - %f = %f \n", i+1, tab[i], avg, calc);
    }


    return 0;
}