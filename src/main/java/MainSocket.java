import sun.applet.Main;
import ui.PanelConnect;
import ui.UserUI;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainSocket {

    private UserUI ui;
    private PanelConnect pane;

    public void initView() {
        pane = new PanelConnect(400, 140);
        ui = new UserUI("Streaming Sound", 400, 140);
        ui.setLocationRelativeTo(null);
        ui.add(pane);
        ui.setVisible(true);
    }

    public void setIp() {
        try {
            InetAddress inet = Inet4Address.getLocalHost();
            pane.setIp(inet.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("error set ip");
            pane.notifyMessage(e.getMessage());
        }

    }
    public static void main(String[] args) {
        MainSocket main = new MainSocket();
        main.initView();
        main.setIp();

    }
}
