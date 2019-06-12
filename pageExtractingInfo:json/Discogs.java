import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class Discogs {
    private static Map<String, List<String>> segregate = new HashMap<>();
    private static List<String> others = new ArrayList<>();

    private static String sentGet(String artistID) throws InterruptedException {
        Thread.sleep(1000);
        String url = "https://api.discogs.com/artists/" + artistID;
        URL link;
        HttpURLConnection con = null;
        int responseCode = 0;
        StringBuilder response = null;

        try {
            link = new URL(url);
            con = (HttpURLConnection) link.openConnection();
            con.setRequestMethod("GET");
            responseCode = con.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (responseCode == 200 && con.getContentType().equals("application/json"));

        try (BufferedReader info = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String newLine;
            response = new StringBuilder();

            while ((newLine = info.readLine()) != null) {
                response.append(newLine);
            }


        } catch (IOException e) {
            e.getMessage();
        }
        assert response != null;
        return response.toString();
    }


    private static void checkMembers(String response, String name, String groupName){
        List <String> group = new ArrayList<>();
        JSONObject obj = new JSONObject(response);

        JSONArray groups = obj.getJSONArray("groups");

        for (int i = 0; i < groups.length(); i++) {
            JSONObject person = groups.getJSONObject(i);
            if (!person.getString("name").equals(groupName))
                group.add(person.getString("name"));
        }

        if (group.isEmpty()) others.add(name);
        else {

            for (String s : group) {
                if (!segregate.containsKey(s)) {
                    segregate.put(s, new ArrayList<>());
                }
                segregate.get(s).add(name);
            }
        }
    }


    private static void analyze(String response) throws InterruptedException {
        Map<Integer, String> membersList = new HashMap<>();
        List<String> inGroup = new ArrayList<>();

        JSONObject obj = new JSONObject(response);
        String groupName = obj.get("name").toString();

        JSONArray members = obj.getJSONArray("members");

        for (int i = 0; i < members.length(); i++) {
            JSONObject person = members.getJSONObject(i);
            membersList.put(person.getInt("id"), person.getString("name"));
        }

        for (Map.Entry<Integer, String> entry : membersList.entrySet()) {
            String name = entry.getValue();
            checkMembers(sentGet(entry.getKey().toString()), name, groupName);
        }

        for (Map.Entry<String, List<String>> entry : segregate.entrySet()) {
            String actualGroup = entry.getValue().toString().replace("[", "").replace("]", "");
            if(entry.getValue().size() == 1) {
                if (!others.contains(actualGroup)) others.add(actualGroup);
            }
            else {
                others.remove(actualGroup);
                System.out.println("______________________________");
                System.out.println("'" + entry.getKey() + "'");
                for (String s: entry.getValue()) {
                    System.out.println("\t" + s);
                    inGroup.add(s);
                }
            }
        }
        System.out.println("______________________________");
        System.out.println("Others");
        for (String s : others) {
            if(!inGroup.contains(s)) System.out.println("\t" + s);
        }

    }


    public static void main(String[] args) throws InterruptedException {
        if(args.length != 1) System.out.println("Proper Usage is: nr of an artist");
        analyze(sentGet(args[0]));

    }

}
