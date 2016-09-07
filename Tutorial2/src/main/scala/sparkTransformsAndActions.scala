import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.{Level, Logger}

/**
  * Created by Justin Baraboo on 9/5/2016.
  * This is for tutorial 2.  I performed several transforms and actions
  * on Shakespeare's complete works
  */
object sparkTransformsAndActions {
  def main(args: Array[String]): Unit = {

    //this tricks windows
    System.setProperty("hadoop.hom.dir", "C:\\Users\\Justin Baraboo\\Desktop\\winutils");

    //this sets up spark
    val sparkConf = new SparkConf().setAppName("SparkActions").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    //this stops spark from spewing lots of stuff
    //http://stackoverflow.com/questions/27781187/how-to-stop-messages-displaying-on-spark-console
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)



    //this takes in the text file, I choose the complete works of Shakespeare taken from Project Gutenburg
    val input = sc.textFile("C:\\Users\\Justin Baraboo\\Desktop\\pg100.txt")

    //perform a transform
    //this finds all the lines that start with A
    val aLines = input.filter(
      x=>{
        x.matches("( )*(a|A)(.*)")
     }
    );

    //and all the lines that start with B
    val bLines = input.filter(
      x=>{
        x.matches("( )*(b|B)(.*)")
      }
    );

    //and then we perform another transform to map them to words and reduce
    //basically a word count but we really just care about getting rid of duplicates
    val aLinesWords = aLines.flatMap(line=>{
      line.split(" ")
    }).map(word=>{
      (word,1)
    }).reduceByKey(_+_);

    val bLinesWords= bLines.flatMap(line=>{
      line.split(" ")
    }).map(word=>{
      (word,1)
    })reduceByKey(_+_);

    //and find all the words that are the same between Alines and Blines
    val sameWords = aLinesWords.intersection(bLinesWords);
    sameWords.saveAsTextFile("SameWords")


    //perform an action
    //lets see how many words are used in each each, and in the intersection
    val aCount = aLinesWords.count()
    val bCount = bLinesWords.count()
    val bothCount = sameWords.count()

    System.out.println("Both count =" + bothCount)
    System.out.println("A count =" + aCount)
    System.out.println("B count =" + bCount)


    //perform another action
    //let's simulate Shakespeare with his more common words
    val newShakespeare = sameWords.takeSample(true,10)
    for(x <- newShakespeare){
      System.out.print(x._1 + " ")
    }

  }
}
