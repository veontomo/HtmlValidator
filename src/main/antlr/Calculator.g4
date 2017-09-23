grammar Calculator ;

expression
    :   left=expression (MUL|DIV) right=expression # MulDiv
    |   left=expression (ADD|SUB) right=expression # AddSub
    |   INT #Int
    |   LPAREN expression RPAREN #Parenthesis ;
INT : [0-9]+ ;
LPAREN: '(';
RPAREN: ')';
ADD : '+' ;
SUB : '-' ;
MUL : '*';
DIV : ':'|'/';
WS : [ \t\r\n\f]+ -> skip ;