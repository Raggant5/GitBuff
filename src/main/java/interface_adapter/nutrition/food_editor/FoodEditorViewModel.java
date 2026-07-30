package interface_adapter.nutrition.food_editor;

import interface_adapter.ViewModel;

/**
 * The View Model for the Add Food and Edit Food View.
 */
public class FoodEditorViewModel extends ViewModel<FoodEditorState> {

    public FoodEditorViewModel() {
        super("add food");
        setState(new FoodEditorState());
    }

}
