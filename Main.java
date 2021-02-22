/*
* File: Main.java
* Author: John Kucera
* Date: February 5, 2020
* Purpose: This java program is designed to use the Banker's algorithm to 
* find all safe sequences for a given set of process/resource data. The given
* data includes n processes, m resource types, available[m], max[n][m], and
* allocation[n][m] and comes in an input file. The user enters the name of the
* file, and the program prints the data while using the data to solve for
* all safe sequences - a backtracking approach using recursion.
*/

// import necessary java classes
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

// Main class
public class Main {
  // main method
  public static void main(String[] args) {
    // Scan filename from user input
    final Scanner inputScanner = new Scanner(System.in);
    System.out.print("\nEnter file name: ");
    final String filename = inputScanner.nextLine();

    // Collect data, which prints it, then close scanner
    dataCollection(filename);
    inputScanner.close();
  } //end of main method

  // dataCollection method: takes input, prints data, calls banker's algorithm
  private static void dataCollection (String filename) {
    // Variable Initialization. n = processes, m = resource types
    int n = 0;
    int m = 0;
    final Vector<Integer> safeSeq = new Vector<Integer>();
    final File file = new File(".//" + filename);

    try {
      // Reading file, Create arrays for data structures using input n and m (must be less than 10)
      final Scanner fileScanner = new Scanner(file);
      n = fileScanner.nextInt();
      m = fileScanner.nextInt();
      if (n >= 10 || m >= 10) {
        System.out.println("\nn and m must have values less than 10. Please try again.");
        System.exit(0);
      } // end of if
      final int[] available = new int[m];
      final int[][] max = new int[n][m];
      final int[][] allocation = new int[n][m];
      final int[][] need = new int[n][m];
      final boolean[] executed = new boolean[n];
      
      // Input data into data structure arrays
      for (int i = 0; i < m; i++){
        // Available[m]
        available[i] = fileScanner.nextInt();
      } // end of for
      for (int j = 0; j < n; j++){
        for (int k = 0; k < m; k++){
          // Max[n][m]
          max[j][k] = fileScanner.nextInt();
        } // end of inner for

        for (int c = 0; c < m; c++){
          // Allocation[n][m]
          allocation[j][c] = fileScanner.nextInt();
        } // end of inner for

        for (int d = 0; d < m; d++){
          // Need[n][m]
          need[j][d] = max[j][d] - allocation[j][d];
        } // end of inner for
      } // end of for
      
      // Printing data
      System.out.println("\n****** Banker's Algorithm for " + filename + " ******\n"
                        + "\n# of Processes: " + n + "\n# of Resource Types: " + m
                        + "\nAvailable: " + Arrays.toString(available)
                        + "\nMax: " + Arrays.deepToString(max)
                        + "\nAllocation: " + Arrays.deepToString(allocation)
                        + "\nNeed: " + Arrays.deepToString(need) + "\n");

      // Use Banker's Algorithm Method with the input data
      System.out.println("Safe Sequences: ");
      bankersAlgorithm(n, m, available, allocation, need, safeSeq, executed);
      fileScanner.close();
    } // end of try
    catch(IOException ex) {
      System.out.println("File not found. Please try again.");
    } // end of catch
  } // end of method
  
  // canBeExecuted method: returns a boolean to tell if a process can be executed using current available
  private static boolean canBeExecuted(int process, int m, int[] available, int[][] need) {     
    boolean executable = true;
    // Determine if all resource types are available
    for (int j = 0; j < m; j++){
      if (available[j] < need[process][j]) {
        executable = false;
      } // end of if
    } // end of for
    return executable;
  } // end of method

  // bankersAlgorithm method: finds all possible safe sequences
  private static void bankersAlgorithm(int n, int m, int[] available, int[][] allocation, int[][] need, Vector<Integer> safeSeq, boolean[] executed) {
    // For all processes n
    for(int i = 0; i < n; i++) {
      // Continue if process is not already executed AND can be executed with available resources
      if (canBeExecuted(i, m, available, need) && !executed[i]) {
        // Add process to safe sequence, add allocation to available
        executed[i] = true;
        safeSeq.add(i);
        for (int j = 0; j < m; j++) {
          available[j] += allocation[i][j];
        } // end of for

        // Banker's algorithm with recursion to continuously find next possibilities
        bankersAlgorithm(n, m, available, allocation, need, safeSeq, executed);
        
        // Clear the vector and available so other possibilities can be searched for
        for (int k = 0; k < m; k++) {
          available[k] -= allocation[i][k];
        } // end of for
        safeSeq.removeElementAt(safeSeq.size() - 1); 
        executed[i] = false;
      } // end of if
    } // end of for

    // Print full sequence if ready
    if(safeSeq.size() == n) {
      System.out.print("< ");
      for (int h : safeSeq){
        System.out.print((h + 1) + " ");
      } // end of for
      System.out.print(">\n");
    } // end of if
  } // end of method
} // end of class