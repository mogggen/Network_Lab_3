package com.company;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class GUI extends JComponent
{
    // class constructor
    public GUI(String[] str)
    {
        JFrame frame = new JFrame();
        PixelCanvas canvas = new PixelCanvas();
        canvas.setBounds(0, 0, 201, 201);
        frame.setSize(201, 201);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Server");
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    class PixelCanvas extends JComponent
    {
        int x, y, c;
        public void update(Graphics g)
        {
            if (c == 0) return;
            if (c > 8) {
                System.out.println("Screams in agony");
                return;
            }
            float[] colVal = {c * 34, c * 34, c * 34};
            g.setColor(new Color(c * 34, c * 34, c * 34));
            g.fillRect(x, y, 1, 1);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            update(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(201, 201);
        }
    }

    //load data from server
    public static class Server
    {
        ServerSocket ss;
        Socket s;
        InputStreamReader in;
        BufferedReader bf;

        public Server(int port) throws IOException
        {
            ss = new ServerSocket(port);
            s = ss.accept();
            System.out.println("client connected");
            in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);
        }

        public char[] listen() throws IOException
        {
            char[] data = new char[256];

            for (int i = 0; i < data.length; i++) {
                data[i] = (char)bf.read();
                if (data[i] == 0) break;
            }
            return data;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<PixelCanvas> info = new ArrayList<>();
        Server server = new Server(4999);
        do {
            try {
                System.out.println(server.listen());
            }catch (SocketException s){
                System.out.println("Error: an empty string was received, Terminating...");
                return;
            }

                //redecorate
                //for (int i = 0; i < info.length; i++) {
                //    info[i] = info[i].replace(" ", "");
                //}
         }while (true);
        //new GUI(new String[]{});
    }
}