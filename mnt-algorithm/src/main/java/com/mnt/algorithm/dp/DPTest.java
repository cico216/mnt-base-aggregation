package com.mnt.algorithm.dp;

/**
 * 动态规划测试
 *
 * @author 姜彪
 * @date 2021/6/2
 */
public class DPTest {



    /****        DP 硬币找零          ****/
    int getMinCounts(int k, int[] values) {
        int[] memo = new int[k + 1]; // 创建备忘录
        memo[0] = 0; // 初始化状态
        for (int i = 1; i < k + 1; i++) { memo[i] = k + 1; }

        for (int i = 1; i < k + 1; i++) {
            for (int coin : values) {
                if (i - coin < 0) { continue; }
                memo[i] = Math.min(memo[i], memo[i - coin] + 1); // 作出决策
                System.err.println(memo[i]);
            }
        }

        return memo[k] == k + 1 ? -1 : memo[k];
    }

    int getMinCountsDPSolAdvance() {
        int[] values = { 3, 5 }; // 硬币面值
        int total = 22; // 总值

        return getMinCounts(total, values); // 输出答案
    }



    /****        DP 最大连续子数组          ****/

    int getMaxSumContinuityArray(int [] values) {
        if(values == null) {throw new IllegalArgumentException("can't parse null");}
        if(values.length == 0) {return 0;}


        int dp = values[0];
        int max = dp;

        int startIndex = 0;
        int endIndex = 0;

        for (int i = 1; i < values.length ; i++) {

            if(values[i] + dp < values[i]) {
//                System.err.println(" start " + i);
                startIndex = i;
            }

            dp = Math.max(values[i] + dp, values[i]);

            if(dp >= max) {
                endIndex = i;
            }

            max = Math.max(max, dp);

        }
        System.err.println(startIndex + " -- " + endIndex);
        return max;
    }

    int getMaxSumContinuityArrayResult() {
        int [] values = {-2, 1, -3, 1, 6, -1, 2, -5, 4};
        return getMaxSumContinuityArray(values);
    }




    /****        0 - 1 背包问题          ****/

    /**
     * 获取背包可以装入的最大价值
     * @param w 物品的质量数组
     * @param v 物品的价值数组
     * @param N 物品的数量
     * @param W 背包的容量
     * @return 背包内可以装入的最大价值
     */
    int getMaxSilve(int[] w, int[] v, int N, int W) {
        // 创建备忘录
        int[][] dp = new int[N+1][W+1];
        // 初始化状态
        for (int i = 0; i < N + 1; i++) { dp[i][0] = 0; }
        for (int j = 0; j < W + 1; j++) { dp[0][j] = 0; }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < W + 1; j++) {
                if( j < w[i]){
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                }
            }
        }
        return dp[N][W];
    }

    /**
     * 给你一个可放总重量为 W 的背包和 N 个物品，
     * 对每个物品，有重量 w 和价值 v 两个属性，
     * 那么第 i 个物品的重量为 w[i]，价值为 v[i]。
     * 现在让你用这个背包装物品，问最多能装的价值是多少？
     * @return 获取最大价值
     */
    int silveDP() {
        int N = 3, W = 5;
        // 物品的总数，背包能容纳的总重量
        int[] w = {0, 3, 2, 1};
        // 物品的重量
        int[] v = {0, 5, 2, 3};
        // 物品的价值
        return getMaxSilve(w, v, N, W); // 输出答案
    }



    public static void main(String[] args) {
        DPTest dpTest = new DPTest();

//        System.err.println(dpTest.getMinCountsDPSolAdvance());
//        System.err.println(dpTest.getMaxSumContinuityArrayResult());
        System.err.println(dpTest.silveDP());


    }

}
