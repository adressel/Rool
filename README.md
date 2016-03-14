# PageRank

This is an implementation of the PageRank algorithm, as described by Sergey Brin and Lawrence Page in [1].

To build the project, run (in the same directory as pom.xml):

    mvn clean compile assembly:single

To run PageRank, two options have been implemented.

    1. java -jar target/PageRank-1.0-jar-with-dependencies.jar <dValue> -i <numberOfIterations> <inputfile> <outputfile>
    2. java -jar target/PageRank-1.0-jar-with-dependencies.jar <dValue> -c <maxDifference> <inputfile> <outputfile>

Both options build a graph according to the information provided within <inputfile>, and write the PageRanks for each node of the graph into <outputfile>. The algorithm uses a <dValue> between 0 and 1, which is described in more detail in [1]. Option 1 will terminate after the specified <numberOfIterations>, while option 2 computes the PageRanks for each node until the difference from one iteration to the next is less than or equal to <maxDifference>.


## Input file format

The provided GraphParser (andreasdressel.pagerank.parser) reads an input file and generates a graph representation. The input file has to be formattet as follows:

1. The first line of the input file starts with zero or more non-whitespace characters, followed by one whitespace character and the number of nodes N in the graph.
2. The next N lines each contain a pair of one unique node ID and a corresponding name.
3. After this information, there is one line starting with zero or more non-whitespace characters, followed by one whitespace character and the number of edges E in the graph.
4. The next E lines each contain one edge in the format: <from> <to>, where <from> and <to> are unique node IDs, separated by one whitespace. This describes a directed edge from the node with ID <from> to the node with ID <to>.

This is an example of a valid input file:

    nodes 4
    7 NodeA B C
    12 Node D EF
    32 Node GHI
    101 NodeJKL
    edges 6
    7 12
    12 32
    32 101
    101 7
    101 12
    101 32


## Output file format

The output file contains N pairs of <nodeID> <PageRank>. Note that the sum of all PageRanks in this implementation is 1.

This is an example of an output file (the result of running the above input file example over 5 iterations with a d-value of 0.85.

    32 0.32885525752314815
    101 0.31353484374999996
    7 0.1193506221064815
    12 0.23825927662037033



[1] Sergey Brin and Lawrence Page: "The Anatomy of a Large-Scale Hypertextual Web Search Engine" (1998)

