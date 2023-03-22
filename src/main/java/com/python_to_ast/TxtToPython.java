package com.python_to_ast;

import java.io.File;

public class TxtToPython {
    public static void main(String[] args) {
        File dir = new File("C:/Users/Elijah/Downloads/code-answers-scores-python/code-answers-scores-python"); //replace with directory w/ text files
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String childPath = child.getAbsolutePath();
                String newPath = childPath.replaceAll(".txt", ".py");
                File rename = new File(newPath);
                boolean flag = child.renameTo(rename);
//                if (flag == true) {
//                    System.out.println("File Successfully Rename");
//                }
//                else {
//                    System.out.println("Operation Failed");
//                }
            }
        }
    }
}
