package com.company;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GUI extends JComponent
{
    // class constructor
    public GUI(String[] str)
    {
        for (String s : str) {
            System.out.println(s);
        }
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
        }

        public String listen() throws IOException
        {
            in = new InputStreamReader(s.getInputStream());
            bf = new BufferedReader(in);

            String str = bf.readLine();
            return str;
        }
    }

    public static void main(String[] args) throws IOException
    {
        ArrayList<PixelCanvas> info = new ArrayList<>();
        Server server = new Server(4999);
        do {
                if (server.listen() != null) {
                    System.out.println(server.listen() != null);
                    System.out.println("here");
                    System.out.println(server);
                    System.out.println(server.listen());
                }

                //redecorate
                //for (int i = 0; i < info.length; i++) {
                //    info[i] = info[i].replace(" ", "");
                //}





                new GUI(new String[]{});
            }while (true);

    }
}