package interface_adapter.dashboard;

import interface_adapter.ViewModel;

public class DashboardViewModel extends ViewModel<String> {

    public DashboardViewModel() {
        super("dashboard");
        setState("dashboard");
    }
}