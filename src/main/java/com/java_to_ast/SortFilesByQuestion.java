package com.java_to_ast;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;
public class SortFilesByQuestion {
    public static void main(String[] args) throws IOException {
        File dir = new File("C:/Users/Elijah/Desktop/ELITE/Research-creativity/test"); //replace with directory w/ text files
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String childPath = child.getAbsolutePath();
                String childName = child.getName();
                String finalPath = childName;
                String[] split = childName.split("\\.");
                switch (split[6]) {
                    case "SquareDiag":
                        finalPath = "C:/Users/Elijah/Desktop/ELITE/Research-creativity/test/SquareDiag/";
                        break;
                    case "WrongStarLine":
                        finalPath = "C:/Users/Elijah/Desktop/ELITE/Research-creativity/test/WrongStarLine/";
                        break;
                    default:
                        System.out.println("No folder for " + split[6]);
                }

                boolean makeFolder = new File(finalPath).mkdirs();

                finalPath = finalPath + childName;

                Path temp = Files.move(Paths.get(childPath), Paths.get(finalPath));
                if (temp == null) {
                    System.out.println("Failed to move file" + childName);
                }
            }
        }
    }
}

