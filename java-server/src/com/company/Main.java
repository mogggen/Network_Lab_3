package com.company;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Main extends JComponent
{
    public static class GUI
    {
        JFrame frame = new JFrame();
        PixelCanvas canvas = new PixelCanvas();
        // class constructor
        public GUI()
        {

            canvas.setBounds(0, 0, 201, 201);
            frame.setSize(201, 201);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Server");
            frame.add(canvas);
            frame.pack();
            frame.setVisible(true);
        }
    }

    static class Pixel
    {
        int x, y, c;
        public Pixel(int x, int y, int c)
        {
            this.x = x;
            this.y = y;
            this.c = c;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getC() {
            return c;
        }
    }

    static class PixelCanvas extends JComponent
    {
        ArrayList<Pixel> arr;
        void Draw(ArrayList<Pixel> arr)
        {
            this.arr = arr;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            System.out.println("sats");
            if (arr != null) {
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).c > 8) {
                        System.out.println("Screams in agony");
                        return;
                    }
                    g.setColor(new Color(arr.get(i).getC() * 34, arr.get(i).getC() * 34, arr.get(i).getC() * 34));
                    g.fillRect(arr.get(i).getX(), arr.get(i).getY(), 10, 10);
                }
            }
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

        public void listen(GUI window) throws IOException
        {
            ArrayList<Character> temp = new ArrayList<>();

            int i = 0;
            while (true) {
                temp.add((char) bf.read());
                if (temp.get(temp.size() - 1) == 0) break;
            }
            ArrayList<Pixel> info = new ArrayList<>();
                //Reformat
                info.clear();
                for (int k = 0; k < temp.size(); k += 3) {
                    info.add(new Pixel(temp.get(k), temp.get(k + 1), temp.get(k + 2))); // always a multiple of three
                }
                window.canvas.Draw(info);
                window.frame.repaint();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        GUI window = new GUI();
        Server server = new Server(4999);
        server.listen(window);
        new Main();
    }
}