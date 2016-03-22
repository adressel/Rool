package andreasdressel.hits.util;

import java.io.IOException;
import java.util.HashSet;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;

/**
 *
 * @author Andreas Dressel
 */
public class WordDocIDMapper extends Mapper<LongWritable, Text, Text, Text> {

  //@todo: populate stopwords
  private static final HashSet<String> stopwords = new HashSet<String>();
  
  public void map(LongWritable key, Text value, Context context)
          throws IOException, InterruptedException {

    Builder builder = new Builder();

    try {
      Document doc = builder.build(value.toString());
      String id  = doc.query("//user_id").get(0).getChild(0).getValue();

      Nodes posts = doc.query("//posts");
      for (int i = 0; i < posts.size(); i++) {
        
        String post = posts.get(i).getChild(0).getValue();
        post = post.replaceAll("[^a-zA-Z0-9]", " ");
        String[] words = post.split(" ");

        for(String word : words) {
          if(!stopwords.contains(word)) {
            context.write(new Text(word), new Text(id));
          }
        }
      }

    } catch (ParsingException ex) {
      System.err.println("Exception while parsing the XML file.");
      ex.printStackTrace();
    }
  }
}
