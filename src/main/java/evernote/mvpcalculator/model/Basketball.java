package evernote.mvpcalculator.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Basketball extends Game {

    private final Map<Integer, Function<Integer, Integer>> playerMetricPositionToFunctionMap = new HashMap<>() {{
        put(4, score -> score * 2);
        put(5, score -> score * 1);
        put(6, score -> score * 1);
    }};

    @Override
    public Map<Integer, Function<Integer, Integer>> getPlayerMetricPositionToFunctionMap() {
        return playerMetricPositionToFunctionMap;
    }

    @Override
    public Integer getNumberOfPlayerData() {
        return 7;
    }
}
