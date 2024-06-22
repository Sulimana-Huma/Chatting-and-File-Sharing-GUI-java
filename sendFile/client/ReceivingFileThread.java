package sendfile.client;


import sendfile.client.MainForm;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;




public class ReceivingFileThread implements Runnable {
    
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected MainForm main;
    protected StringTokenizer st;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    
    public ReceivingFileThread(Socket soc, MainForm m){
        this.socket = soc;
        this.main = m;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }

    @Override
    public void run() {
      
    try {
        while (!Thread.currentThread().isInterrupted()) {
            String data = dis.readUTF();
            st = new StringTokenizer(data);
            String CMD = st.nextToken();
            if (CMD.equals("CMD_SENDFILE")) {
                String filename = st.nextToken();
                long filesize = Long.parseLong(st.nextToken());
                String consignee = st.nextToken();
                String path = main.getMyDownloadFolder() + filename;
                FileOutputStream fos = new FileOutputStream(path);
                InputStream input = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(input);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                long totalReceived = 0;
                while ((count = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                    totalReceived += count;
                    if (totalReceived >= filesize) break;
                }
                fos.flush();
                fos.close();
                JOptionPane.showMessageDialog(null, "File downloaded to \n'" + path + "'");
            }
        }
    } catch (IOException e) {
        // Handle exception
    }

       
    }
}

