package com.jvm.garbagecollection;

import com.util.ClassUtils;

/**
 * 我的机器运行环境：
 * #44-Ubuntu SMP Tue Dec 1 14:39:05 UTC 2015 x86_64 x86_64 x86_64 GNU/Linux
 *
 * JDK 8，Hotspot
 *
 * 因此没有Perm Space，而是Metaspace
 *
 * Created by yjh on 15-12-30.
 */
public class Collectors {
    private static final int _1MB = 1024 * 1024;

    public static class SizeOfObject {
        /*
        基本信息：对象内存布局，对象的大小
        注意：以HotSpot为例，Java中的对象内存布局包括：MarkWord，ClassPointer，实例数据，padding
        如果是数组还有数组长度；如果开启了字段压缩，会进行指针压缩，子类的窄变量会插入父类的宽变量之中

        klass类型指针在64位中的Hotspot的实现也是4个字节，而数组长度用4个字节保存，因此它们补齐成为了一个8字节；
        */
        public static void main(String[] args) {
            //Object
            System.out.println(ClassUtils.sizeOf(new Object())); //16 8字节MarkWord，4字节klass指针，4字节padding
            //数组
            System.out.println(ClassUtils.sizeOf(new byte[0])); //16 8字节MarkWord，4字节klass指针，4字节数组长度
            System.out.println(ClassUtils.sizeOf(new byte[7])); //24 padding补齐
            System.out.println(ClassUtils.sizeOf(new byte[_1MB])); //1024 * 1024 + 16 = 1048592
            //窄对象
            System.out.println(ClassUtils.sizeOf(new Integer(1))); //16 int和klass补齐
            System.out.println(ClassUtils.sizeOf(new Byte("1"))); //16 byte和klass补齐
            System.out.println(ClassUtils.sizeOf(new Character('a'))); //16 char和klass补齐
        }
    }

    /*
    辅助信息控制参数：
    -XX:+PrintGCDetails
    XX:+PrintTLAB
    -Xloggc:filename
    -XX:+PrintHeapAtGC
    -XX:+PrintGC:PrintGCTimeStamps
     */

    /*
    Serial: -client模式的默认选择，-XX:+UseSerialGC开启Serial+Serial Old
    -Xms20m -Xmx20m -Xmn10M -XX:+UseSerialGC -XX:SurvivorRatio=8 -XX:+PrintGCDetails

    DefNew；
     */
    private static void testSerial() {
        byte[] a = new byte[_1MB * 4];
        byte[] b = new byte[_1MB * 4]; //Minor GC
        byte[] c = new byte[_1MB * 4]; //Minor GC
        byte[] d = new byte[_1MB * 4]; //Minor GC -> Full GC -> OOM
    }

    /*
    ParNew: -XX:+UseUseParNewGC 开启ParNew+PS OldGen
    -Xms20m -Xmx20m -Xmn10m -XX:+UseParNewGC -XX:SurvivorRatio=8 -XX:+PrintGCDetails

    ParNew；
     */
    private static void testParNew() {
        byte[] a = new byte[_1MB * 4];
        byte[] b = new byte[_1MB * 4]; //Minor GC
        byte[] c = new byte[_1MB * 4]; //Minor GC
        byte[] d = new byte[_1MB * 4]; //Minor GC -> Full GC -> OOM
    }

    /*
    Parallel Scavenge: -server模式的默认选择，开启PS+ParOldGen(本质MSC)，GCTimeRatio默认为99
    -Xms20m -Xmx20m -Xmn10m -XX:+UseParallelGC -XX:SurvivorRatio=8 -XX:+PrintGCDetails
    -XX:MaxGCPauseMillis=5 -XX:GCTimeRatio=99

    其他参数：
    -XX:UseAdaptiveSizePolicy（开关参数）不需要指定新生代的大小（-Xmn）；

    PSYoungGen
    Parallel Old只能和Parallel Scavenge搭配
    Parallel Scavenge只能和Serial Old，Parallel Old相搭配
     */
    private static void testParallelScavenge() {
        byte[] a = new byte[_1MB * 4];
        {
            byte[] b = new byte[_1MB * 4];
            byte[] c = new byte[_1MB * 4];
        }
        /*
        因为局部变量表会被复用，因此此时b和c所引用的两个数组还是可达的，
        通过声明两个int变量覆盖调b和c在局部变量表中的reference，GC就可以释放这两个数组了
         */
        int i1 = 0;
        int i2 = 0;
        byte[] d = new byte[_1MB * 4]; //Minor GC -> Full GC
    }

