#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>
#include <time.h>
#include "history.h"

#define MAX 5000

//function to split up input commands into tokens
void splitInput(struct cmd* cmd, char *input)
{
	char inputCpy[MAX];
	char** argArray = (char**) malloc(sizeof(char) * MAX);

	strcpy(inputCpy, input);

	//handles if first command is blank, (user just returns with no input)
	if(strlen(input) == 0)
	{
		fprintf(stderr, "$ ");
		return;
	}

	char* token = strtok(inputCpy, " ");
	struct cmd* history = cmd;
	int argCount = 0;

	while(token != NULL)
	{
		argArray[argCount] = (char*) malloc(sizeof(char) * (strlen(token)+1));
		strcpy(argArray[argCount], token);
		argCount++;
		token = strtok(NULL, " ");
	}

	argArray[argCount] = NULL;


	char* command = argArray[0];

	//handles if first command is a space
    if(command == NULL){
		fprintf(stderr, "$ ");
		return;
	}

	//if first command is exit, exit program
	if(strcmp(command, "exit") == 0) 
	{
			//clears allocated memory for history
			clear_history(history);
			//freeArray(argArray);
			free(argArray);
			free(command);
			free(token);
			exit(0);
	}

	//if command is history, print history
	else if(strcmp(command, "history") == 0)
	{
		print_history(history);
	}

	//if first command is cd
	else if(strcmp(command, "cd") == 0)
	{
		//token = strtok (NULL, " ");
		char* dirArg = argArray[1];

		if(dirArg == NULL)
		{
			printf("error: need to enter a directory\n");
			fprintf(stderr, "$ ");
		}

		
		//change directory to second input, if invalid display errors
		else if(chdir(dirArg) == -1)
		{
			
			printf("error: %s does not exist\n", token);

			fprintf(stderr, "$ ");
		}

		//make sure directory changed is the right directory, print directory
		else
		{
			char* dir = getcwd(NULL, 0);

			if(dir != NULL)
			{
				if(strcmp(dirArg, dir) == 0)
				{
					printf("%s\n", dir);
					fprintf(stderr,"$ ");
				}
				else
				{
					char* dir2 = getcwd(NULL, 0);
					printf("%s\n", dir2);
					fprintf(stderr,"$ ");
				}
			}

			free(dir);
			free(dirArg);
		}
	}

	else if(strcmp(command, " ") == 0)
	{
		fprintf(stderr, "$ ");
		return;
	}

	//if anything else is input, print all to console
	else
	{
		const char* disable = getenv("CHAOS_MONKEY_DISABLE");

		pid_t pid = fork();
		
		if (pid < 0)
		{
    		// error, failed to fork()
			printf("error\n");
			exit(0);
		} 
		else if (pid == 0)
		{
			if((execvp(command, argArray) < 0))//executes non-built in command, if return is less than 0 error
			{
					printf("error, failed to execute command\n");
					fprintf(stderr, "$ ");
    				_exit(EXIT_FAILURE);   // exec never returns
			}
		}

		else if(disable == NULL)//if chaos monkey is disabled
		{
			srandom(time(NULL));//seed random
			int toKill = random();//make random
    		int status;

			if(toKill % 4 == 0)//kills process approx. 25% of time
			{
				kill(pid, SIGKILL);
			}

			if(waitpid(pid, &status, 0) < 0)//error handling
			{
				printf("error in wait");
			}
			
			else if(WIFEXITED(status))//print if child was not killed
			{
				printf("Child %d exited normally\n", pid);
			}
			
			else if(WIFSIGNALED(status))//print if child is killed
			{
				printf("Child %d was murdered by a chaos monkey in retaliation for Harambe\n", pid);
			}
    		
			fprintf(stderr, "$ ");
		}

		//when parent, wait for child to be done to print $
		else if(pid > 0){
			(waitpid(pid, NULL, 0));
			fprintf(stderr, "$ ");
		}		
	}	
}

//function to read input from user
int readInput()
{

	//allocate space for array that will hold all the user input strings
	char *userInput = (char *) malloc(sizeof(char*) * MAX);
	//char *userInput[1024];
	struct cmd* history = NULL;

	//initialize history struct
	history = init_history();

	fprintf(stderr, "$ ");

	//Getting input from user/stdin and placing in an array	
	while(fgets(userInput, MAX, stdin) != NULL)
	{
		userInput[strlen(userInput) - 1] = '\0';

		//adds user input into history
		add_history(history, userInput);

		//passing array with user input into split input function
		splitInput(history, userInput);
	}

	//free memory
	free(userInput);
	return 0;	
}


int main(void)
{
	readInput();

	return 0;
}


