package net.jgp.books.sparkWithJava.ch05.lab200.piComputeCluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Compute Pi.
 * 
 * @author jgp
 */
public class PiComputeClusterApp implements Serializable {
  private static final long serialVersionUID = -1546L;
  private static long counter = 0;

  /**
   * Mapper class, creates the map of dots
   * @author jgp
   */
  private final class DotMapper
      implements MapFunction<Row, Integer> {
    private static final long serialVersionUID = 38446L;

    @Override
    public Integer call(Row r) throws Exception {
      double x = Math.random() * 2 - 1;
      double y = Math.random() * 2 - 1;
      counter++;
      if (counter % 1000 == 0) {
        System.out.println("" + counter + " operations done so far");
      }
      return (x * x + y * y <= 1) ? 1 : 0;
    }
  }

  /**
   * Reducer class, reduces the map of dots
   * @author jgp
   */
  private final class DotReducer implements ReduceFunction<Integer> {
    private static final long serialVersionUID = 12859L;

    @Override
    public Integer call(Integer x, Integer y) throws Exception {
      return x + y;
    }
  }

  /**
   * main() is your entry point to the application.
   * 
   * @param args
   */
  public static void main(String[] args) {
    PiComputeClusterApp app = new PiComputeClusterApp();
    app.start(100);
  }

  /**
   * The processing code.
   */
  private void start(int slices) {
    SparkSession spark = SparkSession
        .builder()
        .appName("JavaSparkPi")
        .master("spark://un:7077")
        .config("spark.executor.memory", "32g")
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
        .map(new DotMapper(), Encoders.INT());
    System.out.println("Mapping dots done");

    int count = dotsDs.reduce(new DotReducer());

    System.out.println("Pi is roughly " + 4.0 * count / n);

    spark.stop();
  }
}