    /*
    CMS(Concurrent Mark Sweep): Old区收集器，
    -Xms20m -Xmx20m -Xmn10m -XX:+UseConcMarkSweepGC -XX:SurvivorRatio=8 -XX:+PrintGCDetails

    CMS默认与ParNew搭配，也可以与Serial Old搭配

    CMS重要参数：
    -XX+UseCMSCompactAtFullCollection：在要FullGC前进行一个压缩
    -XX:CMSFullGCsBeforeCompaction：多少次不带压缩的FullGC之后来一次带压缩的FullGC
    -XX:CMSInitiatingOccupancyFraction=92：old区达到92%占用是触发CMS进行回收
    -XX:+CMSParallelRemarkEnabled：并行的remark
    -XX:+CMSIncrementalMode：增量是并发收集，单线程模式，已经过时
    -XX:+CMSClassUnloadingEnabled

    GC日志：
    [GC (Allocation Failure) [ParNew: 5671K->479K(9216K), 0.0043829 secs] 5671K->4577K(19456K), 0.0044310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [GC (Allocation Failure) [ParNew: 4657K->534K(9216K), 0.0023518 secs] 8755K->8730K(19456K), 0.0023893 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
    [GC (CMS Initial Mark) [1 CMS-initial-mark: 8196K(10240K)] 12826K(19456K), 0.0007920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [CMS-concurrent-mark-start]
    [CMS-concurrent-mark: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [CMS-concurrent-preclean-start]
    [CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [CMS-concurrent-abortable-preclean-start]
    [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [GC (CMS Final Remark) [YG occupancy: 4630 K (9216 K)][Rescan (parallel) , 0.0004989 secs][weak refs processing, 0.0000057 secs][class unloading, 0.0002846 secs][scrub symbol table, 0.0003080 secs][scrub string table, 0.0001564 secs][1 CMS-remark: 8196K(10240K)] 12826K(19456K), 0.0013329 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
    [CMS-concurrent-sweep-start]
    [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [CMS-concurrent-reset-start]
    [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    [GC (Allocation Failure) [ParNew: 4765K->4765K(9216K), 0.0000121 secs][CMS: 8196K->4504K(10240K), 0.0026124 secs] 12961K->4504K(19456K), [Metaspace: 2877K->2877K(1056768K)], 0.0026672 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     */
    private static void useMemory() {
        byte[] b1 = new byte[_1MB * 4];
        byte[] b2 = new byte[_1MB * 4];
        //方法返回前是一个Safe Point可以进行GC活动
    }
    private static void testCMS() {
        byte[] b1 = new byte[_1MB * 4];
        useMemory();
        useMemory();
        useMemory();
    }

    /*
    G1:可预测的低停顿，
    -Xmx50m -Xms50m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+PrintGCDetails

    重要参数：
    -XX:MaxGCPauseMillis=n：最大垃圾回收停顿时间，这是一个软指标
    -XX:InitiatingHeapOccupancyPercent：启用GC是堆的占用比例，默认值45
    -XX:G1ReservePercent=n：默认10%，保留内存，“to-space”内存不足时启用
    -XX:ConcGCThreads=n：并发标记线程数量
    -XX:ParallelGCThreads=n：并行模式下线程数量

    GC过程：
    （1）初始标记：initial-mark，pause
    （2）并发标记：concurrent-root-region-scan-start/end，concurrent-mark-start/end
    （3）重新（最终）标志：remark
    （4）筛选回收：cleanup
     */
    private static void testG1() {
        byte[] b1 = new byte[_1MB * 4];
        for (int i = 0; i < 10; ++i) {
            useMemory();
        }
    }



    public static void main(String[] args) {
        testG1();
    }
}
