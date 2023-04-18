# Using the Java Code Creativity Measure Calculation Implementation

---
## Introduction
This folder contains all the files necessary for calculating the creativity measure for Java code as outlined in https://etd.library.emory.edu/concern/etds/b8515p78f?locale=en. The creativity measure is calculated by converting code into abstract syntax trees and finding the tree edit distance between pairs of trees. For more detailed information about the proposed creativity measure, please refer to the thesis found at the website above.

---
# General Walkthrough for Calculating Distance-Based Creativity Measure for Java Code

## Step 1: Sort your Data
Before running any calculations, please organize your Java code into folders by their coding problems. For example, if your dataset included Java files for two coding problems (ex. AllOddNums and AllEvenNums), you should have a main folder containing two folders inside named "AllOddNums" and "AllEvenNums", respectively. Once you have sorted your Java files into separate folders organized by coding problem, you can move on to the next step.

## Step 2: Calculate the Average Tree Edit Distances for Java Code
For this step, you should use the files in the `Tree Edit Distance Calculation` folder. There are more specific instructions of how to use the files in the README file found in that folder. Note that you will need to have IntelliJ IDEA installed. If it is, please open the `Tree Edit Distance Calculation` folder as a project in IntelliJ IDEA. Please make adjustments to the files as instructed in the folder's README file.

## Step 3: Calculate the z-scores for Java Code
Once you have the results folder containing CSV files for each coding problem from the previous step, use the files found in the `Z-score Calculation` folder in this directory to calculate the z-scores that will be your final result. The z-scores themselves are defined as the final **creativity measure** in our study. For this step, please ensure that you change the directory defined in the Python file to the path of your results folder from the previous step. This will then calculate the z-scores for each respective CSV file and add them to each CSV as a new column.

Please have Python3 installed on your system before doing this step. You can use the following commands in your command line after once you have Python installed.

    cd "Z-score Calculation"
    python3 "Z-score Calculation.py"

Make sure to have Pandas and Scipy libraries installed on your system before running the above commands. If you don't have them installed, please use pip (which should already have been installed with Python) to install them first as shown below:

    python3 -m pip install pandas
    python3 -m pip install scipy

## Other Notes

If there is any issue with anything in this implementation, please reach out to Elijah Chou (elijah.chou@emory.edu or elijah.chou0321@gmail.com) for clarifications. Thanks!