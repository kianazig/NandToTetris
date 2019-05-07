import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CompilationEngine {
	
	Scanner inputFileScanner;
	FileWriter outputFileWriter;
	String indentation;
	String nextLine;
	
	public CompilationEngine(File inputFile, File outputFile) throws IOException {
		inputFileScanner = new Scanner(inputFile);
		outputFileWriter = new FileWriter(outputFile);
		indentation = "";
		nextLine = "";
	}
	
	public void CompileClass() throws IOException {
		inputFileScanner.nextLine();
		outputFileWriter.write(indentation + "<class>\n");
		indentation += "\t";
		outputFileWriter.write(indentation + inputFileScanner.nextLine() +"\n");
		outputFileWriter.write(indentation + inputFileScanner.nextLine() +"\n");
		outputFileWriter.write(indentation + inputFileScanner.nextLine() +"\n");
		CompileClassVarDec();
		while(!nextLine.equals("<symbol> } </symbol>")) {
			CompileSubroutineDec();		
		}
		outputFileWriter.write(indentation + nextLine + "\n");
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</class>\n");
		outputFileWriter.close();
	}
	
	public void CompileClassVarDec() throws IOException {
		nextLine = inputFileScanner.nextLine();
		boolean declarationFinished = false;
		while (nextLine.equals("<keyword> static </keyword>") || nextLine.equals("<keyword> field </keyword>")) {
			outputFileWriter.write(indentation + "<classVarDec>\n");
			indentation += "\t";
			declarationFinished = false;
			outputFileWriter.write(indentation + nextLine + "\n");
			do {
				nextLine = inputFileScanner.nextLine();
				outputFileWriter.write(indentation + nextLine + "\n");
				if (nextLine.equals("<symbol> ; </symbol>")) {
					declarationFinished = true;
					indentation = indentation.replaceFirst("\t", "");
					outputFileWriter.write(indentation + "</classVarDec>\n");
				}
			}while(!declarationFinished);
			nextLine = inputFileScanner.nextLine();
		}
	}
	
	public void CompileSubroutineDec() throws IOException {
		if (nextLine.length() == 0) {
			nextLine = inputFileScanner.nextLine();
		}
		outputFileWriter.write(indentation + "<subroutineDec>\n");
		indentation += "\t";
		
		outputFileWriter.write(indentation + nextLine + "\n");
		nextLine = "";
		outputFileWriter.write(indentation + inputFileScanner.nextLine() + "\n");
		outputFileWriter.write(indentation + inputFileScanner.nextLine() + "\n");
		outputFileWriter.write(indentation + inputFileScanner.nextLine() + "\n");
		compileParameterList();
		outputFileWriter.write(indentation + nextLine + "\n");
		nextLine = inputFileScanner.nextLine();
		compileSubroutineBody();	
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</subroutineDec>\n");
	}
	
	public void compileParameterList() throws IOException {
		if (nextLine.length() == 0) {
			nextLine = inputFileScanner.nextLine();
		}
		
		outputFileWriter.write(indentation + "<parameterList>\n");
		indentation += "\t";
		
		while(!nextLine.equals("<symbol> ) </symbol>")){
			outputFileWriter.write(indentation + nextLine + "\n");
			nextLine = inputFileScanner.nextLine();
		}
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</parameterList>\n");
	}
	
	public void compileSubroutineBody() throws IOException {
		outputFileWriter.write(indentation + "<subroutineBody>\n");
		indentation += "\t";
		
		if (nextLine.length() == 0) {
			nextLine = inputFileScanner.nextLine();
		}
		outputFileWriter.write(indentation + nextLine + "\n"); //writes '{'
		nextLine = inputFileScanner.nextLine();
		
		while (nextLine.equals("<keyword> var </keyword>")) {
			compileVarDec();
		}
		do {
			compileStatements();	
		} while(!nextLine.equals("<symbol> } </symbol>"));
		
		outputFileWriter.write(indentation + nextLine + "\n"); //writes '}'
		nextLine = inputFileScanner.nextLine();
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</subroutineBody>\n");
	}
	
	public void compileVarDec() throws IOException {
		outputFileWriter.write(indentation + "<varDec>\n");
		indentation += "\t";
		
		do {
			outputFileWriter.write(indentation + nextLine + "\n");
			nextLine = inputFileScanner.nextLine();
		}while(!nextLine.equals("<symbol> ; </symbol>"));
		
		outputFileWriter.write(indentation + nextLine + "\n");
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</varDec>\n");
		
		nextLine = inputFileScanner.nextLine();
	}
	
	public void compileStatements() throws IOException {
		outputFileWriter.write(indentation + "<statements>\n");
		indentation += "\t";
		
		boolean isStatement = false;
		do {
			if (nextLine.length() == 0) {
				nextLine = inputFileScanner.nextLine();
			}
			if(nextLine.equals("<keyword> if </keyword>")) {
				isStatement = true;
				compileIf();
			}
			else if (nextLine.equals("<keyword> while </keyword>")) {
				isStatement = true;
				compileWhile();
			}
			else if (nextLine.equals("<keyword> let </keyword>")) {
				isStatement = true;
				compileLet();
			}
			else if (nextLine.equals("<keyword> do </keyword>")) {
				isStatement = true;
				compileDo();
			}
			else if (nextLine.equals("<keyword> return </keyword>")) {
				isStatement = true;
				compileReturn();
			}
			else {
				isStatement = false;
			}
		} while(isStatement);
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</statements>\n");
	}
	
	public void compileLet() throws IOException {
		outputFileWriter.write(indentation + "<letStatement>\n");
		indentation += "\t";
	
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		if (nextLine.equals("<symbol> [ </symbol>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			CompileExpression();
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
		}
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();

		CompileExpression();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</letStatement>\n");
	}
	
	public void compileIf() throws IOException {
		outputFileWriter.write(indentation + "<ifStatement>\n");
		indentation += "\t";
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		CompileExpression();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		compileStatements();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		while (nextLine.equals("<keyword> else </keyword>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			compileStatements();
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
		}		
		indentation = indentation.replaceFirst("\t", "");		
		outputFileWriter.write(indentation + "</ifStatement>\n");
	}
	
	public void compileWhile() throws IOException {
		outputFileWriter.write(indentation + "<whileStatement>\n");
		indentation += "\t";
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		CompileExpression();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		compileStatements();
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</whileStatement>\n");
	}
	
	public void compileDo() throws IOException {
		outputFileWriter.write(indentation + "<doStatement>\n");
		indentation += "\t";
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		CompileSubroutineCall();
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</doStatement>\n");
	}
	
	public void compileReturn() throws IOException {
		outputFileWriter.write(indentation + "<returnStatement>\n");
		indentation += "\t";
		
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		if (!nextLine.equals("<symbol> ; </symbol>")) {
			CompileExpression();
		}
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</returnStatement>\n");
	}
	
	public void CompileExpression() throws IOException {
		outputFileWriter.write(indentation + "<expression>\n");
		indentation += "\t";
		
		CompileTerm();
		//check if nextLine is correct
		boolean nextLineIsOp = false;
		do {
			nextLineIsOp = false;
			if (nextLine.equals("<symbol> + </symbol>") || nextLine.equals("<symbol> - </symbol>") || 
					nextLine.equals("<symbol> * </symbol>") || nextLine.equals("<symbol> / </symbol>") || 
					nextLine.equals("<symbol> &amp; </symbol>") || nextLine.equals("<symbol> | </symbol>") || 
					nextLine.equals("<symbol> &lt; </symbol>") || nextLine.equals("<symbol> &gt; </symbol>") || 
					nextLine.equals("<symbol> = </symbol>")) {
				nextLineIsOp = true;
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				CompileTerm();
			}
		}while(nextLineIsOp);	
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</expression>\n");
	}
	
	public void CompileTerm() throws IOException {
		outputFileWriter.write(indentation + "<term>\n");
		indentation += "\t";
		
		if (nextLine.endsWith("</integerConstant>") || nextLine.endsWith("</stringConstant>") 
				|| nextLine.endsWith("</keyword>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
		}
		else if (nextLine.endsWith("</identifier>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			if (nextLine.equals("<symbol> [ </symbol>")) {
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				CompileExpression();
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
			}
			else if (nextLine.equals("<symbol> ( </symbol>")) {
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				CompileExpressionList();
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
			}
			else if (nextLine.equals("<symbol> . </symbol>")) {
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				CompileExpressionList();
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
			}
		
		}
		else if (nextLine.equals("<symbol> ( </symbol>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			CompileExpression();
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
		}
		else if (nextLine.equals("<symbol> - </symbol>") || nextLine.equals("<symbol> ~ </symbol>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			CompileTerm();
		}
		
		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</term>\n");
	}
	
	private void CompileSubroutineCall() throws IOException {
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		if (nextLine.equals("<symbol> . </symbol>")) {
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
			outputFileWriter.write(indentation + nextLine +"\n");
			nextLine = inputFileScanner.nextLine();
		}
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
		CompileExpressionList();
		outputFileWriter.write(indentation + nextLine +"\n");
		nextLine = inputFileScanner.nextLine();
	}
	
	public void CompileExpressionList() throws IOException {
		outputFileWriter.write(indentation + "<expressionList>\n");
		indentation += "\t";
		
		if (!nextLine.equals("<symbol> ) </symbol>")) {
			CompileExpression();
			while(nextLine.equals("<symbol> , </symbol>")) {
				outputFileWriter.write(indentation + nextLine +"\n");
				nextLine = inputFileScanner.nextLine();
				CompileExpression();
			}
		}

		indentation = indentation.replaceFirst("\t", "");
		outputFileWriter.write(indentation + "</expressionList>\n");

	}
}
