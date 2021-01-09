package customExceptions;

public class NumOfArgsException extends Exception{
    public NumOfArgsException(String className, Integer expectedArgs, Integer actualArgs){
        super(
                className + " requires " + expectedArgs + " arguments."
                + " but" + actualArgs + "was given."
        );
    }
}
