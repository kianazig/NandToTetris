import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JackTokenizer {
	
	private Scanner inputFileScanner;
	private String currentToken;
	private String tokenRemainder;
	private boolean isComment;
	
	enum tokenType {KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST};
	enum keyWord {CLASS, METHOD, FUNCTION, CONSTRUCTOR, INT, BOOLEAN,
		CHAR, VOID, VAR, STATIC, FIELD, LET, DO, IF, ELSE, WHILE, RETURN, 
		TRUE, FALSE, NULL, THIS};
	
	public JackTokenizer(File inputFile) {
		try {
			inputFileScanner = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		currentToken = "";
		tokenRemainder = "";
		isComment = false;
	}
	
	public boolean hasMoreTokens() {
		boolean tokenRemainderHasTokens = false;
		for (int i=0; i<tokenRemainder.length(); i++) {
			if (!Character.isWhitespace(tokenRemainder.charAt(i))) {
				tokenRemainderHasTokens = true;
			}
		}
		
		return inputFileScanner.hasNext() || tokenRemainderHasTokens;
	}
	
	public void advance() {
		currentToken = "";
		String nextString;
		if (tokenRemainder.length()==0) {
			nextString = inputFileScanner.nextLine();
		}
		else {
			nextString = tokenRemainder;
		}

		boolean firstQuote = true;
	
		for(int i=0; i<nextString.length(); i++) {
			char nextChar = nextString.charAt(i);
			if (isComment) {
				if (nextChar == '*' && i+1<nextString.length())
					if (nextString.charAt(i+1) == '/') {
						isComment = false;
						if (i+2<nextString.length()) {
							tokenRemainder = nextString.substring(i+2, nextString.length());
							advance();
							break;
						}
						else {
							tokenRemainder = "";
							advance();
							break;
						}
				}
			}
			else if (Character.isWhitespace(nextChar) && !firstQuote) {
				currentToken += nextChar;
			}
			else if (Character.isWhitespace(nextChar)) {
				if (i+1 < nextString.length()) {
					tokenRemainder = nextString.substring(i+1, nextString.length());
				}
				else {
					tokenRemainder = "";
				}
				break;
			}
			else {
				if (nextChar=='/' && i+1<nextString.length()) {
					if (nextString.charAt(i+1)=='/') {
						tokenRemainder = "";
						if (currentToken.length() == 0) {
							advance();
						}
						break;
					}
					if (i+2<nextString.length()) {
						if (nextString.charAt(i+1)=='*' && nextString.charAt(i+2)=='*') {
							isComment = true;
							if (i+3<nextString.length()) {
								tokenRemainder = nextString.substring(i+3, nextString.length());
							}
							else {
								tokenRemainder = "";
							}
						}
					}
				}
				
				if (!isComment) {
					if (nextChar=='{' || nextChar=='}' || nextChar=='(' || nextChar==')' || 
							nextChar=='[' || nextChar==']' || nextChar=='.' || nextChar==',' || 
							nextChar==';' || nextChar=='+' || nextChar=='-' || nextChar=='*' || 
							nextChar=='/' || nextChar=='&' || nextChar=='|' || nextChar=='<' || 
							nextChar=='>' || nextChar=='=' || nextChar=='~' ) {
						tokenRemainder = nextString.substring(i, nextString.length());
						if(currentToken.length()==0) {
								currentToken = Character.toString(nextChar);
							if (nextString.length()>1) {
								tokenRemainder = nextString.substring(1, nextString.length());
							}
							else {
								tokenRemainder = "";
							}
						}
						break;
					}
					
					if (nextChar == '"') {
						if (firstQuote) {
							firstQuote = false;	
							if (currentToken.length()!=0) {
								tokenRemainder = nextString.substring(i, nextString.length());
								break;
							}
						}
						else if (!firstQuote) {
							currentToken += nextChar;
							if (i+1 < nextString.length()) {
								tokenRemainder = nextString.substring(i+1, nextString.length());
							}
							else {
								tokenRemainder = "";
							}
							break;
						}
					}
					currentToken += nextChar;	
					
				}
			}
				
				
			
			if (i == nextString.length()-1) {
				tokenRemainder = "";
			}

		}
		
		if (currentToken.length() == 0) {
			advance();
		}		
		
	}
	
	public int tokenType() {
		switch(currentToken) {
		case "class":
		case "constructor":
		case "function":
		case "method":
		case "field":
		case "static":
		case "var":
		case "int":
		case "char":
		case "boolean":
		case "void":
		case "true":
		case "false":
		case "null":
		case "this":
		case "let":
		case "do":
		case "if":
		case "else":
		case "while":
		case "return":
			return tokenType.KEYWORD.ordinal();
		case "{":
		case "}":
		case "(":
		case ")":
		case "[":
		case "]":
		case ".":
		case ",":
		case ";":
		case "+":
		case "-":
		case "*":
		case "/":
		case "&":
		case "|":
		case "<":
		case ">":
		case "=":
		case "~":
			return tokenType.SYMBOL.ordinal();
		}
		
		boolean isInteger = true;
		for (int i=0; i<currentToken.length(); i++) {
			if(!Character.isDigit(currentToken.charAt(i))) {
				isInteger = false;
			}
		}
		if (isInteger) {
			int integerValue = Integer.parseInt(currentToken);
			if(integerValue < 32768) {
				return tokenType.INT_CONST.ordinal();
			}
		}
		
		if (currentToken.charAt(0)=='"' && currentToken.charAt(currentToken.length()-1)=='"') {
			boolean validStringConstant = true;	
			for (int i=1; i<currentToken.length()-1; i++) {
				if(currentToken.charAt(i)=='"' || currentToken.charAt(i)=='\n') {
					validStringConstant = false;
				}
			}
			if (validStringConstant) {
				return tokenType.STRING_CONST.ordinal();
			}
		}
		
		if(!Character.isDigit(currentToken.charAt(0))) {
			boolean validIdentifier = true;
			for (int i=0; i<currentToken.length(); i++) {
				if(!(Character.isLetter(currentToken.charAt(i)) || Character.isDigit(currentToken.charAt(i)) || currentToken.charAt(i)=='_')) {
					validIdentifier = false;
				}
			}
			if (validIdentifier) {
				return tokenType.IDENTIFIER.ordinal();
			}
		}
		
		return -1;
	}
	
	public int keyWord() {
		switch(currentToken) {
		case "class":
			return keyWord.CLASS.ordinal();
		case "constructor":
			return keyWord.CONSTRUCTOR.ordinal();
		case "function":
			return keyWord.FUNCTION.ordinal();
		case "method":
			return keyWord.METHOD.ordinal();
		case "field":
			return keyWord.FIELD.ordinal();
		case "static":
			return keyWord.STATIC.ordinal();
		case "var":
			return keyWord.VAR.ordinal();
		case "int":
			return keyWord.INT.ordinal();
		case "char":
			return keyWord.CHAR.ordinal();
		case "boolean":
			return keyWord.BOOLEAN.ordinal();
		case "void":
			return keyWord.VOID.ordinal();
		case "true":
			return keyWord.TRUE.ordinal();
		case "false":
			return keyWord.FALSE.ordinal();
		case "null":
			return keyWord.NULL.ordinal();
		case "this":
			return keyWord.THIS.ordinal();
		case "let":
			return keyWord.LET.ordinal();
		case "do":
			return keyWord.DO.ordinal();
		case "if":
			return keyWord.IF.ordinal();
		case "else":
			return keyWord.ELSE.ordinal();
		case "while":
			return keyWord.WHILE.ordinal();
		case "return":
			return keyWord.RETURN.ordinal();
		}
		return -1;
	}
		
	
	public char symbol() {
		if (currentToken.length()==1) {
			return currentToken.charAt(0);
		}
		return ' ';
	}
	
	public String identifier() {
		return currentToken;
	}
	
	public int intVal() {
		return Integer.parseInt(currentToken);
	}
	
	public String stringVal() {
		return currentToken.substring(1, currentToken.length()-1);
	}
		
}
