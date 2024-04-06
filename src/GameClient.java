import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.net.ConnectException;
import java.util.List;

public class GameClient {
    public static void main(String[] args) {
        String username;
        String server_ip;
        int server_port;

        if (args.length == 3) {
            username = args[0];
            server_ip = args[1];
            server_port = Integer.parseInt(args[2]);
        } else {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Nome do Jogador:"));
            JTextField playerField = new JTextField();
            panel.add(playerField);
            panel.add(new JLabel("Endereço IP do Servidor:"));
            JTextField ipField = new JTextField();
            panel.add(ipField);
            panel.add(new JLabel("Porta do Servidor:"));
            JTextField portField = new JTextField();
            panel.add(portField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Digite as informações do jogador e do servidor", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                System.out.println("Operação cancelada pelo usuário.");
                System.exit(0);
            }
            username = playerField.getText();
            server_ip = ipField.getText();
            server_port = Integer.parseInt(portField.getText());
        }

        try {
            Registry registry = LocateRegistry.getRegistry(server_ip, server_port);
            GameInterface gameImpl = (GameInterface) registry.lookup("GameServer");

            SwingUtilities.invokeLater(() -> {
                try {
                    new Client(gameImpl, username);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        } catch (ConnectIOException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Client extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private GameInterface gameImpl;
    private Game game;
    private Player player;


    public Client(GameInterface gameImpl,String username) throws RemoteException {
        this.gameImpl = gameImpl;
        this.player = gameImpl.joinGame(username);

        setTitle("Jogador: " + player.username + "(" + player.mark + ")");
        
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel currentlyPlayerLabel = new JLabel("Procurando Jogador");
        currentlyPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(currentlyPlayerLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        panel.add(buttonPanel, BorderLayout.CENTER);

        JLabel resultPlayerLabel = new JLabel("Jogador Vencedor:");
        resultPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(resultPlayerLabel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        JButton quitButton = new JButton("Sair");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        gameImpl.playerLeave(player.username);
                        gameImpl.quiteGame(player.username);
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        exist();
                        e1.printStackTrace();
                    }
                    System.exit(0);
            }
        });
        panel.add(quitButton, BorderLayout.SOUTH);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                buttonPanel.add(buttons[i][j]);
            }
        }

        setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // still matching
                try {
                    game = gameImpl.getGameByUsername(player.username);
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    exist();
                    e1.printStackTrace();
                }

                if(game==null){
                    System.out.println(username + " procurando outro jogador");
            currentlyPlayerLabel.setText("Procurando jogador");
                }
                

                // matching success
                if(game!=null ){
                    setTitle("Jogador: " + player.username + "(" + player.mark + ")");
                    try {
                        String[][] updatedBoard = gameImpl.getBoard(username);
                        updateBoard(updatedBoard);
                        currentlyPlayerLabel.setText("Vez do Jogador " + gameImpl.getCurrentPlayer(username).username + "(" + gameImpl.getCurrentPlayer(username).mark + ")\nTempo restante: " + gameImpl.getTimerCount(player.username));
                        String result = gameImpl.checkWin(player.username);
                        Player winner = gameImpl.getWinner(player.username);
                        if ( winner!=null) {
                            String message = "";
                            if (winner.mark.equals(player.mark)) {
                                message = "Você ganhou!";
                            }else {
                                message = "Você perdeu.";
                            }
                            currentlyPlayerLabel.setText("Jogador " + winner.username + " ganhou");
                            showGameOverDialog(message);
                        }
                        if(result != null && result.equals("Draw")){
                            currentlyPlayerLabel.setText("Empate");
                            showGameOverDialog("Jogo empatou");
                        } 
                    } catch (RemoteException ex) {
                        exist();
                        ex.printStackTrace();
                    }
                }
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void showGameOverDialog(String message) throws RemoteException {
        int choice = JOptionPane.showOptionDialog(Client.this,
                message,
                "Fim de Jogo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Sair", "Nova Partida"},
                "Sair");
        if (choice == JOptionPane.YES_OPTION) {
            gameImpl.playerLeave(player.username);
            gameImpl.removeGame(player.username);
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            gameImpl.removeGame(player.username);
            player = gameImpl.joinGame(player.username);
        }
    }

    private void updateBoard(String[][] board) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(board[i][j]);
                }
            }
        });
    }

    public void exist(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    class ButtonClickListener implements ActionListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            // System.out.println(game!= null);
            // try {
            //     System.out.println(gameImpl.getCurrentPlayer(player.username).mark.equals(player.mark));
            // } catch (RemoteException e1) {
            //     // TODO Auto-generated catch block
            //     e1.printStackTrace();
            // }
            // System.out.println(button.getText().equals(""));
            try {
                if ( game!= null && button.getText().equals("") && gameImpl.getCurrentPlayer(player.username).mark.equals(player.mark) && !gameImpl.gameOver(player.username) ) {
                    button.setText(player.mark);
                    // Send the move to the server
                    gameImpl.makeMove(player.username,row, col);
                }
            } catch (RemoteException e1) {
                exist();
                e1.printStackTrace();
            }
        }
    }
}
