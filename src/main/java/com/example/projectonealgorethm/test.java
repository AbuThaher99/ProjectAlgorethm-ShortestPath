package com.example.projectonealgorethm;

import java.io.*;
import java.util.*;

public class test {
    private static class City {
        private String adjCity;
        private int cost;
        private int hotelCost;

        public City(String adjCity, int cost, int hotelCost) {
            this.adjCity = adjCity;
            this.cost = cost;
            this.hotelCost = hotelCost;
        }

        public String getAdjCity() {
            return adjCity;
        }

        public int getCost() {
            return cost;
        }

        public int getHotelCost() {
            return hotelCost;
        }
    }

    static Map<String, List<Path>> memo2 = new HashMap<>();


    static String start = new String();
    static String end = new String();


    static Map<String, Path> dpTable = new HashMap<>();

    private static Path findShortestPathDP(String start, String end) {
        memo.put(end, new Path(0, end));

        for (int i = 0; i < adjList.size(); i++) {
            for (Map.Entry<String, List<City>> entry : adjList.entrySet()) {
                String current = entry.getKey();
                List<City> neighbors = entry.getValue();
                if (current.equals(end)) {
                    continue; // Skip if the current node is the destination
                }
                Path minPathCost = new Path(Integer.MAX_VALUE, null);

                for (City neighbor : neighbors) {
                    Path pathCost = memo.getOrDefault(neighbor.getAdjCity(), new Path(Integer.MAX_VALUE, null));

                    if (pathCost.getCost() != Integer.MAX_VALUE) {
                        int totalCost = neighbor.getCost() + neighbor.getHotelCost() + pathCost.cost;
                        if (totalCost < minPathCost.cost) {
                            System.out.println("totalCost: " + totalCost);
                            minPathCost = new Path(totalCost, current + "," + pathCost.path);
                        }
                    }
                }
                memo.put(current, minPathCost);
                dpTable.put(current, new Path(minPathCost.cost, minPathCost.path));
            }
        }


        return dpTable.get(start);
    }


    private static List<Path> findsame(String start, String end) {
        Map<String, Path> memo = new HashMap<>();
        Map<String, List<Path>> dpTable = new HashMap<>();
        memo.put(end, new Path(0, end));

        for (int i = 0; i < adjList.size(); i++) {
            for (Map.Entry<String, List<City>> entry : adjList.entrySet()) {
                String current = entry.getKey();
                List<City> neighbors = entry.getValue();

                if (current.equals(end)) {
                    continue; // Skip if the current node is the destination
                }

                Path minPathCost = new Path(Integer.MAX_VALUE, null);

                for (City neighbor : neighbors) {
                    Path pathCost = memo.getOrDefault(neighbor.getAdjCity(), new Path(Integer.MAX_VALUE, null));

                    if (pathCost.getCost() != Integer.MAX_VALUE) {
                        int totalCost = neighbor.getCost() + neighbor.getHotelCost() + pathCost.getCost();
                        if (totalCost < minPathCost.getCost()) {
                            minPathCost = new Path(totalCost, current + "," + pathCost.path);
                        } else if (totalCost == minPathCost.getCost()) {
                            // Add the current path as an alternative path with the same cost
                            dpTable.putIfAbsent(current, new ArrayList<>());
                            dpTable.get(current).add(new Path(totalCost, current + "," + pathCost.path));
                        }
                    }
                }

                memo.put(current, minPathCost);
            }
        }
        // delete the path that are the same path in this arraylist
        List<Path> paths = new ArrayList<>();
        paths.add(dpTable.get(start).get(0));
        for (int i = 1; i < dpTable.get(start).size(); i++) {
            if (!dpTable.get(start).get(i).path.equals(paths.get(paths.size() - 1).path)) {
                paths.add(dpTable.get(start).get(i));
            }
        }


        return paths;
    }


    public static void printAllPaths(String start, String end) {
        List<Path> paths = findAllPathsDP(start, end);
        Collections.sort(paths, Comparator.comparingInt(Path::getCost));
        //  AllPathsFrom.setText("All Paths and from " + start + " to " + end + "\n");
        for (int i = 0; i < paths.size(); i++) {
            System.out.println((i + 1) + ". " + "Path: " + paths.get(i).path + " Cost: " + paths.get(i).cost + "\n");
        }
    }


    private static List<Path> findAllPathsDP(String current, String end) {
        if (current.equals(end)) {
            Path pathCost = new Path(0, current);
            return Collections.singletonList(pathCost);
        }
        if (memo2.containsKey(current)) {
            return memo2.get(current);
        }

        List<Path> allPaths = new ArrayList<>();
        List<City> neighbors = adjList.getOrDefault(current, Collections.emptyList());
        for (City neighbor : neighbors) {
            List<Path> pathCosts = findAllPathsDP(neighbor.getAdjCity(), end);
            for (Path pathCost : pathCosts) {
                if (pathCost.cost != Integer.MAX_VALUE) {
                    int totalCost = neighbor.getCost() + neighbor.getHotelCost() + pathCost.cost;
                    String path = current + "," + pathCost.path;
                    Path newCost = new Path(totalCost, path);
                    allPaths.add(newCost);
                }
            }
        }
        memo2.put(current, allPaths);
        return allPaths;
    }

    static Map<String, List<City>> adjList = new HashMap<>();
    static Map<String, Path> memo = new HashMap<>();

