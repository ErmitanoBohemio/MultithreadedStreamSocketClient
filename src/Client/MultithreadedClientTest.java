/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import javax.swing.JFrame;

/**
 *
 * @author OscarFabianHP
 */
public class MultithreadedClientTest {
    public static void main(String[] args) {
        MultithreadedClient application;
        
        if(args.length == 0){
            application = new MultithreadedClient("127.0.0.1"); //connect to localhost
        }
        else{
            application = new MultithreadedClient(args[0]); //use args to connect
        }
        
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runClient();
    }
    
}
