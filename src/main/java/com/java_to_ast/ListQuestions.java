package com.java_to_ast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ListQuestions {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "C:/Users/Elijah/Desktop/ELITE/Research-creativity/CS170-F18-code-questions/";
        File dir = new File(path); //replace with directory w/ text files
        String[] quizList = dir.list();
        for (String quiz : quizList) {
            String quizPath = path + quiz + "/";
            File quizDir = new File(quizPath);
            String[] questionList = quizDir.list();
            for (String question : questionList) {
                String questionDir = quizPath + question;
                Scanner scanner = new Scanner(new FileInputStream(questionDir));
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.indexOf("<title>") != -1 && line.indexOf("</title>") != -1) {
                        System.out.println(line.substring(line.indexOf("<title>"), line.indexOf("</title>")));
                        System.out.println(questionDir);
                    }
                }
            }
        }
    }
}

