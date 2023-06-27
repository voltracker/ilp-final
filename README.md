# Informatics Large Practical (2022)

This was my implementation of the informatics large practical (ILP) coursework for the corresponding course at the University of Edinburgh.

## The Task

In essence, the goal was to pathfind from a start point to a goal, and back, in as few steps as possible. The scenario set out for us was that the program was a order processing and pathfinding system for a pizza delivery drone. The drone was only allowed to move in the 16 cardinal directions for a fixed distance at a time.

One step in a cardinal direction was referred to as a 'move' and we were constrained to 2000 moves per day, with the drone being required to finish at the start point, before running out of moves.


## My Solution

My solution involved creating a *visibility graph*, which according to [wikipedia](https://en.wikipedia.org/wiki/Visibility_graph) is:
> a graph of intervisible locations, typically for a set of points and obstacles in the Euclidean plane

Once I had computed this graph, I then performed an A* search on it to each of the desired endpoints, before approximating the path found in terms of the 16 cardinal directions. Once I had approximated each of the paths to the endpoint, I found the number of moves required to get to and from each of the endpoints.

As our goal was to deliver as many pizzas as possible, I used a greedy algorithm, where I would keep completing orders at the endpoint with the fewest moves, before moving onto the endpoint with the second fewest moves, and so on. This would continue until the next delivery is deemed unachievable due to the drone having too few moves remaining.



