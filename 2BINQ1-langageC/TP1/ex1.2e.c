#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	int value = 0;
	scanf("%d", &value);

	if(value <= 0 ){
		printf("bad value \n");
		return 1;
	}

	for (int i = 1; i <= value; ++i)
	{
		if((value % i) == 0)
			printf("%d / %d \n", value, i);
	}



	return 0;
}