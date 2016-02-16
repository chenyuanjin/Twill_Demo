package demo;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.twill.api.*;
import org.apache.twill.api.logging.LogEntry;
import org.apache.twill.api.logging.PrinterLogHandler;
import org.apache.twill.internal.DefaultResourceSpecification;
import org.apache.twill.yarn.YarnTwillRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenyj on 16/2/16.
 * 这个demo展示了通过继承AbstractTwillRunnable类,将自己的应用接入YARN,
 * 同时实例了通过ResourceSpecification指定计算资源规格
 */
public class RunnableDemo {
    public static final Logger logger = LoggerFactory.getLogger(RunnableDemo.class);

    public static void main(String[] args) {
        //指定zookeeper的ip和端口
        if (args.length < 1) {
            System.err.println("Arguments format: <host:port of zookeeper server>");
            System.exit(1);
        }

        String zkStr = args[0];

        //指定hadoop的位置,否则运行时会报错
        System.setProperty("hadoop.home.dir", "/Volumes/Data/Work/hadoop");

        YarnConfiguration yarnConfiguration = new YarnConfiguration();

        String yarnClasspath =
                yarnConfiguration.get(YarnConfiguration.YARN_APPLICATION_CLASSPATH,
                        Joiner.on(",").join(YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH));

        List<String> applicationClassPaths = Lists.newArrayList();
        Iterables.addAll(applicationClassPaths, Splitter.on(",").split(yarnClasspath));

        //指定资源规格,5个实例每个实例1CPU,2GB内存不限制网络带宽,如果不指定则是1个实例,1CPU,1GB内存
        ResourceSpecification rs = new DefaultResourceSpecification(1,2000,5,-1,-1);

        final TwillRunnerService twillRunner = new YarnTwillRunnerService(new YarnConfiguration(), zkStr);
        twillRunner.start();

        final TwillController controller = twillRunner.prepare(new TwillRunnableDemo(),rs)
                .addLogHandler(new PrinterLogHandler(new PrintWriter(System.out, true)))
                .setLogLevel(LogEntry.Level.DEBUG)
                .withApplicationClassPaths(applicationClassPaths)
                .withBundlerClassAcceptor(new HadoopClassExecluder())
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Futures.getUnchecked(controller.terminate());
                } finally {
                    twillRunner.stop();
                }
            }
        });

        try {
            controller.awaitTerminated();
        } catch (ExecutionException e) {
            logger.error("Error", e);
        }
    }

    static class HadoopClassExecluder extends ClassAcceptor {
        @Override
        public boolean accept(String className, URL classUrl, URL classPathUrl) {
            // exclude hadoop but not hbase package

            return !(className.startsWith("org.apache.hadoop") && !className.startsWith("org.apache.hadoop.hbase"));
        }
    }
}
