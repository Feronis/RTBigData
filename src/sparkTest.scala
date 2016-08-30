/**
  * Created by Justin Baraboo on 8/28/2016.
  *
  * "modified" [read as: almost identical] code provided from class tutorial powerpoint
  *
  */
import org.apache.spark.{SparkContext, SparkConf};




object scalaTest{

  def main(args: Array[String]) {

    //this tricks windows
    System.setProperty("hadoop.hom.dir", "C:\\Users\\Justin Baraboo\\Desktop\\winutils");

    //this starts up spark
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //this takes in the text file, I choose the complete works of Shakespeare taken from Project Gutenburg
    val input = sc.textFile("C:\\Users\\Justin Baraboo\\Desktop\\pg100.txt")

    //this takes the text file and does mapping by
    val wc = input.flatMap(
                      line=> {
                        line.split(" ")   //splits each line into an array of words
                      }
                    ).map(
                         word=>(word,1)   //and creates a key value pair of word,1
                    ).cache()


    //when scala does sorting based on strings, it defaults to ascii values of strings
    //this makes all capitals come before all lowercases

    //basic idea taken from http://apachesparkbook.blogspot.com/2015/12/sortbykey-example.html
    implicit val sortIntegersByString = new Ordering[String] {
             override def compare(a: String, b: String) = {
               val c = a.toLowerCase()
               val d = b.toLowerCase()
               val result = c.compare(d)
                  result
               }
          }

    //this takes these key value pairs and reduces them, aggregating the words
    val output = wc.reduceByKey(_+_).sortByKey() //we then sort by the key values, ie the words to order the pairs

    //and we save it.
    output.saveAsTextFile("output")
  }
}
