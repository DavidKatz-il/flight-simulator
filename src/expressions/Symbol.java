package expressions;

import interpreter.Utilities;

public class Symbol implements Expression{
    private double value;
    private String simulator;

    public Symbol() {

    }
    public Symbol(double value) {
        this.value=value;
    }

    public void setValue(double value){ this.value=value;}
    public void setSimulator(String simulator){
        this.simulator=simulator;
    }
    public String getSimulator(){ return this.simulator; }

    @Override
    public double calculate() {
        if (simulator != null)
            this.value = Utilities.getSimNumber(simulator).calculate();
        return this.value;
    }

}
