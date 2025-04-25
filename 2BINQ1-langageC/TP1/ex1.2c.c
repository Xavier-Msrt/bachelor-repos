#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	int value1 = 0;
	int value2 = 0;
    
    printf("Entre val1 : ");
	scanf("%d", &value1);
    printf("Entre val2 : ");
	scanf("%d", &value2);


	printf("Value1 : %d\n", value1);
	printf("Value2 : %d\n", value2);

	int temp = value2;
	value2 = value1;
	value1 = temp;

	printf("Value1 : %d\n", value1);
	printf("Value2 : %d\n", value2);
	return 0;
}