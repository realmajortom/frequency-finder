import scala.collection.mutable.Map

object Main extends App {
  def frequencyFinder(text: String): List[String] = {
    // Splits input text by sentence b/c phrases can't span sentences. Then splits each sentence into a list of words.
    val sentences: List[List[String]] = text.toLowerCase.split(Array('.', '!', '?')).toList.map(_.split(" ").toList)

    // Iterates through each sentence, generating every possible chunk ranging between 3 & 10 words
    val phrases: List[String] = sentences.flatMap { words =>
      val max = if (words.size >= 10) 10 else words.size
      (for (i <- 3 to max) yield {
        // Slides over words, then transforms each subset of words into a single string and removes trailing punctuation/newlines/leading spaces
        words.sliding(i).toList.map(_.mkString(" ").replaceAll("^[ \\t]+|\n|[,;:]$", ""))
      }).flatten
    }

    // Counts the occurrences of each phrase and removes phrases occurring less than twice
    val phraseCounts: Map[String, Int] = phrases.foldLeft(Map[String, Int]()) { (map, phrase) =>
      val count = map.get(phrase).getOrElse(0)
      map.addOne(phrase, count + 1)
    }.filterInPlace((_, count) => count > 1)

    // Filter out phrases that are a part of a larger frequently-occurring phrase
    phraseCounts.keys.filterNot { phrase =>
      /*
        According to spec we want to remove sub-phrases, not substrings ... so we cant just do `largerPhrase.contains(smallerPhrase)`
        because while "the lazy dog" is a substring of "the lazy dogZ", it's not a sub-phrase; therefore we want to keep it as a frequently occurring phrase.

        My super hacky workaround is to append whitespace at the beginning and end of the sub-phrase and parent-phrase,
        then perform substring match. " the lazy dog " is NOT a substring of " the lazy dogZ " so it will remain as expected
       */
      phraseCounts.keys.exists(p => p != phrase && s" $p ".contains(s" $phrase "))
    }.map(p => p -> phraseCounts.get(p).getOrElse(0)).toSeq.sortWith(_._2 < _._2).map(_._1).toList
  }

  val result = frequencyFinder("The quick brown fox jumped over the lazy dog.\nThe lazy dog, peeved to be labeled lazy, jumped over a snoring turtle.\nIn retaliation the quick brown fox jumped over ten snoring turtles.\nThen the quick brown fox refueled with some ice cream.")
  val result2 = frequencyFinder("The lazy dog. The lazy dog. The lazy dogs. The lazy dogs.")
  println(result)
  println(result2)
}
