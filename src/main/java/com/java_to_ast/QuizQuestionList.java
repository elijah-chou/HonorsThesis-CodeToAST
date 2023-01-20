package com.java_to_ast;
import java.io.File;
import java.util.ArrayList;

public class QuizQuestionList {
    public static void main(String[] args) {
        File dir = new File("C:/Users/Elijah/Desktop/ELITE/Research-creativity/code-answers-scores"); //replace with directory w/ text files
        File[] directoryListing = dir.listFiles();
        ArrayList<String> questionList = new ArrayList<>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String name = child.getName();
                String[] splitName = name.split("\\.");
                String questionName = splitName[6];
                if (!questionList.contains(questionName)) {
                    questionList.add(questionName);
                }
            }
            for (String name : questionList) {
                System.out.println(name);
            }
        }
    }
}
