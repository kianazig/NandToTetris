import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
	File outputFile;
	FileWriter outputFileWriter;
	int labelNumber;
	String fileName;
	String fileNameWithoutExtension;
	int returnLabelNumber;

	public CodeWriter(File outputFile) throws IOException {
		this.outputFile = outputFile;
		outputFileWriter = new FileWriter(outputFile);
		labelNumber = 0;
		returnLabelNumber = 0;
	
		writeInit();
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		fileNameWithoutExtension = fileName.replace(".vm", "");
	}
	
	public void writeInit() throws IOException {
		//SP = 256
		outputFileWriter.write("@256\n");
		outputFileWriter.write("D=A\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=D\n");
		
		//call Sys.init
		writeCall("Sys.init", 0);
	}
	
	public void writeLabel(String label) throws IOException {
		outputFileWriter.write("("+label+")\n");
	}
	
	public void writeGoto(String label) throws IOException {
		outputFileWriter.write("@"+label+"\n");
		outputFileWriter.write("0;JMP\n");
	}
	
	public void writeIf(String label) throws IOException {
		//pop top value of the stack
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("AM=M-1\n");
		outputFileWriter.write("D=M\n");
		
		//if it's not equal to zero, jump
		outputFileWriter.write("@"+label+"\n");
		outputFileWriter.write("D;JNE\n");
	}
	
	public void writeFunction(String functionName, int numVars) throws IOException {
		//generate function label
		outputFileWriter.write("("+functionName+")\n");
		
		//initialize the local variables to 0
		for (int i=0; i<numVars; i++) {
			outputFileWriter.write("@0\n");
			outputFileWriter.write("D=A\n");
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M\n");
			outputFileWriter.write("M=D\n");
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("M=M+1\n");
		}
		
	}
	
	public void writeCall(String functionName, int numArgs) throws IOException {
		outputFileWriter.write("@"+fileNameWithoutExtension+"."+functionName+"$ret."+returnLabelNumber+"\n");
		
		//push retAddrLabel
		outputFileWriter.write("D=A\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("M=D\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=M+1\n");
	
		//push LCL
		outputFileWriter.write("@LCL\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("M=D\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=M+1\n");
		
		//push ARG
		outputFileWriter.write("@ARG\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("M=D\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=M+1\n");
		
		//push THIS
		outputFileWriter.write("@THIS\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("M=D\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=M+1\n");
		
		//push THAT
		outputFileWriter.write("@THAT\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("M=D\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=M+1\n");
		
		//ARG = SP - 5 - nArgs
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@5\n");
		outputFileWriter.write("D=D-A\n");
		outputFileWriter.write("@"+Integer.toString(numArgs)+"\n");
		outputFileWriter.write("D=D-A\n");
		outputFileWriter.write("@ARG\n");
		outputFileWriter.write("M=D\n");
		
		//LCL = SP
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@LCL\n");
		outputFileWriter.write("M=D\n");
		
		//goto functionName
		outputFileWriter.write("@"+functionName+"\n");
		outputFileWriter.write("0;JMP\n");
		
		//(retAddrLabel)
		outputFileWriter.write("("+fileNameWithoutExtension+"."+functionName+"$ret."+returnLabelNumber+")\n");
	
		returnLabelNumber++;
	}
	
	public void writeReturn() throws IOException {		
		//store endFrame in R14 -- endFrame = LCL
		outputFileWriter.write("@LCL\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("M=D\n");
		
		//store retAddr in R15 -- retAddr = *(endFrame - 5)
		outputFileWriter.write("@5\n");
		outputFileWriter.write("D=A\n");
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("A=M-D\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@R15\n");
		outputFileWriter.write("M=D\n");
		
		//*ARG = pop()
		writePushPop("C_POP", "argument", 0);
		
		//SP = ARG + 1
		outputFileWriter.write("@ARG\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("D=D+1\n");
		outputFileWriter.write("@SP\n");
		outputFileWriter.write("M=D\n");
		
		//THAT = *(endFrame - 1)
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("A=M-1\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@THAT\n");
		outputFileWriter.write("M=D\n");
		
		//THIS = *(endFrame - 2)
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@2\n");
		outputFileWriter.write("A=D-A\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@THIS\n");
		outputFileWriter.write("M=D\n");
		
		//ARG = *(endFrame - 3)
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@3\n");
		outputFileWriter.write("A=D-A\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@ARG\n");
		outputFileWriter.write("M=D\n");
		
		//LCL = *(endFrame - 4)
		outputFileWriter.write("@R14\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@4\n");
		outputFileWriter.write("A=D-A\n");
		outputFileWriter.write("D=M\n");
		outputFileWriter.write("@LCL\n");
		outputFileWriter.write("M=D\n");
		
		//goto retAddr
		outputFileWriter.write("@R15\n");
		outputFileWriter.write("A=M\n");
		outputFileWriter.write("0;JMP\n");
	}
	
	public void writeArithmetic(String command) throws IOException {
		if (command.equalsIgnoreCase("add")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//add next stack value and place result on stack
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=D+M\n");
		}
		else if (command.equalsIgnoreCase("sub")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//subtract next stack value and place result on stack
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=M-D\n");
		}
		else if (command.equalsIgnoreCase("neg")) {
			//take value on stack and negate it
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=-M\n");
		}
		else if (command.equalsIgnoreCase("eq")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//peek at next stack value
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			
			//D = difference of two stack values
			outputFileWriter.write("D=M-D\n");
			
			//place -1 on stack (true)
			outputFileWriter.write("M=-1\n");
			
			//if D = 0, jump to LABEL.i
			outputFileWriter.write("@LABEL."+Integer.toString(labelNumber)+"\n");
			outputFileWriter.write("D;JEQ\n");
			
			//else, set top of stack to 0 (false)
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=0\n");
			
			outputFileWriter.write("(LABEL."+Integer.toString(labelNumber)+")\n");
			labelNumber++;
		}
		else if (command.equalsIgnoreCase("gt")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//peek at next stack value
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			
			//D = difference of two stack values
			outputFileWriter.write("D=M-D\n");
			
			//place -1 on stack (true)
			outputFileWriter.write("M=-1\n");
			
			//if difference is greater than 0, jump to LABEL.i
			outputFileWriter.write("@LABEL."+Integer.toString(labelNumber)+"\n");
			outputFileWriter.write("D;JGT\n");
			
			//else, set top of stack to 0 (false)
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=0\n");
			
			outputFileWriter.write("(LABEL."+Integer.toString(labelNumber)+")\n");
			labelNumber++;
		}
		else if (command.equalsIgnoreCase("lt")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//peek at next stack value
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			
			//D = difference of two stack values
			outputFileWriter.write("D=M-D\n");
			
			//place -1 on the stack (true)
			outputFileWriter.write("M=-1\n");
			
			//if difference is less than 0, jump to LABEL.i
			outputFileWriter.write("@LABEL."+Integer.toString(labelNumber)+"\n");
			outputFileWriter.write("D;JLT\n");
			
			//else, set top of stack to 0 (false)
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=0\n");
			
			outputFileWriter.write("(LABEL."+Integer.toString(labelNumber)+")\n");
			labelNumber++;
		}
		else if (command.equalsIgnoreCase("and")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//perform AND on two stack values, place result on stack
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=D&M\n");
		}
		else if (command.equalsIgnoreCase("or")) {
			//decrement Stack Pointer, D = mem[SP]
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("AM=M-1\n");
			outputFileWriter.write("D=M\n");
			
			//perform OR on two stack values, place result on stack
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=D|M\n");
		}
		else if (command.equalsIgnoreCase("not")) {
			//perform NOT on top stack value, place result on stack
			outputFileWriter.write("@SP\n");
			outputFileWriter.write("A=M-1\n");
			outputFileWriter.write("M=!M\n");
		}
	}
	
	public void writePushPop(String commandType, String segment, int index) throws IOException {
		if (commandType.equalsIgnoreCase("C_PUSH")) {
		    if (segment.equalsIgnoreCase("local")) {
		    	//D = index
		    	outputFileWriter.write("@"+index+"\n");
		    	outputFileWriter.write("D=A\n");
		    	
		    	//D = mem[baseAdr(LCL) + index]
		    	outputFileWriter.write("@LCL\n");
		    	outputFileWriter.write("A=M+D\n");
		    	outputFileWriter.write("D=M\n");
		    }
		    else if (segment.equalsIgnoreCase("argument")) {
		    	//D = index
		    	outputFileWriter.write("@"+index+"\n");
		    	outputFileWriter.write("D=A\n");
		    	
		    	//D = mem[baseAdr(ARG) + index]
		    	outputFileWriter.write("@ARG\n");
		    	outputFileWriter.write("A=M+D\n");
		    	outputFileWriter.write("D=M\n");
		    }
		    else if (segment.equalsIgnoreCase("this")) {
		    	//D = index
		    	outputFileWriter.write("@"+index+"\n");
		    	outputFileWriter.write("D=A\n");
		    	
		    	//D = mem[baseAdr(THIS) + index]
		    	outputFileWriter.write("@THIS\n");
		    	outputFileWriter.write("A=M+D\n");
		    	outputFileWriter.write("D=M\n");
		    }
		    else if (segment.equalsIgnoreCase("that")) {
		    	//D = index
		    	outputFileWriter.write("@"+index+"\n");
		    	outputFileWriter.write("D=A\n");
		    	
		    	//D = mem[baseAdr(THAT) + index]
		    	outputFileWriter.write("@THAT\n");
		    	outputFileWriter.write("A=M+D\n");
		    	outputFileWriter.write("D=M\n");
		    }
			else if (segment.equalsIgnoreCase("constant")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
			}
			else if (segment.equalsIgnoreCase("static")) {
				//D = mem[Foo.i]
				outputFileWriter.write("@"+fileNameWithoutExtension+"."+index+"\n");
				outputFileWriter.write("D=M\n");;
			}
			else if (segment.equalsIgnoreCase("temp")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
		    	outputFileWriter.write("D=A\n");
		    	
		    	//D = mem[5 + index]
		    	outputFileWriter.write("@5\n");
		    	outputFileWriter.write("A=A+D\n");
		    	outputFileWriter.write("D=M\n");
			}
			else if (segment.equalsIgnoreCase("pointer")) {
				if (index == 0) {
					//accessing pointer 0 should result in accessing THIS
					
			    	//D = mem[baseAdr(THIS)]
			    	outputFileWriter.write("@THIS\n");
			    	outputFileWriter.write("D=M\n");
				}
				else {
					//accessing pointer 1 should result in accessing THAT
					
					//D = mem[baseAdr(THAT)]
			    	outputFileWriter.write("@THAT\n");
			    	outputFileWriter.write("D=M\n");
				}
			}
		

		    //put value of D onto Stack
			outputFileWriter.write("@SP\n");
	    	outputFileWriter.write("A=M\n");
	    	outputFileWriter.write("M=D\n");
	    	
	    	//increment Stack Pointer
	    	outputFileWriter.write("@SP\n");
	    	outputFileWriter.write("M=M+1\n");
		}
		else if (commandType.equalsIgnoreCase("C_POP")) {
			if(segment.equalsIgnoreCase("argument")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
				
				//D = baseAdr(ARG) + index
				outputFileWriter.write("@ARG\n");
				outputFileWriter.write("D=M+D\n");
				
				//mem[R13] = baseAdr(ARG + index)
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("M=D\n");
				
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[baseAdr(ARG) + index] = D
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("A=M\n");
				outputFileWriter.write("M=D\n");
			}
			else if (segment.equalsIgnoreCase("local")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
				
				//D = baseAdr(LCL) + index
				outputFileWriter.write("@LCL\n");
				outputFileWriter.write("D=M+D\n");
				
				//mem[R13] = baseAdr(LCL) + index
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("M=D\n");
				
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[baseAdr(LCL) + index] = D
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("A=M\n");
				outputFileWriter.write("M=D\n");
			}
			else if (segment.equalsIgnoreCase("static")) {
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[Foo.i] = D
				outputFileWriter.write("@"+fileNameWithoutExtension+"."+index+"\n");
				outputFileWriter.write("M=D\n");
			}
			else if (segment.equalsIgnoreCase("this")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
				
				//D = baseAdr(this) + index
				outputFileWriter.write("@THIS\n");
				outputFileWriter.write("D=M+D\n");
				
				//mem[R13] = baseAdr(this) + index
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("M=D\n");
				
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[baseAdr(this) + index] = D
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("A=M\n");
				outputFileWriter.write("M=D\n");
			}
			else if (segment.equalsIgnoreCase("that")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
				
				//D = baseAdr(that) + index
				outputFileWriter.write("@THAT\n");
				outputFileWriter.write("D=M+D\n");
				
				//mem[R13] = baseAdr(that) + index
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("M=D\n");
				
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[baseAdr(that) + index] = D
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("A=M\n");
				outputFileWriter.write("M=D\n");
			}
			else if (segment.equalsIgnoreCase("pointer")) {
				if (index == 0) {					
					//accessing pointer 0 should result in accessing THIS
					
					//decrement Stack Pointer, D = mem[SP]
					outputFileWriter.write("@SP\n");
					outputFileWriter.write("AM=M-1\n");
					outputFileWriter.write("D=M\n");
					
					//mem[baseAdr(this)] = D
					outputFileWriter.write("@THIS\n");
					outputFileWriter.write("M=D\n");
				}
				else {
					//accessing pointer 1 should result in accessing THAT
									
					//decrement Stack Pointer, D = mem[SP]
					outputFileWriter.write("@SP\n");
					outputFileWriter.write("AM=M-1\n");
					outputFileWriter.write("D=M\n");
					
					//mem[baseAdr(that)] = D
					outputFileWriter.write("@THAT\n");
					outputFileWriter.write("M=D\n");
				}
			}
			else if (segment.equalsIgnoreCase("temp")) {
				//D = index
				outputFileWriter.write("@"+index+"\n");
				outputFileWriter.write("D=A\n");
				
				//D = index + 5
				outputFileWriter.write("@5\n");
				outputFileWriter.write("D=A+D\n");
				
				//mem[R13] = index + 5
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("M=D\n");
				
				//decrement Stack Pointer, D = mem[SP]
				outputFileWriter.write("@SP\n");
				outputFileWriter.write("AM=M-1\n");
				outputFileWriter.write("D=M\n");
				
				//mem[index + 5] = D
				outputFileWriter.write("@R13\n");
				outputFileWriter.write("A=M\n");
				outputFileWriter.write("M=D\n");
			}
		}
	}
	
	public void close() throws IOException {
		outputFileWriter.close();
	}
}