    private static Path findShortestPathDPf(String current, String end) {
        if (current.equals(end)) {
            return new Path(0, current);
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }

        Path minPathCost = new Path(Integer.MAX_VALUE, null);
        List<City> neighbors = adjList.getOrDefault(current, Collections.emptyList());
        for (City neighbor : neighbors) {
            Path pathCost = findShortestPathDPf(neighbor.getAdjCity(), end);
            if (pathCost.cost != Integer.MAX_VALUE) {
                int totalCost = neighbor.getCost() + neighbor.getHotelCost() + pathCost.cost;
                if (totalCost < minPathCost.cost) {
                    minPathCost = new Path(totalCost, current + "," + pathCost.path);
                }
            }
        }
        System.out.println(current + " with all cities:");
        for (Map.Entry<String, Path> entry : memo.entrySet()) {

                System.out.println(entry.getKey() + "=" + entry.getValue().getCost());
        }
        System.out.println();
        memo.put(current, minPathCost);

        return minPathCost;
    }



    private static int findMinimumCost(Map<String, List<City>> cityMap, String startCity, String endCity) {


        Map<String, Integer> minCostMap = new HashMap<>();
        try {
            Map<String, String> pathMap = new HashMap<>();
            minCostMap.put(startCity, 0);

            for (String city : cityMap.keySet()) {
                if (!city.equals(startCity)) {
                    minCostMap.put(city, Integer.MAX_VALUE);
                }
            }
            Queue<String> queue = new LinkedList<>();
            queue.offer(startCity);
            while (!queue.isEmpty()) {
                String currentCity = queue.poll();
                List<City> adjacentCities = cityMap.get(currentCity);

                if (adjacentCities != null) {
                    for (City cityInfo : adjacentCities) {
                        String adjacentCity = cityInfo.getAdjCity();
                        int distance = cityInfo.cost;
                        int cost = cityInfo.hotelCost;
                        int totalCost = minCostMap.get(currentCity) + cost + distance;
                        if (!minCostMap.containsKey(adjacentCity) || totalCost < minCostMap.get(adjacentCity)) {
                            minCostMap.put(adjacentCity, totalCost);
                            pathMap.put(adjacentCity, currentCity);
                            queue.offer(adjacentCity);
                            //


                        }

                    }

                }
            }


            // Backtrack to find the path
            List<String> path = new ArrayList<>();
            String currentCity = endCity;

            while (!currentCity.equals(startCity)) {

                path.add(currentCity);
                currentCity = pathMap.get(currentCity);
            }

            path.add(startCity);
            Collections.reverse(path);

            System.out.println("Path: " + path);
            System.out.println("DP Table:" + minCostMap);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return minCostMap.get(endCity);
    }







    public static void main(String[] args) throws FileNotFoundException {

        List<City> startCities = new ArrayList<>();
        startCities.add(new City("A", 22, 70));
        startCities.add(new City("B", 8, 80));
        startCities.add(new City("C", 12, 80));
        adjList.put("Start", startCities);

        List<City> aCities = new ArrayList<>();
        aCities.add(new City("D", 8, 50));
        aCities.add(new City("E", 10, 70));
        adjList.put("A", aCities);

        List<City> bCities = new ArrayList<>();
        bCities.add(new City("D", 25, 50));
        bCities.add(new City("E", 10, 70));
        adjList.put("B", bCities);

        List<City> cCities = new ArrayList<>();
        cCities.add(new City("D", 13, 50));
        cCities.add(new City("E", 13, 70));
        adjList.put("C", cCities);

        List<City> dCities = new ArrayList<>();
        dCities.add(new City("F", 25, 50));
        dCities.add(new City("G", 30, 70));
        dCities.add(new City("H", 18, 70));
        dCities.add(new City("I", 27, 60));
        adjList.put("D", dCities);

        List<City> eCities = new ArrayList<>();
        eCities.add(new City("F", 12, 50));
        eCities.add(new City("G", 10, 70));
        eCities.add(new City("H", 8, 70));
        eCities.add(new City("I", 7, 60));
        adjList.put("E", eCities);

        List<City> fCities = new ArrayList<>();
        fCities.add(new City("J", 26, 50));
        fCities.add(new City("K", 13, 70));
        fCities.add(new City("L", 15, 60));
        adjList.put("F", fCities);

        List<City> gCities = new ArrayList<>();
        gCities.add(new City("J", 8, 50));
        gCities.add(new City("K", 10, 70));
        gCities.add(new City("L", 10, 60));
        adjList.put("G", gCities);

        List<City> hCities = new ArrayList<>();
        hCities.add(new City("J", 20, 50));
        hCities.add(new City("K", 10, 70));
        hCities.add(new City("L", 10, 60));
        adjList.put("H", hCities);

        List<City> iCities = new ArrayList<>();
        iCities.add(new City("J", 15, 50));
        iCities.add(new City("K", 10, 70));
        iCities.add(new City("L", 7, 60));

        adjList.put("I", iCities);

        List<City> jCities = new ArrayList<>();
        jCities.add(new City("End", 10, 0));
        adjList.put("J", jCities);

        List<City> kCities = new ArrayList<>();
        kCities.add(new City("End", 10, 0));
        adjList.put("K", kCities);

        List<City> lCities = new ArrayList<>();
        lCities.add(new City("End", 10, 0));
        adjList.put("L", lCities);


//       Path c = findShortestPathDP("Start", "End");
//        System.out.println("Shortest Path: " + c.path + " with cost: " + c.cost);
//        List<Path> df = findsame("Start", "End");
//        System.out.println("The altrnative  Path: " + df.size());
//        for (Path p : df) {
//            System.out.println("Shortest Path: " + p.path + " with cost: " + p.cost);
//
//        }
//        printAllPaths("Start", "End");
//        int cost = findMinimumCost(adjList,"Start", "End");
//        System.out.println("Shortest Path: " + cost);

        findShortestPathDPf("Start", "End");
    //    int d = findMinimumCost(adjList,"Start", "End");


    }
}




