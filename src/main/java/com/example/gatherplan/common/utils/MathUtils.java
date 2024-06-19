package com.example.gatherplan.common.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class MathUtils {
    public List<Set<String>> combinations(List<String> elements) {
        List<Set<String>> result = new ArrayList<>();
        boolean[] visited = new boolean[elements.size()];

        for (int r = 2; r <= elements.size(); r++) {
            recursionCombinations(elements, visited, 0, r, result);
        }

        Collections.reverse(result);

        return result;
    }


    private void recursionCombinations(List<String> elements, boolean[] visited, int depth, int r, List<Set<String>> result) {
        if (r == 0) {
            Set<String> temp = new HashSet<>();
            for (int i = 0; i < elements.size(); i++) {
                if (visited[i]) {
                    temp.add(elements.get(i));
                }
            }
            result.add(temp);
            return;
        }

        if (depth != elements.size()) {
            visited[depth] = true;
            recursionCombinations(elements, visited, depth + 1, r - 1, result);

            visited[depth] = false;
            recursionCombinations(elements, visited, depth + 1, r, result);
        }
    }
}
