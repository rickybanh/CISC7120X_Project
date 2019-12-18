package com.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Interpreter implements Expr.Visitor<Object> {
    private final Map<String, Double> values = new HashMap<>();

    Set< Map.Entry< String,Double> > st = values.entrySet();

    private double result;
    public Token variable1;

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        variable1 = expr.name;
        values.put(expr.name.lexeme, result);

//        for (Map.Entry< String,Double> me:st)
//        {
//            System.out.print(me.getKey()+" = ");
//            System.out.println(me.getValue());
//        }
        return value;
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        result = (double)expr.value;
        return result;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {

            case MINUS:
                checkNumberOperand(expr.operator, right);
                result = -(double)right;
                return result;
        }
        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                result = (double)left - (double)right;
                return result;

            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    result = (double)left + (double)right;
                    return result;
                }
                Toy.error(expr.operator, "Error");

            case STAR:
                checkNumberOperands(expr.operator, left, right);
                result = (double)left * (double)right;
                return result;
        }
        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        Toy.error(operator, "Error Operand");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        Toy.error(operator, "Error Operand");
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        return object.toString();
    }

    void interpret(Expr expression) {
        Object value = evaluate(expression);

        System.out.println(variable1.lexeme + " = " + stringify(value));
    }

}
