import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

	private Scanner inputFileScanner;
	private String currentCommand;

	public Parser(File inputFile){
		try {
			inputFileScanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasMoreCommands() {
		return inputFileScanner.hasNextLine();
	}
	
	public void advance() {
		String nextLine="";
		boolean lineIsComment;
		do {
			lineIsComment = false;
			nextLine = inputFileScanner.nextLine();
			if(nextLine.length()>1) {
				while(Character.isWhitespace(nextLine.charAt(0))) {
					nextLine = nextLine.substring(1);
				}
				if(nextLine.charAt(0) == '/'){
					lineIsComment = true;
				}
			}
			
		}while(nextLine.length()<1 || lineIsComment);
		currentCommand = nextLine;
		
		if (currentCommand.contains("//")) {
			currentCommand = currentCommand.substring(0, currentCommand.indexOf("/")-1);
		}
		
		while(Character.isWhitespace(currentCommand.charAt(currentCommand.length()-1))) {
			currentCommand = currentCommand.substring(0, currentCommand.length()-1);
		}
		
	}
	 
	public String commandType() {
		if (currentCommand.startsWith("pop")) {
			return "C_POP";
		}
		else if (currentCommand.startsWith("push")) {
			return "C_PUSH";
		}
		else if (currentCommand.startsWith("label")) {
			return "C_LABEL";
		}
		else if (currentCommand.startsWith("goto")) {
			return "C_GOTO";
		}
		else if (currentCommand.startsWith("if-goto")) {
			return "C_IF";
		}
		else if (currentCommand.startsWith("function")) {
			return "C_FUNCTION";
		}
		else if (currentCommand.startsWith("call")) {
			return "C_CALL";
		}
		else if (currentCommand.startsWith("return")) {
			return "C_RETURN";
		}
		else {
			return "C_ARITHMETIC";
		}
	}
	
	public String arg1() {
		String[] command = currentCommand.split(" ");
		if (command.length==1) {
			return command[0];
		}
		else {
			return command[1];
		}
	}
	
	public int arg2() {
		String[] command = currentCommand.split(" ");
		return Integer.parseInt(command[command.length-1]);
	}
	
}
