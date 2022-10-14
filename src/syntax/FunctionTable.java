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

    public void judge(Function function, int line) {
        for (Function function1 : functions) {
            if (function1.getName().equals(function.getName())) {
                ArrayList<Variable> p1, p2;
                p1 = function1.getParams();
                p2 = function.getParams();
                if (p1.size() != p2.size()) {
                    ErrorWriter.add(new Error(line, 'd'));
                } else {
                    int length = p1.size();
                    for (int i = 0; i < length; i++) {
                        if (p1.get(i).getDimensions().size() != p2.get(i).getDimensions().size()) {
                            ErrorWriter.add(new Error(line, 'e'));
                        }
                    }
                }
                return;
            }
        }
        ErrorWriter.add(new Error(line, 'c'));
    }

    public boolean isVoid(String name){
        for(Function function:functions){
            if(name.equals(function.getName()) && !function.returnValue){
                return true;
            }
        }
        return false;
    }
}
