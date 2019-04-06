// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

@1		// A = R1
D=M 	// D = Mem[R1]
@counter// A = counter (random memory address)
M=D		// Mem[counter] = D = Mem[R1]

@0		// A = 0
D=A		// D = 0
@2		// A = R2
M=D		// Mem[R2] = 0

(Start_Loop)
	
	@counter	// A = counter
	D=M			// D = Mem[counter]
	@End_Loop	// A = End_Loop
	D;JEQ		
	// If D = 0, Jump to End_Loop
	
	@0			// A = R0
	D=M			// D = Mem[R0]
	@2			// A = R2
	M=D+M		// Mem[R2] = Mem[R0] + Mem[R2]
	
	@counter	// A = counter
	M=M-1		// Mem[counter] = Mem[counter] - 1
	
	@Start_Loop	// A = Start_Loop
	0;JMP
	// Jump to Start_Loop
	
(End_Loop)

// Terminate Program Safely
(Terminate)
@Terminate
0;JMP