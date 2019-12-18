package com.interpreter;

import java.util.List;

import static com.interpreter.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException{}
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    Expr parse(){
        try{
            return identifier();
        }
        catch (ParseError error){
            return null;
        }
    }

    private Expr identifier(){
        return expression();
    }

    private Expr expression() {
        Expr expr = term();
        while (match(MINUS, PLUS)){
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator,right );

        }
        return expr;
    }

    private Expr term(){
        Expr expr = fact();
        while(match(STAR)){
            Token operator = previous();
            Expr right = fact();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr fact(){
        if(match(PLUS, MINUS)){
            Token operator = previous();
            Expr right = fact();
            return new Expr.Unary(operator, right);
        }
        if(match(LITERAL)){
            return new Expr.Literal(previous().literal);
        }
        if(match(IDENTIFIER)){
            if (match(EQUAL)) {
                return new Expr.Assign(tokens.get(current - 2), expression());
            }
            return expression();
        }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }
        throw error(peek(),"Error27");
    }

    private boolean match(TokenType... types){
        for(TokenType type:types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message){
        if(check(type)) return advance();

        throw error(peek(), message);
    }

    private boolean check(TokenType type){
        if(isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance(){
        if(!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd(){
        return peek().type == EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message){
        Toy.error(token, message);
        return new ParseError();
    }

}
