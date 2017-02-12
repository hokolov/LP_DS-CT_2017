package DST.Seminar00;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ZeroMain {
	public static void main(String[] args) throws ParseException {
		System.out.println("HELLo World!");
//---------------------------------------------------------------------------------------------			
		Scanner scanner1 = new Scanner(System.in);
		System.out.print("Please, enter your name: ");
		String userName = scanner1.nextLine();
		System.out.println("Hello, " + userName + "!");
		int fact = 1;
		for(int i = 1; i<=userName.length(); i++){
			fact*=i;
		}	
		System.out.println("Your name has " + userName.length() + " letters. " 
		+ userName.length() + "! = " + fact);
//---------------------------------------------------------------------------------------------		
		Scanner scanner2 = new Scanner(System.in);
		System.out.print("Please, enter your birth date in format (DD.MM.YYYY): ");
		String userBirthDate = scanner2.next();
//---------------------------------------------------------------------------------------------		
		scanner1.close();
		scanner2.close();
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy");
	    long d1=ft.parse(ft.format(dNow)).getTime();
	    long d2=ft.parse(userBirthDate).getTime();
	    System.out.println("Today is " + ft.format(dNow) + ", you are " + (((Math.abs(d1-d2)/(1000*60*60*24))))/365 + " year (" + ((Math.abs(d1-d2)/(1000*60*60*24))) + " days) old."); //Sorry for this line, but it works :)
	}
}


//http://stackoverflow.com/questions/3526485/how-do-you-subtract-dates-in-java