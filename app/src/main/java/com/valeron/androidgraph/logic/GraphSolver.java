package com.valeron.androidgraph.logic;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;

public class GraphSolver {

    private Function foo;
    private double a;
    private double b;
    private double eps;

    public GraphSolver(Function foo, double a, double b, double eps) throws RuntimeException {
        if(!foo.checkSyntax()){
            throw new RuntimeException("Ошибка: неверный синтакс функции.");
        }
        this.foo = foo;
        this.a = a;
        this.b = b;
        this.eps = eps;
    }

    public double solve(SolveMethod method){
        switch (method){
            case Chords: return methodChord(foo, a, b, eps);
            //TODO: other methods
            default: return 0;
        }
    }

    private double methodChord (Function foo, double a, double b, double e) {
        double x_next = 0;
        double tmp;
        do{
            tmp = x_next;
            x_next = b - foo.calculate(b) * (a - b) / (foo.calculate(a) - foo.calculate(b));
            a = b;
            b = tmp;
        } while (Math.abs(x_next - b) > e);
        return x_next;
    }

}
