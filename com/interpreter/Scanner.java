package com.interpreter;
import static com.interpreter.TokenType.*;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens(){
        while(!isAtEnd()){
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF,"",null, line));
        return tokens;
    }

    private void scanToken(){
        char c = advance();
        switch (c){
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '=':
                addToken(EQUAL);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            default:
                if(isLiteral(c)){
                    literal();
                }
                else if (isLetter(c)){
                    identifier();
                }
                else{
                    Toy.error(line, "Unexpected character");
                }
                break;
        }
    }

    private void identifier(){
        while(isLetter(peek())) advance();

        if(isLiteral(peek())){
            advance();
            while(isLiteral(peek())) advance();
        }
        addToken(IDENTIFIER);
    }
    private void literal(){
        while(isLiteral(peek())) {
            advance();
        }
        addToken(LITERAL, Double.parseDouble(source.substring(start, current)));
    }

    private boolean match(char expected){
        if(isAtEnd())return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private boolean isLetter(char c){
        if (String.valueOf(c).matches("[a-zA-Z]|_*")){
            return true;
        }
        return false;
    }

    private boolean isLiteral(char c) {
        return isDigit(c)||isNonZero(c);
    }

    private boolean isNonZero(char c){
        if (String.valueOf(c).matches("[1-9]")){
            return true;
        }
        return false;
    }

    private boolean isDigit(char c){
        if (String.valueOf(c).matches("[0-9]")){
            return true;
        }
        return false;
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char advance(){
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}