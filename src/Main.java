import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    static String api_version = "1";
    static String api_date = "latest";
    static String api_scope = "currencies";

    static String API = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@"+ api_version +"/" + api_date +"/"+ api_scope;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    static JFrame frame = new JFrame("Currency Exchanger");
    static JLabel success_text = new JLabel("-");

    public static void main(String[] args) {
        openUI();
    }

    public static void openUI() {
        Integer x_all = 230;

        success_text.setBounds(x_all, 340, 300, 30);
        JLabel text = new JLabel("Gebe die Währung an die du umwandeln möchtest:");
        text.setBounds(x_all, 50, 300, 30);

        JTextField textField = new JTextField();
        textField.setBounds(x_all, 90, 300, 30);

        JLabel text2 = new JLabel("Gebe die Währung an in die du umwandeln möchtest:");
        text2.setBounds(x_all, 130, 300, 30);

        JTextField textField2 = new JTextField();
        textField2.setBounds(x_all, 170, 300, 30);

        JLabel text3= new JLabel("Gebe die Menge an die du umwandeln möchtest:");
        text3.setBounds(x_all, 210, 300, 30);

        JTextField textField3 = new JTextField();
        textField3.setBounds(x_all, 250, 300, 30);

        JButton button = new JButton("Start Exchange");
        button.setBounds(x_all, 290, 300, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String textFrom_textField = textField.getText();
               String textFrom_textField2 = textField2.getText();
               Double textFrom_textField3 = Double.parseDouble(textField3.getText());

               startRequest(textFrom_textField, textFrom_textField2, textFrom_textField3);
            }
        });

        frame.setSize(800, 600);
        frame.setLocation(100, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultLookAndFeelDecorated(true);
        frame.add(button);
        frame.add(success_text);
        frame.add(text);
        frame.add(text2);
        frame.add(text3);
        frame.add(textField);
        frame.add(textField2);
        frame.add(textField3);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static Double convertCurrency(Double amount, Double currency_amount) {
        return (amount * currency_amount);
    }

    public static void coloredPrint(String text, String color, Boolean background) {
        if (color == "g") {
            if (background == false) {
                System.out.println(ANSI_GREEN + text + ANSI_RESET);
            } else {
                System.out.println(ANSI_GREEN_BACKGROUND + text + ANSI_RESET);
            }
        } else if (color == "r") {
            if (background == false) {
                System.out.println(ANSI_RED + text + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED_BACKGROUND + text + ANSI_RESET);
            }
        }  else if (color == "b") {
            if (background == false) {
                System.out.println(ANSI_BLUE + text + ANSI_RESET);
            } else {
                System.out.println(ANSI_BLUE_BACKGROUND + text + ANSI_RESET);
            }
        }
    }

    public static void startRequest(String currency_convert, String currency_convert2, Double amount) {
        try {
            System.out.println(API + "/" + currency_convert + "/" + currency_convert2 + ".json");
            URL url = new URL(API + "/" + currency_convert + "/" + currency_convert2 + ".json");

            coloredPrint("-------- API Data ----------", "r", true);
            coloredPrint("Protocol: " + url.getProtocol(),"r", true);
            coloredPrint("Host Name: " + url.getHost(),"r", true);
            coloredPrint("Port Number: " + url.getPort(),"r", true);
            coloredPrint("File Name: " + url.getFile(),"r", true);
            coloredPrint("-------- API Data ----------", "r", true);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner urlscanner = new Scanner(url.openStream());

                while (urlscanner.hasNext()) {
                    informationString.append(urlscanner.nextLine());
                }

                urlscanner.close();

                JSONParser parser = new JSONParser();
                Object obj  = parser.parse(String.valueOf(informationString));

                JSONArray array = new JSONArray();
                array.add(obj);

                JSONObject countryData = (JSONObject) array.get(0);
                Double currency_val = (Double) countryData.get(currency_convert2);

                success_text.setText("RESPONSE: " + amount + " ("+ currency_convert +")  entsprechen " + convertCurrency(amount, currency_val) + " ("+ currency_convert2 +")");
                coloredPrint("RESPONSE: " + amount + " ("+ currency_convert +")  entsprechen " + convertCurrency(amount, currency_val) + " ("+ currency_convert2 +")", "g", true);
            }
        } catch(Exception error) {
            error.printStackTrace();
        }
    }

}