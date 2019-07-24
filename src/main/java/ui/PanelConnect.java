package ui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class PanelConnect extends JPanel implements ActionListener {

    private JTextField tvPort;
    private JTextField tvIp;
    private JButton btnStart;
    private DatagramSocket serverSocket;
    private SourceDataLine speakers;

    public PanelConnect(int width, int height) {
        super();
        setBounds(0, 0, width, height);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        addComponent();
    }

    private void addComponent() {

        tvIp = new JTextField();
        tvIp.setColumns(30);
        tvIp.setBounds(0, 0, 150, 50);
        tvIp.setEnabled(false);
        tvIp.setDisabledTextColor(Color.BLUE);
        tvIp.setToolTipText("Ip address");

        tvPort = new JTextField();
        tvPort.setColumns(30);
        tvPort.setBounds(0, 100, 100, 50);
        tvPort.setToolTipText("Port");

        btnStart = new JButton("Start server");
        btnStart.setBounds(0, 250, 100, 50);
        btnStart.setActionCommand("btnStart");
        btnStart.addActionListener(this);

        add(tvIp);
        add(tvPort);
        add(btnStart);
    }

    public void setIp(String ip) {
        tvIp.setText(ip);
    }

    public void setPort(String port) {
        tvPort.setText(port);
    }

    public void notifyMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("btnStart")) {
            doStartServer();
        }

        if(command.equals("btnStop")) {
            doStopServer();
        }
    }

    private void doStopServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                btnStart.setText("Start server");
                btnStart.setActionCommand("btnStart");
                tvPort.setEnabled(true);
                JOptionPane.showMessageDialog(this, "server stopped");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleInbits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleInbits, channels, signed, bigEndian);
    }

    private void doStartServer() {
        try {
            tvPort.setEnabled(false);
             serverSocket = new DatagramSocket(Integer.parseInt(tvPort.getText()));
            JOptionPane.showMessageDialog(this, "Server started in port :" + tvPort.getText());
            btnStart.setText("Stop server");
            btnStart.setActionCommand("btnStop");

            final AudioFormat format = getAudioFormat();

            final DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);

            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                           if(!serverSocket.isClosed()) {

                               byte[] buffer = new byte[1024];

                               DatagramPacket response = new DatagramPacket(buffer, buffer.length);

                               serverSocket.receive(response);

                               speakers.write(response.getData(), 0, response.getData().length);
                           }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            thread.start();
        } catch (Exception e) {
            tvPort.setEnabled(true);
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
}
