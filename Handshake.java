import java.util.*;
import java.net.*;
import java.util.regex.*;

public class Handshake {

    private static Map<String, String> credentials = new HashMap<String, String>();

    public static void main(String[] args) {
        //Scanner sc = new Scanner(System.in);
        System.out.println("WOL (aka. Wake On Lan) handshake!");
        System.out.println("If you do not already have an account," +
        " please register one at https://wol.sht.gr/register");

        System.out.println("Username: ");
        // checking if the username complies to legal characters
        String usernameRegex = "^((?=.*[a-z])|(?=.*[A-Z]))[a-zA-Z0-9]{5,16}$";
        Pattern usernamePattern = Pattern.compile(usernameRegex);
        boolean usernameFlag = true;
        do {
            //String username = sc.nextLine();
            String username = "geocfu"; // testing
            Matcher testInput = usernamePattern.matcher(username);
            if (testInput.matches() == true) {
                credentials.put("username", username);
                usernameFlag = false;
            }
            else {
                System.out.println("Username must be 5 to 16 characters long," +
                " and must not contain any special characters, ex.flaminglemin .");
                System.out.print("Username: ");
            }
        } while (usernameFlag);

        System.out.println("Password: ");
        // checking if the password complies to legal characters
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,32}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        boolean passwordFlag = true;
        do {
            //String password = sc.nextLine();
            String password = "Yi$66666";
            Matcher testInput = passwordPattern.matcher(password);
            if (testInput.matches() == true) {
                credentials.put("password", password);
                passwordFlag = false;
            }
            else {
                System.out.println("Password must be 8 to 32 characters long," +
                " it must contain at least one lower case and one upper case character," +
                " one number and one special character, ex. Password123@ .");
                System.out.print("Password: ");
            }
        } while (passwordFlag);

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] mac = ni.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "":""));
            }
            credentials.put("mac", sb.toString());
            //credentials.put("mac", "2C4D54036333");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        credentials.put("address", "https://wol.sht.gr/backend/handshake.php");
        // write config reader
        //send credentials used only one time for handshake
        POST handshake = new POST(credentials);
        //save all the required credentials for .properties creation
        StringBuffer stringBufferResponse = handshake.getResponse();
        String stringResponse = stringBufferResponse.toString();
        String[] response = stringResponse.split("\\,");
        credentials.put("uuid", response[1]);
        credentials.put("token", response[2]);
        PropertiesWriter configuration = new PropertiesWriter(credentials);
        System.out.println(response[0]); // testing
    }
}
