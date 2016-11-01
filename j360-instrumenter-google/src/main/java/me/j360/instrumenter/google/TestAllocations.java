package me.j360.instrumenter.google;

import com.google.monitoring.runtime.instrumentation.AllocationRecorder;
import com.google.monitoring.runtime.instrumentation.Sampler;

/**
 * Package: me.j360.instrumenter.google
 * User: min_xu
 * Date: 2016/11/1 上午11:29
 * 说明：
 */
public class TestAllocations {

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
