package demo;

import org.apache.twill.api.AbstractTwillRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenyj on 16/2/16.
 */
public class TwillRunnableDemo  extends AbstractTwillRunnable{
    public static final Logger logger = LoggerFactory.getLogger(TwillRunnableDemo.class);
        @Override
        public void run() {
            logger.info("this is from TwillRunnableDemo");
        }
}
