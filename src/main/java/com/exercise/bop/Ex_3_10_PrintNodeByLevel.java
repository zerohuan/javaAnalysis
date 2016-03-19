package com.exercise.bop;

import java.util.LinkedList;

/**
 * Created by yjh on 2016/3/13.
 */
public class Ex_3_10_PrintNodeByLevel {
    private static class Node {
        private int value;
        private Node left;
        private Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    //从上往下打印
    public static void printNodeByLevel(Node root) {
        if (root != null) {
            LinkedList<Node> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int len = queue.size();
                while (len-- > 0) {
                    Node n = queue.poll();
                    System.out.print(n.value);
                    if (n.left != null)
                        queue.add(n.left);
                    if (n.right != null)
                        queue.add(n.right);
                }
                System.out.println();
            }
        }
    }

    //从下往上，从左到右打印，这里实际和用栈思想相同
    public static void printNodeBottomLevel(Node root) {
        if (root != null) {
            StringBuilder buffer = new StringBuilder();
            LinkedList<Node> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                int len = queue.size();
                while (len-- > 0) {
                    Node n = queue.poll();
                    buffer.append(n.value);
                    //注意入队列的顺序这里是先右后左
                    if (n.right != null)
                        queue.add(n.right);
                    if (n.left != null)
                        queue.add(n.left);
                }
                buffer.append("\n");
            }

            System.out.println(buffer.reverse());
        }
    }


    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(7);
        Node n8 = new Node(8);
        n1.left = n2;n1.right=n3;
        n2.left = n4;n2.right=n5;
        n3.right=n6;
        n5.left=n7;n5.right=n8;
        printNodeBottomLevel(n1);
    }
}
