#include <stdlib.h>
#include <stdio.h>

int main(int argc, char const *argv[])
{
	for (char i = 'A'; i <= 'Z'; ++i)
	{
		printf("Caractere = %c code dec = %d code hexa. = %x  \n", i, i, i);
		
	}

	for (char i = '0'; i <= '9'; ++i)
	{
		
		printf("Caractere = %c code dec = %d code hexa. = %x  \n", i, i, i);
	}
	return 0;
}