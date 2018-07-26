package net.jgp.books.sparkWithJava.ch05.lab101.piComputeLambda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Compute Pi.
 * 
 * @author jgp
 */
public class PiComputeLambdaApp implements Serializable {
  private static final long serialVersionUID = -1547L;
  private static long counter = 0;

  /**
   * main() is your entry point to the application.
   * 
   * @param args
   */
  public static void main(String[] args) {
    PiComputeLambdaApp app = new PiComputeLambdaApp();
    app.start(100);
  }

  /**
   * The processing code.
   */
  private void start(int slices) {
    SparkSession spark = SparkSession
        .builder()
        .appName("JavaSparkPi")
        .master("local")
        .getOrCreate();

    int n = 100000 * slices;
    List<Integer> l = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      l.add(i);
    }

    Dataset<Row> incrementalDf = spark
        .createDataset(l, Encoders.INT())
        .toDF();
    System.out.println("Initial dataframe built");
    Dataset<Integer> dotsDs = incrementalDf
        .map((status) -> {
          double x = Math.random() * 2 - 1;
          double y = Math.random() * 2 - 1;
          counter++;
          if (counter % 1000 == 0) {
            System.out.println("" + counter + " operations done so far");
          }
          return (x * x + y * y <= 1) ? 1 : 0;
        }, Encoders.INT());
    System.out.println("Mapping dots done");

    int count = dotsDs.reduce((x, y) -> x + y);

    System.out.println("Pi is roughly " + 4.0 * count / n);

    spark.stop();
  }
}
