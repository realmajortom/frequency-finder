Given a string representing a document, write a function or class which returns the top 10 most frequent repeated phrases. 

Constraints:
1. A phrase is a stretch of three to ten consecutive words and cannot span sentences. 
2. Omit a phrase if it is a subset of another, longer phrase, even if the shorter phrase occurs more frequently (for example, if “cool and collected” and “calm cool and collected” are repeated, do not include “cool and collected” in the returned set).
3. A phrase is repeated if it is used two or more times.


Sample input & output:

Example input 1:
The quick brown fox jumped over the lazy dog.
The lazy dog, peeved to be labeled lazy, jumped over a snoring turtle.
In retaliation the quick brown fox jumped over ten snoring turtles.
Then the quick brown fox refueled with some ice cream.

Example output 1:
['the lazy dog', 'the quick brown fox jumped over']

 ---
 
Example input 2:
The lazy dog. The lazy dog. The lazy dogs. The lazy dogs.

Example output 2:
['the lazy dog', 'the lazy dogs']
