package com.xbb.processes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateProcesses {

	public enum Ways
	{
		PROCESS,PROCESSBUILDER;
	}
	
	public static void main(String[] args) {
		
		//关机!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		Ways ways = Ways.PROCESS;
		
		//取消关机
//		Ways ways = Ways.PROCESSBUILDER;

		Process p = null;
	    ProcessBuilder pb = null;  
		BufferedReader stdout = null;
		String line = null;
		
		switch (ways)
		{
		case PROCESS:
			try {
				p = Runtime.getRuntime().exec("cmd /c shutdown -s -t 900", null, new File("C:\\"));
				stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = stdout.readLine()) != null) {
					System.out.println(line);
				}
				stdout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		break;
		case PROCESSBUILDER:
			try
			{
			List<String> list = new ArrayList<String>();  
		     
		    list.add("cmd");  
		    list.add("/c");  
		    list.add("shutdown -a");  
		    pb = new ProcessBuilder(list);  
		    pb.directory(new File("C:\\"));  
		    p = pb.start();  
		     
		    stdout = new BufferedReader(new InputStreamReader(p  
		      .getInputStream()));  
		    while ((line = stdout.readLine()) != null) {  
		     System.out.println(line);  
		    }  
		    stdout.close();  
			}
			catch (Exception e) {}
		}
	}  
}
