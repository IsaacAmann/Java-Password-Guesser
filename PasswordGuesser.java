import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Thread;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

public class PasswordGuesser
{
	public final static int MAX_PASSWORD_LENGTH = 2;
	public final static int MAX_ASCII_CODE = 125;
	public final static int NUM_THREADS = 3;
	
	public static boolean hashFound = false;
	public static String foundPassword;
	public static byte[] passwordHash;
	public static char[] passwordSpace = new char[MAX_PASSWORD_LENGTH];
	public static MessageDigest messageDigest;
	public static ExecutorService threadPool;
	
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		messageDigest = MessageDigest.getInstance("SHA-256");
		String text = args[0];
		messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
		passwordHash = messageDigest.digest();
		
		threadPool = Executors.newFixedThreadPool(NUM_THREADS);
		
		String hex = String.format("%064x", new BigInteger(1, passwordHash));
		System.out.println("Hash testing for: " +  hex);
		
		iterateStringsLoop(null, MAX_PASSWORD_LENGTH - 1);
		
		//Wait for threadpool to shutdown
		try
		{
			threadPool.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch(InterruptedException e)
		{
			
		}
		System.out.println("Found password: " + foundPassword);
	}

	//Add strings 
	private static void iterateStringsLoop(char[] currentString, int currentIndex)
	{
		
		//Statements to be run in the first iteration
		if(currentString == null)
		{
			currentString = new char[PasswordGuesser.MAX_PASSWORD_LENGTH];
			//Set all chars to null character
			for(int i = 0; i < currentString.length; i++)
				currentString[i] = 0;
		}

		for(int i = 0; i < PasswordGuesser.MAX_ASCII_CODE; i++)
		{
			//Skipping characters between 0 and 33
			if(i > 0 && i < 32)
				i = 33;
			currentString[currentIndex] = (char)i;
			try
			{
				//Pass string to thread pool to be hashed and checked against the password hash
				threadPool.execute(new HashCheckThread(passwordHash, String.copyValueOf(currentString).trim()));
			}
			catch(Exception exception)
			{
				//exception.printStackTrace();
			}
			if(PasswordGuesser.hashFound == true)
			{
				currentIndex = 0;
				threadPool.shutdownNow();
				break;
			}
			if(currentIndex > 0)
				iterateStringsLoop(currentString, currentIndex - 1);
		}
	}
}
	
