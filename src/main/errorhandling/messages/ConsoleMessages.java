package main.errorhandling.messages;

public class ConsoleMessages {
    private StringBuilder errorMessage;
    private StringBuilder successMessage;
    private boolean erroresMostrados;

    public ConsoleMessages(){
        this.errorMessage = new StringBuilder();
        this.successMessage = new StringBuilder();
        this.erroresMostrados = false;
    }

    public void setErrorMessage(String error) {
        errorMessage.append(error).append('\n');
    }
    public void setSuccessMessage(String exito) {
        successMessage.append(exito).append('\n');
    }

    public String getErrorMessage() {
        if (!erroresMostrados && !errorMessage.isEmpty()) {
            errorMessage.append("[ConErrores]\n");
            erroresMostrados = true;
        }
        return errorMessage.toString();
    }
    public String getSuccessMessage() {
        if (errorMessage.isEmpty() && successMessage.indexOf("[SinErrores]") == -1) {
            setSuccessMessage("\n[SinErrores]");
        }
        return successMessage.toString();
    }
}