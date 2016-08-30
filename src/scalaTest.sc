import org.apache.spark.{SparkContext, SparkConf};

object scalaTest{

  def main(args: Array[String]) {
    println("hello world");

    System.setProperty("hadoop.hom.dir", "C:\\Users\\Justin Baraboo\\Desktop\\winutils");

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val input = sc.textFile("C:\\Users\\Justin Baraboo\\Desktop\\pg100.txt")


    println("hello world");
  }
}