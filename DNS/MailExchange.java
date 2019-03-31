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

    private static ArrayList<String> ip = new ArrayList<String>();

    private static void lookUpMailHosts(String domainName) throws NamingException, UnknownHostException {

        InitialDirContext iDirC = new InitialDirContext();
        Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[] {"MX"});
        Attribute attributeMX = attributes.get("MX");

        if (attributeMX == null) {
            System.out.println("No MX attribute");
            System.exit(1);
        }

        String[][] pvhn = new String[attributeMX.size()][2];
        for (int i = 0; i < attributeMX.size(); i++) {
            pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
        }

        Arrays.sort(pvhn, Comparator.comparingInt(o -> Integer.parseInt(o[0])));

        String[] sortedHostNames = new String[pvhn.length];
        for (int i = 0; i < pvhn.length; i++) {
            sortedHostNames[i] = pvhn[i][1].endsWith(".") ?
                    pvhn[i][1].substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
        }

        IPSearch(sortedHostNames);
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
        while (true) {
            lookUpMailHosts(userInput.readLine());
            TCPConnecting();
        }

    }
}
