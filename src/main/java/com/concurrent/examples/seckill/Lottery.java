package com.concurrent.examples.seckill;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个抽奖活动的模拟，分桶支持并发
 *
 * 负载均衡环境下，多台服务器实例，使用数据库乐观锁，不需要在web层建立内存缓存进行加锁
 *
 * Created by yjh on 15-12-25.
 */
public class Lottery {
    private static final class Ticket {
        private final int id;
        private int userid;
        private Instant soldTime;

        public Ticket(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "id=" + id +
                    ", userid=" + userid +
                    ", soldTime=" + soldTime +
                    '}';
        }
    }

    private static final class BucketLock extends ArrayList<Ticket> {
        private int nextIndex; //下一个未被抢的index
        private final ReentrantLock lock;
        private final Condition notEmpty;

        public BucketLock() {
            lock = new ReentrantLock();
            notEmpty = lock.newCondition();
        }

        /**
         * 单例应用环境，在内存中同步
         * @param userid
         * @return
         * @throws InterruptedException
         */
        public boolean getOne(int userid) throws InterruptedException {
            boolean flag = false;
            lock.lockInterruptibly();
            try {
                int count = size();
                if (nextIndex < count) {
                    Ticket t;
                    (t = get(nextIndex)).userid = userid;
                    t.soldTime = Instant.now();
                    nextIndex++;
                    flag = true;
                }
            } finally {
                lock.unlock();
            }
            return flag;
        }
    }

    private static final List<List<Ticket>> TICKET_BUCKET;

    static {
        //生成票箱，一个共500张票，平均放在5个箱中，另外还有5个空箱
        TICKET_BUCKET = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            int j = i * 100;
            int end = j + 100;
            //每个有票的桶持有一个锁
            List<Ticket> bucket = new BucketLock();
            for (; j < end; ++j) {
                bucket.add(new Ticket(j));
            }
            //不可修改
            TICKET_BUCKET.add(Collections.unmodifiableList(bucket));
        }
        //用一个单例的空桶
        List<Ticket> empty = Collections.emptyList();
        for (int i = 0; i < 5; ++i)
            TICKET_BUCKET.add(empty);
        System.out.println(TICKET_BUCKET);
    }

    public void lottery(int userid) {
        /*
        生产环境中：
        检查用户是否满足抽奖条件，比如权限，等级，抽奖次数是否用完；
         */
        /*
        随机选择一个桶进行抽取
         */


    }

    public static void main(String[] args) {

    }
}
