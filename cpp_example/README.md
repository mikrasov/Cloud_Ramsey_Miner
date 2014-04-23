 simple taboo search

 simple_taboo_search.c 	: taboos edges abd walks up from
 simple_taboo_search-6.c  a small graph size by adding a node
			  to each counter example found -- the 6 version
			  looks for R(6,6) while the regular version looks
			  for R(5,5)
 jrb.[ch]	: red-black tree implementation due to Jim Plank
		  http://web.eecs.utk.edu/~plank/
 dllist.[ch]	: doubly linked list code due to Jim Plank
		  http://web.eecs.utk.edu/~plank/
 jval.[ch]	: generic C types used by jrb.[ch] and dllist.[ch]
 
 fifo.[ch]	: FIFO routines for implementing various forms of taboo

 to run
	./simple_taboo_search
	./simple_taboo_search-6

	if best_count = 9999999 the search has ended and the program will
	terminate

