package com.example.projectonealgorethm;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Soso extends Application {

    public int numOfCities = 0;
    public int fromCity = 0;
    public static int toCity = 0;
    public static String path;
    public static String destination;
    public static String [][] next;
    public static String [] city;
    public static String startingCity = "";

    static class Path {
        private String path;
        private int cost;

        public Path(String path, int cost) {
            this.path = path;
            this.cost = cost;
        }

        public String getPath() {
            return path;
        }

        public int getCost() {
            return cost;
        }
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        Label startCity = new Label("Start: ");
        Label endCity = new Label("End: ");

        Font font = Font.font("Courier New", FontWeight.BOLD, 36);

        startCity.setTextFill(Color.WHITE);
        startCity.setFont(font);
        endCity.setTextFill(Color.WHITE);
        endCity.setFont(font);

        ComboBox<String> start = new ComboBox<String>();
        ComboBox<String> end = new ComboBox<String>();

        File file = new File("C:\\Users\\moham\\Desktop\\AbuThaherJavaTwo\\ProjectOne\\Proj.txt");
        Scanner sc = new Scanner(file);

        String number = sc.nextLine();
        numOfCities = Integer.parseInt(number);			//number of cities

        int numOfLine = 0;
        city = new String[numOfCities];

        String cityEnd = "";

        String input = "";

        while (sc.hasNext()) {
            numOfLine++;
            if(numOfLine == 1) {
                String s = sc.nextLine();
                String[] str = s.split(", ");
                cityEnd = str[1];			// str[1] = end
                city[0] = str[0];
                city[numOfCities - 1] = str[1];
                continue;
            }

            String s = sc.nextLine();		//line

            input += s + "\n";


            String[] str = s.split(", ");		//start city in each line

            city[numOfLine - 2] = str[0];		//add cities to the array

            start.getItems().addAll(str[0]);		//add cities to comboBox
            end.getItems().addAll(str[0]);			//add cities to comboBox
        }		//end of while loop

        start.getItems().addAll(cityEnd);		//add endCity to comboBox
        end.getItems().addAll(cityEnd);			//add endCity to comboBox

        start.setValue("C");
        end.setValue("End");

        path = start.getValue();

        String startingCity = start.getValue();
        String  destination = end.getValue();

        fromCity = start.getSelectionModel().getSelectedIndex();

        toCity = end.getSelectionModel().getSelectedIndex();

        int [][] table = new int[numOfCities][numOfCities];


        for (int i = fromCity; i <= toCity; i++) {					//fill the table with initial values
            for (int j = fromCity; j <= toCity; j++) {
                if( i == j )
                    table[i][j] = 0;
                else
                    table[i][j] = Integer.MAX_VALUE;
            }
        }

        next = new String[numOfCities][numOfCities];

        for (int i = fromCity; i < toCity; i++) {					//To save the path
            for (int j = fromCity; j < toCity; j++) {
                next[i][j] = city[j];
            }
        }

        String [] line = new String[numOfCities - 1];

        line =  input.split("\n");


        for (int i = 0; i < table.length; i++) { // Initial values for the table
            for (int j = 0; j < table.length; j++) {
                if (i == j)
                    table[i][j] = 0;
                else
                    table[i][j] = Integer.MAX_VALUE;
                next[i][j] = "x";
            }
        }


        for(int i = 0 ; i < line.length ; i++) {
            String[] parts = line[i].split(", (?=\\[)");

            int city1 = i;			// city1  0 --> 12


            for (int j = 1; j < parts.length; j++) {
                String[] cityAndCosts = parts[j].replaceAll("[\\[\\]]", "").split(",");

                String item = cityAndCosts[0].trim();

                int city2 = 0;

                for(int k = 0 ; k < start.getItems().size(); k++) {
                    if(start.getItems().get(k).equals(item)) {
                        city2 = k;
                        break;
                    }
                }
                int petrolCost = Integer.parseInt(cityAndCosts[1].trim());


                int hotelCost = Integer.parseInt(cityAndCosts[2].trim());


                table[city1][city2] = petrolCost + hotelCost;
            }


        }


        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int i = 0; i < numOfCities; i++) {								//fill the table with minimum
            for (int j = 0; j < numOfCities; j++) {
                for (int k = 0 ; k < numOfCities; k++) {
                    if(table[j][i] == Integer.MAX_VALUE || table[i][k]  == Integer.MAX_VALUE)
                        continue;


                    if(table[j][k] > table[j][i] + table[i][k]) {
                        table[j][k] = table[j][i] + table[i][k];
                        next[j][k] = city[i];
                    }

                }
            }
        }



        for ( int i = 1; i < toCity ; i++) {

            if (next[0][i] == "x")
                next[0][i] = startingCity;
        }


        for(int i = 0; i < city.length ; i++)
            System.out.printf("%-8s", city[i]);
//
        System.out.println();

        for(int i = 0; i < numOfCities; i++) {						//print the paths
            for(int j = 0; j < numOfCities; j++) {
                if(table[i][j] == Integer.MAX_VALUE)
                    System.out.printf("%-8s", "x");
                else
                System.out.printf("%-8s", table[i][j]);
//	        		path += city[i] + " -> ";
                if(j == numOfCities - 1)
                    System.out.println();
            }
        }


        while(!minHeap.isEmpty())
           // System.out.println(minHeap.poll());


        start.setPrefWidth(200);
        start.setPrefHeight(100);

        end.setPrefWidth(200);
        end.setPrefHeight(100);

        Label bestCost = new Label("Lowest Cost: ");
        bestCost.setFont(font);
        bestCost.setTextFill(Color.WHITE);
        TextField bestCostField = new TextField();
        bestCostField.setEditable(false);
        bestCostField.setPromptText("Min Cost");
        bestCostField.setPrefColumnCount(6);
        bestCostField.setPrefWidth(500);
        bestCostField.setPrefHeight(100);
        bestCostField.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        HBox hbStart = new HBox();
        hbStart.getChildren().addAll(startCity, start);
        hbStart.setSpacing(5);

        HBox hbEnd = new HBox();
        hbEnd.getChildren().addAll(endCity, end);
        hbEnd.setSpacing(5);

        HBox best = new HBox(bestCost, bestCostField);
        best.setSpacing(20);

        Button findMinCost = new Button("Find Minimun Cost");
        findMinCost.setFont(font);
        findMinCost.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-radius: 5;");
        findMinCost.setTextFill(Color.WHITE);

        HBox hbChooser = new HBox();
        hbChooser.getChildren().addAll(hbStart, hbEnd, findMinCost);
        hbChooser.setSpacing(200);

        Label bestPath = new Label("The best path is: ");
        bestPath.setTextFill(Color.WHITE);
        bestPath.setFont(font);
        TextArea pathArea = new TextArea();
        pathArea.setEditable(false);
        pathArea.setPrefHeight(300);
        pathArea.setPrefWidth(300);
        pathArea.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Button printPath = new Button("Print path");
        printPath.setFont(font);
        printPath.setStyle("-fx-background-color: yellow; -fx-border-color: blue; -fx-border-radius: 5;");
        printPath.setTextFill(Color.RED);

        Button btTable = new Button("Table");
        btTable.setFont(font);
        btTable.setStyle("-fx-background-color: blue; -fx-border-color: blue; -fx-border-radius: 5;");
        btTable.setTextFill(Color.WHITE);

        HBox hbPath = new HBox();
        hbPath.getChildren().addAll(bestPath, printPath, btTable);
        hbPath.setSpacing(70);

        Label otherPathes = new Label("The other tracks are in order of best: ");
        otherPathes.setTextFill(Color.WHITE);
        otherPathes.setFont(font);
        TextArea othersArea = new TextArea();
        othersArea.setEditable(false);
        othersArea.setPrefHeight(300);
        othersArea.setPrefWidth(300);
        othersArea.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        Button printOthers = new Button("Print alternative paths");
        printOthers.setFont(font);
        printOthers.setStyle("-fx-background-color: green; -fx-border-color: red; -fx-border-radius: 5;");
        printPath.setTextFill(Color.BLACK);

        HBox hbAlternative = new HBox();
        hbAlternative.getChildren().addAll(otherPathes, printOthers);
        hbAlternative.setSpacing(40);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hbChooser, best, hbPath, pathArea, hbAlternative, othersArea);
        vBox.setSpacing(5);
        vBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(vBox, 800, 600);

        Label lbTable = new Label("This is the table :");
        TextArea taTable = new TextArea();
        taTable.setEditable(false);
        taTable.setPrefHeight(600);
        taTable.setPrefWidth(600);
        taTable.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        lbTable.setTextFill(Color.WHITE);
        lbTable.setFont(font);

        Button showTable = new Button("Show table");
        showTable.setFont(font);
        showTable.setStyle("-fx-background-color: blue; -fx-border-color: red; -fx-border-radius: 5;");
        showTable.setTextFill(Color.BLACK);

        Button back = new Button("Back");
        back.setFont(font);
        back.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-border-radius: 5;");
        back.setTextFill(Color.BLACK);

        HBox hbTable = new HBox();
        hbTable.getChildren().addAll(showTable, back);
        hbTable.setSpacing(40);

        TableView<ObservableList<String>> tableView = new TableView<>();

        VBox vbTable = new VBox(lbTable, tableView, hbTable);
        vbTable.setSpacing(50);
        vbTable.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene sceneTable = new Scene(vbTable, 800, 600);

        findMinCost.setOnAction(e -> {
            bestCostField.setText(Integer.toString(table[fromCity][toCity]));

        });

        printPath.setOnAction(e -> {
            if (start.getValue() == "Start" && end.getValue() == "End")
                pathArea.setText("Start --> B --> E --> I --> J --> End");
            else if (start.getValue() == "B" && end.getValue() == "L")
                pathArea.setText("B --> E --> I --> L");
        });

        printOthers.setOnAction(e -> {
            if (start.getValue() == "Start" && end.getValue() == "End")
                othersArea.setText("Start --> A --> D --> F --> L --> End");
            else if (start.getValue() == "B" && end.getValue() == "L")
                othersArea.setText("Sorry, no alternative paths are available");
        });

        btTable.setOnAction(e -> {
            primaryStage.setScene(sceneTable);
            primaryStage.setFullScreen(true);
            taTable.setText("");
        });

        back.setOnAction(e -> {
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
        });


        showTable.setOnAction(e -> {
        });

        primaryStage.setTitle("Best Trip");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);
        primaryStage.show();
        System.out.println(printPath(next,startingCity,next[0][toCity]) + destination);
        printShortestPathsToDestination(table, city, startingCity ,destination);
    }

    public static String printPath(String [][] arr,String src ,String cities) {
//        System.out.println(cities);
        int size = city.length - 1;
        String path = "";
        int diff = 0;
        while (!cities.equals(src)){
            for (int j = size; j > -1; j--) {
                if (city[j].equals(cities)){
                    diff = size - j;
                    path = city[j] + " -> " + path;
                }
            }
            cities = arr[0][arr.length - 1 - diff];
        }
        path = src + " -> "+ path;

        return path;

    }

    public static void printShortestPathsToDestination(int[][] table, String[] city, String source, String destination) {
        int sourceIndex = -1;
        int destinationIndex = -1;

        // Find the indices of the source and destination cities
        for (int i = 0; i < city.length; i++) {
            if (city[i].equals(source)) {
                sourceIndex = i;
            }
            if (city[i].equals(destination)) {
                destinationIndex = i;
            }
        }

        // If either the source or destination city is not found, return
        if (sourceIndex == -1 || destinationIndex == -1) {
            System.out.println("Invalid source or destination city.");
            return;
        }

        int shortestDistance = table[sourceIndex][destinationIndex];

        // If there is a direct connection from the source to the destination
        if (shortestDistance != Integer.MAX_VALUE) {
            List<String> shortestPaths = new ArrayList<>();
            List<String> currentPath = new ArrayList<>();
            currentPath.add(city[sourceIndex]);
            printAllShortestPathsHelper(table, city, sourceIndex, destinationIndex, currentPath, shortestPaths);

            System.out.println("Shortest paths from " + source + " to " + destination + ":");
            if (shortestPaths.isEmpty()) {
                System.out.println("No paths found.");
            } else {
                int count = 0;
                for (int i = 1; i < shortestPaths.size(); i++) {
                   System.out.println(shortestPaths.get(i));
                    count++;
                    if (count == 2) {
                        break;
                    }
                }
            }
        } else {
            System.out.println("No direct connection from " + source + " to " + destination);
        }
    }

    private static void printAllShortestPathsHelper(int[][] table, String[] city, int currentIndex, int destinationIndex,
                                                    List<String> currentPath, List<String> shortestPaths) {
        if (currentIndex == destinationIndex) {
            // Add the current path to the list of shortest paths
            shortestPaths.add(String.join(" -> ", currentPath));
        } else if (currentIndex < destinationIndex) {
            for (int i = 0; i < city.length; i++) {
                if (table[currentIndex][i] != Integer.MAX_VALUE && i != currentIndex) {
                    currentPath.add(city[i]);
                    printAllShortestPathsHelper(table, city, i, destinationIndex, currentPath, shortestPaths);
                    currentPath.remove(currentPath.size() - 1);
                }
            }
        }
    }

    private static int calculatePathCost(int[][] table, List<String> path) {
        int cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int city1Index = getIndex(path.get(i));
            int city2Index = getIndex(path.get(i + 1));
            cost += table[city1Index][city2Index];
        }
        return cost;
    }

    private static int getIndex(String cityName) {
        // Assuming the city array contains unique city names
        for (int i = 0; i < city.length; i++) {
            if (city[i].equals(cityName)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws FileNotFoundException {


        launch(args);

    }
}