package com.herbak.marcell.beadando.frontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class NavigationBar extends HorizontalLayout {

    public NavigationBar() {
        Button mainPageButton = new Button("Main Page");
        mainPageButton.addClickListener(e -> mainPageButton.getUI().ifPresent(ui -> ui.navigate("/")));

        this.setSpacing(true);
        this.setPadding(true);
        this.add(mainPageButton);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                boolean isStudent = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));

                if (isStudent) {
                    Button studentDashboardButton = new Button("Student Dashboard");
                    studentDashboardButton.addClickListener(e ->
                            studentDashboardButton.getUI().ifPresent(ui -> ui.navigate("/student-dashboard"))
                    );

                    this.add(studentDashboardButton);
                }
                Button logoutButton = new Button("Logout");
                logoutButton.addClickListener(e -> {
                    VaadinSession.getCurrent().getSession().invalidate();
                    logoutButton.getUI().ifPresent(ui -> ui.navigate("/login"));
                });
                this.add(logoutButton);
            } else {
                showLoginButton();
            }
        } else {
            showLoginButton();
        }
    }

    private void showLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.addClickListener(e -> loginButton.getUI().ifPresent(ui -> ui.navigate("/login")));
        this.add(loginButton);
    }
}
