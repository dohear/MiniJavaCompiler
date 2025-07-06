# MiniJava Compiler

This is a complete frontend for a MiniJava Compiler written in Java targeting specifically the Embedded Xinu Operating System. The front end includes a tokenizer, parser/syntactic analyzer, and a four-pass type checker. This was written for my compiler course taken at Marquette University (COSC 4400, fall 24) as a part of a group effort alongside Erik Gutierrez, Varisha Asim, and myself with guidance from our professor Dr.Jack Forden. 

## Language Support

#### Object-Oriented Programming

-Class declarations with single inheritance
-Method definitions 
-Field declarations and access
-Object instantiation with new operator
-Method overriding with type compatibility checking


#### Type System

-Primitive types: int, boolean, void
-Reference types: Object references and multi-dimensional arrays
-String literals with built-in String type
-Null references with type-safe assignment
-Comprehensive type checking


#### Control Structures

-Conditional statements (if/else)
-Loop constructs (while)
-Block statements with proper scoping
-Assignment statements


#### Expressions

-Arithmetic operations (+, -, *, /, unary -)
-Logical operations (&&, ||, !)
-Comparison operations (<, >, ==, !=)
-Array indexing and multi-dimensional arrays
-Method calls with parameter type checking
-Field access with inheritance resolution


#### Threading Support

-Special Thread class declarations
-Void method declarations (for run methods)
-Thread creation and management


#### Xinu Operating System Integration

-Built-in Xinu class with system calls
-I/O operations: print(), println(), printint(), readint()
-Thread management: yield(), sleep(), threadCreate()

## Compiler Architecture

-Lexical Analyzer: JavaCC-generated tokenizer with comprehensive token support
-Parser: LL(k) (top down) parser built with JavaCC from a provided grammar specification
-Abstract Syntax Tree: Visitor pattern implementation with 50+ AST node types
-Type Checker; Four-pass type checker with inheritance and scope management
    -Pass 1: class registration
    -Pass 2: inharitance definition
    -Pass 3: inheritance validation
    -Pass 4: method body analysis

## Build Requirements

-JDK8 or higher
-JavaCC (Java Compiler Compiler) - for parser generation
-GNU Make

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
