package com.example.gatherplan;

import com.example.gatherplan.common.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Testtest {
    public static void main(String[] args) {

        List<String> participants = List.of("박승일", "이재훈", "박정빈", "이채원");
        List<Set<String>> combinations = MathUtils.combinations(participants);

        System.out.println(combinations);

        List<Set<String>> list = new ArrayList<>();
        String[] arr = {"1", "2", "3", "4", "5", "6", "7"}; //조합을 만들 배열
        boolean[] visited = new boolean[arr.length];
        //2. 재귀를 이용해 구현
        System.out.println("\n---------- 2. 재귀 ----------");

        for (int r = 2; r <= arr.length; r++) {
            comb2(arr, visited, 0, r, list);
        }

        System.out.println("list = " + list);
    }

    //2. 재귀를 이용해 구현
    static void comb2(String[] arr, boolean[] visited, int depth, int r, List<Set<String>> list) {
        if (r == 0) {
            Set<String> temp = new HashSet<>();
            for (int i = 0; i < arr.length; i++) {
                if (visited[i]) {
                    temp.add(arr[i]);
                }
            }
            list.add(temp);
            return;
        }

        if (depth != arr.length) {
            visited[depth] = true;
            comb2(arr, visited, depth + 1, r - 1, list);

            visited[depth] = false;
            comb2(arr, visited, depth + 1, r, list);
        }
    }
}
