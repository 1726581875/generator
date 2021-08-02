package zhangyu.fool.generate.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.writer.CodeWriter;

/**
 * @author xiaomingzhang
 * @date 2021/8/2
 */
public class WriterTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(WriterTask.class);

    private final CodeWriter writer;

    private final String destPath;

    public WriterTask(CodeWriter writer,String destPath){
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
