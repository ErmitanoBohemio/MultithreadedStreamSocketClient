/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author OscarFabianHP
 */
public class MultithreadedClient extends JFrame implements Runnable{

    private JTextField enterField;
    private JTextArea displayArea;
    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    private String serverHost;

    public MultithreadedClient(String server) {
        serverHost = server; //set name of server
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        enterField = new JTextField();
        enterField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendDataServer(e.getActionCommand());
                enterField.setText("");
            }
        });
        
        add(enterField, BorderLayout.NORTH);
        setSize(300, 150);
        setVisible(true);
    }
    
    //start the client thread
    public void runClient(){
        try {
            //make connection to server
            connection = new Socket(InetAddress.getByName(serverHost), 12345);
            
            //get streams for input and output
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
            
            displayMessage("CLIENT CONNECTED TO SERVER");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        ExecutorService worker = Executors.newFixedThreadPool(1);
        worker.execute(this);
    }
    
    @Override
    public void run() {
        String message = "";
        do {
            try {
                message = (String) input.readObject(); //lee mensajes que vienen del servidor
                displayMessage(message); //muestra mensajes en pantalla de la app cliente
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } while (!message.equals("SERVER>> TERMINATE"));
    }

    private void displayMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                displayArea.append(message);
                displayArea.append(System.lineSeparator());
            }
        });
    }
    
    //metodo utilitario que envia (write) mensajes al servidor
    private void sendDataServer(String message){
        try {
            output.writeObject(message);
            output.flush();
            displayMessage("CLIENT>> "+message); //muestra mensaje eviado al servidor en app Cliente
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /*private void closeConnection(){
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }*/
    
}
