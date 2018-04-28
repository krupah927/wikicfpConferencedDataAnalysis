package assignq4;

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

public class AssignQ4 {

	//mapper class implementation
  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

	  //for value
	  private final static IntWritable one = new IntWritable(1);
	  private Text word = new Text();

	  public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		  // split the input by tab delimiter 
           String[] splitStr=value.toString().split("\t");
           //extract city and year from the data 
           word.set(splitStr[2]+" "+splitStr[0].substring(splitStr[0].length()-4,splitStr[0].length()));
           //set the composite key contained in word 
           context.write(word, one);
	  	}
  	}

  //reducer implementation 
  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    //integer varible to store result
	  	private IntWritable result = new IntWritable();

	  	public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
	  		//count the values for each key
	  		int sum = 0;
	  	for (IntWritable val : values) {
          sum += val.get();
        }
        result.set(sum); 
        //output the count per key
        context.write(key, result);
      }
    }
    //configurations
    public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf, "word count");
      job.setJarByClass(AssignQ4.class);
      job.setMapperClass(TokenizerMapper.class);
      job.setCombinerClass(IntSumReducer.class);
      job.setReducerClass(IntSumReducer.class);
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1]));
      System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
  }
