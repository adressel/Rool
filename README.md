# PageRank

This is an implementation of the PageRank algorithm, as described by Sergey Brin and Lawrence Page in [1].

To build the project, run (in the same directory as pom.xml):

    mvn clean compile assemlby:single

To run PageRank, two options have been implemented.

    1. java -jar target/PageRank-1.0-jar-with-dependencies.jar <dValue> -i <numberOfIterations> <inputfile> <outputfile>
    2. java -jar target/PageRank-1.0-jar-with-dependencies.jar <dValue> -c <maxDifference> <inputfile> <outputfile>

Both options build a graph according to the information provided within <inputfile>, and write the PageRanks for each node of the graph into <outputfile>. The algorithm uses a <dValue> between 0 and 1, which is described in more detail in [1]. Option 1 will terminate after the specified <numberOfIterations>, while option 2 computes the PageRanks for each node until the difference from one iteration to the next is less than or equal to <maxDifference>.

Note that the sum of all PageRanks in this implementation is 1.



[1] Sergey Brin and Lawrence Page: "The Anatomy of a Large-Scale Hypertextual Web Search Engine" (1998)

