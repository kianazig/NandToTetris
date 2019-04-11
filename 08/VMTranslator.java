import java.io.File;
import java.io.IOException;

public class VMTranslator {
	
	public static void main(String[] args) throws IOException {
		
		File inputFile = null, outputFile = null;
		
		String inputString = "";
		for(int i=0; i<args.length; i++) {
			inputString += args[i];
		}
		
		File inputFilePath = new File(inputString);
		CodeWriter codeWriter;
		
		if (inputFilePath.isDirectory()) {
			File[] allDirectoryFiles = inputFilePath.listFiles();
			outputFile = new File(inputString+"\\"+inputFilePath.getName()+".asm");
			outputFile.createNewFile();
			codeWriter = new CodeWriter(outputFile);
			for(int i=0; i<allDirectoryFiles.length; i++) {
				if (allDirectoryFiles[i].getName().endsWith(".vm")) {
					inputFile = allDirectoryFiles[i];
					runTranslation(inputFile, codeWriter);
				}
			}
		}
		else {
			inputFile = inputFilePath;
			outputFile = new File(inputString.replace(".vm", ".asm"));
			outputFile.createNewFile();
			codeWriter = new CodeWriter(outputFile);
			runTranslation(inputFile, codeWriter);
		}
		
		codeWriter.close();
	}
	
	private static void runTranslation(File inputFile, CodeWriter codeWriter) throws IOException {
		Parser parser = new Parser(inputFile);
		
		codeWriter.setFileName(inputFile.getName());
		
		while(parser.hasMoreCommands()) {
			parser.advance();
			String commandType = parser.commandType();
			String arg1 = "";
			int arg2 = 0;
			if(!commandType.equalsIgnoreCase("C_RETURN")) {
				arg1 = parser.arg1();
			}
			if (commandType.equalsIgnoreCase("C_PUSH") || 
					commandType.equalsIgnoreCase("C_POP") ||
					commandType.equalsIgnoreCase("C_FUNCTION") ||
					commandType.equalsIgnoreCase("C_CALL")) {
				arg2 = parser.arg2();
			}
			
			if (commandType.equalsIgnoreCase("C_PUSH")) {
				codeWriter.writePushPop("C_PUSH", arg1, arg2);
			}
			else if (commandType.equalsIgnoreCase("C_POP")) {
				codeWriter.writePushPop("C_POP", arg1, arg2);
			}
			else if (commandType.equalsIgnoreCase("C_ARITHMETIC")){
				codeWriter.writeArithmetic(arg1);
			}
			else if (commandType.equalsIgnoreCase("C_FUNCTION")) {
				codeWriter.writeFunction(arg1, arg2);
			}
			else if (commandType.equalsIgnoreCase("C_CALL")) {
				codeWriter.writeCall(arg1, arg2);
			}
			else if (commandType.equalsIgnoreCase("C_RETURN")) {
				codeWriter.writeReturn();
			}
			else if (commandType.equalsIgnoreCase("C_LABEL")) {
				codeWriter.writeLabel(arg1);
			}
			else if (commandType.equalsIgnoreCase("C_GOTO")) {
				codeWriter.writeGoto(arg1);
			}
			else if (commandType.equalsIgnoreCase("C_IF")) {
				codeWriter.writeIf(arg1);
			}
		}
	}
	
}
