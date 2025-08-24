package com.example.scripttomcpserver.solution;

import java.util.*;
import java.io.*;

public class MinOperationsForIsomorphic {
    /**
     * 主方法，读取输入并输出结果
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        while (t-- > 0) {
            int n = scanner.nextInt();
            long[] A = new long[n];
            long[] B = new long[n];

            // 读取数组A
            for (int i = 0; i < n; i++) {
                A[i] = scanner.nextLong();
            }

            // 读取数组B
            for (int i = 0; i < n; i++) {
                B[i] = scanner.nextLong();
            }

            System.out.println(minOperations(A, B, n));
        }
        scanner.close();
    }

    /**
     * 计算使A和B同构所需的最少操作次数
     * @param A 数组A
     * @param B 数组B
     * @param n 数组长度
     * @return 最少操作次数
     */
    private static int minOperations(long[] A, long[] B, int n) {
        // 预处理A和B中每个元素的变换路径
        List<Map<Long, Integer>> aTransforms = new ArrayList<>();
        List<Map<Long, Integer>> bTransforms = new ArrayList<>();

        for (long num : A) {
            aTransforms.add(getTransformations(num));
        }
        for (long num : B) {
            bTransforms.add(getTransformations(num));
        }

        // 收集所有可能的变换值
        Set<Long> allValues = new HashSet<>();
        for (Map<Long, Integer> map : aTransforms) {
            allValues.addAll(map.keySet());
        }
        for (Map<Long, Integer> map : bTransforms) {
            allValues.addAll(map.keySet());
        }

        int minTotal = Integer.MAX_VALUE;

        // 对于每个可能的变换值，计算将A和B中的元素变换到该值的最小总代价
        for (long value : allValues) {
            List<Integer> aCosts = new ArrayList<>();
            List<Integer> bCosts = new ArrayList<>();

            // 计算A中每个元素变换到value的最小代价
            boolean valid = true;
            for (Map<Long, Integer> map : aTransforms) {
                if (map.containsKey(value)) {
                    aCosts.add(map.get(value));
                } else {
                    valid = false;
                    break;
                }
            }

            if (!valid) continue;

            // 计算B中每个元素变换到value的最小代价
            valid = true;
            for (Map<Long, Integer> map : bTransforms) {
                if (map.containsKey(value)) {
                    bCosts.add(map.get(value));
                } else {
                    valid = false;
                    break;
                }
            }

            if (!valid) continue;

            // 对代价进行排序，以便贪心匹配
            Collections.sort(aCosts);
            Collections.sort(bCosts);

            // 计算总代价
            int total = 0;
            for (int i = 0; i < n; i++) {
                total += aCosts.get(i) + bCosts.get(i);
            }

            if (total < minTotal) {
                minTotal = total;
            }
        }

        return minTotal;
    }

    /**
     * 计算一个数的所有变换值及对应的操作次数
     * @param x 初始数
     * @return 变换值到操作次数的映射
     */
    private static Map<Long, Integer> getTransformations(long x) {
        Map<Long, Integer> transformations = new HashMap<>();
        int steps = 0;
        while (true) {
            // 如果已经记录过这个值，就不再继续，因为已经找到了更小的steps
            if (transformations.containsKey(x)) {
                break;
            }
            transformations.put(x, steps);
            if (x == 1) {
                break;
            }
            x = Long.bitCount(x);
            steps++;
        }
        return transformations;
    }
}