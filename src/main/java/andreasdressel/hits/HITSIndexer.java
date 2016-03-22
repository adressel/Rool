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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.mahout.classifier.bayes.XmlInputFormat;

/**
 *
 * @author Andreas Dressel
 */
public class HITSIndexer { 
  
  public static void main(String[] args) {
    
    Configuration conf = new Configuration();
    conf.set("xmlinput.start", "<user>");
    conf.set("xmlinput.end", "</user>");
    
    /*
    The XML file will have the following format:
    
    ...
    <users>
     <user>
      <user_id> 1 </user_id>
      <posts>
       <post> abcde </post>
       <post> fghij </post>
       ...
      </posts>
     <user>
     <user_id> 2 </user_id>
     ...
     </user
    ...
    </users>
    ...
    
    */
    
    try {
      
      Job job = Job.getInstance(conf, "HITSIndexer");

      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);

      job.setMapperClass(WordDocIDMapper.class);
      job.setReducerClass(WordDocIDReducer.class);

      job.setInputFormatClass(XmlInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);

      FileInputFormat.addInputPath(job, new Path("inputfile"));
      FileOutputFormat.setOutputPath(job, new Path("outputfile"));

      job.waitForCompletion(true);
    
    } catch(InterruptedException | ClassNotFoundException | IOException ex) {
      ex.printStackTrace();
    }
  }  
  
}
