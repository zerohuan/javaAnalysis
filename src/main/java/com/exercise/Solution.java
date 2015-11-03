package com.exercise;

/**
 * Created by yjh on 15-11-3.
 */
public class Solution {
    public String PrintMinNumber(int [] numbers) {
        if(numbers == null && numbers.length == 0)
            return "";
        String[] strs = new String[numbers.length];
        for(int i = 0; i < numbers.length; i++)
            strs[i] = String.valueOf(numbers[i]);
        quickSort(strs, 0, strs.length - 1);
        StringBuilder sb = new StringBuilder();
        for(String s : strs) {
            sb.append(s);
        }
        return sb.toString();
    }

    //快排
    public void quickSort(String[] input, int l, int h) {
        if(l < h) {
            int m = partition(input, l, h);
            quickSort(input, l, m - 1);
            quickSort(input, m + 1, h);
        }
    }

    public int partition(String[] input, int l, int h) {
        String x = input[l];
        int i = l, j = h;
        while(i < j) {
            while(i < j && compare(input[j], x) > 0) {
                    --j;
            }
            if(i < j) {
                input[i] = input[j];
                ++i;
            }
            while(i < j && compare(input[i], x) < 0) {
                    ++i;
            }
            if(i < j) {
                input[j] = input[i];
                --j;
            }
        }
        input[i] = x;
        return i;
    }

    public int compare(String str1, String str2) {
        int i = 0, j = 0;
        while(i < str1.length() || j < str2.length()) {
            if(str1.charAt(i) > str2.charAt(j)) {
                return 1;
            } else if(str1.charAt(i) == str2.charAt(j)) {
                if(i < str1.length() - 1 && j < str2.length() - 1) {
                    i++;j++;
                } else if(i < str1.length() - 1) {
                    i++;
                    j = 0;
                } else if(j < str2.length() - 1) {
                    j++;
                    i = 0;
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        }
        return 0;
    }

    public int FirstNotRepeatingChar(String str) {
        if(str == null || str.length() == 0)
            return -1;

        int[] counts = new int[54];
        int[] sites = new int[54];
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int val;
            if('a' <= c && c <= 'z') {
                val = c - 93 + 26;
            } else {
                val = c - 65;
            }
            if(counts[val] == 0)
                sites[val] = i;
            counts[val]++;
        }
        int firstSite = str.length();
        for(int i = 0; i < counts.length; i++) {
            if(counts[i] == 1)
                if(sites[i] < firstSite)
                    firstSite = sites[i];
        }
        return firstSite;
    }

    public static void main(String[] args) {
        int[] nums = {545, 54};
        Solution solution = new Solution();
        System.out.println(solution.FirstNotRepeatingChar("google"));

    }
}
