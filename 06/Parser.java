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
	}
	
	public String commandType() {
		if (currentCommand.charAt(0)=='@') {
			return "A_COMMAND";
		}
		else if (currentCommand.charAt(0)=='(') {
			return "L_COMMAND";
		}
		else {
			return "C_COMMAND";
		}
	}
	
	public String symbol() {
		if (currentCommand.charAt(0)=='(') {
			return currentCommand.substring(1, currentCommand.length()-1);
		}
		else {
			while(Character.isWhitespace(currentCommand.charAt(currentCommand.length()-1))) {
				currentCommand = currentCommand.substring(0, currentCommand.length()-1);
			}
			return currentCommand.substring(1);
		}
	}
	
	public String dest() {
		String[] command = currentCommand.split("=");
		if(command.length==1) {
			return "";
		}
		while(Character.isWhitespace(command[0].charAt(0)) || Character.isWhitespace(command[0].charAt(command[0].length()-1))){
			if(Character.isWhitespace(command[0].charAt(0))) {
				command[0] = command[0].substring(1);
			}
			else if(Character.isWhitespace(command[0].charAt(command[0].length()-1))) {
				command[0] = command[0].substring(0, command[0].length()-1);
			}
		}
		return command[0];
	}
	
	public String comp() {
		String[] command0 = currentCommand.split("=");
		String[] command;
		if(command0.length>1) {
			command = command0[1].split(";");
		}
		else {
			command = command0[0].split(";");
		}
		
		while(command[0].contains("/")) {
			command[0] = command[0].substring(0, command[0].indexOf('/'));
		}
		
		while(Character.isWhitespace(command[0].charAt(0)) || Character.isWhitespace(command[0].charAt(command[0].length()-1))){
			if(Character.isWhitespace(command[0].charAt(0))) {
				command[0] = command[0].substring(1);
			}
			else if(Character.isWhitespace(command[0].charAt(command[0].length()-1))) {
				command[0] = command[0].substring(0, command[0].length()-1);
			}
		}
		return command[0];
	}
	
	public String jump() {
		String[] command = currentCommand.split(";");
		if(command.length == 1) {
			return "";
		}
		else {
			
			while(command[1].contains("/")) {
				command[1] = command[1].substring(0, command[1].indexOf('/'));
			}
			
			while(Character.isWhitespace(command[1].charAt(0)) || Character.isWhitespace(command[1].charAt(command[1].length()-1))){
				if(Character.isWhitespace(command[1].charAt(0))) {
					command[1] = command[1].substring(1);
				}
				else if(Character.isWhitespace(command[1].charAt(command[1].length()-1))) {
					command[1] = command[1].substring(0, command[1].length()-1);
				}
			}
			return command[1];
		}
	}
}
