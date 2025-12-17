# Fixed2025?

When my Compiler Construction course wrapped up in the fall of 2024, my MiniJava Compilers front end was still pretty rough around the edges, the type checker wasnt quite fully functional, and could crash/hang under certain circumstances implementation and and the parser had a few bugs to work our (mostly with 2d arrays). Though I may have left this project for a while, my knowledge of and love for compilers has only grown. Unable to let this unfinished project go, I returned to it a year later in December 2025 to finish the front end. I decided to leave the main branch an honest representation of what I was able to acheive that semester and pushed the complete version to a seperate "Fixed2025" branch.

# MiniJava Compiler

This is a complete frontend for a MiniJava Compiler written in Java targeting specifically the Embedded Xinu Operating System. The front end includes a tokenizer, parser/syntactic analyzer, and a four-pass type checker. This was written for my compiler course taken at Marquette University (COSC 4400, fall 24) as a part of a group effort alongside Erik Gutierrez, Varisha Asim, and myself with guidance from our professor Dr.Jack Forden.

## Language Support

#### Object-Oriented Programming

- Class declarations with single inheritance
- Method definitions
- Field declarations and access
- Object instantiation with new operator
- Method overriding with type compatibility checking

#### Type System

- Primitive types: int, boolean, void
- Reference types: Object references and multi-dimensional arrays
- String literals with built-in String type
- Null references with type-safe assignment
- Comprehensive type checking

#### Control Structures

- Conditional statements (if/else)
- Loop constructs (while)
- Block statements with proper scoping
- Assignment statements

#### Expressions

- Arithmetic operations (+, -, \*, /, unary -)
- Logical operations (&&, ||, !)
- Comparison operations (<, >, ==, !=)
- Array indexing and multi-dimensional arrays
- Method calls with parameter type checking
- Field access with inheritance resolution

#### Threading Support

- Special Thread class declarations
- Void method declarations (for run methods)
- Thread creation and management

#### Xinu Operating System Integration

- Built-in Xinu class with system calls
- I/O operations: print(), println(), printint(), readint()
- Thread management: yield(), sleep(), threadCreate()

## Compiler Architecture

- Lexical Analyzer: JavaCC-generated tokenizer with comprehensive token support
- Parser: LL(k) (top down) parser built with JavaCC from a provided grammar specification
- Abstract Syntax Tree: Visitor pattern implementation with 50+ AST node types
- Type Checker; Four-pass type checker with inheritance and scope management
  - Pass 1: class registration
  - Pass 2: inharitance definition
  - Pass 3: inheritance validation
  - Pass 4: method body analysis

## Project Structure

```
minijava-compiler/
├── Makefile                       # Build configuration
├── Parse/
│   ├── MiniJava.jj               # JavaCC grammar specification
│   ├── MiniJava.java             # Generated parser (auto-generated)
│   ├── MiniJavaTokenManager.java # Generated lexer (auto-generated)
│   ├── Main.java                 # Parser main entry point
│   └── *.java                    # Other generated parser files
├── Absyn/                        # Abstract Syntax Tree (50+ node types)
│   ├── Absyn.java               # Base AST node class
│   ├── Program.java             # Root AST node
│   ├── ClassDecl.java           # Class declarations
│   ├── MethodDecl.java          # Method declarations
│   ├── *Expr.java               # Expression nodes
│   ├── *Stmt.java               # Statement nodes
│   └── *Type.java               # Type nodes
├── Semant/                       # Semantic Analysis
│   ├── TypeChecker.java         # Multi-pass type checker
│   ├── PrintVisitor.java        # AST pretty printer
│   └── Main.java                # Semantic analyzer entry point
├── Types/                        # Type System Implementation
│   ├── Type.java                # Base type class
│   ├── CLASS.java               # Class type descriptor
│   ├── FUNCTION.java            # Method type descriptor
│   ├── RECORD.java              # Field collection type
│   └── *.java                   # Primitive and compound types
└── Symbol/                       # Symbol Table Management
    ├── Table.java               # Scoped symbol table
    └── Symbol.java              # Symbol implementation
```

## Building the Compiler

### Prerequisites

- **JDK 8 or higher** (tested with JDK 17 and JDK 21)
- **JavaCC** (Java Compiler Compiler) - for parser generation

```bash
# Build everything (parser + type checker)
make all

# Build only the parser
make parser

# Build main components
make main

# Clean compiled .class files
make clean

# Clean everything including generated parser files
make realclean
```

## Usage

The compiler reads MiniJava source code from **standard input** (stdin). You must use input redirection or pipes.

### Running the Full Compiler (with Type Checking)

```bash
# Using input redirection
java Semant.Main < your_program.java

# Using cat and pipe
cat your_program.java | java Semant.Main
```

### Running Just the Parser

```bash
# Parse only (no type checking)
java Parse.Main < your_program.java
```

### Example

Try the included sample file:

```bash
java Semant.Main < inputfile.java
```

This will:

1. Tokenize the input
2. Parse and build the Abstract Syntax Tree (AST)
3. Run four-pass type checking
4. Output the AST structure

## Example MiniJava Program

```java
class HelloWorld {
    public static void main(String[] args) {
        Xinu.println();
        Xinu.print("Hello from MiniJava!");
    }
}
```

## Important Notes

- **Frontend Only**: This compiler performs lexical analysis, parsing, and semantic analysis only. It does not generate executable code or bytecode.
- **Target Platform**: MiniJava programs are designed to run on the Embedded Xinu Operating System, but the compiler itself runs on any system with a JDK.
- **No Backend**: No code generation or optimization passes are implemented.
- **Input Method**: The compiler reads from stdin, not from command-line file arguments.
