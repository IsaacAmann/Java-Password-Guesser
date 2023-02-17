import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class PasswordGuesser
{
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		String text = args[0];
		
		byte[] digest = messageDigest.digest();
		
		String hex = String.format("%064x", new BigInteger(1, digest));
		System.out.println(hex);
		
		//Check if arrays equal Array.equal(a,b)
		
	}
}
