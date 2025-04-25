#include <stdlib.h>
#include <stdio.h>
#include <limits.h>


int main(int argc, char const *argv[])
{
    int n = 0;
    s
    scanf("%d", &n);

    int fact = 1;
    int i = 2;

    while ( i <= n)
    {
        

        double factReal = (double) fact * i;
        if(factReal > INT_MAX){
            printf("INT depassement capacités\n");
            return 1;
        }

        fact = (int) factReal;

        i++;

    }



    printf("La factorielle est %d\n", fact);

    printf("INT_MAX : %d\n", INT_MAX);

    // la factorielle de 13 est incorect 
    // !!!! dans un while le i++ ce fait a la fin
    return 0;
}