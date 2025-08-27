package main.com.minijava.compiler.errorhandling.messages;

public class ConsoleMessages {
    private String errorMessage;
    private String successMessage;

    public ConsoleMessages(){
        this.errorMessage = "";
        this.successMessage = "";
    }

    public void setErrorMessage(String error) {
        errorMessage = errorMessage + error + '\n';
    }
    public void setSuccessMessage(String exito) {
        successMessage = successMessage + exito + '\n';
    }

    public String getErrorMessage() { return errorMessage; }
    public String getSuccessMessage() {
        if (errorMessage.isEmpty()) {
            setSuccessMessage("\n" + "[SinErrores]");
        }
        return successMessage;
    }
}
