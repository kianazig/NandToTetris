import java.io.*;

public class VMWriter {
	
	FileWriter outputFileWriter;

	public VMWriter(File outputFile) throws IOException {
		outputFileWriter = new FileWriter(outputFile);
	}
	
	public void writePush(String segment, int index) throws IOException {
		String segmentName = "";
		//if ......
		
		outputFileWriter.write("push "+segmentName+" "+index+"\n");
	}
	
	public void writePop(String segment, int index) throws IOException {
		String segmentName = "";
		//if ......
		
		outputFileWriter.write("pop "+segmentName+" "+index+"\n");
	}
	
	public void WriteArithmetic(String command) throws IOException {
		if (command.equalsIgnoreCase("ADD")) {
			outputFileWriter.write("add\n");
		}
		else if (command.equalsIgnoreCase("SUB")) {
			outputFileWriter.write("sub\n");
		}
		else if (command.equalsIgnoreCase("NEG")) {
			outputFileWriter.write("neg\n");
		}
		else if (command.equalsIgnoreCase("EQ")) {
			outputFileWriter.write("eq\n");
		}
		else if (command.equalsIgnoreCase("GT")) {
			outputFileWriter.write("gt\n");
		}
		else if (command.equalsIgnoreCase("LT")) {
			outputFileWriter.write("lt\n");
		}
		else if (command.equalsIgnoreCase("AND")) {
			outputFileWriter.write("and\n");
		}
		else if (command.equalsIgnoreCase("OR")) {
			outputFileWriter.write("or\n");
		}
		else if (command.equalsIgnoreCase("NOT")) {
			outputFileWriter.write("not\n");
		}
		
	}
	
	public void WriteLabel(String label) throws IOException {
		outputFileWriter.write("label "+label+"\n");
	}
	
	public void WriteGoto(String label) throws IOException {
		outputFileWriter.write("goto "+label+"\n");
	}
	
	public void WriteIf(String label) throws IOException{
		outputFileWriter.write("if-goto "+label+"\n");
	}
	
	public void writeCall(String name, int nArgs) {
		
	}
	
	public void writeFunction(String name, int nLocals) {
		
	}
	
	public void writeReturn() throws IOException {
		outputFileWriter.write("return\n");
	}
	
	public void close() throws IOException {
		outputFileWriter.close();
	}
}
