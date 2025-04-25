#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	int val1 = 0;
	int val2 = 0;

    printf("Entre val1 : ");
	scanf("%f", &val1);
    printf("Entre val2 : ");
	scanf("%f", &val2);

	int produit = val1*val2;

	printf("Produit : %f\n", produit);
	return 0;
}
