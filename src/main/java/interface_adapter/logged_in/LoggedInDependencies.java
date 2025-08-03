package interface_adapter.logged_in;

import interface_adapter.logout.LogoutController;

public record LoggedInDependencies(LoggedInViewModel loggedInViewModel, LogoutController logoutController) {

}
