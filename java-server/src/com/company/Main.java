package com.company;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;

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
            this.x = x & 0xFF;
            this.y = y & 0xFF;
            this.c = c & 0xFF;
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

    //creates a uniform map of the 9 color inputs
    static int Map(int input, int inputStart, int inputEnd, float outputStart, float outputEnd)
    {
        return (int)(outputStart + (outputEnd - outputStart) / (inputEnd - inputStart) * (input - inputStart));
    }

    //handles the drawing of the input value
    static class PixelCanvas extends JComponent
    {
        Pixel pixel;
        void SetParam(Pixel pixel)
        {
            this.pixel = pixel;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (pixel != null) {
                if (pixel.c == 0) {
                    return;
                }
                int up = Map(pixel.getC(), 0, 8, 0, 255);
                int down = Map(pixel.getC(), 8, 0, 0, 255);
                g.setColor(new Color(down, up, down));
                g.fillRect((int)pixel.getX(), pixel.getY(), 10, 10);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(201, 201);
        }
    }

    public static class Server
    {
        ServerSocket ss;
        Socket s;
        InputStream in;

        public Server(int port) throws IOException {
            ss = new ServerSocket(port);
            s = ss.accept();
            in = s.getInputStream();
            System.out.println("client connected");
        }

        //Reads Bytes from the input stream
        public void listen(GUI window) throws IOException {
            Pixel pixel = new Pixel((byte) in.read(), (byte) in.read(), (byte) in.read());
            System.out.println(pixel.getX() + " " + pixel.getY() + " " + pixel.getC());
            window.canvas.SetParam(pixel);
            window.frame.repaint();
        }
    }

    public static void main(String[] args) throws IOException{

        GUI window = new GUI();
        Server server = new Server(4999);
        new Main();
        while(true) {
            try {
                Thread.sleep(1000);
                server.listen(window);
            }catch (ExportException e){
                System.out.println(e);
                break;
            }
            catch (Exception e){
                System.out.println(e);
                return;
            }
        }
    }
}