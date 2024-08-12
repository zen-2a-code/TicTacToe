public class Player {
    private String name;
    private boolean playerTurn;
    private String playersChar;

    /**
     * @param name        String The name of the player
     * @param playersChar String players char
     * @param firstPlayer boolean enter true if this is the first player, otherwise is false
     */
    public Player(String name, String playersChar, boolean firstPlayer) {
        this.name = name;
        this.playersChar = playersChar;
        this.playerTurn = firstPlayer;
    }

    public String getPlayersName() {
        return this.name;
    }

    public String getPlayersSymbol() {
        return this.playersChar;
    }

    public boolean getTurnStatus() {
        return this.playerTurn;
    }

    private void toggleOnPlayerTurn() {
        this.playerTurn = true;
    }

    private void toggleOffPlayerTurn() {
        this.playerTurn = false;
    }

    /**
     * This method toggles ON the inactive player playerTurn status and toggles OFF
     * the current active player playerTurn status.
     *
     * @param player1 Player object
     * @param player2 Player object
     */
    public static void switchPlayerTurn(Player player1, Player player2) {
        if (player1.playerTurn) {
            player1.playerTurn = false;
            player2.playerTurn = true;
        } else {
            player1.playerTurn = true;
            player2.playerTurn = false;
        }
    }

    public static boolean arePlayerCharactersMatching(Player player1, Player player2) {
        return player1.playersChar.equals(player2.playersChar);
    }

    public static boolean arePlayerNamesMatching(Player player1, Player player2) {
        return player1.getPlayersName().equals(player2.getPlayersName());
    }
}
