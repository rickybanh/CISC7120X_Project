package com.interpreter;

enum TokenType {
    LEFT_PAREN, RIGHT_PAREN, MINUS, PLUS, SEMICOLON, STAR,
    EQUAL,

    // Grammar Names
    IDENTIFIER, EXPRESSION, TERM, FACTOR,
    LETTER, LITERAL, NONZERODIGIT, DIGIT,

    EOF
}
