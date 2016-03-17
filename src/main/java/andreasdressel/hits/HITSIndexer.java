package andreasdressel.hits;

import andreasdressel.hits.util.WordDocIDMapper;
import andreasdressel.hits.util.WordDocIDReducer;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


/**
 *
 * @author Andreas Dressel
 */
public class HITSIndexer { 
  
  public static void main(String[] args) {
    
    Configuration conf = new Configuration();
    
    try {
      
      Job job = Job.getInstance(conf, "HITSIndexer");

      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      job.setMapperClass(WordDocIDMapper.class);
      job.setReducerClass(WordDocIDReducer.class);

      job.setInputFormatClass(TextInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);

      FileInputFormat.addInputPath(job, new Path("inputfile"));
      FileOutputFormat.setOutputPath(job, new Path("outputfile"));

      job.waitForCompletion(true);
    
    } catch(InterruptedException | ClassNotFoundException | IOException ex) {
      ex.printStackTrace();
    }
  }  
  
}
