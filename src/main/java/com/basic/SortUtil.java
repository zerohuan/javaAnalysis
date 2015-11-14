package com.basic;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 *
 *
 * Created by yjh on 15-11-8.
 */
public class SortUtil {
    private static boolean checkInput(int[] input) {
        return !(input == null || input.length == 0);
    }

    /**
     * 插入排序
     * @param input
     */
    public static void insertionSort(int[] input) {
        if(!checkInput(input))
            return;
        int j,i;
        for(i = 1; i < input.length; ++i) {
            int tmp = input[i];
            for(j = i; j > 0 && input[j - 1] > tmp; --j)
                input[j] = input[j - 1];
            input[j] = tmp;
        }
    }

    /**
     * 希尔排序
     * @param input
     */
    public static void shellSort(int[] input) {
        if(!checkInput(input))
            return;
        int i, j, increment;
        for(increment = input.length >>> 1; increment > 0; increment >>>= 1) {
            for(i = increment; i < input.length; ++i) {
                int tmp = input[i];
                for(j = i; j >= increment && input[j - increment] > tmp; j -= increment)
                    input[j] = input[j - increment];
                input[j] = tmp;
            }
        }
    }

    /**
     * 下滤
     * @param input
     * @param i
     * @param size
     */
    public static void fixDown(int[] input, int i, int size) {
        int temp = input[i];
        int j = (i << 1) + 1;
        while(j < size) {
            if(j + 1 < size && input[j + 1] > input[j])
                j++;
            if(input[j] < temp)
                break;
            input[i] = input[j];
            i = j;
            j = (j << 1) + 1;
        }
        input[i] = temp;
    }

    /**
     * 堆排序
     * @param input
     */
    public static void heapSort(int[] input) {
        if(!checkInput(input))
            return;

        //首先进行堆序化
        for(int i = input.length >> 1; i >= 0; --i) {
            fixDown(input, i, input.length);
        }

        //进行排序
        for(int i = input.length - 1; i > 0; --i) {
            int temp = input[0];
            input[0] = input[i];
            input[i] = temp;
            fixDown(input, 0, i);
        }
    }

    public static void selectSort(int[] input) {
        if(!checkInput(input))
            return;
        for(int i = 0; i < input.length - 1; i++) {
            int min = input[i];
            int index = i;
            for(int j = i + 1; j < input.length; j++) {
                if(input[j] < min) {
                    index = j;
                    min = input[j];
                }
            }
            int temp = input[index];
            input[index] = input[i];
            input[i] = temp;
        }
    }

    /**
     * 归并排序
     *
     * @param input
     */
    public static void mergeSort(int[] input) {
        if(!checkInput(input))
            return;
        int[] temp = new int[input.length];
        mergeSort(input, 0, input.length-1, temp);
    }

    public static void mergeSort(int[] input, int left, int right, int[] temp) {
        if(left < right) {
            int mid = (left + right) >> 1;
            mergeSort(input, left, mid, temp);
            mergeSort(input, mid + 1, right, temp);
            merge(input, left, mid, right, temp);
        }
    }

    public static void merge(int[] input, int left, int mid, int right, int[] temp) {
        int i = left;
        int j = mid + 1;
        int k = left;

        while(i <= mid && j <= right) {
            if(input[i] <= input[j]) {
                temp[k++] = input[i++];
            } else {
                temp[k++] = input[j++];
            }
        }

        while(i <= mid) {
            temp[k++] = input[i++];
        }

        while(j <= right) {
            temp[k++] = input[j++];
        }

        for(int m = left; m <= right; m++) {
            input[m] = temp[m];
        }
    }

    /**
     * 三数中值分割
     */
    public static int median3(int[] input, int left, int right) {
        int center = (left + right) >> 1;

        if(input[left] > input[right])
            swap(input, left, right);
        if(input[left] > center)
            swap(input, left, center);
        if(input[center] > right)
            swap(input, center, right);
        swap(input, center, right - 1);

        return input[right - 1];
    }
    //小于cutOff不使用快排使用插入排序
    private static final int cutOff  = 3;
    public static void quickSort(int[] input) {
        if(!checkInput(input))
            return;
        quickSort(input, 0, input.length - 1);
    }
    public static void quickSort(int[] input, int left, int right) {
        if(left + cutOff <= right) {
            int i = left, j = right - 1;
            int pivot = median3(input, left, right);
            for(;;) {
                while(input[++i] < pivot){}
                while(input[--j] > pivot){}
                if(i < j)
                    swap(input, i, j);
                else
                    break;
            }
            swap(input, i, right - 1);
            quickSort(input, left, i - 1);
            quickSort(input, i + 1, right);
        } else {
            insertionSort(input);
        }
    }

    public static void swap(int[] num, int i, int j) {
        int temp = num[i];
        num[i] = num[j];
        num[j] = temp;
    }

    private static void test(Consumer<int[]> consumer, int[] case0) {
        System.out.println("original array: " + Arrays.toString(case0));
        consumer.accept(case0);
        System.out.println("sorted array: " + Arrays.toString(case0));
    }

    public static void main(String[] args) {
        int[] case1 = null;
        int[] case2 = {};
        int[] case3 = {34,8,64,51,32,21};

        System.out.println("插入排序");
        test(SortUtil::insertionSort, case1);
        test(SortUtil::insertionSort, case2);
        test(SortUtil::insertionSort, Arrays.copyOf(case3, case3.length));

        System.out.println("希尔排序");
        test(SortUtil::shellSort, case1);
        test(SortUtil::shellSort, case2);
        test(SortUtil::shellSort, Arrays.copyOf(case3, case3.length));

        System.out.println("堆排序");
        test(SortUtil::heapSort, case1);
        test(SortUtil::heapSort, case2);
        test(SortUtil::heapSort, Arrays.copyOf(case3, case3.length));

        System.out.println("选择排序");
        test(SortUtil::selectSort, case1);
        test(SortUtil::selectSort, case2);
        test(SortUtil::selectSort, Arrays.copyOf(case3, case3.length));

        System.out.println("归并排序");
        test(SortUtil::mergeSort, case1);
        test(SortUtil::mergeSort, case2);
        test(SortUtil::mergeSort, Arrays.copyOf(case3, case3.length));

        System.out.println("快速排序");
        test(SortUtil::quickSort, case1);
        test(SortUtil::quickSort, case2);
        test(SortUtil::quickSort, Arrays.copyOf(case3, case3.length));

    }

}
