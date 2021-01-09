package customExceptions;

public class VarTypeNotSupportedException extends Exception{
    public VarTypeNotSupportedException(String varType){
        super("The varType: " + varType + " is not supported.");
    }
}
