#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
int main(int argc, char const *argv[])
{
    int n = 0;
    printf("Entre une nombre possitif: ");
    scanf("%d", &n);

    int fact = 1;

    for (int i = 2; i <= n; ++i)
    {
        fact = fact * i;
    }



    printf("La factorielle est %d\n", fact);

    printf("INT_MAX : %d\n", INT_MAX);

    // la factorielle de 13 est incorect 

    return 0;
}