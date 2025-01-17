package com.herbak.marcell.beadando.frontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class NavigationBar extends HorizontalLayout {

    public NavigationBar() {
        Button mainPageButton = new Button("Főoldal");
        mainPageButton.addClickListener(e -> mainPageButton.getUI().ifPresent(ui -> ui.navigate("/")));

        this.setSpacing(true);
        this.setPadding(true);
        this.setWidthFull();
        this.add(mainPageButton);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                int role = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")) ? 0 : authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")) ? 1 : 2;

                if (role == 0) {
                    Button teacherButton = new Button("Oktatóm");

                    teacherButton.addClickListener(e ->
                            teacherButton.getUI().ifPresent(ui -> ui.navigate("/teacher-profile"))
                    );

                    this.add(teacherButton);
                }
                showLogoutButton();
            } else {
                showLoginButton();
            }
        } else {
            showLoginButton();
        }
    }

    private void showLoginButton() {
        Button loginButton = new Button("Belépés");
        loginButton.addClickListener(e -> loginButton.getUI().ifPresent(ui -> ui.navigate("/login")));
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        this.add(spacer, loginButton);
    }
    private void showLogoutButton(){
        Button logoutButton = new Button("Kilépés");

        logoutButton.addClickListener(e -> {
            VaadinSession.getCurrent().getSession().invalidate();
            logoutButton.getUI().ifPresent(ui -> ui.navigate("/"));
        });
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        this.add(spacer, logoutButton);
    }
}
