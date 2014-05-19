package com.rytong.template.debugtool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.erlide.jinterface.ErlLogger;

import com.rytong.template.debugtool.util.SyncSocket.ClientThread;

public class SyncSocket {

    private static List<ClientThread> clients;
    private HashMap<ClientThread, String> download_store;
    private boolean status = false;

    public int port = 8080;
    public ServerSocket server = null;
    public ServerThread serverThread ;
    public Socket socket = null;
    public SyncSocket(){

    }

    public SyncSocket getSyncSocket(){
        return this;
    }

    public boolean getServerStatus(){
        return status;
    }


    public void CreateSocket(int port){
        try {
            this.port = port;
            server = new ServerSocket(port);
            clients = new ArrayList<ClientThread>();
            serverThread = new ServerThread(server);
            download_store = new HashMap<ClientThread, String>();
            serverThread.start();
            status = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            status = false;

        }

    }


    @SuppressWarnings("deprecation")
    public void CloseSocket(){
        try {


            if (serverThread != null)
                serverThread.stop();

            for(int i = clients.size() -1; i >=0; i--){
                clients.get(i).getWriter().println("close");
                clients.get(i).getWriter().flush();
                clients.get(i).stop();
                clients.get(i).reader.close();
                clients.get(i).writer.close();
                clients.get(i).socket.close();
                clients.remove(i);
            }

            if (server != null)
                server.close();

            status = false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ErlLogger.debug("CloseSocket stack~");
            e.printStackTrace();
            status = true;
        } //关闭ServerSocket

    }

    public int sendServerMsg(String msg){
        //ErlLogger.debug("msg:"+msg);
        ErlLogger.debug("size:"+clients.size());
        int len = clients.size();
        if (len == 0){
            ErlLogger.debug("return 2");
            return 2;
        }
        for (int i = len-1; i >= 0; i--){
            clients.get(i).getWriter().print(msg);
            clients.get(i).getWriter().flush();
        }
        return 1;
    }

    public String getClientMsg(){
        //ErlLogger.debug("msg:"+msg);
        ErlLogger.debug("size:"+clients.size());
        int len = clients.size();
        if (len == 0){
            ErlLogger.debug("return 2");
            return null;
        }
        for (int i = len-1; i >= 0; i--){
            ClientThread client = clients.get(i);
            clients.get(i).getWriter().flush();
            String tmp_msg = download_store.get(client);
            ErlLogger.debug("tmp_msg 2:"+tmp_msg);
            if (tmp_msg != null){
                client.cur_msg = "";
                return tmp_msg;
            }
        }
        return "";
    }


    class ServerThread extends Thread{
        private ServerSocket serverSocket;

        public ServerThread(ServerSocket serverSocket){
            this.serverSocket = serverSocket;
        }

        public void run(){
            ErlLogger.debug("ServerThread   run~");
            while(true){
                Socket socket;
                try {
                    ErlLogger.debug("before accepted");
                    socket = serverSocket.accept();
                    ErlLogger.debug("accepted");
                    ClientThread client = new ClientThread(socket);

                    client.start();
                    clients.add(client);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    class ClientThread extends Thread{
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String cur_msg = "";
        //private User user;

        public ClientThread(Socket socket){
            try {
                this.socket = socket;
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                ErlLogger.debug("new client!");
                //String inf = reader.readLine();
               // ErlLogger.debug("inf:"+inf);
                //writer.println("connected");
                //writer.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @SuppressWarnings("deprecation")
        public void run(){

            String message = "";
            while(true){
                try {
                    message = reader.readLine();
                    if (message.equals("close")){
                        reader.close();
                        writer.close();
                        socket.close();

                        for(int i=clients.size()-1; i>=0;i-- ){
                            if (clients.get(i) == this){
                                ClientThread temp = clients.get(i);
                                clients.remove(i);
                                temp.stop();
                                return;
                            }
                        }
                    } else {
                        ErlLogger.debug("get:"+message);
                        cur_msg = cur_msg.concat(message).concat("\r\n");
                        download_store.put(this, cur_msg);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        public BufferedReader getReader(){
            return reader;
        }

        public PrintWriter getWriter(){
            return writer;
        }
    }

}

