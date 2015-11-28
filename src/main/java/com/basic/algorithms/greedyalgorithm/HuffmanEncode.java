package com.basic.algorithms.greedyalgorithm;

import java.util.*;

/**
 * Huffman编码
 *
 * Created by yjh on 15-11-25.
 */
public class HuffmanEncode {
    public static abstract class HuffTreeNode implements Comparable<HuffTreeNode> {
        protected HuffTreeNode left;
        protected HuffTreeNode right;

        private String value;
        private int count; //权值

        public HuffTreeNode(String value, int count) {
            this.value = value;
            this.count = count;
        }

        @Override
        public String toString() {
            return "LeafNode{" +
                    "value='" + value + '\'' +
                    ", count=" + count +
                    '}';
        }

        @Override
        public int compareTo(HuffTreeNode o) {
            if(count < o.count)
                return -1;
            else if(count > o.count)
                return 1;
            else
                return 0;
        }
    }

    public static class LeafNode extends HuffTreeNode {
        public LeafNode(String value, int count) {
            super(value, count);
        }
    }

    public static class NonLeafNode extends HuffTreeNode {
        public NonLeafNode(String value, int count) {
            super(value, count);
        }
    }

    public static class HuffmanTreeUtil {
        /**
         * Huffman算法实现，构建Huffman树
         * @param minHeap
         * @return
         */
        public static HuffTreeNode buildTree(SimpleMinHeap minHeap, Map<String, Integer> decodeMap) {
            HuffTreeNode p = null;
            StringBuilder sb = new StringBuilder();
            char prefix = 'T';
            int i = 1;
            while(minHeap.size() > 1) {
                HuffTreeNode n1 = minHeap.pop();
                HuffTreeNode n2 = minHeap.pop();
                HuffTreeNode n3 = new NonLeafNode(sb.append(prefix).append(i++).toString(),
                        n1.count + n2.count);
                n3.left = n1;
                n3.right = n2;
                minHeap.insert(n3);
                p = n3;
                sb.setLength(0);
            }
            return p;
        }

        /**
         * 前序遍历打印Huffman树
         * @return
         */
        public static String printRoot(HuffTreeNode root) {
            List<HuffTreeNode> toPrints = new ArrayList<>();

            HuffTreeNode p = root;
            Stack<HuffTreeNode> s = new Stack<>();
            while(p != null) {
                toPrints.add(p);
                //右儿子入栈
                if(p.right != null) {
                    s.push(p.right);
                }
                if(p.left != null) {
                    p = p.left;
                } else {
                    if(s.isEmpty())
                        break;
                    p = s.pop();
                }
            }

            return "HuffmanTreeUtil{" + toPrints.toString() + "}";
        }
    }

    /**
     * 简单最小堆，用于每轮取最小频率的节点
     */
    public static final class SimpleMinHeap {
        private final List<HuffTreeNode> forest = new ArrayList<>();

        public SimpleMinHeap(HuffTreeNode[] chars) {
            if(chars == null || chars.length == 0)
                throw new RuntimeException("输入数组不能为空");
            forest.addAll(Arrays.asList(chars));

            //进行堆序化
            for(int i = forest.size() >> 1; i >= 0; --i) {
                precDown(i);
            }
        }

        /**
         * 将位置i的节点下滤
         * @param i
         */
        private void precDown(int i) {
            int child = (i << 1) + 1;
            HuffTreeNode temp;
            final int size = forest.size();

            for(temp = forest.get(i); child < size; i = child, child = (i << 1) + 1) {
                if(child + 1 < size && forest.get(child + 1).compareTo(forest.get(child)) < 0) {
                    child++;
                }
                if(forest.get(child).compareTo(temp) < 0) {
                    forest.set(i, forest.get(child));
                } else {
                    break;
                }
            }
            forest.set(i, temp);
        }

        /**
         * 将位置i的节点上滤
         * @param i
         */
        private void precUp(int i) {
            HuffTreeNode temp = forest.get(i);
            for(int parent = (i - 1) >> 1; parent >= 0 && forest.get(parent).compareTo(forest.get(i)) > 0; i = parent) {
                forest.set(i, forest.get(parent));
            }
            forest.set(i, temp);
        }

        /**
         * 插入新元素
         * @param node
         * @return
         */
        HuffTreeNode insert(HuffTreeNode node) {
            forest.add(node);
            precUp(forest.size() - 1);
            return node;
        }

        /**
         * 返回堆顶的元素
         * @return
         */
        HuffTreeNode top() {
            return forest.isEmpty() ? null : forest.get(0);
        }

        /**
         * deleteMin操作
         * @return
         */
        HuffTreeNode pop() {
            if(!forest.isEmpty()) {
                HuffTreeNode min = forest.get(0);
                int size =  forest.size();
                forest.set(0, forest.get(size - 1));
                forest.remove(size - 1);
                if(forest.size() > 0)
                    precDown(0);

                return min;
            } else
                return null;
        }

        private int size() {
            return forest.size();
        }

        private void swap(int i, int j) {
            HuffTreeNode temp = forest.get(i);
            forest.set(i, forest.get(j));
            forest.set(j, temp);
        }

        @Override
        public String toString() {
            return "SimpleMinHeap{" +
                    "chars=" + forest +
                    '}';
        }
    }

    public static void main(String[] args) {
        HuffTreeNode[] chars = {
                new LeafNode("a", 10), new LeafNode("e", 15), new LeafNode("i", 12),
                new LeafNode("s", 3), new LeafNode("t", 4), new LeafNode("sp", 13),
                new LeafNode("nl", 1)
        };
        SimpleMinHeap minHeap = new SimpleMinHeap(chars);
        System.out.println(minHeap);
        Map<String, Integer> decodeMap = new HashMap<>();
        System.out.println(HuffmanTreeUtil.printRoot(HuffmanTreeUtil.buildTree(minHeap, decodeMap)));
        System.out.println(decodeMap);
    }
}
