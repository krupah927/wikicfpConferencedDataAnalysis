package assign2;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AssignQ2 {
//mapper implementation
  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
        private Text word1 =new Text();
        private Text word2 = new Text();
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

    	//split the input to different columns
        String[] splitStr=value.toString().split("\t");
        word1.set(splitStr[0]);
        //get keys -cities
        word.set(splitStr[2]);
        
        context.write(word, word1);

    }
  }
//reducer implementation
  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
    private Text result= new Text();

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	// group the conferences for each city
    	String sum="";
      for (Text val : values) {
          sum +=val.toString();
          if(val.toString()!=" " || val.toString()!="\n" || val.toString()!="  ")
                  sum+=",";
        }
      //result values 
        result.set(sum);
        
        //output key values - reducer output
        context.write(key, result);
      }
    }
//configurations 
    public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf, "word count");
      job.setJarByClass(AssignQ2.class);
      job.setMapperClass(TokenizerMapper.class);
      job.setCombinerClass(IntSumReducer.class);
      job.setReducerClass(IntSumReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(Text.class);
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
  }
