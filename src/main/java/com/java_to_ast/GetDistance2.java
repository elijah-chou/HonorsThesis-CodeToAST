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
        List<String> finalFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try {
                Tree2 test = new Tree2(files.get(i));
                finalFiles.add(files.get(i));
            } catch (Exception e) {
                System.out.println(files.get(i) + " could not be parsed into AST.");
            }
        }
        int curDis;
        double[][] score_distance = new double[finalFiles.size()][1];
        int count = 0;

        for (int i = 0; i < finalFiles.size(); i++) {
            Tree2 file_1_tree = new Tree2(finalFiles.get(i));
            int total = 0;
            int totalFiles = 0;
            for (int j = 0; j < finalFiles.size(); j++) {
                if (i != j) {
                    int min = Integer.MAX_VALUE;
                    Tree2 file_2_tree = new Tree2(finalFiles.get(j));
                    curDis = Tree2.ZhangShasha(file_1_tree, file_2_tree);
                    if (curDis < min) {
                        min = curDis;
                    }
                    total += min;
                    totalFiles++;
//                    System.out.println(i + " " + j + " " + curDis);
                }
            }
//            System.out.println(files1.get(i) + " has total of " + total + "and was compared with " + totalFiles + " files.");
            double avg = (total * 1.0) / (totalFiles * 1.0);
            score_distance[i][0] = avg;
        }

        double max = Integer.MIN_VALUE;
        for (int i = 0; i < score_distance.length; i++) {
            if (score_distance[i][0] > max) {
                max = score_distance[i][0];
            }
        }
        String csvName = "C:/Users/Elijah/Desktop/ELITE/Research-creativity/testResults/";
        String[] findProblem = finalFiles.get(0).split("\\.");
        csvName = csvName + findProblem[6] + ".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csvName, false));
        writer.writeNext(new String[]{"Year", "Semester", "Quiz #", "Student ID", "Coding Problem", "Score", "Total", "Distance", "Percent"});
        for (int i = 0; i < score_distance.length; i++) {
            String[] split = finalFiles.get(i).split("\\.");
            String[] separateYear = split[0].split("\\\\");
            String[] tmp = {separateYear[separateYear.length - 1] + "", split[2] + "", split[4] + "", split[5] + "",
                    split[6] + "", split[7] + "", split[8] + "", score_distance[i][0] + "", (score_distance[i][0] / max) + ""};
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
        //defines the directory entry of folder of coding programs to be compared with each other
        String homeDir = "C:/Users/Elijah/Desktop/ELITE/Research-creativity/test";
        File dir = new File(homeDir);
        String[] javaQuestions = dir.list();
        for (String question : javaQuestions) {
            String newPath = homeDir + "/" + question;
            calculateDistanceForProblem(newPath, question);
        }

    }
}