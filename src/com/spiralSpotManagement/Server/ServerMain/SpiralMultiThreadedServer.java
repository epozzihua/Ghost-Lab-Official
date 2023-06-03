package com.spiralSpotManagement.Server.ServerMain;

import com.spiralSpotManagement.Server.Controllers.BillingControllers.BillingController;
import com.spiralSpotManagement.Server.Controllers.SearchControllers.SearchController;
import com.spiralSpotManagement.Server.Controllers.SpotCategoryControllers.SpotCategoryController;
import com.spiralSpotManagement.Server.Controllers.SpotController.SpotCommentController;
import com.spiralSpotManagement.Server.Controllers.SpotController.SpotCommentReactionController;
import com.spiralSpotManagement.Server.Controllers.SpotController.SpotController;
import com.spiralSpotManagement.Server.Controllers.LocationControllers.LocationController;
import com.spiralSpotManagement.Server.Controllers.LocationLevelControllers.LocationLevelController;
import com.spiralSpotManagement.Server.Controllers.UserModuleControllers.UserCategoryController;
import com.spiralSpotManagement.Server.Controllers.UserModuleControllers.UserController;
import com.spiralSpotManagement.Server.DbController.PropertyVariables;
import com.spiralSpotManagement.Server.Model.RequestBody;
import com.spiralSpotManagement.Server.Model.Spot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

// Server class
/*

 @author : Ntwari Egide - Scrum Master
 @author : Ishimwe Gervais

 */
public class SpiralMultiThreadedServer {



/*
* This method is called once to set the db configurations
*/
//
//    public void startServer()throws Exception{
//    String url = "jdbc:mysql://remotemysql.com:3306/2YQ7auowc7?" + "autoReconnect=true&useSSL=false";
//    String username = "2YQ7auowc7";
//    String password = "R2IMVJC67L";
//
//    PropertyVariables propertyVariables = new PropertyVariables(url,username,password,3306l,1000l);
//    propertyVariables.setPropertiesInFile();
//  }

    public void startServer()throws Exception{
    String url = "jdbc:mysql://remotemysql.com:3306/2YQ7auowc7?" + "autoReconnect=true&useSSL=false";
    String username = "2YQ7auowc7";
    String password = "R2IMVJC67L";

    PropertyVariables propertyVariables = new PropertyVariables(url,username,password,3306l,1000l);
    propertyVariables.setPropertiesInFile();
  }


    public static void main(String[] args)
    {
        ServerSocket server = null;

        try {

            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);

            // running infinite loop for getting
            // client request
            while (true) {
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSock
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }

        public void run()
        {
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try {
                // get the outputstream of client
                out = new ObjectOutputStream(
                        clientSocket.getOutputStream());

                // get the inputstream of client
                in = new ObjectInputStream(clientSocket.getInputStream());

                RequestBody requestBody;
                while ((requestBody = (RequestBody) in.readObject()) != null) {
                    //Reading the url
                    String url = requestBody.getUrl();

                    List<Object> responseObject = null;
                    switch (url){
                        case "/users":

                            responseObject =  new UserController().mainMethod(requestBody);
                            break;
                        case"/user-category":
                            responseObject = new UserCategoryController().mainMethod(requestBody);
                            break;
                        case "/spot":
                            responseObject = new SpotController().mainSpotController(requestBody);

                            break;

                        case "/sportCategory":
                            responseObject = new SpotCategoryController().mainMethod(requestBody);
                            break;
                        case "/spot-comment":
                            responseObject = new SpotCommentController().mainMethod(requestBody);
                            break;
                        case "/spot-reaction":
                            responseObject = new SpotCommentReactionController().mainMethod(requestBody);
                            break;

                        case "/search":
                            responseObject = new SearchController().mainMethod(requestBody);
                            break;

                        case "/report":
                            break;

                        case "/location":
                            responseObject = new LocationController().mainMethod(requestBody);
                            break;
                        case "/location-levels":
                            responseObject = new LocationLevelController().mainMethod(requestBody);
                            break;

                        case "/billing":
                            responseObject = new BillingController().mainMethod(requestBody);
                            break;

                        default:

                    }
                    // writing the received message from
                    // client
//                   Users users = new Users();
//                    users.setEmail(((Users) requestBody.getObject()).getEmail());
//                    System.out.println(users.getEmail());
////                    System.out.printf(
////                            " Sent from the client: %s\n",
////                            requestBody.getObject());
//                    List<Users> usersList = new ArrayList<>();
//                    usersList.add(users);
//                    usersList.add(users);

                    out.writeObject(responseObject);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
