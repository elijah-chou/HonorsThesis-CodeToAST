package com.java_to_ast;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.*;
import static java.nio.file.StandardCopyOption.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/* right: 0; compile error: 1; time limit: 2; runtime error: 3; wrong answer: 4*/

public class CompareFinal{

    public static boolean flag = false;
    public static String insAnswer = "";
    public static void main(String[] args) throws Exception {
        // the folder where instruction codes are located
        String quizdir = "/Users/cynthia/Desktop/ELITE-2019/Research-autograder/test-folder";
        List<String> filelist = Tree2.importfiles(Paths.get(quizdir), "");
        for (String file : filelist) {
            // answer directory can be changed in order to match with quiz
            flag = false;
            comparefile(file,
                    "/Users/cynthia/Desktop/ELITE-2019/Research-autograder/test-answer-folder"
            );
        }

    }

    /**
     * This method checks if the question is a coding question, extract information (grade, year) from students' file
     * names, and extract codes from the files
     * @param instructorFileName: instructor code file
     * @param answerDir: Directory that contains students' code of this question
     * @throws Exception
     */
    public static void comparefile(String instructorFileName, String answerDir) throws Exception {
        String b = extract(instructorFileName);
        Pattern p = Pattern.compile("<title>(.+?)</title>");
        Pattern p2 = Pattern.compile("<type>(.+?)</type>");
        Matcher typematcher = p2.matcher(b);
        typematcher.find();
        if (!typematcher.group(1).equals("JavaCode")){
            // if it's not a coding question, return
            System.out.println("non-code quiz type");
            return;
        }
        Matcher m = p.matcher(b);
        m.find();
        String quizname = m.group(1);
        List<String> filelist = Tree2.importfiles(Paths.get(answerDir),"");

        // extract information from students' code and compare solution
        for(String file: filelist) {
            String a = extract(file);
            String[] split = file.split("\\\\");
            split = split[split.length-1].split("[.]");
            if (split.length == 11) {
                String studentname = split[5]+"-"+split[4]+"-" + split[7] + "-" + split[8] + "-" +quizname;
                compareSolution(a, b, studentname);
            }
        }
    }


    public static void treeCompare(String rootDir){
        Paths.get(rootDir+"Type 0");
    }

