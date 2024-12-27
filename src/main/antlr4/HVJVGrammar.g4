grammar HVJVGrammar;

////////////////////////////////////////////////////////////////////////////////
// lexer rules
////////////////////////////////////////////////////////////////////////////////
// types
BOOL: 'bool';
INT: 'int';
VOID: 'void';

// conditions
IF: 'if';
ELSE: 'else';

// loops
FOR: 'for';
WHILE: 'while';

// functions
RETURN: 'return';
GOTO: 'goto';

// arithmetic operators
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';

// logical operators
AND: '&&';
OR: '||';
NOT: '!';
EQUAL: '==';
NOT_EQUAL: '!=';
LESS: '<';
LESS_EQUAL: '<=';
GREATER: '>';
GREATER_EQUAL: '>=';
ASSIGN: '=';

// logical values
TRUE: 'true';
FALSE: 'false';

// ternary operator
QUESTION: '?';
COLON: ':';

// separators
SEMICOLON: ';';
COMMA: ',';
DOT: '.';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACKET: '[';
RBRACKET: ']';

// literals
fragment DIGIT: [0-9]+;
fragment LOWERCASE: [a-z];
fragment UPPERCASE: [A-Z];
WORD: (LOWERCASE | UPPERCASE) (LOWERCASE | UPPERCASE | DIGIT | '_')*;
DECIMAL: DIGIT;
WHITE_SPACE: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;

////////////////////////////////////////////////////////////////////////////////
// parser rules
////////////////////////////////////////////////////////////////////////////////
program
    : functions?
    ;

identifier
    : WORD
    ;

dataType
    : BOOL
    | INT
    ;

returnType
    : dataType
    | VOID
    ;

booleanValue
    : TRUE
    | FALSE
    ;

signSymbol
    : PLUS
    | MINUS
    ;

decimalValue
    : signSymbol? DECIMAL
    ;

values
    : decimalValue
    | booleanValue
    ;

functions
    : function*
    ;

function
    : returnType identifier LPAREN parameters? RPAREN functionBlock
    ;

parameters
    : parameter (COMMA parameter)*
    ;

parameter
    : dataType identifier
    ;

functionBlock
    : LBRACE statements? RBRACE
    ;

statements
    : statement+
    ;

statement
    : ifStatement #statementIf
    | ifElseStatement #statementIfElse
    | forStatement #statementFor
    | whileStatement #statementWhile
    | declaration SEMICOLON #statementDeclaration
    | assignment SEMICOLON #statementAssignment
    | ternaryOperatorAssignment SEMICOLON #statementTernaryOperatorAssignment
    | ternaryOperatorExpression SEMICOLON #statementTernaryOperatorExpression
    | GOTO label SEMICOLON #statementGoto
    | label SEMICOLON #statementLabel
    | functionCall SEMICOLON #statementFunctionCall
    | RETURN SEMICOLON #statementReturn
    | RETURN expression SEMICOLON #statementReturnExpression
    ;

label
    : WORD
    ;

ifStatement
    : IF LPAREN expression RPAREN LBRACE statements? RBRACE
    ;

ifElseStatement
    : IF LPAREN expression RPAREN LBRACE statements? RBRACE ELSE LBRACE statements? RBRACE
    ;

forStatement
    : FOR LPAREN declaration SEMICOLON expression SEMICOLON assignment RPAREN LBRACE statements? RBRACE
    ;

whileStatement
    : WHILE LPAREN expression RPAREN LBRACE statements? RBRACE
    ;

declaration
    : dataType identifier ASSIGN expression
    ;

assignment
    : identifier ASSIGN expression
    ;

ternaryOperatorAssignment
    : identifier ASSIGN expression QUESTION expression COLON expression
    ;

ternaryOperatorExpression
    : expression QUESTION expression COLON expression
    ;

functionCall
    : identifier LPAREN arguments? RPAREN
    ;

arguments
    : expression (COMMA expression)*
    ;

expression
    : expression op=(MULT | DIV) expression    #expMultDiv
    | expression op=(PLUS | MINUS) expression  #expPlusMinus
    | expression op=(AND | OR) expression      #expAndOr
    | expression op=(EQUAL | NOT_EQUAL | LESS | LESS_EQUAL | GREATER | GREATER_EQUAL) expression #expLogical
    | decimalValue                              #expDecimalValue
    | booleanValue                              #expBooleanValue
    | identifier                                #expIdentifier
    | functionCall                              #expFunctionCall
    | LPAREN expression RPAREN                  #expParenthesis
    | PLUS expression                           #expPlus
    | MINUS expression                          #expMinus
    | NOT expression                            #expNot
    ;