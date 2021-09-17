package com.demo.sys;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程配置类
 * @author jky 2021-05-11
 *
 */
@Configuration
@ComponentScan("com.demo.sys")
public class AsyncTaskConfig implements AsyncConfigurer {

    // ThredPoolTaskExcutor的处理流程
    // 当池子大小小于corePoolSize，就新建线程，并处理请求
    // 当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去workQueue中取任务并处理
    // 当workQueue放不下任务时，就新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝处理
    // 当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(20);// 最小线程数
        taskExecutor.setMaxPoolSize(200);// 最大线程数
        taskExecutor.setQueueCapacity(50);// 等待队列
        
        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.setKeepAliveSeconds(1);//非核心线程的闲置超时时间，超过这个时间就会被回收。  当将allowCoreThreadTimeOut设置为true时对corePoolSize生效。
        taskExecutor.setThreadFactory(new DefaultThreadFactory());//通过线程工厂可以对线程的一些属性进行定制。
       // taskExecutor.setRejectedExecutionHandler(new DefaultRejectedExecutionHandler());//当线程池中的资源已经全部使用，添加新线程被拒绝时，会调用
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
  //通过线程工厂可以对线程的一些属性进行定制。
  	static class DefaultThreadFactory implements ThreadFactory {
  		  private static final AtomicInteger poolNumber = new AtomicInteger(1);
  		  private final ThreadGroup group;
  		  private final AtomicInteger threadNumber = new AtomicInteger(1);
  		  private final String namePrefix;

  		  DefaultThreadFactory() {
  		      SecurityManager var1 = System.getSecurityManager();
  		      this.group = var1 != null?var1.getThreadGroup():Thread.currentThread().getThreadGroup();
  		      this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
  		  }
  		  @Override
  		  public Thread newThread(Runnable var1) {
  		      Thread var2 = new Thread(this.group, var1, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
  		      if(var2.isDaemon()) {
  		          var2.setDaemon(false);
  		      }

  		      if(var2.getPriority() != 5) {
  		          var2.setPriority(5);
  		      }

  		      return var2;
  		  }
  	}
}
