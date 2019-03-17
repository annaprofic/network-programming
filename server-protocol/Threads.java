package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Threads implements Runnable {
    private BufferedReader inFromClient;
    private DataOutputStream outFromClient;
    private long answer;
    private long currentNumber;
    private boolean correctChar;


    private static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private long checkAnswer(long currNum, long answer){
        if (currNum <= Long.MAX_VALUE - answer) return answer + currNum;
        else correctChar = false;
        return -1;
    }

    private long checkCurrent(long currNum, String str){
        long currStr = Long.parseLong(str);
        if (currNum <= Long.MAX_VALUE / 10 - currStr) return currNum * 10 + currStr;
        else correctChar = false;
        return -1;
    }

    private void checkSpace(String message, int i, long max){
        if (i == 0 || i == max - 1) correctChar = false;
        else if (message.charAt(i + 1) == ' ') correctChar = false;
        else {
            correctChar = true;
            answer = checkAnswer(currentNumber, answer);
            currentNumber = 0;
        }
    }

    private void checkAnotherChar(String currentStr){
        correctChar = false;
        if (isNumeric(currentStr)) {
            currentNumber = checkCurrent(currentNumber, currentStr);
            correctChar = true;
        }
    }


    Threads(Socket connection)throws IOException {
        inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        outFromClient = new DataOutputStream(connection.getOutputStream());
        answer = 0;
        currentNumber = 0;
        correctChar = false;
    }

    @Override
    public void run() {
        long max;
        try {
            while (inFromClient != null) {
                String message = inFromClient.readLine();
                answer = 0;
                currentNumber = 0;
                if (message != null ) max = message.length();
                else break;

                for (int i = 0; i < max; i++) {

                    if (message.charAt(i) == ' ') checkSpace(message, i, max);

                    else checkAnotherChar(Character.toString(message.charAt(i)));

                    if (!correctChar) break;
                }

                answer = checkAnswer(currentNumber, answer);

                if (correctChar && answer >= 0) {
                    currentNumber = 0;
                    outFromClient.writeUTF(answer + "\r\n");
                } else outFromClient.writeUTF("ERROR\r\n");
            }
        } catch (IOException ignored) {
        }
    }
}
