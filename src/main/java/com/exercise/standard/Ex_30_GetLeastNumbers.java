package com.exercise.standard;

import com.exercise.util.SwapUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 输入n个整数，找出其中最小的K个数。
 *
 * Created by yjh on 16-1-26.
 */
public class Ex_30_GetLeastNumbers {
    /*
    考虑两个问题，1.查找n个数中最小的k个数；2.查找n个数中最小第k个数；
    这两个问题的解法基本是相通的，但所求的目标结果不同。这里以问题1为主进行介绍。
    这个问题在于我们需要找到的是最小的k个数，因此大部分情况下没有必要进行全局排序（这k个数没有必要进行排序，另外n-k更不需要排序），所做的工作越少越佳，结合具体的时间/空间要求选择具体的方法。

    解法总结：
    （1）选择排序或插入排序，时间O(n * k)，修改原数组顺序，基于排序的方法的考虑是k很小的情况；
    （2）基于快速选择的方法，时间按O(n)，修改原数组顺序，有快速排序中Partition改进而来；
    （3）大量数据情况下，使用前k个数建立一个大小为k的最大堆，遍历剩下的(n-k)个数，如果比堆顶元素小则替换堆顶元素并重新进行堆序化；
         时间O(k+(n-k)*lgk);不会修改原数组顺序。这个方法的另一个变种是建立大小为n的堆，虽然时间开销相似但是显然需要更大的内存开销。

    实际应用时应该综合n的大小，k的大小来判断，如果k很小（相对于n），使用选择/插入排序也很合适；如果n很大，那就要考虑使用最大堆了。
    如果k > n - k，可以考虑求解问题的相反方向。
    思考此类问题，要考虑几种不同的因素：（1）输入数据量大小；（2）能否一次性读入内存；（3）是否允许修改原数据顺序/结构等；
     */
    //解法一：选择排序
    public static List<Integer> getLeastKNumbers_1(int[] n, int k) {
        List<Integer> res = new ArrayList<>();
        int len;
        if (n != null && (len = n.length) >= k && k > 0) {
            for (int j = 0; j < k; ++j) {
                int minNIndex = j;
                int i;
                for (i = j + 1; i < len; ++i) {
                    if (n[minNIndex] > n[i]) {
                        minNIndex = i;
                    }
                }
                SwapUtil.swap(n, j, minNIndex);
                res.add(n[j]);
            }
        }

        return res;
    }

    //解法二：基于快速选择，使用三元取中的方法选取枢纽元
    public static List<Integer> getLeastKNumbers_2(int[] n, int k) {
        List<Integer> res = new ArrayList<>();
        int len;
        if (n != null && (len = n.length) >= k && k > 0) {
            int l = 0, r = len - 1;
            int index = partition(n, l, r);
            while (index != k - 1) {
                if (index >= k)
                    r = index - 1;
                else
                    l = index + 1;
                index = partition(n, l, r);
            }
            for (int i = 0; i < k; ++i)
                res.add(n[i]);
        }
        return res;
    }
    private static Random rnd = new Random();
    private static int partition(int[] n, int l, int r) {
        if (n == null || n.length <= 0 || l < 0 || r >= n.length)
            throw new IllegalArgumentException();

        int index = l + rnd.nextInt(r - l + 1); //随机选取枢纽元
        SwapUtil.swap(n, index, r);

        int small = l - 1;
        for (index = l; index < r; ++index) {
            if (n[index] < n[r]) {
                ++small;
                if (small != index)
                    SwapUtil.swap(n, index, small);
            }
        }
        ++small;
        SwapUtil.swap(n, small, r);
        return small;
    }

    //解法三：基于最大堆
    public static List<Integer> getLeastKNumbers_3(int[] n, int k) {
        List<Integer> res = new ArrayList<>();
        int len;
        if (n != null && (len = n.length) >= k && k > 0) {
            //取前k个数先组成最大堆
            int[] heap = new int[k];
            System.arraycopy(n, 0, heap, 0, k);
            heapify(heap);
            for (int i = k; i < len; ++i) {
                if (n[i] < heap[0]) {
                    heap[0] = n[i];
                    fixDown(heap, 0);
                }
            }
            for (int i = 0; i < k; ++i)
                res.add(heap[i]);
        }
        return res;
    }
    private static void heapify(int[] n) {
        for (int i = (n.length >> 1); i >= 0; --i) {
            fixDown(n, i);
        }
    }
    private static void fixDown(int[] n, int i) {
        int temp = n[i], child = (i << 1) + 1, len;
        while (child < (len = n.length)) {
            if (child + 1 < len && n[child + 1] > n[child])
                ++child;
            if (n[child] < temp)
                break;
            n[i] = n[child];
            i = child;
            child = (i << 1) + 1;
        }
        n[i] = temp;
    }

    //测试大量数据下最大堆方法的表现：
    private static Logger logger = LogManager.getLogger();
    public static void testHeapMethod() {
        //生成数据
        int[] data = new int[10_000_000];
        for (int i = 0; i < 10_000_000; ++i) {
            data[i] = rnd.nextInt(1000_000_000);
        }
        logger.debug("start test");
        List<Integer> res = getLeastKNumbers_3(data, 100);
        logger.debug("end test");
        System.out.println(res);
    }

    public static void main(String[] args) {
//        int[] n = new int[]{4,5,1,6,2,7,3,8};
//        System.out.println(getLeastKNumbers_3(n, 0));
        testHeapMethod();
    }
}
