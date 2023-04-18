package com.java_to_ast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GetDistance2 {

    /**
     * This method calculates the tree edit distances between each file, and then calculates the average distance for
     * each. A percentage is also calculated, which is standardized using the maximum average distance within each
     * cohort. The method ends by writing all results into a CSV file. The method is also written to catch any
     * parsing errors that may occur with Java programs that cannot be parsed with JavaParser for any reason.
     * @param dir1
     * @param question
     * @throws Exception
     */
    public static void calculateDistanceForProblem(String dir1, String question) throws Exception {
        System.out.println("Now calculating tree edit distances for " + question);
        List<String> files = Tree2.importfiles(Paths.get(dir1), "");
        List<String> finalFileNames = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                // This try-catch block is used to filter out any Java code that cannot be parsed into abstract syntax trees because of any syntax errors.
                Tree2 test = new Tree2(files.get(i));
                finalFileNames.add(files.get(i));
            } catch (Exception e) {
                // Feel free to comment out this line in case you do not need to know what files specifically are not parseable into AST.
                System.out.println(files.get(i) + " could not be parsed into AST.");
            }
        }
        int curDis;
        double[][] score_distance = new double[finalFileNames.size()][1];
        int count = 0;
        /**
         * The following block of code calculates the tree edit distance from one program to all other programs in the same folder. These distances are added to a total,
         * and then the total is divided by the number of comparisons made to make a final average.
         */
        for (int i = 0; i < finalFileNames.size(); i++) {
            Tree2 file_1_tree = new Tree2(finalFileNames.get(i));
            int total = 0;
            int totalFiles = 0;
            for (int j = 0; j < finalFileNames.size(); j++) {
                if (i != j) {
                    int min = Integer.MAX_VALUE;
                    Tree2 file_2_tree = new Tree2(finalFileNames.get(j));
                    curDis = Tree2.ZhangShasha(file_1_tree, file_2_tree);
                    if (curDis < min) {
                        min = curDis;
                    }
                    total += min;
                    totalFiles++;
                }
            }
            double avg = (total * 1.0) / (totalFiles * 1.0);
            score_distance[i][0] = avg;
        }
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < score_distance.length; i++) {
            if (score_distance[i][0] > max) {
                max = score_distance[i][0];
            }
        }
        // Change the csvName variable to the final path where you want the results to be stored at.
        String csvName = "C:/Users/ecool/Desktop/Full Score Results/";
        csvName = csvName + question + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvName, false));
        /**
         * The code below extracts information from the file names of each Java project and also includes the final calculated distance from above.
         * Note that your data may not include the same information as our raw data files, so please adjust the code block below accordingly.
         */
        writer.writeNext(new String[]{"Year", "Semester", "Quiz #", "Student ID", "Coding Problem", "Score", "Maximum", "Distance"});
        for (int i = 0; i < score_distance.length; i++) {
            String[] split = finalFileNames.get(i).split("\\.");
            String[] separateYear = split[0].split("\\\\");
            String[] tmp = {separateYear[separateYear.length - 1] + "", split[2] + "", split[4] + "", split[5] + "",
                    split[6] + "", split[7] + "", split[8] + "", score_distance[i][0] + ""};
            writer.writeNext(tmp);
        }
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        generateDistance();
    }

    /**
     * This method finds all folders in a directory and initiates the calculateDistanceForProblem method for each
     * folder. This should be used after sorting all possible questions using the SortFilesByQuestion.java class.
     *
     */
    public static void generateDistance() throws Exception {
        // Defines the main directory of ***SORTED*** coding programs to be compared with each other. Please change accordingly.
        String homeDir = "C:/Users/ecool/Desktop/code-answers-scores/code-answers-scores";
        // Defines the main directory where the final CSV files are stored. Please change accordingly.
        String resultsDir = "C:/Users/ecool/Desktop/Full Score Results";
        File dir = new File(homeDir);
        String[] javaQuestions = dir.list();
        for (String question : javaQuestions) {
            String newPath = homeDir + "/" + question;
            String resultPath = resultsDir + "/" + question + ".csv";
            File testFile = new File(resultPath);
            // Tests to see if the calculations were already done for the respective coding question. If not, it goes ahead and calculates the tree edit distances.
            if (testFile.exists()) {
                System.out.println("CSV file for " + question + " already exists.");
            }
            else {
                calculateDistanceForProblem(newPath, question);
            }
        }
    }
}