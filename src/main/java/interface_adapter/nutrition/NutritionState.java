package interface_adapter.nutrition;

/**
 * The state for the Nutrition View Model.
 */
public class NutritionState {
    private int dailyCalorieTarget;
    private int dailyProteinGrams;
    private double bmi;
    private String message = "Visit your profile and save your details to see personalized "
            + "nutrition recommendations.";

    public int getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(int dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    public int getDailyProteinGrams() {
        return dailyProteinGrams;
    }

    public void setDailyProteinGrams(int dailyProteinGrams) {
        this.dailyProteinGrams = dailyProteinGrams;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
