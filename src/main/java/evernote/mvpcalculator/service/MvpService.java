package evernote.mvpcalculator.service;

import com.opencsv.exceptions.CsvException;
import evernote.mvpcalculator.exception.EmptyFileException;
import evernote.mvpcalculator.exception.WrongNumberOfPlayerDataException;
import evernote.mvpcalculator.exception.NoPlayerInfoException;
import evernote.mvpcalculator.exception.WrongPlayerDataException;
import evernote.mvpcalculator.model.Game;
import evernote.mvpcalculator.model.Player;
import evernote.mvpcalculator.utils.CsvReader;
import evernote.mvpcalculator.utils.DynamicGameSupplierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

@Service
public class MvpService {

    @Autowired
    private CsvReader reader;

    @Value("${game.header.lineNumber}")
    private Integer headerLineNumber;

    @Value("${game.header.positionInLine}")
    private Integer headerPositionInLine;

    @Value("${game.winningTeam.reward}")
    private Integer winningTeamReward;

    // Field might be useful for future development
    private List<Game> games;

    public String calculateMvp(List<MultipartFile> csvFiles) throws IOException, CsvException {
        List<List<String[]>> gamesInfos = reader.readList(csvFiles);
        games = new ArrayList<>();
        for (List<String[]> gameInfo : gamesInfos) {
            if (gameInfo.isEmpty()) {
                throw new EmptyFileException();
            }
            games.add(processGameInfo(gameInfo));
        }
        return mergePlayerStatistics(games);
    }

    private Game processGameInfo(List<String[]> gameInfo) {
        Game game = createGame(gameInfo);

        List<Player> players = new ArrayList<>();
        // Map of team names to its scores
        Map<String, Integer> teamToScoreMap = new HashMap<>();

        if (gameInfo.size() < 2) {
            throw new NoPlayerInfoException();
        }

        // Loop through each gameInfo line, except header
        for (int i = headerLineNumber + 1; i < gameInfo.size(); i++) {
            String[] playerInfo = gameInfo.get(i);
            if (playerInfo.length != game.getNumberOfPlayerData()) {
                throw new WrongNumberOfPlayerDataException();
            }
            if (Arrays.stream(playerInfo).anyMatch(data -> data.isEmpty() || data.isBlank())) {
                throw new WrongPlayerDataException();
            }

            Player player;
            try {
                player = createPlayer(game, playerInfo);
            }
            catch (NumberFormatException exception) {
                throw new WrongPlayerDataException();
            }

            calculateTeamScores(game, playerInfo, teamToScoreMap);

            players.add(player);
        }
        game.setPlayers(players);

        String winningTeam = Collections.max(teamToScoreMap.entrySet(), Entry.comparingByValue()).getKey();
        rewardWinningTeam(game.getPlayers(), winningTeam);

        return game;
    }

    private Game createGame(List<String[]> gameInfo) {
        String header = gameInfo.get(headerLineNumber)[headerPositionInLine];
        return DynamicGameSupplierFactory.getGame(header);
    }

    private Player createPlayer(Game game, String[] playerInfo) {
        Map<String, Object> playerAttributes = new HashMap<>();
        game.getPlayerAttributesToPositionMap().forEach((attributeName, attributePosition) ->
                playerAttributes.put(attributeName, playerInfo[attributePosition]));

        List<Integer> playerMetrics = new ArrayList<>();
        game.getPlayerMetricPositionToFunctionMap().keySet().forEach(key -> {
            playerMetrics.add(Integer.valueOf(playerInfo[key]));
        });

        // Map of player metrics' positions in csv file to the functions being applied to them,
        //  to calculate score received for each metric
        Map<Integer, Function<Integer, Integer>> playerMetricPositionToFunctionMap = game.getPlayerMetricPositionToFunctionMap();
        int playerScore = calculatePlayerPersonalScore(playerMetricPositionToFunctionMap, playerInfo);

        return Player.builder()
                .id(playerInfo[game.getPlayerIdPosition()])
                .team(playerInfo[game.getTeamPosition()])
                .attributes(playerAttributes)
                .metrics(playerMetrics)
                .score(playerScore)
                .build();
    }

    private int calculatePlayerPersonalScore(Map<Integer, Function<Integer, Integer>> playerMetricPositionToFunctionMap, String[] playerInfo) {
        int score = 0;
        for (Entry<Integer, Function<Integer, Integer>> entry : playerMetricPositionToFunctionMap.entrySet()) {
            Integer metricPosition = entry.getKey();
            Function<Integer, Integer> functionToBeAppliedToMetric = entry.getValue();
            score += functionToBeAppliedToMetric.apply(Integer.parseInt(playerInfo[metricPosition]));
        }
        return score;
    }

    private void calculateTeamScores(Game game, String[] playerInfo, Map<String, Integer> teamToScoreMap) {
        String playerTeam = playerInfo[game.getTeamPosition()];

        // Position of winner-team-deciding metric in the file
        Integer teamWinConditionMetricPosition = game.getTeamWinConditionMetricPosition();
        // Player's contribution to win
        Integer playerScoreForTeam = Integer.valueOf(playerInfo[teamWinConditionMetricPosition]);

        // Expect current player's team is not yet registered in a map and has zero score
        Integer playerTeamScore = 0;
        // If current player's team is already registered in a map
        if (teamToScoreMap.containsKey(playerTeam)) {
            // Then get it's current score
            playerTeamScore = teamToScoreMap.get(playerTeam);
        }
        // Append player's contribution to team score
        teamToScoreMap.put(playerTeam, playerTeamScore + playerScoreForTeam);
    }

    // Add bonus score points to players of winning team
    private void rewardWinningTeam(List<Player> players, String winningTeam) {
        players.forEach(player -> {
            if (player.getTeam().equals(winningTeam)) {
                player.setScore(player.getScore() + winningTeamReward);
            }
        });
    }

    // Sum up player's scores over all games played
    private String mergePlayerStatistics(List<Game> games) {
        Map<String, Integer> finalPlayerScores = new HashMap<>();
        games.forEach(game -> {
            game.getPlayers().forEach(player -> {
                Integer score = 0;
                String playerId = player.getId();
                if (finalPlayerScores.containsKey(playerId)) {
                    score = finalPlayerScores.get(playerId);
                }
                finalPlayerScores.put(playerId, score + player.getScore());
            });
        });
        return Collections.max(finalPlayerScores.entrySet(), Entry.comparingByValue()).getKey();
    }
}
