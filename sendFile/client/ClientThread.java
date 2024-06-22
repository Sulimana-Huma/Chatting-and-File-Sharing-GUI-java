package sendfile.client;


import java.awt.Color;//provide functionality for handling colors
import java.io.DataInputStream;//
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hanhhoatranthi
 */
public class ClientThread implements Runnable{
    
    Socket socket;//socket represents connection to the server
    DataInputStream dis;//read data from server
    DataOutputStream dos;//send data to the server
    MainForm main;//MainForm client side GUI class object
    StringTokenizer st;//complete msg sent don't break after space
    protected DecimalFormat df = new DecimalFormat("##,#00");//for number formating
    
    public ClientThread(Socket socket, MainForm main){//initialize ClientThread object + constructor
        this.main = main;//main obj
        this.socket = socket;//client socket
        try {
            dis = new DataInputStream(socket.getInputStream());//read data from socket
        } catch (IOException e) {
            main.appendMessage("[IOException]: "+ e.getMessage(), "Error", Color.RED, Color.RED);
        }
    }


    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String data = dis.readUTF();//read data from input stream of socket + read any msg received?
                st = new StringTokenizer(data);//
                /** Get Message CMD **/
                String CMD = st.nextToken();//Extracts the first token, which is the command. e.g "CMD_ONLINE"
                switch(CMD){
                    case "CMD_MESSAGE":
                        SoundEffect.MessageReceive.play(); //  Play Audio clip
                        String msg = "";
                        String frm = st.nextToken();//message from
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();//gets complete msg as like string
                        }
                        main.appendMessage(msg, frm, Color.MAGENTA, Color.BLUE);//append msg into chatHall
                        break;
                        
                    case "CMD_ONLINE":
                        Vector online = new Vector();//prepares sychronized online list
                        while(st.hasMoreTokens()){
                            String list = st.nextToken();
                            if(!list.equalsIgnoreCase(main.username)){//ignore my name from 
                                online.add(list);
                            }
                        }
                        main.appendOnlineList(online);
                        break;
                    
                        
                    // This function will notify the client that there is a received file, Accept or reject the file
                    case "CMD_FILE_XD":  // Format:  CMD_FILE_XD [sender] [receiver] [filename]
                        String sender = st.nextToken();
                        String receiver = st.nextToken();
                        String fname = st.nextToken();
                        int confirm = JOptionPane.showConfirmDialog(main, "From: "+sender+"\nname file: "+fname+"\nDo you Accept this file?");
                        //SoundEffect.FileSharing.play(); //   Play Audio
                        if(confirm == 0){ // The client accepts the request, then notifies the sender to send the file
                            /* choose where to save the file */
                            main.openFolder(); //select directory
                            try { 
                                dos = new DataOutputStream(socket.getOutputStream()); //create stream for sending data to sender 
                                // Format:  CMD_SEND_FILE_ACCEPT [ToSender] [Message]
                                String format = "CMD_SEND_FILE_ACCEPT "+sender+" Accept";
                                dos.writeUTF(format);//send data to sender
                                
/* This function will create a filesharing socket to create a stream of incoming file processing and this socket will automatically close when completed.  */
                                Socket fSoc = new Socket(main.getMyHost(), main.getMyPort());//socket for file sharing created
                                DataOutputStream fdos = new DataOutputStream(fSoc.getOutputStream());//dos for file sharing
                                fdos.writeUTF("CMD_SHARINGSOCKET "+ main.getMyUsername());// informs the server that a file sharing socket has been set up.
                                /*  Run Thread for this   */
                                new Thread(new ReceivingFileThread(fSoc, main)).start();
                            } catch (IOException e) { 
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        } else { // client rejects the request, then sends the result to the sender
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                // Format:  CMD_SEND_FILE_ERROR [ToSender] [Message]
                                String format = "CMD_SEND_FILE_ERROR "+sender+" The user rejected your request or lost connection.!";
                                dos.writeUTF(format);
                            } catch (IOException e) {
                                System.out.println("[CMD_FILE_XD]: "+e.getMessage());
                            }
                        }                       
                        break;   
                        
                    default: 
                        main.appendMessage("[CMDException]:Order unknown"+ CMD, "CMDException", Color.RED, Color.RED);
                    break;
                }
            }
        } catch(IOException e){
            main.appendMessage(" Lost connection to the Server, please try again.!", "Error", Color.RED, Color.RED);
        }
    }
}