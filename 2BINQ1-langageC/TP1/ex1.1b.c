#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	float val1 = 0;
	float val2 = 0;

    printf("Entre val1 : ");
	scanf("%f", &val1);
    printf("Entre val2 : ");
	scanf("%f", &val2);

	float produit = val1*val2;

	printf("Produit : %f\n", produit);
	return 0;
}
