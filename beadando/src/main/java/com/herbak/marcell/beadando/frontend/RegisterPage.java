package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("/register")
@PageTitle("Register Page")
@AnonymousAllowed
public class RegisterPage extends AppLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            event.forwardTo("");
        }
    }

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private TextField firstNameField;
    private TextField lastNameField;
    private ComboBox<Role> roleComboBox;
    private Button submitButton;

    UserService userService;

    @Autowired
    public RegisterPage(UserService userService){
        this.userService = userService;

        NavigationBar navbar = new NavigationBar();
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        usernameField = new TextField("Felhasználónév");
        passwordField = new PasswordField("Jelszó");
        emailField = new TextField("Email");
        firstNameField = new TextField("Keresztnév");
        lastNameField = new TextField("Vezetéknév");
        roleComboBox = new ComboBox<>("Fiók típusa");
        roleComboBox.setItems(Role.values());
        submitButton = new Button("Küldés", e -> addUser());

        content.add(new H1("Regisztráció"),usernameField, passwordField, emailField, firstNameField, lastNameField, roleComboBox, submitButton);
        addToNavbar(navbar);
        setContent(content);
    }

    private void addUser() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        String firstName = firstNameField.getValue();
        String lastName = lastNameField.getValue();
        Role role = roleComboBox.getValue();

        if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && role != null) {
            userService.registerNewUser(username, password, email, firstName, lastName, role);

            Notification.show("Felhasználó sikeresen létrehozva!", 3000, Notification.Position.MIDDLE);

            submitButton.getUI().ifPresent(ui -> ui.navigate("/login"));
        } else {
            Notification.show("Kérem töltsön ki minden mezőt!", 2000, Notification.Position.MIDDLE);
        }
    }
}
