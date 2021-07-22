import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.security.SecureRandom;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Game {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException{

        final String HMAC_ALGO = "HmacSHA3-256";
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[16];

        if(args.length<3 || args.length%2 == 0){
            System.out.println("Error arguments");
            System.out.println("Example: java Game rock paper scissors lizard Spock");
            System.out.println("Example2: java Game A B C");
            return;
        } else{
            Set <String> s = new HashSet<String>();
            for(int i=0; i<args.length; i++) {
                if(!s.add(args[i])) {
                   System.out.print("Error duplicate elements: ");
                   System.out.println(args[i]);
                   System.out.println("Please enter non-duplicate arguments");
                   return;
                }
            }
        }

        
        random.nextBytes(bytes);
        String encodedKey = new BigInteger(1, bytes).toString(16);
        Mac signer = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(bytes, HMAC_ALGO);
        signer.init(keySpec);

        Random rand = new Random();
        int pcMove = rand.nextInt(args.length);

        String messageStr = args[pcMove];
        byte[] digest = signer.doFinal(messageStr.getBytes("utf-8"));
        String encodedHmac = new BigInteger(1, digest).toString(16);


        
        System.out.println("HMAC: " + encodedHmac);

        System.out.println("Available moves:");
        for (int i = 0; i < args.length; i++) {
            System.out.println(i+1 + " - " + args[i]);
        }
        System.out.println("0 - exit");
        Scanner sc = new Scanner(System.in);
        int userMove;
        do{
            System.out.println("Enter numbers in the range from 1 - " + args.length);
            System.out.print("Enter your move:");
            while (!sc.hasNextInt()) {
                System.out.println("That's not a number!");
                System.out.print("Enter your move:");
                sc.next();
            }

            userMove = sc.nextInt();
        } while(userMove<0 || userMove>args.length);
        if(userMove == 0) {
            System.out.println("You leave. Bye");
            return;
        }

        System.out.println("Your move: " + args[userMove-1]);
        System.out.println("Computer move: " + messageStr);
        if(pcMove == userMove-1) {
            System.out.println("NH");
        } else {
            if(userMove > args.length/2) {
                if(userMove -1 - pcMove <= args.length/2 && userMove -1 - pcMove > 0){
                    System.out.println("you lose");
                } else {
                    System.out.println("you win");
                }
            } else {
                if(userMove -1 - pcMove >= args.length/-2 && userMove -1 - pcMove < 0) {
                    System.out.println("you win");
                } else {
                    System.out.println("you lose");
                }
            }
        }
        

        System.out.println("HMACKey: " + encodedKey);
    }
}
