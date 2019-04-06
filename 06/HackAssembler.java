import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HackAssembler {
	
	static File inputFile, outputFile;
	static FileWriter outputFileWriter;
	static SymbolTable symbolTable;
	static Code code = new Code();
	
	
	public static void main(String[] args) throws IOException {
		String inputFileNameWithExtension = "";
		for(int i=0; i<args.length; i++) {
			inputFileNameWithExtension += args[i];
		}
		
		String inputFileNameWithoutExtension = inputFileNameWithExtension.replaceAll(".asm", "");
				
		inputFile = new File(inputFileNameWithExtension);
		outputFile = new File(inputFileNameWithoutExtension+".hack");
		outputFile.createNewFile();
		outputFileWriter = new FileWriter(outputFile);
		
		initialization();
		firstPass();
		secondPass();
		
		outputFileWriter.close();
	}

	private static void secondPass() throws IOException {
		Parser parser = new Parser(inputFile);
		int nextMemoryAddress = 16;
		String nextCommand="";
		
		while(parser.hasMoreCommands()) {
			parser.advance();
			if(parser.commandType().equalsIgnoreCase("A_COMMAND")) {
				boolean symbolIsInteger = true;
				String symbol = parser.symbol();
				int memoryAddress;
				for(int i=0; i<symbol.length(); i++) {
					if(!Character.isDigit(symbol.charAt(i))) {
						symbolIsInteger = false;
					}
				}
				
				
				if (symbolIsInteger) {
					memoryAddress = Integer.parseInt(symbol);
				}
				else if(symbolTable.contains(symbol)) {
					memoryAddress = symbolTable.getAddress(symbol);
				}
				else {
					memoryAddress = nextMemoryAddress;
					symbolTable.addEntry(symbol, memoryAddress);
					nextMemoryAddress++;
				}
				
				nextCommand = Integer.toBinaryString(memoryAddress);
				while(nextCommand.length()<16) {
					nextCommand = "0" + nextCommand;
				}
				outputFileWriter.write(nextCommand+"\n");
			}
			else if(parser.commandType().equalsIgnoreCase("C_COMMAND")) {
				String comp = code.comp(parser.comp());
				String dest = code.dest(parser.dest());
				String jump = code.jump(parser.jump());
				nextCommand = "111"+comp+dest+jump;
				outputFileWriter.write(nextCommand+"\n");
			}
			
		}
	}

	private static void firstPass() {
		Parser parser = new Parser(inputFile);
		int instructionNumber = 0;
		
		while(parser.hasMoreCommands()) {
			parser.advance();
			if(parser.commandType().equalsIgnoreCase("L_COMMAND")){
				symbolTable.addEntry(parser.symbol(), instructionNumber);
			}
			else {
				instructionNumber++;
			}
		}
		
	}

	private static void initialization() {
		symbolTable = new SymbolTable();
		symbolTable.addEntry("R0", 0);
		symbolTable.addEntry("R1", 1);
		symbolTable.addEntry("R2", 2);
		symbolTable.addEntry("R3", 3);
		symbolTable.addEntry("R4", 4);
		symbolTable.addEntry("R5", 5);
		symbolTable.addEntry("R6", 6);
		symbolTable.addEntry("R7", 7);
		symbolTable.addEntry("R8", 8);
		symbolTable.addEntry("R9", 9);
		symbolTable.addEntry("R10", 10);
		symbolTable.addEntry("R11", 11);
		symbolTable.addEntry("R12", 12);
		symbolTable.addEntry("R13", 13);
		symbolTable.addEntry("R14", 14);
		symbolTable.addEntry("R15", 15);
		symbolTable.addEntry("SCREEN", 16384);
		symbolTable.addEntry("KBD", 24576);
		symbolTable.addEntry("SP", 0);
		symbolTable.addEntry("LCL", 1);
		symbolTable.addEntry("ARG", 2);
		symbolTable.addEntry("THIS", 3);
		symbolTable.addEntry("THAT", 4);
	}

}
