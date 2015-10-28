package com.exercise;

/**
 *
 *
 * Created by yjh on 15-10-28.
 */
public class StackWithMin {
    private int val;
    private StackWithMin next;
    private StackWithMin min;


    public void push(int node) {
        StackWithMin temp = next;
        next = new StackWithMin();
        next.val = node;
        next.next = temp;
        StackWithMin tempM = min;
        StackWithMin prev = null;
        while(tempM != null && tempM.val < node) {
            prev = tempM;
            tempM = tempM.min;
        }
        next.min = tempM;
        if(prev != null) {
            prev.min = next;
        } else {
            min = next;
        }
    }

    public void pop() {
        if(next != null) {
            StackWithMin n = next;
            next = next.next;
            StackWithMin temp = min;
            StackWithMin prev = null;
            while(temp != n) {
                prev = temp;
                temp = temp.min;
            }
            if(prev != null) {
                prev.min = temp.min;
            } else {
                min = min.min;
            }

        }
    }

    public int top() {
        if(next != null)
            return next.val;
        else
            throw new RuntimeException();
    }

    public int min() {
        if(min != null)
            return min.val;
        else
            throw new RuntimeException();
    }

    public void print() {
        StackWithMin temp = next;
        while (temp != null) {
            System.out.print(temp.val + " ");
            temp = temp.next;
        }
    }

    public static void main(String[] args) {
        StackWithMin solution = new StackWithMin();
        solution.push(3);
        System.out.println(solution.min.val);
        solution.push(4);
        System.out.println(solution.min.val);
        solution.push(2);
        System.out.println(solution.min.val);
        solution.push(3);
        System.out.println(solution.min.val);
        solution.pop();
        System.out.println(solution.min.val);
        solution.pop();
        System.out.println(solution.min.val);
        solution.pop();
        System.out.println(solution.min.val);
        solution.push(0);
        System.out.println(solution.min.val);

    }
}
