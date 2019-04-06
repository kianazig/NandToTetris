// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.


(Start_Loop)

@KBD		// A = Keyboard 
D=M			// D = Mem[Keyboard]

@Fill_White		// A = White
D;JEQ

//Set pixel control to black
@Counter	// A = Counter
M=0			// Mem[Counter] = 0
(Fill_Black_Start)
	@Counter	// A = Counter
	D=M			// D = Mem[Counter]
	@SCREEN 	// A = Screen Start Address
	D=A+D		// A = Current Screen Pixel
	
	@KBD		// Keyboard Start Address
	D=D-A		// Checks if Current Adr = Keyboard Adr
	@Fill_White_End	// Stop filling if true
	D;JEQ
	
	@Counter	// A = Counter
	D=M			// D = Mem[Counter]
	@SCREEN 	// A = Screen Start Address
	A=A+D		// A = Current Screen Pixel
	M=-1		// Set Current Screen Pixel Black
	
	@Counter	// A = Counter
	M=M+1		// Increment Counter

	@Fill_Black_Start	// Loop
	0;JMP
(Fill_Black_End)
	@Fill_White_End	// Skip Fill White
	0;JMP
	
//Set pixel control to white
(Fill_White)
@Counter	// A = Counter
M=0			// Mem[Counter] = 0
(Fill_White_Start)
	@Counter	// A = Counter
	D=M			// D = Mem[Counter]
	@SCREEN 	// A = Screen Start Address
	D=A+D		// A = Current Screen Pixel
	
	@KBD		// Keyboard Start Address
	D=D-A		// Checks if Current Adr = Keyboard Adr
	@Fill_White_End	// Stop filling if true
	D;JEQ
	
	@Counter	// A = Counter
	D=M			// D = Mem[Counter]
	@SCREEN 	// A = Screen Start Address
	A=A+D		// A = Current Screen Pixel
	M=0			// Set Current Screen Pixel Black
	
	@Counter	// A = Counter
	M=M+1		// Increment Counter

	@Fill_White_Start	// Loop
	0;JMP
(Fill_White_End)

@Start_Loop		// A = Start Loop
0;JMP
//Jump to Start Loop