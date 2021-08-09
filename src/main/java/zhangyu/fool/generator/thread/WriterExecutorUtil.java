package zhangyu.fool.generator.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;

/**
 * @author xiaomingzhang
 * @date 2021/7/5
 * 封装线程池工具类
 */
public class WriterExecutorUtil {

    private static final Logger log = LoggerFactory.getLogger(WriterExecutorUtil.class);

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = initThreadPoolExecutor();


    /**
     * 提交任务需要注意异常捕获
     * @throws RejectedExecutionException
     * @throws NullPointerException
     */
    public static Future<?> submit(Runnable task) {
        return THREAD_POOL_EXECUTOR.submit(task);
    }

    /**
     * @throws RejectedExecutionException
     * @throws NullPointerException
     */
    public static <T> Future<T> submit(Runnable task, T result) {
        return THREAD_POOL_EXECUTOR.submit(task, result);
    }

    /**
     * @throws RejectedExecutionException
     * @throws NullPointerException
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return THREAD_POOL_EXECUTOR.submit(task);
    }




    /**
     * 线程池
     * @return
     */
    private static ThreadPoolExecutor initThreadPoolExecutor() {

        /**
         * 核心线程数
         * 1、核心线程默认不会预先创建，在达到coreSize之前，来一个任务创建一个核心线程（可以调用prestartCoreThread/prestartAllCoreThreads方法提前创建核心线程）
         * 2、核心线程默认创建后会一直保持在线程池，不会超时消亡（除非设置allowCoreThreadTimeOut = true）
         * 3、在工作队列满之前，线程池里的线程数不会超过corePoolSize
         */
        int corePoolSize = 10;
        /**
         * 最大线程数
         * 1、强制 maximumPoolSize >= corePoolSize
         * 2、当工作队列满了，并且当前线程数 < maximumPoolSize,才会创建新线程
         */
        int maximumPoolSize = 10;
        /**
         * 线程空闲状态存活时间
         * 1、默认当线程数大于核心数时，这是多余空闲线程在终止前等待新任务的最长时间
         * 2、如果设置allowCoreThreadTimeOut = true，核心线程也会空闲消亡
         */
        long keepAliveTime = 5;
        /**
         * 工作任务队列，大小默认值为 Integer.MAX_VALUE
         */
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue(10000);
        /**
         * 线程池已经关闭或对最大线程和最大队列都使用饱和时触发
         * 拒绝策略，有如下四种处理策略：
         * AbortPolicy（默认处理策略，当线程池满了，直接抛弃任务并抛出异常）、
         * DiscardPolicy（当线程池满了，直接丢弃任务，不抛异常）
         * CallerRunsPolicy（当线程池满了，直接交由主线程运行，不会等待线程池线程运行）、
         * DiscardOldestPolicy（当线程池满了，丢弃最老的任务）
         * 也可以自定义处理策略，继承RejectedExecutionHandler接口实现rejectedExecution方法即可
         */
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        //创建一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, callerRunsPolicy);
        /**
         * 允许核心线程超时
         */
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        log.info("初始化导出线程池 => [corePoolSize={}, maximumPoolSize={}, keepAliveTime={}s, workQueueSize={}]",corePoolSize,maximumPoolSize,keepAliveTime, 10000);
        return threadPoolExecutor;
    }



    /**
     * 自定义处理策略 例子
     */
    private static class MyAbortPolicy implements RejectedExecutionHandler {

        public MyAbortPolicy() { }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.warn("线程池满了");
            //处理逻辑
            //...
        }
    }


}