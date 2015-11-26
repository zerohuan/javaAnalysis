package com.basic.algorithms.greedyalgorithm;

import com.basic.util.testUtil.AbstractTestable;
import com.basic.util.testUtil.MTestable;
import com.basic.util.testUtil.MTester;

/**
 *
 *
 * Created by yjh on 15-11-25.
 */
public class DispatchProblem {
    /*
        简单调度问题（作业的处理时间是向处理器提交直到完成的时间）：
        问题1：一组作业j1,j2,j3...,jn，已知运行时间t1,t2,t3,...,tn，处理器只有一个求最短的平均处理时间；
        解：按运行时间单调非减的顺序进行执行；

        问题2：处理器数为P时，问题1的答案；
        解：按运行时间单调非减的顺序处理，每次取P个任务分别在P个处理器上进行处理；
        这个问题有多个最优解，即使P不整除N也有多个最优解；
        思考多个最优解，考虑总时间；
     */
    private MTester mTester = new MTester();

    //作业，已排序
    private static final int[] jobs = {3,5,6,10,11,14,15,18,20};

    private static final MTestable problem2_1 = new AbstractTestable("调度问题2，正好整除") {
        @Override
        public void test() throws Exception {
            dispatch(jobs, 3);
        }
    };
    private static final MTestable problem2_2 = new AbstractTestable("调度问题2，不能整除") {
        @Override
        public void test() throws Exception {
            dispatch(jobs, 3);
        }
    };

    public static int dispatch(int[] jobs, int cpuNum) {
        int n;
        if(jobs == null || (n = jobs.length) == 0 || cpuNum == 0)
            throw new RuntimeException("输入不正确");
        int s = 0, i = 0;



        return s;
    }

    public static void main(String[] args) {

    }
}
