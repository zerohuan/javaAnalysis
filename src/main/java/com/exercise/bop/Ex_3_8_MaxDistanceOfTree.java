package com.exercise.bop;

/**
 * Created by yjh on 2016/3/12.
 */
public class Ex_3_8_MaxDistanceOfTree {
    private static class Node {
        private int val;
        private Node left;
        private Node right;
    }

    private static class Result {
        static final Result EMPUTY = new Result(-1, 0);
        private int maxDistance;
        private int maxDepth;

        public Result(int maxDistance, int maxDepth) {
            this.maxDistance = maxDistance;
            this.maxDepth = maxDepth;
        }
    }

    private static int FindMaxLen(Node root) {
        return FindMaxLenHelper(root).maxDistance;
    }

    private static Result FindMaxLenHelper(Node root) {
        //ÎªÒ¶½Úµã
        if (root == null)
            return Result.EMPUTY;

        Result leftR = FindMaxLenHelper(root.left);
        Result rightR = FindMaxLenHelper(root.right);

        Result result = new Result(-1,-1);
        result.maxDepth = Math.max(leftR.maxDepth, rightR.maxDepth) + 1;
        result.maxDistance = Math.max(Math.max(leftR.maxDistance,rightR.maxDistance), (leftR.maxDepth + rightR.maxDepth + 1));
        return result;
    }
}
