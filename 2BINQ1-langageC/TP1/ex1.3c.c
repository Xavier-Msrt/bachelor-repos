#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
int main(int argc, char const *argv[])
{
    double n = 0;
    printf("Entre une nombre possitif: ");
    scanf("%lf", &n);

    double fact = 1;

    for (int i = 2; i <= n; ++i)
    {
        fact = fact * i;
    }


    printf("La factorielle est %lf\n", fact);


    // avec un type de variable de plus grand taill on arrive a stocker tout les nombre 13 est donc correct
    return 0;