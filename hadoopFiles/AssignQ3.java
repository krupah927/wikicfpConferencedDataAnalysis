package assign3;

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

public class AssignQ3 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

  //  private final static IntWritable one = new IntWritable(1);
    //variables to store key value data
	  private Text word = new Text();
        private Text word1 =new Text();
private Text word2 = new Text();
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {

    			// split the input by tab delimiter 
                String[] splitStr=value.toString().split("\t");
                //get 1st column data and exclude year
                word2.set(splitStr[0].substring(0,splitStr[0].length()-4));
                //get 3rd column
                word.set(splitStr[2]);
                //set and pass key values to reduces
                context.write(word2, word);
                
    	}
  }

  public static class IntSumReducer
       extends Reducer<Text,Text,Text,Text> {
    private Text result = new Text();

    public void reduce(Text key, Iterable<Text> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	//concatinate values per city 
    	String sum="";
          for (Text val : values) {
            sum +=val.toString()+",";
          }
          result.set(sum);
          //output the key value result 
          context.write(key, result);
        }
      }

      public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(AssignQ3.class);
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
