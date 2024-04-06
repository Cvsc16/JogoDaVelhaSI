import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game implements Serializable {
    public Player playerX;
    public Player playerO;
    public String[][] board;
    public Player currentPlayer;
    public boolean gameOver = false;
    Player winner = null;
    Player loser = null;
    boolean isDraw = false;
    public transient Timer moveTimer;
    public transient int TimeOut = 30;
    public long turnStartTime;

    public Game(Player p1, Player p2) {
        p1.isPlaying = true;
        p2.isPlaying = true;
        if (p1.mark.equals("X")) {
            playerX = p1;
            playerO = p2;
        } else {
            playerX = p2;
            playerO = p1;
        }
        this.currentPlayer = playerX;
        board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
        moveTimer = new Timer();
        turnStartTime = System.currentTimeMillis();
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    makeRandomMove();
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, TimeOut * 1000);
    }

    public Player getPlayerX(){
        return playerX;
    }

    public Player getPlayerO(){
        return playerO;
    }
    public Boolean gameOver(){
        return gameOver;
    }

    public String[][] getBoard() throws RemoteException {
        return board;
    }

    public Player getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }
    public Player getWinner() throws RemoteException{
        return winner;
    }

    public String checkWin() throws RemoteException {

        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0].equals("X") && board[i][1].equals("X") && board[i][2].equals("X")) {
                winner = playerX;
                loser = playerO;
            }
            if (board[i][0].equals("O") && board[i][1].equals("O") && board[i][2].equals("O")) {
                winner = playerO;
                loser = playerX;
            }
            // Check columns
            if (board[0][i].equals("X") && board[1][i].equals("X") && board[2][i].equals("X")) {
                winner = playerX;
                loser = playerO;
            }
            if (board[0][i].equals("O") && board[1][i].equals("O") && board[2][i].equals("O")) {
                winner = playerO;
                loser = playerX;
            }
        }
        if (board[0][0].equals("X") && board[1][1].equals("X") && board[2][2].equals("X")) {
            winner = playerX;
            loser = playerO;
        }
        if (board[0][2].equals("X") && board[1][1].equals("X") && board[2][0].equals("X")) {
            winner = playerX;
            loser = playerO;
        }
        if (board[0][0].equals("O") && board[1][1].equals("O") && board[2][2].equals("O")) {
            winner = playerO;
            loser = playerX;
        }
        if (board[0][2].equals("O") && board[1][1].equals("O") && board[2][0].equals("O")) {
            winner = playerO;
            loser = playerX;
        }

        boolean isDraw = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    isDraw = false;
                    break;
                }
            }
        }

        if (isDraw && winner==null && loser==null) {
            gameOver = true;
            this.playerX.isPlaying = false;
            this.playerO.isPlaying = false;
            return "Draw";
        }

        if (winner != null) {
            gameOver = true;
            this.playerX.isPlaying = false;
            this.playerO.isPlaying = false;

            return winner.mark;
        }
        return null;
    }

    public void togglePlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    public String makeMove(int row, int col) throws RemoteException {
        
        if (!gameOver && board[row][col].isEmpty()) {
            if (moveTimer != null) {
                moveTimer.cancel();
            }

            this.board[row][col] = this.currentPlayer.mark;
            System.out.println(currentPlayer.username + "("+ currentPlayer.mark +")" + " moveu para (" + row + ", " + col + ")");
            togglePlayer();
            moveTimer = new Timer();
            turnStartTime = System.currentTimeMillis();
            moveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        makeRandomMove();
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, TimeOut * 1000);
            return "movimento de sucesso";
        }

        return "movimento sem sucesso";
    }
    public int getTimerCount() throws RemoteException{
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - turnStartTime;
        long TimeLeft = this.TimeOut*1000 - timeElapsed;
        return  (int) (TimeLeft/1000) ;
    }

    public void makeRandomMove() throws RemoteException {
        Random random = new Random();
        int emptyRow, emptyCol;
        do {
            emptyRow = random.nextInt(3);
            emptyCol = random.nextInt(3);
        } while (!board[emptyRow][emptyCol].isEmpty() && !gameOver);
        makeMove(emptyRow, emptyCol);
        System.out.println(currentPlayer.username + "("+ currentPlayer.mark +")" + " fez uma jogada aleatória para (" + emptyRow + ", " + emptyCol + ")");
    }

    void quiteGame(String username){
        this.playerO.isPlaying = false;
        this.playerX.isPlaying = false;
        gameOver = true;
        if(this.playerX.username.equals(username)){
            
            winner = this.playerO;
            loser =  this.playerX;
        }else{
            winner = this.playerX;
            loser =  this.playerO;
        }
    }


}
