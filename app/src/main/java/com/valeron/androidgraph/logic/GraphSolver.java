package com.valeron.androidgraph.logic;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphSolver {


    private Function foo;
    private Function foo1;
    private double a;
    private double b;
    private double eps;
    public static final int MAX_ITERS = 1000;

    public GraphSolver(Function foo, double a, double b, double eps) throws Exception {
        if(!foo.checkSyntax()){
            throw new Exception("Ошибка: неверный синтакс функции.");
        }
        this.foo = foo;
        this.a = a;
        this.b = b;
        this.eps = eps;
    }

    public GraphSolver(Function foo, Function foo1, double a, double b, double eps) throws Exception {
        if(!foo.checkSyntax()){
            throw new Exception("Ошибка: неверный синтакс функции.");
        }
        if(!foo1.checkSyntax()){
            throw new Exception("Ошибка: неверный синтакс функции.");
        }
        this.foo = foo;
        this.foo1 = foo1;
        this.a = a;
        this.b = b;
        this.eps = eps;
    }

    public Map<String, Double> solve(SolveMethod method) throws Exception {
        HashMap<String, Double> resMap = new HashMap<>();
        switch (method){
            case Chords:
                resMap.put("x", doMethodChord(foo, a, b, eps));
                return resMap;
            case Touch:
                resMap.put("x", doMethodTouch(foo, a, b, eps));
                return resMap;
            case Newton:
                return doMethodNewton(Arrays.asList(foo, foo1), a, b , eps);

            default: return null;
        }
    }

    private double doMethodChord (Function f, double a, double b, double eps) throws Exception {
        if(a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double xCurr = a;
        if(a == 0){
            xCurr = (b - a) / 2;
        }else if ((b - a) / 2 == 0){
            xCurr = a;
        }
        double xPrev = 0;
        double xDoublePrev = 0;
        while(Math.abs(xCurr - xPrev) > eps){
            xDoublePrev = xPrev;
            xPrev = xCurr;
            double numerator = f.calculate(xPrev) * (xPrev - xDoublePrev);
            double denominator =(f.calculate(xPrev) - f.calculate(xDoublePrev));
            if(denominator == 0 || Double.isNaN(denominator)){
                throw new Exception("Ошибка: метод не сходится.");
            }
            xCurr -= numerator/denominator;
        }
        return xCurr;
    }

    public static double doMethodTouch(Function f, double a, double b, double eps){
        Expression derF = new Expression("der(f(x), x , g)", f);
        Argument arg = new Argument("g = 0");
        derF.addArguments(arg);
        if(a > b) {
            double temp = a;
            a = b;
            b = temp;
        }
        double xCurr = a;
        if(a == 0){
            xCurr = (b - a) / 2;
        }else if ((b - a) / 2 == 0){
            xCurr = a;
        }

        double xPrev = 0;
        while(Math.abs(xCurr - xPrev) > eps){
            xPrev = xCurr;
            arg.setArgumentValue(xPrev);
            xCurr = xPrev - 0.1 * f.calculate(xPrev)/derF.calculate();
        }
        return xCurr;
    }

    public static Map<String, Double> doMethodNewton(List<Function> functions, double x01, double x02, double eps) throws Exception {
        int k = 0;
        Function f1 = functions.get(0);
        Function f2 = functions.get(1);

        double a = x01;
        double b = x02;
        double x = a;
        double y = b;


        do{
            a = x;
            b = y;

            double df1dx = getDerivative(f1, a, b,  0);
            double df1dy = getDerivative(f1, a, b,  1);
            double df2dx = getDerivative(f2, a, b,  0);
            double df2dy = getDerivative(f2, a, b,  1);

            double J = df1dx * df2dy - df1dy * df2dx;
            if(J == 0){
                throw new Exception("Ошибка: Якобиан равен нулю");
            }
            double A = f1.calculate(a, b)/J;
            double B = f2.calculate(a, b)/J;

            x = a - A * df2dy + B * df1dy;
            y = b + A * df2dx - B * df1dx;

            k++;
        }while((Math.abs(x-a ) > eps || Math.abs(y - b) > eps ) && k < MAX_ITERS);

        HashMap<String, Double> resMap = new HashMap<>();
        resMap.put("x", x);
        resMap.put("y", y);
        resMap.put("i", (double)k);

        return resMap;
    }


    /**
     * if derNum == 0 then df/dx else if der == 1 df/dy
     * @param f
     * @param x
     * @param y
     * @return
     */
    public static double getDerivative(Function f, double x, double y, int derNum){
        double eps = 1e-5;
        f.setArgumentValue(derNum, x + eps);
        f.setArgumentValue((derNum + 1) % 2, y);
        double first = f.calculate();
        f.setArgumentValue(derNum, x);
        double sec = f.calculate();
        return (first - sec)/eps;
    }

}
