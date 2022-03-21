import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiceGame {
    private final List<Player> players;
    private final List<Die> dice;
    private final int maxRolls;
    private Player currentPlayer;

    public DiceGame(int countPlayers, int countDice, int maxRolls) {

        this.players = new ArrayList<>();
        this.dice = new ArrayList<>();
        this.maxRolls = maxRolls;

        if (countPlayers < 2) {

            throw new IllegalArgumentException();
        } else {

            IntStream.range(0, countPlayers).mapToObj(i -> new Player()).forEach(players::add);
            IntStream.range(0, countDice).mapToObj(i -> new Die(6)).forEach(dice::add);

        }
    }

    private boolean allDiceHeld() {

        return dice.stream().allMatch(Die::isBeingHeld);
    }
    public boolean currentPlayerCanRoll() {

        return currentPlayer.getRollsUsed() < maxRolls;
    }

    public int getCurrentPlayerScore() {

        return currentPlayer.getScore();
    }

    public String getDiceResults() {

        return dice.stream()
                .map(Die::toString)
                .collect(
                        Collectors.joining("\n")
                );
    }

    public String getFinalWinner() {
        List<Integer> wins = players.stream()
                .map(Player::getWins)
                .sorted()
                .toList();


        return players.stream()
                .filter(player -> player.getWins() == wins.get(wins.size() - 1))
                .findFirst().map(Player::toString)
                .orElse(null);
    }

    public boolean nextPlayer() {

        if (getCurrentPlayerNumber() < players.size()) {
            currentPlayer = players.get(getCurrentPlayerNumber());
            return true;
        } else {

            return false;
        }
    }

    public String getGameResults() {
        List<Player> winningPlayers = new ArrayList<>();

        List<Integer> sortedScores = players.stream().map(Player::getScore).sorted(Comparator.reverseOrder()).toList();


        List<Player> losingPlayers = new ArrayList<>();

        int points;
        points = players.stream().mapToInt(Player::getScore).sum();

        players.forEach(player -> {

            if (points > 0) {

                if (player.getScore() == sortedScores.get(0)) {
                    winningPlayers.add(player);
                } else {
                    losingPlayers.add(player);
                }
            } else {

                losingPlayers.add(player);
            }
        });

        winningPlayers.forEach(Player::addWin);
        losingPlayers.forEach(Player::addLoss);

        String collect = players.stream()
                .map(Player::toString)
                .collect(Collectors.joining("\n"));
        return collect;
    }



    public int getCurrentPlayerNumber() {
        return currentPlayer.getPlayerNumber();
    }

    public void playerHold(char num) {
        dice.stream()
                .filter(die -> die.getDieNum() == num)
                .forEach(Die::holdDie);
    }

    public void resetDice() {
        dice.forEach(Die::resetDie);
    }

    public void rollDice() {
        currentPlayer.roll();
        dice.forEach(Die::rollDie);
    }


    public boolean autoHold(int face) {

        boolean res = false;

        for (var die : dice) {
            if (die.getFaceValue() == face) {
                if (!isHoldingDie(die.getFaceValue())) {
                    die.holdDie();
                    res = true;
                    break;
                }
                res = true;
                break;
            }
        }

        return res;
    }

    private boolean isHoldingDie(int face) {

        List<Die> heldDie = dice.stream().filter(Die::isBeingHeld).toList();
        boolean res;
        for (var die : heldDie) {
            res = die.getFaceValue() == face;
            if (res) {
                return true;
            }
            break;
        }

        return false;
    }
    public void scoreCurrentPlayer() {
        List<Die> heldDie = dice.stream()
                                .filter(Die::isBeingHeld)
                                .toList();
        boolean capt = false;
        boolean crew = false;

        boolean ship = false;

        for (var die : heldDie) {
            ship = die.getFaceValue() == 6;
            if (ship) {
                break;
            }
        }
        for (var die : heldDie) {
            capt = die.getFaceValue() == 5;
            if (capt) {
                break;
            }
        }
        for (var die : heldDie) {
            crew = die.getFaceValue() == 4;
            if (crew) {
                break;
            }
        }

        if (ship && capt && crew) {
            int score = heldDie.stream().mapToInt(Die::getFaceValue).sum() - (6 + 5 + 4);
            currentPlayer.setScore(currentPlayer.getScore() + score);
        }
    }

    public void startNewGame() {
        currentPlayer = players.get(0);
        resetPlayers();
    }

    public void resetPlayers() {
        players.forEach(Player::resetPlayer);
    }
}
