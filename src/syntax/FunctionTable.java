package syntax;

import util.Error;
import util.ErrorWriter;

import java.util.ArrayList;

public class FunctionTable {
    ArrayList<Function> functions;

    public FunctionTable() {
        functions = new ArrayList<Function>();
    }

    public void add(Function function, int line) {
        for (Function function1 : functions) {
            if (function1.getName().equals(function.getName())) {
                ErrorWriter.add(new Error(line, 'b'));
                return;
            }
        }
        functions.add(function);
    }

    public Function get(String name){
        for(Function function:functions){
            if(name.equals(function.getName())){
                return function;
            }
        }
        return null;
    }

    public void generateIntermediateCode(String name){
        for(Function function:functions){
            if(name.equals(function.getName())){
                function.generateIntermediateCode();
            }
        }
    }
}
