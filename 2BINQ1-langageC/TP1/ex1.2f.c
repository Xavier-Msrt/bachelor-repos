#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	int a = 0;
	int b = 0;

	
	do{
        printf("Entre a : ");
		scanf("%d", &a);
	}while(a < 0);

	do{
        printf("Entre b : ");
		scanf("%d", &b);
	}while(b < 0);


    int max = 0, min = 0;

	if(a > b){
        max = a;
        min = b;

	}else{
        max = b;
        min = a;
	}


    int counter = 0;
    for (int i = 0; i < min; ++i)
    {
        if(max-min >= 0){
            counter++;
            max = max-min;
        }
    }


    printf("Le quotient %d et le reste est %d\n", counter, max);

	return 0;
}