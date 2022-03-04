import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //Server Socket field
    private ServerSocket serversocket;
    //Constructor
    public Server(ServerSocket serversocket) {
        this.serversocket = serversocket;
    }
    //Starting server
    public void StartServer() {
        try {
            //While loop for runnning the program endless.
            while (!serversocket.isClosed()) {
                Socket socket = serversocket.accept();
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                //Creating thread for multiclients
                Thread thread = new Thread(clientHandler);
                thread.start();


            }
        } catch (IOException e) {

        }
    }
    //Closing the server
        public void closeServerSocket(){
            try{
                if(serversocket!=null){
                    serversocket.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(1234);
        Server server= new Server(serverSocket);
        server.StartServer();


    }


}
