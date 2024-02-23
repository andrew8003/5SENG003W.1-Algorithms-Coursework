//Algorithms Coursework Refer/Defer - Andrew Hanna - w1816963

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


class Node {
    int x;
    int y;
    int dist;
    Node parent;

    public Node(int x, int y, int dist, Node parent) {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.parent = parent;
    }
}

public class AlgorithmsCoursework {

    private static final String BenchmarkFile = "maze40_4.txt"; //-----------Change File Name To Benchmark File Name Here ----------------//

    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static final char START = 'S';
    private static final char FINISH = 'F';
    private static final char ICE = '.';
    private static final char WALL = '0';

    private static final String[] DIRECTION_NAMES = {"right", "left", "down", "up"};

    public static List<Node> findShortestPath(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        Node start = null;
        Node finish = null;
        boolean[][] visited = new boolean[rows][cols];
        int[][] distance = new int[rows][cols];


        for (int i = 0; i < rows; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == START) {
                    start = new Node(i, j, 0, null);
                } else if (grid[i][j] == FINISH) {
                    finish = new Node(i, j, 0, null);
                }
            }
        }


        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.dist));
        queue.add(start);
        distance[start.x][start.y] = 0;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.x == finish.x && current.y == finish.y) {

                List<Node> path = new ArrayList<>();
                while (current != null) {
                    path.add(0, current);
                    current = current.parent;
                }
                return path;
            }

            visited[current.x][current.y] = true;

            for (int[] direction : DIRECTIONS) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                if (isValidMove(grid, newX, newY) && !visited[newX][newY]) {
                    int newDist = current.dist + 1;

                    if (newDist < distance[newX][newY]) {
                        distance[newX][newY] = newDist;
                        Node newNode = new Node(newX, newY, newDist, current);
                        queue.add(newNode);
                    }
                }
            }
        }

        return null; // No path found
    }

    private static boolean isValidMove(char[][] grid, int x, int y) {
        int rows = grid.length;
        int cols = grid[0].length;

        return (x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y] != WALL);
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime(); //Getting Start Time For The Program
    
        char[][] grid = readMapFromFile(BenchmarkFile);
    
        if (grid != null) {
            List<Node> path = findShortestPath(grid);
    
            if (path != null) {
                System.out.println("Path found:");
                int step = 1;
                Node previousNode = null;
                for (Node node : path) {
                    if (previousNode == null) {
                        System.out.println("Starting position: (" + (node.y + 1) + ", " + (node.x + 1) + ")");
                    } else {
                        int directionIndex = getDirectionIndex(previousNode, node);
                        String direction = DIRECTION_NAMES[directionIndex];
                        System.out.println(step + ". Move " + direction + " to (" + (node.y + 1) + ", " + (node.x + 1) + ")");
                        step++;
                    }
                    previousNode = node;
                }
                System.out.println("Done!");
            } else {
                System.out.println("No path found.");
            }
        } else {
            System.out.println("Failed to read maze from the input file");
        }
    
        long endTime = System.nanoTime(); //Getting end time for the program
        double executionTime = (endTime - startTime) / 1_000_000.0; //Time In Milliseconds
        System.out.println("Execution time: " + executionTime + " milliseconds"); //printing the runtime for the program
    }

    private static char[][] readMapFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(BenchmarkFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            int width = -1;

            while ((line = br.readLine()) != null) {
                lines.add(line);
                if (width == -1) {
                    width = line.length();
                } else if (line.length() != width) {
                    System.out.println("Invalid maze format: Inconsistent line lengths.");
                    return null;
                }
            }

            int height = lines.size();
            char[][] grid = new char[height][width];

            for (int i = 0; i < height; i++) {
                line = lines.get(i);
                for (int j = 0; j < width; j++) {
                    char c = line.charAt(j);
                    grid[i][j] = c;
                }
            }

            return grid;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int getDirectionIndex(Node fromNode, Node toNode) {
        int dx = toNode.x - fromNode.x;
        int dy = toNode.y - fromNode.y;

        if (dx == 0 && dy == 1) {
            return 0; // right
        } else if (dx == 0 && dy == -1) {
            return 1; // left
        } else if (dx == 1 && dy == 0) {
            return 2; // down
        } else if (dx == -1 && dy == 0) {
            return 3; // up
        }

        return -1; // invalid direction
    }
    
}
