import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameImpl extends UnicastRemoteObject implements GameInterface {

    private List<Player> allPlayers = new ArrayList<Player>();
    private List<Game> allGames = new ArrayList<Game>();
    private List<Player> MatchPairs = new ArrayList<Player>();

    public GameImpl() throws RemoteException {
        
    }

    public Player joinGame(String username) throws RemoteException {
        Boolean playerExisting = false;
        Player player = null;

        for (Player p : allPlayers) {
            if (p.username.equals(username)){
                playerExisting = true;
                player = p;
                p.active = true;
                p.isPlaying = false;
                break; 
            }
        }
        
        if(player == null){
            player = new Player(username, null);
            player.username = username;
            player.active = true;
            player.isPlaying = false;
            allPlayers.add(player);
        }

        MatchPairs.add(player);

        System.out.println("Jogadores:");
        for (Player p : MatchPairs) {
            System.out.println("Nome: " + p.username + " | Ativo: " + (p.active ? "Sim" : "Não") + " | Marca: " + p.mark);
        }

        System.out.println("Total de Jogadores: " + MatchPairs.size());
        if(MatchPairs.size() == 2){
            player.mark = "O";
            Game game = new Game(MatchPairs.get(0),MatchPairs.get(1));
            allGames.add(game);
            MatchPairs.clear();
        }else{
            player.mark = "X";
        }

        return player;
    }


    public Game getGameByUsername(String username){
        Game game = null;
        for(Game g: allGames){
            if( (g.getPlayerX().username.equals(username) || g.getPlayerO().username.equals(username))){
                game = g;
            }
        }
        return game;
    }

    public Player getWinner(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.getWinner();
    }

    public Boolean gameOver(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.gameOver();
    }

    public Player getCurrentPlayer(String username) throws RemoteException {
        Game g = getGameByUsername(username);
        try {
            return g.getCurrentPlayer();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void playerLeave(String username){
        for(Player p: allPlayers){
            if(p.username.equals(username)){
                p.active = false;
                System.out.print("O jogador ");
                System.out.print(p.username + " existe e está ativo: " + p.active);
                System.out.println(" ");
            }
        }
        
    }

    public String[][] getBoard(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.getBoard();
    }

    public String checkWin(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.checkWin();
    }

    public String makeMove(String username,int row, int col) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.makeMove(row, col);
    }

    public void removeGame(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        if(g != null){
            for(Player p: allPlayers){
                if(g.getWinner()!=null){
                    if(p.username.equals(g.getWinner().username)){
                        p.points += 5;
                    }
                    if(p.username.equals(g.loser.username)){
                        p.points -= 5;
                    }
                }
            }
            allGames.remove(g);
        }
    }

    public void quiteGame(String username) throws RemoteException{
        // 
        Game g = getGameByUsername(username);
        g.quiteGame(username);
    }
    public int getTimerCount(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.getTimerCount();
    }

    public boolean getIsdraw(String username) throws RemoteException{
        Game g = getGameByUsername(username);
        return g.isDraw;
    }
}

