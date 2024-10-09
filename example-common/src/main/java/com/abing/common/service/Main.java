package com.abing.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 19:27
 * @Description
 */
public class Main {

    static List<String> resList = new ArrayList<>();

    static double[] result = new double[4];

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("请输入L:");
        double L = input.nextDouble();
        System.out.println("请输入@:");
        double a = input.nextDouble();

        double L2 = L / (2 * a + 1);
        double L1 = L2 * a;
        double L3 = L2 * a;

        double n0 = L / 2;

        double n1 = 0D;
        double n2 = L1;
        double n3 = L1 + L2;
        double n4 = L3;

//        addResult(n1,n2,n3,n4);

        // 可自定义设置
        double[] distance = {1,2,3,4,5,6,7,8,9,10};

        double rightFixDistance = 5;
        double leftFixDistance = 1;

        boolean firstInit = true;
        // 新的两点之间的距离
        double tempDistance = 0;
        // n2往右走的距离
        double rightDistance = 0;
        // n0往左走的距离
        double leftDistance = 0;
        // 下一次需要减去的距离索引
        int index = 0;
        int tempIndex = 1;

        do {
            if (firstInit){
                // 第一次n2 向右走
                rightDistance = n2 + rightFixDistance;
                // 第一次n0 向左走
                leftDistance = n0 - leftFixDistance;

                tempDistance = leftDistance - rightDistance;
                firstInit = false;
                continue;
            }
            rightDistance += distance[index];
            tempDistance = leftDistance - rightDistance;

            if (tempDistance > distance[index] && isInitResultValue()){
                result[0] = n2 - distance[index];
                result[1] = rightDistance;
                result[2] = n3 - distance[index];
                result[3] = n3 + distance[index];
                resList.add(buildString(result,tempIndex));
                tempIndex++;
            }else {
                result[0] -= distance[index];
                result[1] = rightDistance;
                result[2] -= distance[index];
                result[3] += distance[index];
                resList.add(buildString(result,tempIndex));
                tempIndex++;
            }

            if (index < distance.length - 1){
                index++;
            }

        }while (tempDistance > distance[index]);

        System.out.println(resList);
    }

    /**
     * 判断是否是第一次添加值
     */
    public static boolean isInitResultValue(){
        return Arrays.stream(result).anyMatch(value->value == 0);
    }

    /**
     * 添加 n1 到 n4
     */
    public static String buildString(double[] arr,int index){
        String n1 = "[n1" + index + " = " + arr[0] + ",";
        String n2 = "n2" + index + " = " + arr[1] + ",";
        String n3 = "n3" + index + " = " + arr[2] + ",";
        String n4 = "n4" + index + " = " + arr[3] + "]";
        return n1 + n2 + n3 + n4;
    }

}
