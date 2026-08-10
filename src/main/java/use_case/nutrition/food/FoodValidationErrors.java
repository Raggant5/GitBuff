package use_case.nutrition.food;

/**
 * Per-field validation errors for the food editor form, returned to the
 * output boundary when an add/edit food use case fails validation.
 */
public class FoodValidationErrors {

    private String caloriesError = "";
    private String proteinError = "";
    private String carbsError = "";
    private String fatError = "";
    private String quantityError = "";
    private String gramsError = "";
    private String generalError = "";

    public String getCaloriesError() {
        return caloriesError;
    }

    public void setCaloriesError(String caloriesError) {
        this.caloriesError = caloriesError;
    }

    public String getProteinError() {
        return proteinError;
    }

    public void setProteinError(String proteinError) {
        this.proteinError = proteinError;
    }

    public String getCarbsError() {
        return carbsError;
    }

    public void setCarbsError(String carbsError) {
        this.carbsError = carbsError;
    }

    public String getFatError() {
        return fatError;
    }

    public void setFatError(String fatError) {
        this.fatError = fatError;
    }

    public String getQuantityError() {
        return quantityError;
    }

    public void setQuantityError(String quantityError) {
        this.quantityError = quantityError;
    }

    public String getGramsError() {
        return gramsError;
    }

    public void setGramsError(String gramsError) {
        this.gramsError = gramsError;
    }

    public String getGeneralError() {
        return generalError;
    }

    public void setGeneralError(String generalError) {
        this.generalError = generalError;
    }

    /**
     * Checks if any errors exist within the food form input.
     * @return if any errors exist
     */
    public boolean hasErrors() {
        return !caloriesError.isEmpty() || !proteinError.isEmpty() || !carbsError.isEmpty() || !fatError.isEmpty()
                || !quantityError.isEmpty() || !gramsError.isEmpty() || !generalError.isEmpty();
    }
}
