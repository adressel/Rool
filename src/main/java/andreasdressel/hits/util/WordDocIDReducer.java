package andreasdressel.hits.util;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author Andreas Dressel
 */
public class WordDocIDReducer extends Reducer<Text, LongWritable, Text, Text> {
  
  public void reduce(Text key, Iterable<LongWritable> values, Context context) 
          throws IOException, InterruptedException {
    
    StringBuilder builder = new StringBuilder();
    for(LongWritable lw : values) {
      builder.append(lw.toString());
      builder.append(" ");
    }
    context.write(key, new Text(builder.toString()));
  }
}
