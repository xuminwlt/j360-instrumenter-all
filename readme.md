# j360-instrumenter

这里简单对常用的instrument进行收集使用和总结

jdk5/6 instrument
 > * javaagent premain jdk5+
 > * javaagent agentmain 依赖Attach Tools API jdk6+

> idea debug javaagent
 - new debug application
 - mainClass:me.j360.agent.demo.MainDemoClass
 -  -javaagent:target/j360-agent-1.0-SNAPSHOT.jar
 - 分别对premain和MainDemoClass打断点进行debug


## google instrumenter  <https://github.com/google/allocation-instrumenter>

对分配的内存大小进行监控,主要针对new和特殊的构造器+回调进行监控,思考下如果监控对象的动态的内存变化呢?

地址目录是我的maven仓库地址


-javaagent:/Users/min_xu/.m2/repository/com/google/code/java-allocation-instrumenter/java-allocation-instrumenter/3.0/java-allocation-instrumenter-3.0.jar

java -javaagent:/Users/min_xu/.m2/repository/com/google/code/java-allocation-instrumenter/java-allocation-instrumenter/3.0/java-allocation-instrumenter-3.0.jar TestAllocations
java -javaagent:/Users/min_xu/.m2/repository/com/google/code/java-allocation-instrumenter/java-allocation-instrumenter/3.0/java-allocation-instrumenter-3.0.jar TestConstructors


```
public class Test {
  public static void main(String [] args) throws Exception {
    AllocationRecorder.addSampler(new Sampler() {
      public void sampleAllocation(int count, String desc, Object newObj, long size) {
        System.out.println("I just allocated the object " + newObj
            + " of type " + desc + " whose size is " + size);
        if (count != -1) { System.out.println("It's an array of size " + count); }
      }
    });
    for (int i = 0 ; i < 10; i++) {
      new String("foo");
    }
  }
}
```

构造器

```
public class Test {

  static int count = 0;

  int x;
  Test() {
    x = count;
  }
  public static void main(String[] args) {
    try {
      ConstructorInstrumenter.instrumentClass(
          Test.class, new ConstructorCallback<Test>() {
            @Override public void sample(Test t) {
              System.out.println(
                  "Constructing an element of type Test with x = " + t.x);
              count++;
            }
          });
    } catch (UnmodifiableClassException e) {
      System.out.println("Class cannot be modified");
    }
    for (int i = 0; i < 10; i++) {
      new Test();
    }
    System.out.println("Constructed " + count + " instances of Test");
  }
}
```


## TProfile alibaba  <https://github.com/alibaba/TProfiler>

监控代码方法执行的时间和数量的监控,分别通过输出log日志形式进行监控,性能影响在20%-30%之间

 1. 可配置监控的时间范围
 2. 可配置监控的方法
 3. 可通过tcp远程执行命令

详见: <https://github.com/xuminwlt/j360-agent>
    -》 如何使用idea调试premain方法

-javaagent:/Users/min_xu/Documents/IdeaProjects/TProfiler/pkg/TProfiler/lib/tprofiler-1.0.1.jar -Dprofile.properties=/Users/min_xu/Documents/IdeaProjects/TProfiler/pkg/TProfiler/lib/profile.properties

tprofile相关的配置

```
#log file name
logFileName = tprofiler.log
methodFileName = tmethod.log
samplerFileName = tsampler.log

#basic configuration items
startProfTime = 9:00:00
endProfTime = 13:00:00
eachProfUseTime = 5
eachProfIntervalTime = 50
samplerIntervalTime = 20
port = 50000
debugMode = true
needNanoTime = false
ignoreGetSetMethod = true

#file paths
logFilePath = ${user.home}/logs/${logFileName}
methodFilePath = ${user.home}/logs/${methodFileName}
samplerFilePath = ${user.home}/logs/${samplerFileName}

#include & excludes items
excludeClassLoader = org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader
includePackageStartsWith = com.taobao;com.taobao.common;com.fotoplace
excludePackageStartsWith = com.taobao.sketch;org.apache.velocity;com.alibaba;com.taobao.forest.domain.dataobject

```


## aspectJ

AspectJ的Load Time Weaving机制，需要配置 -javaagent: [path to aspectj-weaver.jar] 。
打开aspectj-weaver.jar，可以看到META-INF/MANIFEST里定义了 Premain-Class: org.aspectj.weaver.loadtime.Agent

如何通过可配置化的手段进行监控:aop.xml

```
mvn clean package exec:exec -Pstart-weaver-demo
```

## greys alibaba <https://github.com/oldmanpushcart/greys-anatomy>

通过远程监控命令监控jvm方法执行的性能,参考了BTrace



## BTrace 从sun剥离了,在github开源 <https://github.com/btraceio/btrace>

通过远程监控命令监控jvm方法执行的性能,具体使用方式:



## spring-instrument

