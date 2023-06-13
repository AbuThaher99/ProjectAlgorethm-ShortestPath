package com.example.projectonealgorethm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class HelloApplication extends Application {
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
    static TextArea Expected;
    static TextArea diffrent;
    static   TextArea all;
    static  TextArea TABLE;
    static  Set<String> citySet = new HashSet<>();
    static int flag=0;
    public static void readFile(Stage pri) throws FileNotFoundException {
        try {
            FileChooser fileChooser = new FileChooser();
            file1 = fileChooser.showOpenDialog(pri);
            Scanner sc = new Scanner(file1);
            if (file1 != null) {
                numCities = sc.nextInt();
                String[] startEnd = sc.next().trim().split(",");
                start = startEnd[0];
                end = startEnd[1];



                while (sc.hasNext()) {
                    String[] lines = sc.next().trim().split("\\r?\\n");
                    System.out.println(lines[0]);

                    for (int i = 0; i < lines.length; i++) {
                        String line = lines[i].trim();

                        if (line.isEmpty()) {
                            continue;
                        }

                        String[] parts = line.trim().split(",\\s*", 2);
                        String cityName = parts[0];
                        citySet.add(cityName);
                        System.out.println(cityName);
                        System.out.println(parts[1]);

                        String[] cityList = parts[1].trim().split("\\],\\s*\\[");
                        System.out.println(cityList[0]);

                        for (String cityInfo : cityList) {
                            String[] cityData = cityInfo.replaceAll("[\\[\\]]", "").trim().split(",\\s*");
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

//                for (int i = 0; i < numCities-1 ; i++) {
//                    for (int j = 0; j < numCities; j++) {
//                        int cost = minCostMatrix[i][j];
//                        if (cost == Integer.MAX_VALUE) {
//                            System.out.print("X\t");
//                        } else {
//                            System.out.print(cost + "\t");
//                        }
//                    }
//                    System.out.println();
//                }



            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter a valid file name.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }



    public static void readFile1(Stage pri) throws FileNotFoundException {
        try {
            FileChooser fileChooser = new FileChooser();
            file1 = fileChooser.showOpenDialog(pri);
            Scanner sc = new Scanner(file1);
            if (file1 != null) {
                numCities = sc.nextInt();
                sc.nextLine(); // Consume the newline after numCities

                String startEndLine = sc.nextLine().trim();
                String[] startEnd = startEndLine.split(",");
                if (startEnd.length < 2) {
                    // Handle error: Invalid start and end format
                    throw new IllegalArgumentException("Invalid start and end format in the file.");
                }
                start = startEnd[0].trim();
                end = startEnd[1].trim();
                System.out.println(start + " AbuTahher  " + end);



                int count = 0;
                while (sc.hasNext()) {
                    String line = sc.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] parts = line.trim().split(",\\s*", 2);
                    String cityName = parts[0];
                    citySet.add(cityName);

                    System.out.println(cityName);
                    System.out.println(parts[1]);

                    String[] cityList = parts[1].trim().split("\\],\\s*\\[");
                    System.out.println(cityList[0]);

                    for (String cityInfo : cityList) {
                        String[] cityData = cityInfo.replaceAll("[\\[\\]]", "").trim().split(",\\s*");
                        int hotelCost = cityData.length > 2 ? Integer.parseInt(cityData[2].trim()) : 0;
                        System.out.println("Info : " + hotelCost + " " + cityData[0] + " " + cityData[1]);
                        citySet.add(cityData[0]);
                    }
                    count++;
                }

                cityNames = new String[numCities];
                cityNames[0] = start;
                citySet.remove(start);
                int index = 1;
                for (String cityName : citySet) {
                    cityNames[index++] = cityName;
                }
                citySet.add(start);
                adjMatrix = new Edge[numCities][numCities];
                // Populate adjacency matrix
                sc = new Scanner(file1); // Reset scanner
                sc.nextInt(); // Skip the first line
                sc.next(); // Skip the second line

                while (sc.hasNext()) {
                    String line = sc.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] parts = line.split(",\\s*");

                    String cityName = parts[0];

                    List<String> cityDataList = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        if (parts[i].startsWith("[")) {
                            cityDataList.add(parts[i]);
                        } else {
                            cityDataList.set(cityDataList.size() - 1, cityDataList.get(cityDataList.size() - 1) + ", " + parts[i]);
                        }
                    }

                    int sourceIndex = getIndex(cityName);

                    for (String cityData : cityDataList) {
                        String[] cityDataArr = cityData.replaceAll("[\\[\\]]", "").split(",\\s*");
                        int destinationIndex = getIndex(cityDataArr[0]);
                        int petrolCost = Integer.parseInt(cityDataArr[1].trim());
                        int hotelCost = cityDataArr.length > 2 ? Integer.parseInt(cityDataArr[2].trim()) : 0;

                        adjMatrix[sourceIndex][destinationIndex] = new Edge(cityDataArr[0], petrolCost, hotelCost);
                        minCostMatrix = new int[numCities][numCities]; // Move initialization here
                        prevCityMatrix = new int[numCities][numCities]; // Move initialization here
                        // Initialize the Dp memo
                        dpMemo = new List[numCities][numCities];
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
            }



        } catch (FileNotFoundException e) {
         e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No path exists from "+start+" to "+end+".");
            alert.showAndWait();
        } else {
            int cost = minCostMatrix[sourceIndex][destinationIndex];

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
            System.out.println("Minimum cost from+"+start+" to "+end+": " + cost);
            Expected.setText("Path: "+start+" -> " + String.join(" -> ", path));
            Expected.appendText("\n"+"Minimum cost from  "+start+"  to  "+end+": " + cost);
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
        diffrent.setText("Different Path: " + String.join(" -> ", bestPath) );
        diffrent.appendText("\n"+"Minimum cost from  "+start+"  to  "+end+": " + cost);
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


    @Override
    public void start(Stage stage) throws IOException {
        Image image = new Image("ab.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(602);
        imageView.setFitWidth(1200);

        Pane fxmlLoader = new Pane();

        ChoiceBox<String> comboBox = new ChoiceBox<>();
        comboBox.setLayoutX(14);
        comboBox.setLayoutY(93);
        ChoiceBox<String> comboBox2 = new ChoiceBox<>();
        comboBox2.setLayoutX(241);
        comboBox2.setLayoutY(93);

        comboBox.getItems().add("Select City");
        comboBox2.getItems().add("Select City");

        Label label = new Label("The Expected Ruselt is:");
        label.setLayoutX(7);
        label.setLayoutY(152);
        label.setPrefWidth(72);
        label.setPrefHeight(17);
         Expected = new TextArea();
        Expected.setLayoutX(89);
        Expected.setLayoutY(139);
        Expected.setPrefSize(303, 43);





        Label label2 = new Label("The Deffrent PATHS are:");
        label2.setLayoutX(7);
        label2.setLayoutY(253);
        label2.setPrefWidth(72);
        label2.setPrefHeight(17);
         diffrent = new TextArea();
        diffrent.setLayoutX(89);
        diffrent.setLayoutY(194);
        diffrent.setPrefSize(303, 118);


        Label label3 = new Label("The ALL PATH is:");
        label3.setLayoutX(7);
        label3.setLayoutY(412);

        label3.setPrefWidth(72);
        label3.setPrefHeight(17);


         all = new TextArea();
        all.setLayoutX(89);
        all.setLayoutY(326);

        TABLE = new TextArea();
        TABLE.setLayoutX(397);
        TABLE.setLayoutY(4);
        TABLE.setPrefSize(790, 519);
        Button show = new Button("Show");
        Button showTable = new Button("showTable");
        showTable.setLayoutX(304);
        showTable.setLayoutY(540);
        showTable.setOnAction(e->{
            printDPTable(minCostMatrix,cityNames);

        });

        Button lood = new Button("lood");
        lood.setLayoutX(63);
        lood.setLayoutY(540);
        lood.setOnAction(e->{
            try {
            Expected.setText("");
            diffrent.setText("");
            all.setText("");


            try {
                readFile1(stage);

                comboBox.getItems().addAll(citySet);
                comboBox2.getItems().addAll(citySet);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            printTheExpectedOutput();
            findDifferentPath(start, end);
            List<PathCost>  f =  findAllPathsDP(start, end);
                all.setText("ALL PATHS from "+start +" to "+end+": "+"\n");
            Collections.sort(f, Comparator.comparingInt(pathCost -> pathCost.cost));
            for(int i =0 ; i<f.size() ;i++){
                System.out.println((i+1)+"."+f.get(i).path + " -> " + f.get(i).cost);
                all.appendText((i+1)+"."+f.get(i).path + " -> " + f.get(i).cost+"\n");
            }

            }catch (Exception ex){
                System.out.println("Error");
            }

        });



        Button find = new Button("find");
        find.setLayoutX(177);
        find.setLayoutY(540);

        find.setOnAction(e->{
            Expected.setText("");
            diffrent.setText("");
            all.setText("");
            memo2.clear();
            minCostMatrix = new int[numCities][numCities]; // Move initialization here
            prevCityMatrix = new int[numCities][numCities]; // Move initialization here
            dpMemo = new List[numCities][numCities];
            totalCost = 0;
             start = comboBox.getValue();
             end = comboBox2.getValue();


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
                                prevCityMatrix[i][j] = prevCityMatrix[k][j];
                            }
                        }
                    }
                }
            }
            if(start.equals("Select City") || end.equals("Select City")){
                Expected.setText("Please Select City");
                diffrent.setText("Please Select City");
                all.setText("Please Select City");
            }else {
                printTheExpectedOutput();
                findDifferentPath(start, end);
                List<PathCost>  f =  findAllPathsDP(start, end);
                all.setText("ALL PATHS from "+start +" to "+end+": "+"\n");
                Collections.sort(f, Comparator.comparingInt(pathCost -> pathCost.cost));
                for(int i =0 ; i<f.size() ;i++){
                    System.out.println((i+1)+"."+f.get(i).path + " -> " + f.get(i).cost);
                    all.appendText((i+1)+"."+f.get(i).path + " -> " + f.get(i).cost+"\n");
                }
            }


        });
        fxmlLoader.getChildren().addAll(imageView,label,label2,label3,Expected,diffrent,all,showTable,lood,find,comboBox,comboBox2,TABLE);
        stage.setTitle("Hello!");
        Scene scene = new Scene(fxmlLoader, 1190, 601);

        stage.setScene(scene);
        stage.show();
    }
    private static void printDPTable(int[][] dp, String[] cityNames) {
        int cellWidth = 8; // Adjust the cell width as needed

        StringBuilder output = new StringBuilder();

        output.append("\t\t\t\t\tTable View\n");
        output.append("--------------------------------------------------------------\n");

        output.append(String.format("%-8s", ""));
        for (int i = 1; i < cityNames.length - 1; i++) {
            output.append(String.format("%-" + cellWidth + "s", cityNames[i]));
        }
        output.append(cityNames[cityNames.length - 1]).append("\n");

        output.append("--------------------------------------------------------------\n");

        for (int i = 0; i < dp.length - 1; i++) {
            output.append(String.format("%-8s", cityNames[i]));
            for (int j = 1; j < dp[i].length - 1; j++) {
                if (dp[i][j] == Integer.MAX_VALUE) {
                    output.append(String.format("%-" + cellWidth + "s", "X"));
                } else {
                    output.append(String.format("%-" + cellWidth + "d", dp[i][j]));
                }
            }
            output.append(dp[i][dp[i].length - 1]).append("\n\n");
        }




        System.out.print(output.toString());
        TABLE.setFont(Font.font("Monospaced"));
        TABLE.setText(output.toString());
    }

    public static void main(String[] args) {
        launch();
    }
}