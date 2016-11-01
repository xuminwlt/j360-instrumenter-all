package me.j360.instrumenter.google;

import com.google.monitoring.runtime.instrumentation.AllocationRecorder;
import com.google.monitoring.runtime.instrumentation.ConstructorCallback;
import com.google.monitoring.runtime.instrumentation.ConstructorInstrumenter;
import com.google.monitoring.runtime.instrumentation.Sampler;

import java.lang.instrument.UnmodifiableClassException;

/**
 * Package: me.j360.instrumenter.google
 * User: min_xu
 * Date: 2016/11/1 上午11:51
 * 说明：
 */
public class TestConstructors {

    static int count = 0;

    int x;
    TestConstructors() {
        x = count;
    }

    public static void main(String[] args) {

        //装插构造方法
        try {
            ConstructorInstrumenter.instrumentClass(
                    TestConstructors.class, new ConstructorCallback<TestConstructors>() {
                        @Override public void sample(TestConstructors t) {
                            System.out.println(
                                    "Constructing an element of type Test with x = " + t.x);
                            count++;
                        }
                    });
        } catch (UnmodifiableClassException e) {
            System.out.println("Class cannot be modified");
        }
        for (int i = 0; i < 10; i++) {
            new TestConstructors();
        }
        System.out.println("Constructed " + count + " instances of Test");


        //装插分配
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
