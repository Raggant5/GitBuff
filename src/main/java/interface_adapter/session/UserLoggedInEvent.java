package interface_adapter.session;

import use_case.login.LoginOutputData;

/**
 * Event published once a user has successfully logged in.
 *
 * <p>Wraps the {@link LoginOutputData} the Login use case produced. Observers are free to read
 * whichever fields they need from it - the use case layer is allowed to depend on entities, so
 * {@code LoginOutputData} legitimately carries {@code entity.Meal}/{@code entity.LoggedWorkout}
 * lists alongside primitive profile fields; each observer converts what it needs into its own
 * feature's display data, the same way {@code interface_adapter.login.LoginPresenter} used to
 * do inline for all of them at once.
 *
 * @param data the login use case's output data
 */
public record UserLoggedInEvent(LoginOutputData data) {
}
