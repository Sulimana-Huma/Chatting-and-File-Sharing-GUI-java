/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sendfile.server;

import java.io.IOException;//for I/O error
import java.net.ServerSocket;//implement server socket, listens incoming client requests
import java.net.Socket;//client socket, connect with server
import java.text.SimpleDateFormat;//format and pass dates current
import java.util.Date;//time in sec
import java.util.Vector;//just like ArrayList but it is synchronized
import javax.swing.JOptionPane;

/**
 *
 * @author hanhhoatranthi
 */
public class MainForm extends javax.swing.JFrame {
      
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); //for time display on server
    Thread t; //t for running server thread
    ServerThread serverThread; //object of ServerThread class
    /** Chat List  **/
    public Vector socketList = new Vector(); //
    public Vector clientList = new Vector();
    /** File Sharing List **/
    public Vector clientFileSharingUsername = new Vector();      //client info
    public Vector clientFileSharingSocket = new Vector();        //client socket info
    /** Server **/
    ServerSocket server;  //ServerSocket object for server

    
     
    public MainForm() {
        initComponents();    //initializes GUI components
        MyInit();            //creates window on the screen
    }
    void MyInit(){
         setLocationRelativeTo(null);
     }
    
    public void appendMessage(String msg){
        Date date = new Date();    //for date on server
        jTextArea1.append(sdf.format(date) +": "+ msg +"\n");     //append date and msg on server (AM/PM) format + colon after time
        jTextArea1.setCaretPosition(jTextArea1.getText().length() - 1);  //so that recent msg is always visible + moves cursor to end of text
    }
    
    public void setSocketList(Socket socket)   
    {
        try {
            socketList.add(socket);  //add active connected clients sockets into vector list of sockets 
            appendMessage("[setSocketList]: Added"); //msg display on server
        } catch (Exception e) { appendMessage("[setSocketList]: "+ e.getMessage()); }   
    }
    public void setClientList(String client){//add clients into vector list
        try {
            clientList.add(client);  //add connected clients into vector
            appendMessage("[setClientList]: Added");  //msg on server
        } catch (Exception e) { appendMessage("[setClientList]: "+ e.getMessage()); }
    }
    public void setClientFileSharingUsername(String user){
        try {
            clientFileSharingUsername.add(user); //add active users involved in file sharing
        } catch (Exception e) { }
    }
    
    public void setClientFileSharingSocket(Socket soc){
        try {
            clientFileSharingSocket.add(soc);//add sockets of file sharing active clients
        } catch (Exception e) { }
    }
    
    public Socket getClientList(String client){
        Socket tsoc = null;//tsoc will hold the socket
        for(int x=0; x < clientList.size(); x++){ //iterate through active clients list
            if(clientList.get(x).equals(client)){//if client name is matched on index x
                tsoc = (Socket) socketList.get(x);//the client socket will also be on index x in socketList
                break;
            }
        }
        return tsoc;
    }
    public void removeFromTheList(String client){
        try {
            for(int x=0; x < clientList.size(); x++){//iterates client list
                if(clientList.elementAt(x).equals(client)){//if client found
                    clientList.removeElementAt(x);//remove client name
                    socketList.removeElementAt(x);//remove client socket
                    appendMessage("[Removed]: "+ client);//msg on server
                    break;
                }
            }
        } catch (Exception e) {
            appendMessage("[RemovedException]: "+ e.getMessage());
        }
    }
    
    public Socket getClientFileSharingSocket(String username){
        Socket tsoc = null;
        for(int x=0; x < clientFileSharingUsername.size(); x++){//iterates list
            if(clientFileSharingUsername.elementAt(x).equals(username)){//if user found on index x from clientFileSharingUsername
                tsoc = (Socket) clientFileSharingSocket.elementAt(x);//get socket of that client on index x from socket list
                break;
            }
        }
        return tsoc;
    }
        public void removeClientFileSharing(String username){
        for(int x=0; x < clientFileSharingUsername.size(); x++){//iterate through list
            if(clientFileSharingUsername.elementAt(x).equals(username)){//if client found at index x
                try {
                    Socket rSock = getClientFileSharingSocket(username);//get socket of that client from function getClientFileSharingSocket
                    if(rSock != null){
                        rSock.close();//close that socket returned
                    }
                    clientFileSharingUsername.removeElementAt(x);//remove client name from index x
                    clientFileSharingSocket.removeElementAt(x);//remove client socket from index x
                    appendMessage("[FileSharing]:Cancel"+ username);//msg on server
                } catch (IOException e) {
                    appendMessage("[FileSharing]: "+ e.getMessage());
                    appendMessage("[FileSharing]: Can not cancel"+ username);
                }
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Humaim server");

        jLabel1.setText("Port:");

        jTextField1.setBackground(new java.awt.Color(204, 255, 204));
        jTextField1.setText("3333");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setText(" start the server");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 255, 204));
        jButton2.setText(" Stop the Server");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(255, 204, 204));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pink_msg_logo.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(303, 303, 303))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1074, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int port = Integer.parseInt(jTextField1.getText());//get text and convert to integer
        serverThread = new ServerThread(port, this);//object of ServerThread class + constructor called + initialized + handling client requests and communication in this class+ serverThread can interact with mainForm class + handles server side operations too
        t = new Thread(serverThread);//thread initialized + the target serverThread will run on server logic
        t.start();//thread started + start accepting client connections

        new Thread(new OnlineListThread(this)).start(); //update online list periodically

        jButton1.setEnabled(false);//disable start server now
        jButton2.setEnabled(true);//enable stop server now
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int confirm = JOptionPane.showConfirmDialog(null, "Close Server.?");//confirmation pane + stop the server
        if(confirm == 0){//if user says yes stop the server
            serverThread.stop();//stop the server
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {//schedules job for thread to create and show GUI
            public void run() {
                new MainForm().setVisible(true);//object of mainForm created + make frame visible
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

   
}
