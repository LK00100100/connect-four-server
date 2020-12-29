package com.lk00100100.connectfourserver;

import com.lk00100100.connectfourserver.data.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance of one game of Connect 4.
 * Every single game is played only once.
 * It has one unique game id.
 * Not concurrency safe.
 */
public class GameInstance {

    private int[][] board; //0 = empty. else, a player.

    private final int numRows = 6;
    private final int numCols = 7;

    public static final int MAX_PLAYERS = 2;
    private int currentPlayer;  //todo: flip after placing piece successfully

    private GameState gameState;

    //0th index is the 1st player id, 1st index is the 2nd player id;
    private List<String> players;

    public GameInstance(String gameId) {
        initBoard();

        players = new ArrayList<>();

        this.currentPlayer = 1;

        this.gameState = GameState.WAIT;
    }

    //note lkeh: these getters are so the rest api can get values and send this object.

    public int[][] getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public List<String> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void initBoard() {
        this.board = new int[this.numRows][this.numCols];

        for (int r = 0; r < this.numRows; r++) {
            for (int c = 0; c < this.numCols; c++) {
                this.board[r][c] = 0;
            }
        }
    }

    /**
     * A User attempts to sit down in the game.
     * If you are already seated, you are given the same Player number.
     * If anything is invalid, you get -1.
     * 1st player is player 1. 2nd player is player 2.
     *
     * @return the player number assigned. -1 if invalid.
     */
    public int grabSeat(String userId) {

        //full game
        if (players.size() == 2)
            return -1;

        //existence check
        for (int i = 0; i < players.size(); i++) {
            if (userId.equals(players.get(i))) {
                return i;
            }
        }

        //sit down
        players.add(userId);
        return players.size();
    }

    /**
     * Attempts to make a move if valid. It has to be playerNum's turn.
     *
     * @return The row of the piece played. Otherwise -1;
     */
    public int makeMove(int playerNum, int col) {
        //not your turn!
        if (currentPlayer != playerNum)
            return -1;

        int rowPlaced = placePiece(playerNum, col);

        if (rowPlaced == -1)
            return -1;

        //flip players
        if (this.currentPlayer == 1)
            this.currentPlayer = 2;
        else
            this.currentPlayer = 1;

        return rowPlaced;
    }

    /**
     * Drops a piece in the column.
     * Affects the data only.
     * Returns -1 on bad move
     *
     * @param playerNum The Player who is placing.
     * @param col       Target column to place piece
     * @return The row of the piece played. Otherwise -1;
     */
    private int placePiece(int playerNum, int col) {

        boolean pieceWasPlaced = false;
        int row = 0;

        if (this.board[row][col] != 0)
            return -1;

        //check each spot to see if there's something below it.
        for (; row < this.numRows; row++) {
            if (row == this.numRows - 1) {
                this.board[row][col] = playerNum;
                pieceWasPlaced = true;
                break;
            }

            //is there anything below?
            if (this.board[row + 1][col] != 0) {
                this.board[row][col] = playerNum;
                pieceWasPlaced = true;
                break;
            }
        }

        if (pieceWasPlaced)
            return row;
        else
            return -1;
    }

    /**
     * Quickly check if the newly placed piece created a victory.
     *
     * @param row the newly placed piece
     * @param col the newly placed piece
     */
    public boolean checkVictoryQuick(int row, int col) {
        int player = this.board[row][col];

        if (this.checkHorizontalQuick(row, col - 3, player)) //to left
            return true;

        if (this.checkHorizontalQuick(row, col, player)) //to right
            return true;

        if (this.checkVerticalQuick(row, col, player)) //to down
            return true;

        if (this.checkDiagonalSEQuick(row, col, player))
            return true;

        if (this.checkDiagonalSWQuick(row, col, player))
            return true;

        if (this.checkDiagonalSEQuick(row - 3, col - 3, player))
            return true;

        return this.checkDiagonalSWQuick(row - 3, col + 3, player);
    }

    /**
     * Checks four horizontal from left to right
     *
     * @param row    -
     * @param col    -
     * @param player -
     */
    private boolean checkHorizontalQuick(int row, int col, int player) {
        for (int i = 0; i < 4; i++) {
            if (this.isOutOfBounds(row, col + i))
                return false;

            if (this.board[row][col + i] != player)
                return false;
        }

        return true;
    }

    /**
     * Checks four vertical from top to bottom
     *
     * @param row    -
     * @param col    -
     * @param player -
     * @return -
     */
    private boolean checkVerticalQuick(int row, int col, int player) {
        for (int i = 0; i < 4; i++) {
            if (this.isOutOfBounds(row + i, col))
                return false;

            if (this.board[row + i][col] != player)
                return false;
        }

        return true;
    }

    /**
     * Checks four diagonal towards south-east
     *
     * @param row    -
     * @param col    -
     * @param player -
     * @return -
     */
    private boolean checkDiagonalSEQuick(int row, int col, int player) {
        for (int i = 0; i < 4; i++) {
            if (this.isOutOfBounds(row + i, col + i))
                return false;

            if (this.board[row + i][col + i] != player)
                return false;
        }

        return true;
    }

    /**
     * Checks four diagonal towards south-west
     *
     * @param row    -
     * @param col    -
     * @param player -
     * @return -
     */
    private boolean checkDiagonalSWQuick(int row, int col, int player) {
        for (int i = 0; i < 4; i++) {
            if (this.isOutOfBounds(row + i, col - i))
                return false;

            if (this.board[row + i][col - i] != player)
                return false;
        }

        return true;
    }

    /**
     * @param row -
     * @param col -
     * @return true if out of bounds.
     */
    private boolean isOutOfBounds(int row, int col) {
        if (row < 0 || row >= this.board.length)
            return true;

        return col < 0 || col >= this.board[0].length;
    }

    /**
     * This just checks the top row to see if the board is full.
     * Note lkeh: Could rewrite this to just keep the count of pieces placed
     * over time but i am lazy.
     *
     * @return true if board is full.
     */
    public boolean isBoardFull() {
        for (int col = 0; col < this.numCols; col++) {
            if (this.board[0][col] == 0)
                return false;
        }

        return true;
    }
}
