package evernote.mvpcalculator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Game {
    // All the positions are zero-based

    private final Map<String, Integer> playerAttributesToPositionMap = new HashMap<>() {{
        put("Name", 0);
        put("PlayerNumber", 2);
    }};

    private List<Player> players = new ArrayList<>();

    // Position in csv file line of a player ID (nickname)
    public Integer getPlayerIdPosition() {
        return 1;
    }

    public Integer getTeamPosition() {
        return 3;
    }

    // Position in csv file line of a metric that composes final team score
    public Integer getTeamWinConditionMetricPosition() {
        return 4;
    }

    // A map of player's non-essential data to its respective position in csv file
    public Map<String, Integer> getPlayerAttributesToPositionMap() {
        return playerAttributesToPositionMap;
    }

    public Integer getNumOfMetrics() {
        return getPlayerMetricPositionToFunctionMap().keySet().size();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // A map of player metrics' positions in csv file to the functions being applied to them,
    //  to calculate score received for each metric
    // Player's metric could be, for example, number of goals made, goals received, assists etc.
    public abstract Map<Integer, Function<Integer, Integer>> getPlayerMetricPositionToFunctionMap();

    public abstract Integer getNumberOfPlayerData();
}
