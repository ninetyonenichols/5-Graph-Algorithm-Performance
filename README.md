FILE: README.md
AUTHOR: Justin Nichols
CLASS: CSC210
INSTRUCTOR: Tyler Conklin
ASSIGNMENT: PA5

_______________________________________________________


INTRO:

My approach relies on a combination of the greedy approach and the brute-force
    approach. It first calls on the greedy approach to find a trip that will be
    a reasonably good approximation of the ideal trip (most of the time). Next,
    it calls upon the brute-force approach, using the approximation produced by
    the greedy algorithm to help narrow the search-space. Specifically, any 
    trips that are already longer than the one that the greedy-algorithm 
    produced are discarded as soon as that fact is known.



_______________________________________________________

EXPERIMENT 1: 'big9.mtx'

heuristic: cost = 1.2622343863999999, 7 milliseconds
mine: cost = 1.2622343863999999, 189 milliseconds
backtrack: cost = 1.2622343863999999, 326 milliseconds

heuristic: cost = 1.2622343863999999, 3 milliseconds
mine: cost = 1.2622343863999999, 171 milliseconds
backtrack: cost = 1.2622343863999999, 281 milliseconds

_______________________________________________________



EXPERIMENT 2: 'big11.mtx'

heuristic: cost = 3.3969307170000005, 4 milliseconds
mine: cost = 1.3566775349999998, 821 milliseconds
backtrack: cost = 1.3566775349999998, 500 milliseconds

heuristic: cost = 3.3969307170000005, 2 milliseconds
backtrack: cost = 1.3566775349999998, 369 milliseconds
mine: cost = 1.3566775349999998, 495 milliseconds


_______________________________________________________


EXPERIMENT 3: 'big12.mtx'

heuristic: cost = 1.8271134757999998, 8 milliseconds
mine: cost = 1.3611004867999998, 810 milliseconds
backtrack: cost = 1.3611004867999998, 1067 milliseconds

heuristic: cost = 1.8271134757999998, 7 milliseconds
backtrack: cost = 1.3611004867999998, 1660 milliseconds
mine: cost = 1.3611004867999998, 731 milliseconds


_______________________________________________________

EXPERIMENT 4: 'big15.mtx'

heuristic: cost = 2.642059844, 14 milliseconds
mine: cost = 2.001340263, 252153 milliseconds
backtrack: cost = 2.001340263, 797534 milliseconds

heuristic: cost = 2.642059844, 7 milliseconds
mine: cost = 2.001340263, 700647 milliseconds
backtrack: cost = 2.001340263, 739755 milliseconds

heuristic: cost = 2.642059844, 25 milliseconds
mine: cost = 2.001340263, 681683 milliseconds
backtrack: cost = 2.001340263, 603432 milliseconds

heuristic: cost = 2.642059844, 9 milliseconds
backtrack: cost = 2.001340263, 360962 milliseconds
mine: cost = 2.001340263, 253832 milliseconds


_______________________________________________________



RESULTS:

The numerical results are somewhat inconsistent. I ran each of these
	experiments many times, regardless of how many times they were
	listed here. There were some times when permuting the order of the tests 
	would affect which function had a longer runtime.
	
For larger files, my approach generally seems to have shorter runtimes than
	the backtracking approach, but even with these larger files there is still
	some fluctuation.
	
I believe there is a lot of noise in the data. I am not sure what exactly is 
	causing it, but some other students have reported it on Piazza (one even
	said their SL was able to replicate the effect, without understanding why).
	
I did the best I could to be thorough about this experiment. But unfortunately,
	due to limitations on the machinery, I do not think it will be possible to
	conclude much. 



