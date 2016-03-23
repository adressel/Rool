package andreasdressel.hits.util;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author Andreas Dressel
 */
public class WordDocIDReducer extends Reducer<Text, Text, Text, Text> {
  
  public void reduce(Text key, Iterable<Text> values, Context context) 
          throws IOException, InterruptedException {
    
    StringBuilder builder = new StringBuilder();
    for(Text t : values) {
      builder.append(t.toString());
      builder.append(" ");
    }
    context.write(key, new Text(builder.toString()));
  }
}
