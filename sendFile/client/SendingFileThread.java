package sendfile.client;


import sendfile.client.SendFile;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class SendingFileThread implements Runnable {
    
    protected Socket socket;
    private DataOutputStream dos;
    protected SendFile form;
    protected String file;
    protected String receiver;
    protected String sender;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    
    public SendingFileThread(Socket soc, String file, String receiver, String sender, SendFile frm){
        this.socket = soc;
        this.file = file;
        this.receiver = receiver;
        this.sender = sender;
        this.form = frm;
    }

    @Override
    public void run() {
    try {
        form.disableGUI(true);
        dos = new DataOutputStream(socket.getOutputStream());
        File filename = new File(file);
        long len = filename.length();
        String clean_filename = filename.getName();
        dos.writeUTF("CMD_SENDFILE " + clean_filename.replace(" ", "_") + " " + len + " " + receiver + " " + sender);
        InputStream input = new FileInputStream(filename);
        OutputStream output = socket.getOutputStream();
        BufferedInputStream bis = new BufferedInputStream(input);
        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        long totalSent = 0;
        while ((count = bis.read(buffer)) > 0) {
            output.write(buffer, 0, count);
            totalSent += count;
            int percent = (int) ((totalSent * 100) / len);
            form.updateProgress(percent);
        }
        output.flush();
        output.close();
    } catch (IOException e) {
        form.updateAttachment(false);
    }

       
    }
}