/*name: MIke Urbano
Student ID: 1001242814 */

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.net.httpserver.*;
import java.util.concurrent.Executors;
import java.net.URL;
import java.net.HttpURLConnection;
/*
THE COM.SUN.NET.HTTPSERVER PACKAGE PROVIDES AN API TO BUILD HTTP SERVERS
IN JAVA; SIMPLESERVER CLASS IMPLEMENTS THE SERVER
*/
public class SimpleServer {

  public static void main(String[] args) throws IOException {
    //this line creates the server, socket 2323 and bound to the localhost
    //as the root URL
    HttpServer simpleServer = HttpServer.create(new InetSocketAddress(2323),0);
    //this line creates the URL to be searhed in the brower
    HttpContext context = simpleServer.createContext("/project1");
    HttpContext context2 = simpleServer.createContext("/projectOne");
    HttpContext context3 = simpleServer.createContext("/");
    HttpContext context4 = simpleServer.createContext("/networks.jpg");
    //this line sets the method that will handle the requests
    context.setHandler(SimpleServer::requestHandler);
    context2.setHandler(SimpleServer::requestHandler);
    context3.setHandler(SimpleServer::requestHandler);
    context4.setHandler(SimpleServer::requestHandler);
    //set executor to use up 10 threads
    simpleServer.setExecutor(Executors.newFixedThreadPool(10));
    //this line starts the server
    simpleServer.start();
    //print to console alert that server is running
    System.out.println("Started a Simple Server on port 2323.");

  }
    /* This section contains the request handler function that will handle the
    /* specific requests depending on the URL used; if the user types the correct
    /* url, then they are presented the index html page that contains an image
    /* correct URL: http://localhost:2323/project1
     previous URL: http://localhost:2323/projectOne
     if the user uses the old url, they are presented with the 301 page
     any other URL will present the user with a 404 page */
  public static void requestHandler(HttpExchange message) throws IOException {
    //saving to variable the requested URL
    URI requestURI = message.getRequestURI();
    printRequestData(message);
    //obtain the requested url in string format
    String testString = requestURI.toString();
    //these next lines compare the url request to hardcoded strings to
    //determine which page to present to the user
    int result1 = testString.compareTo("/project1");
    int result2 = testString.compareTo("/projectOne");
    int result3 = testString.compareTo("/cse.png");
    //these next functions use the results from the above comparisons to
    //present the appropriate page; they use and httpexchange variable to
    //send the appropriate response with the correct code (200,301,404)
    if(result1 == 0) {
      String response = new String(Files.readAllBytes(Paths.get("index.html")));
      message.sendResponseHeaders(200, response.getBytes().length);
      OutputStream outputStream = message.getResponseBody();
      printResponseData(message);
      outputStream.write(response.getBytes());
      outputStream.close();
    }
    if(result2 == 0) {
      String response = new String(Files.readAllBytes(Paths.get("index303.html")));
      message.sendResponseHeaders(301, response.getBytes().length);
      OutputStream outputStream = message.getResponseBody();
      printResponseData(message);
      outputStream.write(response.getBytes());
      outputStream.close();
    }
    if(result3 == 0) {
      Headers headers = message.getResponseHeaders();
      headers.add("Content-Type", "image/png");
      File file = new File("cse.png");
      byte[] bytes = new byte [(int)file.length()];
      FileInputStream fileInputStream = new FileInputStream(file);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
      bufferedInputStream.read(bytes, 0, bytes.length);
      message.sendResponseHeaders(200,file.length());
      OutputStream outputStream = message.getResponseBody();
      printResponseData(message);
      outputStream.write(bytes, 0, bytes.length);
      outputStream.close();
    }
    else {
      String response = new String(Files.readAllBytes(Paths.get("index404.html")));
      message.sendResponseHeaders(404, response.getBytes().length);
      OutputStream outputStream = message.getResponseBody();
      printResponseData(message);
      outputStream.write(response.getBytes());
      outputStream.close();
      }
    }
  public static void printRequestData(HttpExchange message) {
    URI requestURI = message.getRequestURI();
    String command = message.getRequestMethod();
    Headers requestHeaders = message.getRequestHeaders();
    System.out.println("REQUEST COMMAND AND REQUESTED URL");
    System.out.println("Command: " + command + "  URL:" + requestURI);
    System.out.println("REQUEST HEADERS: ");
    requestHeaders.entrySet().forEach(System.out::println);
    System.out.println("\n\n\n");
    //long threadId = Thread.currentThread().getId();
    //System.out.println(threadId);
  }
  public static void printResponseData(HttpExchange message) {
    Headers responseHeaders = message.getResponseHeaders();
    System.out.println("RESPONSE HEADERS");
    responseHeaders.entrySet().forEach(System.out::println);
    OutputStream os = message.getResponseBody();

    System.out.println("\n\n\n");
  }


}
