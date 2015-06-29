package ast.structs;

import parser.SemanticException;

import java.util.Hashtable;
import java.util.Stack;

/**
 * Created by alejandro on 28/06/15.
 */
public class SymbolTable {

    private final Stack< Hashtable<String, Variable> > ambitos;

    private final Hashtable<String, Declaration> globalDeclarations;

    public SymbolTable() {
        ambitos = new Stack< Hashtable<String, Variable> >();
        globalDeclarations = new Hashtable<String, Declaration>();
        Hashtable ambitoGlobal = new Hashtable<String, Variable>();
        ambitos.push(ambitoGlobal);
    }

    public void createContext() {
        Hashtable nuevoAmbito = new Hashtable<String, Variable>();
        ambitos.push(nuevoAmbito);
    }

    public void deleteContext() {
        ambitos.pop();
    }

    public void addVariable(Variable var) throws SemanticException {
        if (!ambitos.peek().containsKey(var.name))
            ambitos.peek().put(var.name, var);
        else
            throw new SemanticException();
    }

    public Variable getVariable(String name) {
        int size = ambitos.size();
        for (int i = size-1; i >= 0; i--) {
            Hashtable<String, Variable> ambito = ambitos.elementAt(i);
            if (ambito.containsKey(name))
                return ambito.get(name);
        }
        return null;
    }

    public Variable getVariableInContext(String name) {
        Hashtable<String, Variable> contexto = ambitos.peek();
        if (contexto.containsKey(name))
            return contexto.get(name);
        else return null;
    }

    public void addDeclaration(String name, Declaration declaration){
        globalDeclarations.put(name, declaration);
    }

    public Declaration getGlobalDeclaration(String name) {
        if(globalDeclarations.containsKey(name))
            return globalDeclarations.get(name);
        else return null;
    }
}
