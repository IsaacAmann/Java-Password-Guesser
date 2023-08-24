import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Thread;

public class HashCheckThread extends Thread
{
	private MessageDigest messageDigest;
	private byte[] passwordHash;
	private byte[] currentStringHash;
	private int startASCIICode;
	private int endASCIICode;
	private String currentString;
	
	public HashCheckThread(byte[] passwordHash, String currentString) throws NoSuchAlgorithmException
	{
		messageDigest = MessageDigest.getInstance("SHA-256");
		this.passwordHash = passwordHash;
		this.currentString = currentString;
	}
	
	@Override
	public void run()
	{
		checkHash(currentString);
	
	}
	
	public String getCurrentHashString()
	{
		if(currentStringHash != null)
		{
			return String.format("%064x", new BigInteger(1, currentStringHash));
		}
		else
		{
			return null;
		}
		
	}
	
	private boolean checkHash(String currentString)
	{
		boolean output = false;
		messageDigest.update(currentString.getBytes(StandardCharsets.UTF_8));
		currentStringHash = messageDigest.digest();
		//System.out.println(currentString);
		System.out.println(String.format("%064x", new BigInteger(1, currentStringHash)) +" == " + String.format("%064x", new BigInteger(1, passwordHash)));
		if(Arrays.equals(currentStringHash, passwordHash))
		{
			output = true;
			PasswordGuesser.foundPassword = String.valueOf(currentString);
			PasswordGuesser.hashFound = true;
			System.out.println("Found match: " + currentString);
		}
		
		return output;
	}	
}
