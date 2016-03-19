package com.exercise.bop;

/**
 * ���ֲ��ҵ��������
 * Created by yjh on 2016/3/13.
 */
public class Ex_3_11_BinarySearch {
    //����һ��iʹ��arr[i]����v��
    public static int binarySearch1(int[] arr, int v) {
        if (arr == null || arr.length == 0)
            return -1;
        int l = 0, r = arr.length - 1;
        while (l < r) {
            int mid = l + (r - l) >> 1;
            if (arr[mid] == v)
                return mid;
            else if (arr[mid] < v)
                l = mid + 1;
            else
                r = mid - 1;
        }
        return -1;
    }

    //��Сiʹ��arr[i]����v
    public static int binarySearch2(int[] arr, int v) {
        if (arr != null && arr.length > 0) {
            int l = 0, r = arr.length - 1;
            while (l < r) {
                int mid = l + (r - l) >> 1;
                if (arr[mid] >= v)
                    r = mid;
                else
                    l = mid + 1;
            }
            if (arr[l] == v)
                return l;
        }
        return -1;
    }

    //���iʹ��arr[i]����v
    public static int binarySearch3(int[] arr, int v) {
        if (arr != null && arr.length > 0) {
            int l = 0, r = arr.length - 1;
            while (l < r - 1) {
                int mid = l + (r - l) >> 1;
                if (arr[mid] <= v)
                    l = mid;
                else
                    r = mid;
            }
            if (arr[r] == v)
                return r;
            else if (arr[l] == v)
                return l;
        }
        return -1;
    }

    //���iʹ��arr[i]С��v
    public static int binarySearch4(int[] arr, int v) {
        if (arr != null && arr.length > 0) {
            int l = 0, r = arr.length - 1;
            while (l < r - 1) {
                int mid = l + (r - l) >> 1;
                if (arr[mid] >= v)
                    r = mid - 1;
                else
                    l = mid;
            }
            if (arr[r] < v)
                return r;
            else if (arr[l] <  v)
                return l;
        }
        return -1;
    }

    //��Сiʹ��arr[i]����v
    public static int binarySearch5(int[] arr, int v) {
        if (arr != null && arr.length > 0) {
            int l = 0, r = arr.length - 1;
            while (l < r) {
                int mid = l + (r - l) >> 1;
                if (arr[mid] <= v)
                    l = mid + 1;
                else
                    r = mid;
            }
            if (arr[l] > v)
                return l;
        }
        return -1;
    }
}
