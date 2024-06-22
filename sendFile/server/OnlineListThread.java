

package sendfile.server;

import java.io.DataOutputStream;//writing data to output stream as series of bytes
import java.io.IOException;//handles I/O errors
import java.net.Socket;//manages connection B/W two machines over a network


public class OnlineListThread implements Runnable {//implements interface
    
    MainForm main;//object of MainForm class
    
    public OnlineListThread(MainForm main){//arg of type MainForm Class, accepts object of MainForm class
        this.main = main;//assigns arg to this.main obj of MainForm declared within class OnlineListThread
    }

    @Override
    public void run() {//method of interface Runnable
        try {
            while(!Thread.interrupted()){//if thread is running
                String msg = "";
                for(int x=0; x < main.clientList.size(); x++){//iterates clientList
                    msg = msg+" "+ main.clientList.elementAt(x);//adding active users into msg from clientList of MainForm class
                }
                
                for(int x=0; x < main.socketList.size(); x++){//iterate through socketList
                    Socket tsoc = (Socket) main.socketList.elementAt(x);//storing socket in tsoc
                    DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());//send data to connected clients on that socket
                    /** CMD_ONLINE [user1] [user2] [user3] **/
                    if(msg.length() > 0){//check if any client connected + if no clients connected no need to send any data
                        dos.writeUTF("CMD_ONLINE" +msg);//list is online users, CMD instruction, writes a UTF-encoded string to the output stream dos, msg contains user1 user2 user3
                    }
                }
                
                Thread.sleep(1900);//pause thread + reduce server load + reduce CPU load + minimize unnecessary trafic 
            }
        } catch(InterruptedException e){
            main.appendMessage("[InterruptedException]: "+ e.getMessage());
        } catch (IOException e) {
            main.appendMessage("[IOException]: "+ e.getMessage());
        }
    }
    
    
}
