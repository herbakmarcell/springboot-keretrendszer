package com.herbak.marcell.beadando.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")
@AnonymousAllowed
public class LoginPage extends AppLayout {
    public LoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // If logged in, redirect to home page or other protected page
            UI.getCurrent().navigate("home"); // Or another page depending on your setup
        }
        NavigationBar navbar = new NavigationBar();
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        LoginForm login = new LoginForm();
        login.setAction("login");
        content.add(
                new H1("Bekre Pál Autósiskola"),
                login
        );

        addToNavbar(navbar);
        setContent(content);
    }
}
