package interface_adapter.nutrition;

import interface_adapter.ViewModel;

public class NutritionViewModel extends ViewModel<String> {

    public NutritionViewModel() {
        super("nutrition");
        setState("nutrition");
    }
}