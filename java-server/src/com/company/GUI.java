package com.company;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GUI
{
    private byte count;
    private final JLabel label;
    private final JFrame frame;
    private final JPanel panel;

    public GUI()
    {
        frame = new JFrame();

        label = new JLabel("Number of clicks: " + count);
        label.setSize(10, 25);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 201, 201));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("WhetherChannelClient");
        frame.pack();
        frame.setVisible(true);
    }

    public static void server(int port) throws IOException
    {
        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();

        System.out.println("client connected");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("client: " + str); // x, y, color
    }

    public static void main(String[] args) throws IOException
    {
        server(4999);
        new GUI();
    }
}