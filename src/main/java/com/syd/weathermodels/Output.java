package com.syd.weathermodels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Output {
	File file = null;
	PrintStream console = System.out;

	FileOutputStream fos = null;
	PrintStream ps = null;

	public Output(String fileName){
		try {
			file = new File("C:\\MyJava\\spring\\sts-bundle\\pivotal-tc-server-developer-3.1.1.RELEASE\\myTcServer\\logs\\" + fileName);
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ps = new PrintStream(fos);
		System.setOut(ps);
	}

	public void redirect(String message){
		System.out.println(message);
	}
	
	public void close(){
		try {
			System.setOut(console);
			fos.close();
			ps.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
