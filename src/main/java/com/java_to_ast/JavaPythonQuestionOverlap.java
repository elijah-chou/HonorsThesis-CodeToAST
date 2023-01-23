package com.java_to_ast;
import java.io.File;
import java.util.ArrayList;

public class JavaPythonQuestionOverlap {
    public static void main(String[] args) {
        File dir = new File("C:/Users/Elijah/Desktop/ELITE/Research-creativity/questions-python"); //replace with directory w/ text files
        File[] directoryListing = dir.listFiles();
        ArrayList<String> pythonQuestions = new ArrayList<>();
        if (directoryListing != null) {
            for (File pythonFile : directoryListing) {
                String fileName = pythonFile.getName();
                String[] split = fileName.split("\\.");
                pythonQuestions.add(split[6]);
            }
        }
        File dir2 = new File("C:/Users/Elijah/Desktop/ELITE/Research-creativity/code-answers-scores");
        String[] javaQuestions = dir2.list();
        for (String question : javaQuestions) {
            for (int i = 0; i < pythonQuestions.size(); i++) {
                if (question.equals(pythonQuestions.get(i))) {
                    System.out.println(question);
                }
            }
        }
    }
}
