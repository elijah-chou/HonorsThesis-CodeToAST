import pandas as pd
import numpy as np
import os
import scipy.stats as stats

# Change the path below to the folder path that contains your CSV
# files from calculating the average tree edit distances.
os.chdir("C:/Users/Elijah/Downloads/code-answers-scores-python/Results")
for filename in os.listdir(os.getcwd()):
    file = pd.read_csv(filename)
    file['Distance Z-score'] = stats.zscore(file['Distance'])
    file.to_csv(filename)