#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "history.h"

#define MAXLINE 512
#define MAXCHARS 5000

struct cmd* init_history()
{
    struct cmd* cmd = (struct cmd*) malloc(sizeof(struct cmd));
    cmd->size = 0;
    cmd->history = malloc(sizeof(char*) * MAXLINE);

    return cmd;
}

void add_history(struct cmd* cmd, char* input)
{
    if(cmd->size == MAXLINE)
    {
        cmd->history[0] = NULL;
        int i;
        for(i = 0; i < cmd->size; i++)
        {
            cmd->history[i] = cmd->history[i+1];
        }

        cmd->history[cmd->size] = malloc(sizeof(char*)*MAXCHARS);
        strncpy(cmd->history[cmd->size], input, MAXCHARS);
    }
    else
    {
        cmd->history[cmd->size] = malloc(sizeof(char*)*MAXCHARS);
        strncpy(cmd->history[cmd->size], input, MAXCHARS);
        (cmd->size)++;
    }
}

void print_history(struct cmd *cmd)
{
    int i;
    for(i = 0; i < cmd->size; i ++)
    {
        printf("[%d] %s\n",i, cmd->history[i]);
    }

    fprintf(stderr, "$");
}

void clear_history(struct cmd* cmd)
{
    int i;
    for(i = 0; i < cmd->size; i++)
    {
        free(cmd->history[i]);
    }
    free(cmd->history);
    free(cmd);
}



