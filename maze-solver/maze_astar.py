'''
    Preston McIllece's Homework 4

    This class implements an A-Star algorithm to solve a maze problem.
'''

import numpy as np
from heapq import heappush, heappop
from animation import draw
import argparse

class Node():
    """
    cost_from_start - the cost of reaching this node from the starting node
    state - the state (row,col)
    parent - the parent node of this node, default as None
    """
    def __init__(self, state, cost_from_start, parent = None):
        self.state = state
        self.parent = parent
        self.cost_from_start = cost_from_start


class Maze():
    
    def __init__(self, map, start_state, goal_state, map_index):
        self.start_state = start_state
        self.goal_state = goal_state
        self.map = map
        self.visited = [] # state
        self.m, self.n = map.shape 
        self.map_index = map_index


    def draw(self, node):
        path=[]
        while node.parent:
            path.append(node.state)
            node = node.parent
        path.append(self.start_state)
    
        draw(self.map, path[::-1], self.map_index)


    def goal_test(self, current_state):
        if current_state == self.goal_state:
            return True
        return False


    def get_cost(self, current_state, next_state):
        return 1


    def get_successors(self, state):
        successors = []
        blockRow, blockCol = state[0], state[1]
        blockRow, blockCol = int(blockRow), int(blockCol)
        rowDirections, colDirections = [0, 0, 1, -1], [-1, 1, 0, 0]

        for n in range(4):
            newRow, newCol = blockRow + rowDirections[n], blockCol + colDirections[n]
            if self.map[newRow][newCol] == 1:
                successors.append((newRow, newCol))
        return successors


    # heuristics function
    def heuristics(self, state):
        goalRow, goalCol = self.goal_state[0], self.goal_state[1]
        stateRow, stateCol = state[0], state[1]

        return abs(goalRow - stateRow) + abs(goalCol - stateCol)


    # priority of node 
    def priority(self, node):
        return node.cost_from_start + self.heuristics(node.state)

    
    # solve it
    def solve(self):
        container = []
        count = 1
        starting_state = self.start_state

        if self.goal_test(starting_state):
            return starting_state

        self.visited.append(starting_state)
        starting_node = Node(starting_state, 0, None)
        heappush(container, (self.priority(starting_node), count, starting_node))

        while container:
            node = heappop(container)[2]
            successors = self.get_successors(node.state)

            for next_state in successors:
                beenVisited = False
                for entry in self.visited:
                    if np.array_equal(next_state, entry):
                        beenVisited = True
                if not beenVisited:
                    self.visited.append(next_state)
                    next_cost = node.cost_from_start + self.get_cost(node.state, next_state)
                    next_node = Node(next_state, next_cost, node)

                    if self.goal_test(next_state):
                        self.draw(next_node)
                        return
                    count += 1
                    heappush(container, (self.priority(next_node), count, next_node))

    
if __name__ == "__main__":

    parser = argparse.ArgumentParser(description='maze')
    parser.add_argument('-index', dest='index', required = True, type = int)
    index = parser.parse_args().index

    # Example:
    # Run this in the terminal solving map 1
    #     python maze_astar.py -index 1
    
    data = np.load('map_'+str(index)+'.npz')
    map, start_state, goal_state = data['map'], tuple(data['start']), tuple(data['goal'])

    game = Maze(map, start_state, goal_state, index)
    game.solve()
    