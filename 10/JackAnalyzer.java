import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JackAnalyzer {

	public static void main(String[] args) throws IOException {
	File inputFile = null, tokenOutputFile = null, outputFile = null;
		
		String inputString = "";
		for(int i=0; i<args.length; i++) {
			inputString += args[i];
		}
		
		File inputFilePath = new File(inputString);
		
		if (inputFilePath.isDirectory()) {
			File[] allDirectoryFiles = inputFilePath.listFiles();
			for(int i=0; i<allDirectoryFiles.length; i++) {
				if (allDirectoryFiles[i].getName().endsWith(".jack")) {
					inputFile = allDirectoryFiles[i];
					tokenOutputFile = new File(inputFile.getAbsolutePath().replace(".jack", "T.xml"));
					tokenOutputFile.createNewFile();
					runJackTokenizer(inputFile, tokenOutputFile);
					outputFile = new File(inputFile.getAbsolutePath().replace(".jack", ".xml"));
					outputFile.createNewFile();
					CompilationEngine compilationEngine = new CompilationEngine(tokenOutputFile, outputFile);
					compilationEngine.CompileClass();
				}
			}
		}
		else {
			inputFile = inputFilePath;
			tokenOutputFile = new File(inputString.replace(".jack", "T.xml"));
			tokenOutputFile.createNewFile();
			runJackTokenizer(inputFile, tokenOutputFile);
			outputFile = new File(inputString.replace(".jack", ".xml"));
			outputFile.createNewFile();

			CompilationEngine compilationEngine = new CompilationEngine(tokenOutputFile, outputFile);
			compilationEngine.CompileClass();
		}
	}
	
	private static void runJackTokenizer(File inputFile, File outputFile) throws IOException {
		JackTokenizer jackTokenizer = new JackTokenizer(inputFile);
		FileWriter outputFileWriter = new FileWriter(outputFile);
		
		outputFileWriter.write("<tokens>\n");
		while(jackTokenizer.hasMoreTokens()) {
			jackTokenizer.advance();
			int tokenType = jackTokenizer.tokenType();
			if(tokenType == 0) {
				int keyWord = jackTokenizer.keyWord();
				outputFileWriter.write("<keyword> "+getKeyWord(keyWord)+" </keyword>\n");
			}
			else if (tokenType == 1) {
				String symbol = Character.toString(jackTokenizer.symbol());
				if (symbol.equals("<")) { symbol = "&lt;"; }
				else if (symbol.equals(">")) { symbol = "&gt;"; }
				else if (symbol.equals("&")) { symbol = "&amp;"; }
				outputFileWriter.write("<symbol> "+ symbol +" </symbol>\n");
			}
			else if (tokenType == 2) {
				outputFileWriter.write("<identifier> "+ jackTokenizer.identifier() +" </identifier>\n");
			}
			else if (tokenType == 3) {
				outputFileWriter.write("<integerConstant> "+ jackTokenizer.intVal() +" </integerConstant>\n");
			}
			else if (tokenType == 4) {
				outputFileWriter.write("<stringConstant> "+ jackTokenizer.stringVal() +" </stringConstant>\n");
			}
		}
		outputFileWriter.write("</tokens>\n");
		outputFileWriter.close();
	}

	private static String getKeyWord(int keyWord) {
		if (keyWord == 0) { return "class"; }
		else if (keyWord == 1) { return "method"; }
		else if (keyWord == 2) { return "function"; }
		else if (keyWord == 3) { return "constructor"; }
		else if (keyWord == 4) { return "int"; }
		else if (keyWord == 5) { return "boolean"; }
		else if (keyWord == 6) { return "char"; }
		else if (keyWord == 7) { return "void"; }
		else if (keyWord == 8) { return "var"; }
		else if (keyWord == 9) { return "static"; }
		else if (keyWord == 10) { return "field"; }
		else if (keyWord == 11) { return "let"; }
		else if (keyWord == 12) { return "do"; }
		else if (keyWord == 13) { return "if"; }
		else if (keyWord == 14) { return "else"; }
		else if (keyWord == 15) { return "while"; }
		else if (keyWord == 16) { return "return"; }
		else if (keyWord == 17) { return "true"; }
		else if (keyWord == 18) { return "false"; }
		else if (keyWord == 19) { return "null"; }
		else if (keyWord == 20) { return "this"; }
		else { return ""; }
	}

}
