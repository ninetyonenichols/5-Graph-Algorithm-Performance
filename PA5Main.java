
/**
 * AUTHOR: Justin Nichols
 * FILE: PA5Main.java
 * ASSIGNMENT: Programming Assignment 5 - GraphAlgPerf
 * COURSE: CSC210; Section D; Spring 2019
 * PURPOSE: tests out three different approaches to handling the traveling-
 * salesperson problem and (optionally) prints out the runtimes for each
 *              
 * 
 * USAGE: 
 * java PA5Main mtxFileName ["HEURISTIC", "BACKTRACK", "MINE", "TIME"]
 * 
 * where: mtxFileName is the path to a file. This file will need to provide 
 * well-formed info on how to construct a digraph (nodes are cities, edges are
 * routes, and weights are lengths of routes)
 *     
 * 
 *  EXAMPLE INPUT (CREATED BY INSTRUCTORS, NOT BY ME)--
 *      Input File:                       
 *   
 * ------------------------
 * | % one or more        |
 * | % comment lines here |
 * |  % starting with "%" |
 * | 5 5 20               |
 * | 1 2 113.0            |
 * | 2 1 113.0            |
 * | 1 5 209.48           |
 * ------------------------
 *  
 * Input-file format must match that shown above.
 * No support exists for any further commands
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class PA5Main {
    public static void main(String[] args) {


        Scanner infile = getInfile(args);
        DGraph routesDGraph = makeDGraph(infile);

        String approach = args[1];

        if (approach.equals("BACKTRACK") || approach.equals("HEURISTIC") ||
                approach.equals("MINE")) {
            Trip idealTrip = findIdealTrip(approach, routesDGraph);
            System.out.printf(idealTrip.toString(routesDGraph));
        } else if (approach.equals("TIME")) {
            runtimes("HEURISTIC", routesDGraph);
            runtimes("BACKTRACK", routesDGraph);
            runtimes("MINE", routesDGraph);
        }
    }

    /*
     * returns the infile in Scanner-form
     * 
     * @Param String[] args, the command-line arguments. Args[0] is the
     * infile's name
     * 
     * @Return Scanner infile. The infile in Scanner-form
     */
    public static Scanner getInfile(String[] args) {
        // retrieving the infile
        String infileName = args[0];
        Scanner infile = null;

        try {
            infile = new Scanner(new File(infileName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }

        return infile;
    }

    /*
     * makes the digraph that will contain info about routes between cities
     * 
     * @param Scanner infile: the input file (containing info to build the
     * graph)
     * 
     * @return DGraph routesDGraph: a directed graph containing info about
     * routes between cities
     */
    public static DGraph makeDGraph(Scanner infile) {
        String line = "";

        // skipping comment-lines
        Boolean comment = true;
        while (comment) {
            line = infile.nextLine();
            comment = (line.startsWith("%"));
        }

        // initializing routesDGraph
        String[] str = line.split("( )+");
        int nRows = (Integer.valueOf(str[0].trim())).intValue();
        int nCols = (Integer.valueOf(str[1].trim())).intValue();
        DGraph routesDGraph = new DGraph(Math.max(nRows, nCols));

        // adding the weighted edges to routesDGraph
        while (infile.hasNextLine()) {
            line = infile.nextLine();

            str = line.split("( )+");
            int row = (Integer.valueOf(str[0].trim())).intValue();
            int col = (Integer.valueOf(str[1].trim())).intValue();
            double dist = (Double.valueOf(str[2].trim())).doubleValue();

            if (row != col) {
                routesDGraph.addEdge(row, col, dist);
            }
        }
        return routesDGraph;
    }

    /*
     * starts off the process of finding the ideal trip using the backtracking
     * approach
     * 
     * @param String approach: a command-line argument that specifies which
     * approach to take in finding the ideal trip
     * 
     * @param DGraph routesDGraph: a directed graph containing info about
     * routes between cities
     * 
     * @return Trip idealTrip: contains info about the shortest path found that
     * passes through all cities
     */
    public static Trip findIdealTrip(String approach, DGraph routesDGraph) {

        int currCity = 1;
        int numNodes = routesDGraph.getNumNodes();
        Trip candTrip = new Trip(numNodes);
        candTrip.chooseNextCity(currCity);
        Trip idealTrip = new Trip(numNodes);
        
        if (approach.equals("HEURISTIC")) {
            nearestNeighbors(routesDGraph, idealTrip, currCity);
        } else if (approach.equals("BACKTRACK")) {
            backtrack(routesDGraph, candTrip, idealTrip, currCity);
        } else {
            // using greedy algorithm to get starting point for brute-force
            // algorithm later
            nearestNeighbors(routesDGraph, idealTrip, currCity);

            // applying brute-force algorithm, using the starting-point that
            // the greedy algorithm gave us
            backtrack(routesDGraph, candTrip, idealTrip, currCity);
        }

        return idealTrip;
    }

    /*
     * This is the recursive call that gets used when the "BACKTRACK" approach
     * is used to find the ideal trip
     * 
     * @param DGraph routesDGraph: a directed graph containing info about
     * routes between cities
     * 
     * @param Trip candTrip: the current trip being constructed (or checked, if
     * construction is finished) as a candidate for being the ideal trip
     * 
     * @param Trip idealTrip: contains info about the shortest path found that
     * passes through all cities
     * 
     * @param int currCity: the most recent city chosen to be part of the
     * candTrip
     * 
     * @return void
     */
    public static void backtrack(DGraph routesDGraph,
            Trip candTrip, Trip idealTrip, int currCity) {

        double candCost = candTrip.tripCost(routesDGraph);
        double idealCost = idealTrip.tripCost(routesDGraph);
        Boolean candCostsLess = (candCost < idealCost);

        // abandoning hopeless trips
        if (!candCostsLess) {
            return;
        }

        // base case
        if (candTrip.isPossible(routesDGraph)) {
            idealTrip.copyOtherIntoSelf(candTrip);
            return;
        }

        // recursive case
        List<Integer> neighbors = routesDGraph.getNeighbors(currCity);
        for (Integer neighbor : neighbors) {
            if (candTrip.isCityAvailable(neighbor)) {
                candTrip.chooseNextCity(neighbor);
                currCity = neighbor;
                backtrack(routesDGraph, candTrip, idealTrip, currCity);
                candTrip.unchooseLastCity();
            }
        }
    }


    /*
     * takes the greedy approach to finding the best trip. Used when
     * approach.equals("HEURISTIC")
     * 
     * @param DGraph routesDGraph: a directed graph containing info about
     * routes between cities
     * 
     * @param Trip idealTrip: contains info about the shortest path found that
     * passes through all cities
     * 
     * @param int currCity: the most recent city chosen to be part of the
     * candTrip
     * 
     * @return void
     */
    public static void nearestNeighbors(DGraph routesDGraph, Trip idealTrip,
            int currCity) {
        
        idealTrip.chooseNextCity(1);

        int numNodes = routesDGraph.getNumNodes();
        for (int i = 2; i <= numNodes; i++) {

            Integer nearestNeighbor = currCity;

            double lowestCost = Double.MAX_VALUE;
            for (Integer neighbor : routesDGraph.getNeighbors(currCity)) {
                double cost = routesDGraph.getWeight(currCity, neighbor);

                if (idealTrip.isCityAvailable(neighbor) && cost < lowestCost) {
                    nearestNeighbor = neighbor;
                    lowestCost = cost;
                }
            }

            idealTrip.chooseNextCity(nearestNeighbor);
            currCity = nearestNeighbor;
        }
    }

    /*
     * tests and prints the runtimes for each of the traveling-salesperson
     * algorithms
     * 
     * @param String approach: a command-line argument that specifies which
     * approach to take in finding the ideal trip
     * 
     * @param DGraph routesDGraph: a directed graph containing info about
     * routes between cities
     * 
     * @return void
     */
    public static void runtimes(String approach, DGraph routesDGraph) {

        long startTime = System.nanoTime();
        Trip idealTrip = findIdealTrip(approach, routesDGraph);
        long endTime = System.nanoTime();
        long runtime = (endTime - startTime) / 1000000;
        
        String approachLC = approach.toLowerCase();
        double cost = idealTrip.tripCost(routesDGraph);
        System.out.println(approachLC + ": cost = " + cost + ", " + runtime
                + " milliseconds");
    }


}
