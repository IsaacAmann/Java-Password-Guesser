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
		this.startASCIICode = startASCIICode;
		this.endASCIICode = endASCIICode;
		this.currentString = currentString;
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
	
	@Override
	public void run()
	{
		checkHash(currentString);
	
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
	
	private void hashCheckLoop(char[] currentString, int currentIndex)
	{
		//Statements to be run in the first iteration
		if(currentString == null)
		{
			currentString = new char[PasswordGuesser.MAX_PASSWORD_LENGTH];
			//Set all chars to null character
			for(int i = 0; i < currentString.length; i++)
				currentString[i] = 0;
		}

		for(int i = startASCIICode; i < endASCIICode; i++)
		{
			//Skipping characters between 0 and 33
			if(i > 0 && i < 32)
				i = 33;
			currentString[currentIndex] = (char)i;
			//System.out.println(String.copyValueOf(currentString).trim());
			//Hash current string and stop loop if it is found
			//System.out.println(String.copyValueOf(currentString).trim());
			if(checkHash(String.copyValueOf(currentString).trim()) == true)
			{
				currentIndex = 0;
				PasswordGuesser.hashFound = true;
			}
			if(currentIndex > 0)
				hashCheckLoop(currentString, currentIndex - 1);
		}
	}	
}
