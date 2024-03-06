Creativity in Programming: Java Code Implementation
---
## Introduction
This repository holds the files used to 1) convert Java code into Abstract Syntax Trees with JavaParser and 2) calculate the distance between two such trees using the algorithms proposed by Kaizhong Zhang and Dennis Shasha in 1989. The purpose of this project is to calculate distances between different code files in an attempt to utilize the distance as a metric of creativity in programming.

Honors Thesis published on the Emory Theses and Dissertations site found here: https://etd.library.emory.edu/concern/etds/b8515p78f?locale=en.

---
## Before Using
Please ensure that you have IntelliJ IDEA installed on your computer. This is the easiest way to ensure that you can build the implementation with the proper dependencies from JavaParser by using the integrated Apache Maven in IntelliJ IDEA. 

***Please ensure that you sort your programs into separate folders based on their coding problems before running this program.***

---
## About the Files

``GetDistance2.java``
This file contains the main class that you will build and run to calculate the average tree edit distance of a program compared to all other programs within the same coding problem group. This implementation will loop through multiple folders of programs sorted by coding problem to calculate average distances within coding problems. Note that some of the logic used in this code is tailored specifically to the format of the data used for this research project, so you will need to change some lines in order to make this implementation work for your data.

The following includes information on which lines you may want to change in order for your implementation to work with your data.
- Line 69: Please change the string to a directory where you would like the final CSV files with the distances to be stored at (end with a "/" as shown in code).
- Lines 76-83: This code block extracts information from each file name to store in CSV. However, your data most likely will not have the same format, so please change this block accordingly, but keep the reference to ``score_distance`` as references the final tree edit distance average.
- Line 98: Please change this to the system path to where your **SORTED** Java files are stored. Please refer to the overall project README for more instruction on how to sort your files.
- Line 100: Change this to the same directory as defined by Line 69, but remove the last "/" as shown in the code.


``Tree2.java``
This file contains the Tree2 class, which utilizes JavaParser classes and functions to parse Java code into abstract syntax trees. The Tree2 class also has a class function that calculates the tree edit distance between two Tree2 objects as outlined by Zhang and Shasha in their original 1989 paper. You most likely will not need to make any adjustments to this file.

---
