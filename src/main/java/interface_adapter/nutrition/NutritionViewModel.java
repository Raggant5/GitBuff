package interface_adapter.nutrition;

import interface_adapter.ViewModel;

public class NutritionViewModel extends ViewModel<NutritionState> {

    public NutritionViewModel() {
        super("nutrition");
        setState(new NutritionState());
    }
}
