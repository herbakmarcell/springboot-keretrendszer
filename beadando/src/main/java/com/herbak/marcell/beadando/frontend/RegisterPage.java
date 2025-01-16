package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.entity.User;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/register")
@PageTitle("Register Page")
@AnonymousAllowed
public class RegisterPage extends AppLayout {

    private TextField usernameField;
    private PasswordField passwordField;
    private TextField emailField;
    private ComboBox<Role> roleComboBox;
    private Button submitButton;

    UserService userService;

    @Autowired
    public RegisterPage(UserService userService){
        this.userService = userService;

        NavigationBar navbar = new NavigationBar();
        VerticalLayout content = new VerticalLayout();

        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        emailField = new TextField("Email");
        roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems(Role.values());
        submitButton = new Button("Submit", e -> addUser());

        FormLayout formLayout = new FormLayout(usernameField, passwordField, emailField, roleComboBox, submitButton);

        addToNavbar(navbar);
        setContent(formLayout);
    }

    private void addUser() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        String email = emailField.getValue();
        Role role = roleComboBox.getValue();

        if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && role != null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setRole(role);

            userService.registerNewUser(username, password, email, role);

            Notification.show("User created successfully!", 3000, Notification.Position.MIDDLE);
        } else {
            Notification.show("Please fill in all fields", 3000, Notification.Position.MIDDLE);
        }
    }
}
