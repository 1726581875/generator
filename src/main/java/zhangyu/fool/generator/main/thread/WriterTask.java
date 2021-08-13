package zhangyu.fool.generator.main.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.main.writer.FoolWriter;

/**
 * @author xiaomingzhang
 * @date 2021/8/2
 */
public class WriterTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(WriterTask.class);

    private final FoolWriter writer;

    private final String destPath;

    public WriterTask(FoolWriter writer, String destPath){
        this.writer = writer;
        this.destPath = destPath;
    }

    @Override
    public void run() {
        try{
            writer.write(destPath);
        }catch (Exception e){
            log.error("task run error ",e);
        }
    }
}
