package org.xingze.etl;

import com.amazonaws.services.waf.model.SampledHTTPRequest;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.xingze.etl.utils.FileUtils;
import org.xingze.etl.utils.IpUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CDNLogCleaningDriver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        int res = ToolRunner.run(conf, new CDNLogCleaningDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        long timeMillisTaskStart = System.currentTimeMillis();

        String inputPath = args[0];
        String outPath = args[1];

        Configuration configuration = getConf();
        FileUtils.deleteOutput(configuration, outPath);

        final Job job = Job.getInstance(configuration);

        // 设置主类
        job.setJarByClass(CDNLogCleaningDriver.class);

        // 设置mapper和reducer
        job.setMapperClass(LogMapper.class);
        // 没有reduce
        job.setNumReduceTasks(0);
        // 设置map和reduce输出的key和value类型
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        // 设置输入和输出的路径。
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outPath));

        long timeMillisTaskEnd = System.currentTimeMillis();


        // 提交job
        if (job.waitForCompletion(true)) {
            long stop = System.currentTimeMillis();
            Counters counters = job.getCounters();
            Counter total = counters.findCounter("logs", "total");
            Counter dirty = counters.findCounter("logs", "dirty");
            Counter success = counters.findCounter("logs", "success");
            System.out.println(timeMillisTaskStart + " "
                    + timeMillisTaskEnd
                    + " " + total.getValue()
                    + " " + dirty.getValue()
                    + " " + success.getValue());
            return 0;
        } else {
            return 1;
        }
    }


    public static class LogMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Counter logItemCounter = context.getCounter("logs", "total");
            logItemCounter.increment(1);
            Boolean logIsDirty = false;

            String[] split = value.toString().split(",");

            // 处理清洗字段+
            String formatedDate = "";
            try {
                Calendar calendar = Calendar.getInstance();
                Date date = DateUtils.parseDate(split[0], new String[]{"[dd/MMM/yyyy:HH:mm:ss +0800]"});
                calendar.setTime(date);
                SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
                formatedDate = formatYYYYMMDD.format(date);
            } catch (ParseException e) {
                logIsDirty = true;
                e.printStackTrace();
            }

            String ip = split[1];

            URL accessUrl = new URL(split[6]);
            String protocol = accessUrl.getProtocol();
            String path = accessUrl.getPath();
            String domain = accessUrl.getHost();

            Long responseSize = 0L;
            try {
                responseSize = Long.parseLong(split[9]);
            } catch (NumberFormatException e) {
                Counter dirtyItemCounter = context.getCounter("logs", "dirty");
                dirtyItemCounter.increment(1);
                logIsDirty = true;
                e.printStackTrace();
            } finally {
                responseSize = 0L;
            }

            String parserIp = IpUtil.parserIp(ip);
            String province = null;
            String city = null;
            String isp = null;
            if (parserIp != null) {
                final String[] splitIP = parserIp.split("\\|");
                province = splitIP[2];
                city = splitIP[3];
                isp = splitIP[4];
            }

            if (!logIsDirty) {
                Counter dirtyItemCounter = context.getCounter("logs", "success");
                dirtyItemCounter.increment(1);
            }
            // 处理清洗字段-

            String proxyIp = split[2];
            String responseTime = split[3];
            String refer = split[4];
            String method = split[5];
            String httpCode = split[7];
            String requestSize = split[8];
            String cacheStatus = split[10];
            String ua = split[11];
            String fileType = split[12];


            CDNAccessLog cdnAccessLog = new CDNAccessLog();

            cdnAccessLog.setDomain(domain);
            cdnAccessLog.setProtocol(protocol);
            cdnAccessLog.setPath(path);
            cdnAccessLog.setProxyIp(proxyIp);
            cdnAccessLog.setResponseTime(responseTime);
            cdnAccessLog.setRefer(refer);
            cdnAccessLog.setMethod(method);
            cdnAccessLog.setAccessUrl(accessUrl.toString());
            cdnAccessLog.setHttpCode(httpCode);
            cdnAccessLog.setRequestSize(requestSize);
            cdnAccessLog.setResponseSize(responseSize);
            cdnAccessLog.setCacheStatus(cacheStatus);
            cdnAccessLog.setUa(ua);
            cdnAccessLog.setFileType(fileType);
            cdnAccessLog.setIp(ip);
            cdnAccessLog.setProvince(province);
            cdnAccessLog.setCity(city);
            cdnAccessLog.setIsp(isp);
            cdnAccessLog.setYear(formatedDate.split("-")[0]);
            cdnAccessLog.setMonth(formatedDate.split("-")[1]);
            cdnAccessLog.setDay(formatedDate.split("-")[2]);

            String log = cdnAccessLog.toString();
            context.write(NullWritable.get(), new Text(log));

        }
    }


}