    /**
     * This method runs both student's code and instructor's and compares solution
     * @param student: student code string
     * @param instructor: instrctor code string
     * @param student_id: student information
     * @throws Exception
     */
    public static int compareSolution(String student, String instructor,String student_id) throws Exception{
        String root = System.getProperty("user.dir");
        Pattern p = Pattern.compile("<answer>((?s).*?)</answer>");
        Matcher matcher = p.matcher(instructor);
        if(matcher.find()) {
            instructor = matcher.group(1);
        }
        //extract the main method from instructor's note
        int indexstart = instructor.lastIndexOf("public static void main");
        int indexstop = instructor.indexOf("{", indexstart) + 1;
        int bracket = 1;
        while (bracket != 0) {
            if (instructor.charAt(indexstop) == '{') {
                bracket++;
            }else if (instructor.charAt(indexstop) == '}') {
                bracket--;
            }
            indexstop++;
        }
        String main = instructor.substring(indexstart, indexstop);
        // replace the main method in students' code if there is any
        int indexmain = student.indexOf("public static void main");
        if (indexmain == -1) {
            int start = student.lastIndexOf("}");
            student = student.substring(0, start) + main + "\n" + "}";
        }else {
            int stop = student.indexOf("{", indexmain) + 1;
            int countbracket = 1;
            while(countbracket != 0 && stop < student.length()) {
                if (student.charAt(stop) == '{') {
                    countbracket++;
                }else if (student.charAt(stop) == '}') {
                    countbracket--;
                }
                stop++;
            }
            student = student.substring(0, indexmain) + main + "\n" + student.substring(stop);
        }
        //find the class name in instructor's code
        String classname = "";
        int start = instructor.indexOf("class") + 5;
        while (instructor.charAt(start) == ' ') {
            start++;
        }
        int end = start;
        while(instructor.charAt(end) != ' ') {
            end++;
        }
        classname = instructor.substring(start, end);

        //write the student's code into the java file with same name (so replace the instructor's code)and execute it
        File f = File.createTempFile("javacode", ".tmp");
        f.deleteOnExit();
        File tmpDir = new File(f.getPath() + ".d");
        tmpDir.mkdir();
        tmpDir.deleteOnExit();
        tmpDir.setReadable(false, false);
        tmpDir.setWritable(false, false);
        tmpDir.setExecutable(false, false);
        tmpDir.setReadable(true, true);
        tmpDir.setWritable(true, true);
        tmpDir.setExecutable(true, true);
        String tmpDirPath = tmpDir.getCanonicalPath();
        // copy turtle file to the same directory in case need it
        Files.copy(new File("/Users/cynthia/Desktop/ELITE-2019/Research-autograder/Turtle/Turtle.java").toPath(), new File(tmpDirPath + "/Turtle.java").toPath(), REPLACE_EXISTING);
        String tmpClass = tmpDirPath + "/" + classname + ".java";
        FileWriter pws = new FileWriter(tmpClass);
        pws.write(student);
        pws.close();

        String javaCompileCommand = "javac -cp . " + tmpClass;
        String javaRunCommand = "java -cp " + tmpDirPath + " " + classname;
        Process pros1 = Runtime.getRuntime().exec(javaCompileCommand, null, tmpDir);
        boolean exitCode = pros1.waitFor(100, SECONDS);
        pros1.destroy();
        Thread.sleep(100);
        int exitval = pros1.exitValue();
        if (exitval != 0 || !exitCode) {
            File directory= new File(root+"/Type 1");
            if(!directory.exists()) directory.mkdir();
            pws = new FileWriter(new File(directory+"/"+student_id));
            pws.write(student);
            pws.close();
            return 1;
        }
        Process pros2 = Runtime.getRuntime().exec(javaRunCommand, null, tmpDir);
        boolean timecheck = pros2.waitFor(5, SECONDS);
        if (timecheck == false) {
            File directory= new File(root+"/Type 2");
            if(!directory.exists()) directory.mkdir();
            System.out.println(directory+"/"+student_id);
            pws = new FileWriter(new File(directory+"/"+student_id));
            pws.write(student);
            pws.close();
            return 2;
        }
        //keep track of the output
        Scanner ins = new Scanner(new InputStreamReader(pros2.getInputStream()));
        Scanner error = new Scanner(new InputStreamReader(pros2.getErrorStream()));
        String lines = "";
        String errline = "";
        while (ins.hasNextLine()) {
            lines+=ins.nextLine();
        }
        ins.close();
        while (error.hasNextLine()) {
            errline+=error.nextLine();
        }
        error.close();
        if (errline.length() != 0) {
            File directory= new File(root+"/Type 3");
            if(!directory.exists()) directory.mkdir();
            System.out.println(directory+"/"+student_id);
            pws = new FileWriter(new File(directory+"/"+student_id));
            pws.write(student);
            pws.close();
            return 3;
        }

        // if it's the first time to run this instructor code
        if (flag == false) {
            FileWriter pw = new FileWriter(tmpClass);
            pw.write(instructor);
            pw.close();
            javaCompileCommand = "javac -cp . " + tmpClass;
            javaRunCommand = "java -cp " + tmpDirPath + " " + classname;

            Process pro1 = Runtime.getRuntime().exec(javaCompileCommand, null, tmpDir);
            pro1.waitFor();
            Process pro2 = Runtime.getRuntime().exec(javaRunCommand, null, tmpDir);
            // keep trace of the output
            Scanner in = new Scanner(new InputStreamReader(pro2.getInputStream()));
            while (in.hasNextLine()) {
                insAnswer+=in.nextLine();
            }
            in.close();
            flag = true;
        }

        if (lines.equals(insAnswer)) {
            File directory= new File(root+"/Type 0");
            if(!directory.exists()) directory.mkdir();
            System.out.println(directory+"/"+student_id);
            pws = new FileWriter(new File(directory+"/"+student_id));
            pws.write(student);
            pws.close();
            return 0;
        }
        File directory= new File(root+"/Type 4");
        if(!directory.exists()) directory.mkdir();
        System.out.println(directory+"/"+student_id);
        pws = new FileWriter(new File(directory+"/"+student_id));
        pws.write(student);
        pws.close();
        return 4;
    }

    /**
     * This method extracts code from a txt file
     * @param filename: File that includes code
     * @throws IOException
     */
    public static String extract(String filename) throws IOException{
        Scanner scan = new Scanner(new File(filename));
        String result = "";
        while(scan.hasNextLine()) {
            result += scan.nextLine() + "\n";
        }
        scan.close();
        return result;
    }
}