CC = gcc
CFLAGS = -Wall -g
PROGS = smash.c history.c




all: smash


smash: $(PROGS)
	$(CC) $(CFLAGS) $^ -o $@

clean:
	rm -f *.o smash
	
