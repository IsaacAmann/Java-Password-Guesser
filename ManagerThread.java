import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Thread;
import java.util.LinkedList;

public class ManagerThread extends Thread
{
	private ArrayList<HashCheckThread> threads;
	private LinkedList<String> workQueue;
	
	public ManagerThread(ArrayList<HashCheckThread> threads)
	{
		this.threads = threads;
		this.workQueue = new LinkedList<String>();
	}
	
	@Override 
	public void run()
	{
		//Start threads
		for(int i = 0; i < threads.size(); i++)
			threads.get(i).start();
		
		//Add work items to the queue
		iterateStringsLoop(null, PasswordGuesser.MAX_PASSWORD_LENGTH - 1);
		
		
		//Wait for HashCheckThreads to exit
		for(int i = 0; i < threads.size(); i++)
		{
			try
			{
				threads.get(i).join();
			}
			catch (InterruptedException e)
			{
				System.out.println("Interrupted");
			}
		}
	}
	
	public String getWorkItem()
	{
		if(workQueue.peekFirst() != null)
		{
			return workQueue.remove();
		}
		else
		{
			return null;
		}
	}
	
	//Add strings 
	private void iterateStringsLoop(char[] currentString, int currentIndex)
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
			//System.out.println(String.copyValueOf(currentString).trim());
			//Hash current string and stop loop if it is found
			//System.out.println(String.copyValueOf(currentString).trim());
			//System.out.println(currentString);
			workQueue.add(String.copyValueOf(currentString).trim());
			if(PasswordGuesser.hashFound == true)
			{
				currentIndex = 0;
				break;
			}
			if(currentIndex > 0)
				iterateStringsLoop(currentString, currentIndex - 1);
		}
	}	
}
