import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class GameServer {
    public static void main(String[] args) {
        String ipAddress;
        int port;

        if (args.length == 2) {
            ipAddress = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            JPanel panel = new JPanel();
            panel.add(new JLabel("Endereço IP:"));
            JTextField ipField = new JTextField(10);
            panel.add(ipField);
            panel.add(new JLabel("Porta:"));
            JTextField portField = new JTextField(5);
            panel.add(portField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Digite o endereço IP e a porta do servidor", JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION) {
                System.out.println("Operação cancelada pelo usuário.");
                System.exit(0);
            }
            ipAddress = ipField.getText();
            port = Integer.parseInt(portField.getText());
        }

        // Inicia o servidor
        try {
            GameInterface game = new GameImpl();
            Registry registry = LocateRegistry.createRegistry(port, null, null);
            registry.rebind("GameServer", game);
            System.out.println("Servidor de jogo está rodando em " + ipAddress + ":" + port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
