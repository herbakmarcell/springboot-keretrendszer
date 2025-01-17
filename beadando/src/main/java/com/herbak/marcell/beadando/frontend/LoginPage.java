package com.herbak.marcell.beadando.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")
@AnonymousAllowed
public class LoginPage extends AppLayout implements BeforeEnterObserver{
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            event.forwardTo("");
        }
    }

    public LoginPage() {
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
