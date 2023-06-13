package com.example.projectonealgorethm;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class test2 {
    public static class Edge {
        private String destination;
        private int petrolCost;
        private int hotelCost;

        public Edge(String destination, int petrolCost, int hotelCost) {
            this.destination = destination;
            this.petrolCost = petrolCost;
            this.hotelCost = hotelCost;
        }

        public String getDestination() {
            return destination;
        }

        public int getPetrolCost() {
            return petrolCost;
        }

        public int getHotelCost() {
            return hotelCost;
        }

        public int getCost() {
            return petrolCost + hotelCost;
        }
    }
    private static Edge[][] adjMatrix;
    static int totalCost = 0;
    private static String[] cityNames;

    private static int numCities;
    static   int[][] minCostMatrix = new int[numCities][numCities];
    private static List<PathCost>[][] dpMemo = new List[numCities][numCities];
    private static Map<String, List<PathCost>> memo2 = new HashMap<>();
    static int[][] prevCityMatrix = new int[numCities][numCities];
    static String  start="";
    static String end="";
    static File file1 = null;
    public static void readFile(String filename) throws FileNotFoundException {
        try {
            file1 = new File(filename);
            Scanner sc = new Scanner(file1);
            if (file1 != null) {
                numCities = sc.nextInt();
                String[] startEnd = sc.next().split(",");
                start = startEnd[0];
                end = startEnd[1];

                Set<String> citySet = new HashSet<>();

                while (sc.hasNext()) {
                    String[] lines = sc.next().split("\\r?\\n");
                    System.out.println(lines[0]);

                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim();

                        if (line.isEmpty()) {
                            continue;
                        }

                        String[] parts = line.split(",\\s*", 2);
                        String cityName = parts[0];
                        citySet.add(cityName);
                        System.out.println(cityName);
                        System.out.println(parts[1]);

                        String[] cityList = parts[1].split("\\],\\s*\\[");
                        System.out.println(cityList[0]);

                        for (String cityInfo : cityList) {
                            String[] cityData = cityInfo.replaceAll("[\\[\\]]", "").split(",\\s*");
                            int hotelCost = cityData.length > 2 ? Integer.parseInt(cityData[2].trim()) : 0;
                            System.out.println("Info : " + hotelCost + " " + cityData[0] + " " + cityData[1]);
                            citySet.add(cityData[0]);
                        }
                    }
                }
                cityNames = citySet.toArray(new String[0]);
                adjMatrix = new Edge[numCities][numCities];
                // Populate adjacency matrix
                sc = new Scanner(file1); // Reset scanner
                sc.nextInt(); // Skip the first line
                sc.next(); // Skip the second line

                while (sc.hasNext()) {
                    String[] lines = sc.next().split("\\r?\\n");
                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim();

                        if (line.isEmpty()) {
                            continue;
                        }

                        String[] parts = line.split(",\\s*", 2);
                        String cityName = parts[0];

                        String[] cityList = parts[1].split("\\],\\s*\\[");
                        int sourceIndex = getIndex(cityName);

                        for (String cityInfo : cityList) {
                            String[] cityData = cityInfo.replaceAll("[\\[\\]]", "").split(",\\s*");
                            int destinationIndex = getIndex(cityData[0]);
                            int petrolCost = Integer.parseInt(cityData[1].trim());
                            int hotelCost = cityData.length > 2 ? Integer.parseInt(cityData[2].trim()) : 0;

                            adjMatrix[sourceIndex][destinationIndex] = new Edge(cityData[0], petrolCost, hotelCost);
                            minCostMatrix = new int[numCities][numCities]; // Move initialization here
                            prevCityMatrix = new int[numCities][numCities]; // Move initialization here
                            // Initialize the Dp memo
                            dpMemo = new List[numCities][numCities];
                        }
                    }
                }


                for (int i = 0; i < numCities; i++) {
                    for (int j = 0; j < numCities; j++) {
                        if (i == j) {
                            minCostMatrix[i][j] = 0;
                        } else if (adjMatrix[i][j] != null) {
                            minCostMatrix[i][j] = adjMatrix[i][j].getCost();
                            prevCityMatrix[i][j] = i; // Initialize previous city to the source
                        } else {
                            minCostMatrix[i][j] = Integer.MAX_VALUE;
                        }
                    }
                }

                for (int k = 0; k < numCities; k++) {
                    for (int i = 0; i < numCities; i++) {
                        for (int j = 0; j < numCities; j++) {
                            if (minCostMatrix[i][k] != Integer.MAX_VALUE && minCostMatrix[k][j] != Integer.MAX_VALUE) {
                                int newCost = minCostMatrix[i][k] + minCostMatrix[k][j];
                                if (newCost < minCostMatrix[i][j]) {
                                    minCostMatrix[i][j] = newCost;
                                    prevCityMatrix[i][j] = prevCityMatrix[k][j]; // Update previous city
                                }
                            }
                        }
                    }
                }

                // Print the minimum cost matrix
//                for (int i = 0; i < numCities -1; i++) {
////                    for (int j = 0; j < numCities; j++) {
//                      //  System.out.print(minCostMatrix[i][j] + "\t");
//                        System.out.print(Arrays.toString(minCostMatrix[i]));
//                  //  }
//                    System.out.println();
//                }

                for (int i = 0; i < numCities-1 ; i++) {
                    for (int j = 0; j < numCities; j++) {
                        int cost = minCostMatrix[i][j];
                        if (cost == Integer.MAX_VALUE) {
                            System.out.print("X\t");
                        } else {
                            System.out.print(cost + "\t");
                        }
                    }
                    System.out.println();
                }



            } else {
              System.out.println("You must choose a file");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }



    private static int getIndex(String cityName) {
        for (int i = 0; i < numCities; i++) {
            if (cityNames[i].equals(cityName)) {
                return i;
            }
        }
        return -1;
    }


    public static void printTheExpectedOutput() {
        int sourceIndex = getIndex(start); // Replace "city1" with the actual city name
        int destinationIndex = getIndex(end); // Replace "city2" with the actual city name

        if (minCostMatrix[sourceIndex][destinationIndex] == Integer.MAX_VALUE) {
            System.out.println("No path exists from city1 to city2.");
        } else {
            int cost = minCostMatrix[sourceIndex][destinationIndex];
            System.out.println("Minimum cost from+"+start+" to "+end+": " + cost);
            totalCost = cost;

            // Reconstruct the path
            List<String> path = new ArrayList<>();
            int currentCityIndex = destinationIndex;
            while (currentCityIndex != sourceIndex) {
                int prevCityIndex = prevCityMatrix[sourceIndex][currentCityIndex];
                String currentCityName = cityNames[currentCityIndex];
                path.add(currentCityName);
                currentCityIndex = prevCityIndex;
            }
            Collections.reverse(path);
            System.out.println("Path: "+start+" -> " + String.join(" -> ", path) + " -> "+end);
        }

    }




    public static List<String> findDifferentPath(String start, String end) {
        int sourceIndex = getIndex(start);
        int destinationIndex = getIndex(end);

        if (minCostMatrix[sourceIndex][destinationIndex] == Integer.MAX_VALUE) {
            System.out.println("No path exists from " + start + " to " + end + ".");
            return Collections.emptyList();
        }

        int cost = minCostMatrix[sourceIndex][destinationIndex];
        System.out.println("Minimum cost from " + start + " to " + end + ": " + cost);
        totalCost = cost;

        // Initialize variables for DFS traversal
        List<String> path = new ArrayList<>();
        boolean[] visited = new boolean[numCities];
        List<String> bestPath = new ArrayList<>();
        int[] bestCost = { Integer.MAX_VALUE };

        // Start DFS traversal
        dfsTraversal(sourceIndex, destinationIndex, visited, path, bestPath, bestCost);

        if (bestPath.isEmpty()) {
            System.out.println("No different path found with minimum cost.");
            return Collections.emptyList();
        }

        System.out.println("Different Path: " + start + " -> " + String.join(" -> ", bestPath) + " -> " + end);
        return bestPath;
    }
    private static void dfsTraversal(int currentCityIndex, int destinationIndex, boolean[] visited, List<String> path, List<String> bestPath, int[] bestCost) {
        visited[currentCityIndex] = true;
        path.add(cityNames[currentCityIndex]);

        if (currentCityIndex == destinationIndex) {
            int currentCost = calculatePathCost(path);
            if (currentCost < bestCost[0]) {
                bestCost[0] = currentCost;
                bestPath.clear();
                bestPath.addAll(path);
            }
        } else {
            for (int i = 0; i < numCities; i++) {
                if (!visited[i] && minCostMatrix[currentCityIndex][i] != Integer.MAX_VALUE) {
                    dfsTraversal(i, destinationIndex, visited, path, bestPath, bestCost);
                }
            }
        }

        path.remove(path.size() - 1);
        visited[currentCityIndex] = false;
    }
    private static int calculatePathCost(List<String> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int sourceIndex = getIndex(path.get(i));
            int destinationIndex = getIndex(path.get(i + 1));
            cost += minCostMatrix[sourceIndex][destinationIndex];
        }
        return cost;
    }



    private static List<PathCost> findAllPathsDP(String current, String end) {
        if (current.equals(end)) {
            PathCost pathCost = new PathCost(0, current);
            return Collections.singletonList(pathCost);
        }
        if (memo2.containsKey(current)) {
            return memo2.get(current);
        }

        List<PathCost> allPaths = new ArrayList<>();
        int currentCityIndex = getIndex(current);
        for (int i = 0; i < numCities; i++) {
            if (adjMatrix[currentCityIndex][i] != null) {
                Edge neighbor = adjMatrix[currentCityIndex][i];
                List<PathCost> pathCosts = findAllPathsDP(neighbor.getDestination(), end);
                for (PathCost pathCost : pathCosts) {
                    if (pathCost.cost != Integer.MAX_VALUE) {
                        int totalCost = neighbor.getPetrolCost() + neighbor.getHotelCost() + pathCost.cost;
                        String path = current + "," + pathCost.path;
                        PathCost newCost = new PathCost(totalCost, path);
                        allPaths.add(newCost);
                    }
                }
            }
        }

        memo2.put(current, allPaths);
        return allPaths;
    }
    public static void main(String[] args) throws FileNotFoundException {

        readFile("C:\\Users\\moham\\Desktop\\AbuThaherJavaTwo\\ProjectOne\\file1.txt");
        printTheExpectedOutput();
      List<String> d= findDifferentPath(start, end);
       List<PathCost>  f = findAllPathsDP(start, end);
        System.out.println("All paths: ");
        Collections.sort(f, Comparator.comparingInt(pathCost -> pathCost.cost));
        for(int i =0 ; i<f.size() ;i++){
            System.out.println((i+1)+"."+f.get(i).path + " -> " + f.get(i).cost);
        }

    }

}
