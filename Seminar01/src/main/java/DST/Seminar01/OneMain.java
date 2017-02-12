package DST.Seminar01;

import java.util.Scanner;

public class OneMain 
{
	public static void main(String[] args) {
		boolean flag = true;		
		String line = null;
		System.out.println("HELLo World!");
		Scanner scanner = new Scanner(System.in);		
		LineParser parseLines = new LineParser();
		
		while(flag){
			System.out.println("Enter comand :");
			if(scanner.hasNextLine()){				
				line = scanner.nextLine();
			}
			if(line.equals("stop")){
				flag = false;
			} else if (!line.equals("")){
				parseLines.pars(line);
			}
		}
		scanner.close();
	}
}