# j360-instrumenter

这里简单对常用的instrument进行收集使用和总结

## google instrumenter

对分配的内存大小进行监控,主要针对new和特殊的构造器+回调进行监控,思考下如果监控对象的动态的内存变化呢?

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


## TProfile alibaba

监控代码方法执行的时间和数量的监控,分别通过输出log日志形式进行监控,性能影响在20%-30%之间

 1. 可配置监控的时间范围
 2. 可配置监控的方法
 3. 可通过tcp远程执行命令


## greys alibaba

通过远程监控命令监控jvm方法执行的性能,参考了BTrace


## BTrace 从sun剥离了,在github开源

通过远程监控命令监控jvm方法执行的性能,具体使用方式:



## aspectJ


## spring-instrument

