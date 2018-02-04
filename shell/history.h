#ifndef __HISTORY_H
#define __HISTORY_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

struct cmd {
    int size;
    char **history;
};

struct cmd* init_history();
void add_history(struct cmd* cmd, char* input);
void print_history(struct cmd* cmd);
void clear_history(struct cmd* cmd);


#endif /* __HISTORY_H */