import sys
from random import random
from operator import add
from pyspark.sql import SparkSession

spark = SparkSession\
  .builder\
  .appName("PythonPi")\
  .getOrCreate()
n = 100000

def throwDarts(_): #A
  x = random() * 2 - 1 #B
  y = random() * 2 - 1 #B
  return 1 if x ** 2 + y ** 2 <= 1 else 0 #B

count = spark.sparkContext.parallelize(range(1, n + 1), 1).map(throwDarts).reduce(add)
print("Pi is roughly %f" % (4.0 * count / n))
spark.stop()
