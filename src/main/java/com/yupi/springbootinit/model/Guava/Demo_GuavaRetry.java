package com.yupi.springbootinit.model.Guava;

import com.github.rholder.retry.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Demo_GuavaRetry {
   public static int a = 3;
   public static int i=0;
   public static void main(String[] args) {
//       创建重试器
       Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
              .retryIfResult(result-> result!=null && !result)
              .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
              .withStopStrategy(StopStrategies.stopAfterAttempt(10))
              .build();
       try {
//           调用call方法 异步执行 重试任务
           retryer.call(() -> {
               a--;
               i++;
               if (a == 0) {
                   System.out.println("结果为0，终止尝试");
                   return true;
              } else {
                   System.out.println("第"+i+"次尝试");
                   return false;
              }
          });
      } catch (ExecutionException e) {
           System.out.println(e);
      } catch (RetryException e) {
           throw new RuntimeException(e);
       }
   }
}