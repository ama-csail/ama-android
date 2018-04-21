package io.github.ama_csail.ama.testserver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class Runner {

    public static void main(String[] args){

        try {

            ServerSocket server = new ServerSocket(8080);

            System.out.println("Server has started on " + InetAddress.getLocalHost().toString().split("/")[1] + ":8080.\r\nWaiting for a connection...");

            Socket client = server.accept();
            System.out.println("A client connected.");
            PrintWriter saver = new PrintWriter("accTest.ama", "UTF-8");
            System.out.println("Created test file for this test");

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                saver.println(line);
                if (line.length() > 300) {
                    line = line.substring(0,299) + "... [truncated]";
                }
                System.out.println(line);

                // TODO: Attempt to save image files

            }
            saver.close();

            server.close();
            Thread.sleep(1000);
            main(new String[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
