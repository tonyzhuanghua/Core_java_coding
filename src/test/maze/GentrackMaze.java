package test.maze;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GentrackMaze {

    private int min = Integer.MAX_VALUE; // minimum steps
    private int originalX; // start X
    private int originalY; //Start Y
    private int endX;  //target X
    private int endY;  //target Y
    private int width;  //maze width
    private int height;  //maze height

    private int[][] maze; // the maze
    private int[][] mark; // to mark the route
    private Map<Integer, int[][]> solutions = new HashMap<>(); // put all solutions for the maze


    public void go(int startX, int startY, int step) {
        int[][] next = new int[][]{ //moving steps following the order by "right--down--left--up"
                {1, 0},
                {0, 1},
                {-1, 0},
                {0, -1}
        };
        int nextX, nextY;
        if (startX == endX && startY == endY) {
            if (step < min) {
                min = step;
            }
            int[][] solution = new int[width][height]; // Deep copy mark

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    solution[x][y] = mark[x][y];
                }

            }
            solutions.put(step, solution); // add the solution to the HashMap
            return;
        }
        for (int posible = 0; posible < next.length; posible++) { //moving steps following the order by right--down--left--up
            nextX = startX + next[posible][0];
            nextY = startY + next[posible][1];
            if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {  //exceed border
                continue;
            }
            if (maze[nextX][nextY] == 0 && mark[nextX][nextY] == 0) {  //No walls and not marked as already come

                mark[nextX][nextY] = 1; // mark the position as part of the route
                go(nextX, nextY, step + 1);  //recurse invoke, try next step
                mark[nextX][nextY] = 0;

            }
        }
    }


    /*
    * choose the shortest route and format print
    * */

    public void printShortestRoute() {

        int[][] mark = solutions.get(min);


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == originalX && y == originalY)
                    System.out.print("S ");
                else if (x == endX && y == endY)
                    System.out.print("E ");
                else if (this.maze[x][y] == 1)
                    System.out.print("# ");
                else if (mark[x][y] == 1)
                    System.out.print("X ");
                else
                    System.out.print("  ");
            }

            System.out.println();
        }
        System.out.println();

    }

    /*
     * init maze from the file
     */
    public void initMaze(String fileName) {


        List<String> list;


        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {

            list = br.lines().collect(Collectors.toList());

            list.forEach(System.out::println);
            System.out.println();

            String line1 = list.get(0);
            String line2 = list.get(1);
            String line3 = list.get(2);

            this.width = Integer.parseInt(line1.split(" ")[0]);
            this.height = Integer.parseInt(line1.split(" ")[1]);

            this.originalX = Integer.parseInt(line2.split(" ")[0]);
            this.originalY = Integer.parseInt(line2.split(" ")[1]);

            this.endX = Integer.parseInt(line3.split(" ")[0]);
            this.endY = Integer.parseInt(line3.split(" ")[1]);

            this.maze = new int[width][height];
            this.mark = new int[width][height];

            this.mark[originalX][originalY] = 1;

            for (int y = 0; y < height; y++) {
                String linei = list.get(y + 3);
                String[] lineString = linei.split(" ");
                for (int x = 0; x < width; x++) {
                    int e = Integer.parseInt(lineString[x]);
                    maze[x][y] = e;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

          String fileName = "src/test/maze/input.txt";
//        String fileName = "src/test/maze/small.txt";
//        String fileName = "src/test/maze/medium_input.txt";
//        String fileName = "src/test/maze/large_input.txt";

        GentrackMaze d = new GentrackMaze();

        d.initMaze(fileName);
        d.go(d.originalX, d.originalY, 0);
        d.printShortestRoute();
        if (d.min < Integer.MAX_VALUE)
            System.out.println("The shortest route will take " + d.min + " steps");
        else
            System.out.println("Not be able to reach the end point");
    }
}