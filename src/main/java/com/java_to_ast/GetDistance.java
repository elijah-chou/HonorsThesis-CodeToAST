package com.java_to_ast;

import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import com.opencsv.CSVWriter;

public class GetDistance {

    public static void main(String[] args) throws Exception{
        generateDistance();
    }

    /**
     * This method generates the distance between each wrong code to its closest right algorithm, and write the result
     * to a csv file for further analysis.
     * @throws Exception
     */
    public static void generateDistance() throws Exception{
        String wrongDir = "/Users/cynthia/Desktop/ELITE-2019/Research-autograder/Type 4";
        String rightDir = "/Users/cynthia/Desktop/ELITE-2019/Research-autograder/Type 0";
        List<String> wrongFiles = Tree2.importfiles(Paths.get(wrongDir), "");
        List<String> rightFiles = Tree2.importfiles(Paths.get(rightDir), "");
        int curDis;
        double[][] score_distance = new double[wrongFiles.size()][2];
        String[] studentID = new String[wrongFiles.size()];
        int count = 0;
        for (String file1: wrongFiles) {
            int min = Integer.MAX_VALUE;
            String minFile;
            for (String file2: rightFiles) {
                Tree2 file_1_tree = new Tree2(file1);
                Tree2 file_2_tree = new Tree2(file2);
                curDis = Tree2.ZhangShasha(file_1_tree, file_2_tree);
                if (curDis < min) {
                    min = curDis;
                    minFile = file2;
                }
            }
            score_distance[count][0] = min * 1.0;
            String[] tmp = file1.split("/");
            tmp = tmp[tmp.length - 1].split("-");
            score_distance[count][1] = Integer.parseInt(tmp[2]) * 1.0 / (Integer.parseInt(tmp[3]) * 1.0);
            studentID[count] = tmp[0];
            count++;
        }

        CSVWriter writer = new CSVWriter(new FileWriter("/Users/cynthia/Desktop/ELITE-2019/Research-autograder/Results/data_ArrowWheel2.csv", false));
        writer.writeNext(new String[]{"Distance", "Score", "Student ID"});
        for (int i = 0; i < score_distance.length; i++) {
            String[] tmp  = {score_distance[i][0] + "", score_distance[i][1] + "", studentID[i]};
            writer.writeNext(tmp);
        }
        writer.close();

    }

    /**
     * This method will delete the repeated right algorithms based on distance
     * Using this method might fasten the comparing process in the previous method
     * @throws Exception
     */
    public static void deleteRepeat() throws Exception {
        String comDir = "/Users/cynthia/Desktop/ELITE-2019/Research-autograder/Type 0";
        List<String> filelist = Tree2.importfiles(Paths.get(comDir), "");
        int i = 0;
        for (String file1: filelist) {
            int j = 0;
            for (String file2: filelist) {
                if (i > j || i == j) {
                    j++;
                    continue;
                }
                File f1 = new File(file1);
                File f2 = new File(file2);
                if((!f1.exists()) || (!f2.exists())) {
                    j++;
                    continue;
                }
                Tree2 file_1_tree = new Tree2(file1);
                Tree2 file_2_tree = new Tree2(file2);
                // The number 10 here is decided by several experiments. You can change it if you feel it's needed.
                if (Tree2.ZhangShasha(file_1_tree, file_2_tree) < 10) {
                    // delete one of the file
                    File file = new File(file1);
                    if(file.delete())
                    {
                        System.out.println("File deleted successfully " + file1);
                    }else {
                        System.out.println("Delete Unsuccessful");
                    }
                }
                j++;
            }
            i++;
        }
    }
}