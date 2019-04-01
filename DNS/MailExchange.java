import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MailExchange {

    private static ArrayList<String> ip = new ArrayList<>();

    private static void lookUpMailHosts(String domainName) {
        InitialDirContext iDirC = null;
        Attributes attributes = null;
        try {
            iDirC = new InitialDirContext();
            attributes = iDirC.getAttributes("dns:/" + domainName, new String[] {"MX"});
        } catch (NamingException e) {
            System.out.println("Wrong name!");
            System.exit(1);
        }

        Attribute attributeMX = attributes.get("MX");

        if (attributeMX == null) {
            System.out.println("No MX attribute");
            System.exit(1);
        }

        String[][] pvhn = new String[attributeMX.size()][2];
        for (int i = 0; i < attributeMX.size(); i++) {
            try {
                pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        Arrays.sort(pvhn, Comparator.comparingInt(o -> Integer.parseInt(o[0])));

        String[] sortedHostNames = new String[pvhn.length];
        for (int i = 0; i < pvhn.length; i++) {
            sortedHostNames[i] = pvhn[i][1].endsWith(".") ?
                    pvhn[i][1].substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
        }

        try {
            IPSearch(sortedHostNames);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    private static void IPSearch(String[] hostNames) throws UnknownHostException {
        ip.clear();
        InetAddress address;
        for (String hostName : hostNames) {
            address = InetAddress.getByName(hostName);
            ip.add(address.getHostAddress());
        }
    }

    private static void TCPConnecting() throws IOException {
        for (String address: ip) {
            Socket clientSocket = new Socket(address, 25);
            BufferedReader inFromServer = new BufferedReader((new InputStreamReader(clientSocket.getInputStream())));
            String message = inFromServer.readLine();
            System.out.println(message);
            clientSocket.close();
        }

    }


    public static void main(String[] args) throws NamingException, IOException {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        System.out.println("If you want to exit just enter 'exit'.");
        while (!exit) {
            String line = userInput.readLine();
            if (line.equals("exit")) exit = true;
            else {
                lookUpMailHosts(line);
                TCPConnecting();
            }
        }
        System.out.println("Goodbye.");

    }
}
