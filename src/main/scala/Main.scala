import scala.collection.mutable.Map

object Main extends App {
  def frequencyFinder(text: String): List[String] = {
    // Split input text by sentence b/c phrases can't span sentences. Then split each sentence into a list of words
    val sentences: List[List[String]] = text.toLowerCase.split(Array('.', '!', '?')).toList.map(_.split(" ").toList)

    // Iterate through each sentence, split each into chunks ranging between 3 & 10 words (or the sentence length if it's below 10)
    val phrases: List[String] = sentences.flatMap { words =>
      val max = if (words.size >= 10) 10 else words.size
      (for (i <- 3 to max) yield {
        // Slides over words, then transforms each subset of words into a string, and removes trailing punctuation/newlines/leading spaces
        words.sliding(i).toList.map(_.mkString(" ").replaceAll("^[ \\t]+|\n|[,;:]$", ""))
      }).flatten
    }

    // Count the occurrences of each phrase
    val phraseCounts: Map[String, Int] = phrases.foldLeft(Map[String, Int]()) { (map, phrase) =>
      val count = map.get(phrase).getOrElse(0)
      map.addOne(phrase, count + 1)
    }.filterInPlace((_, count) => count > 1)

    // Filter out phrases that are a substring of a larger phrase
    phraseCounts.keys.filterNot { phrase =>
      phraseCounts.keys.exists(p => p != phrase && p.contains(phrase))
    }.map(p => p -> phraseCounts.get(p).getOrElse(0)).toSeq.sortWith(_._2 < _._2).map(_._1).toList
  }

  val result = frequencyFinder("The quick brown fox jumped over the lazy dog.\nThe lazy dog, peeved to be labeled lazy, jumped over a snoring turtle.\nIn retaliation the quick brown fox jumped over ten snoring turtles.\nThen the quick brown fox refueled with some ice cream.")
  val result2 = frequencyFinder("The lazy dog. The lazy dog. The lazy dogs. The lazy dogs.")
  println(result)
  println(result2)
}
