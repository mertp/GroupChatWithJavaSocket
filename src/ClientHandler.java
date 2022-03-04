import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements  Runnable {
    //Creating a arraylist for all clients
    public static ArrayList<ClientHandler> clientHandlers=new ArrayList();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    //Constructor

    public ClientHandler(Socket socket){
        try{
            this.socket=socket;
            //Setting up bufferedWriters and readers
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Taking username info via bufferedReader
            this.clientUsername=bufferedReader.readLine();
            //Adding the object to the arrayList
            clientHandlers.add(this);
            broadMessage("Server :"+ clientUsername+ "has entered the chat");
        } catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    //Thread function for not blocking the rest of the program until the message
    @Override
    public  void  run(){
    String messagefromClient;
    while(socket.isConnected()){
        try{
            messagefromClient= bufferedReader.readLine();
            broadMessage(messagefromClient);
        }
        catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
            break;
        }
    }
    }

    public void broadMessage(String message){
        for(ClientHandler clientHandler:clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadMessage("Server :"+clientUsername+" has left the chat");
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
            removeClientHandler();
            try{
                if(bufferedReader!=null){
                    bufferedReader.close();
                }
                if(bufferedWriter!=null){
                    bufferedWriter.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
    }
}
