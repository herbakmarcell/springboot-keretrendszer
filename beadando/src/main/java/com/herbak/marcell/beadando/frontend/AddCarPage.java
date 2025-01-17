package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.CarType;
import com.herbak.marcell.beadando.entity.Role;
import com.herbak.marcell.beadando.service.CarService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("/add-car")
@PageTitle("Autó felvétele")
@PermitAll
public class AddCarPage extends AppLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleTeacher = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"));

            if (!hasRoleTeacher) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }


    private ComboBox<CarType> typeComboBox;
    private TextField carNameField;
    private TextField licensePlateField;
    private Button submitButton;

    CarService carService;

    @Autowired
    public AddCarPage(CarService carService) {
        String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        this.carService = carService;
        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        typeComboBox = new ComboBox<>("Váltó típusa");
        typeComboBox.setItems(CarType.values());
        carNameField = new TextField("Autó márkája");
        licensePlateField = new TextField("Rendszám");

        submitButton = new Button("Autó felvétele", e -> {
            addCar(username);

            submitButton.getUI().ifPresent(ui -> ui.navigate("/"));
        }
        );

        content.add(new H1("Autó felvétele"),typeComboBox, carNameField, licensePlateField, submitButton);
        setContent(content);
    }

    void addCar(String username){

        CarType carType = typeComboBox.getValue();
        String carName = carNameField.getValue();
        String licensePlate = licensePlateField.getValue();

        if (carType != null && carName != null && !carName.isEmpty() && licensePlate != null && !licensePlate.isEmpty()) {
            carService.addCar(carType, carName, licensePlate, username);
            Notification.show("Gépjármű sikeresen hozzáadva!", 2000, Notification.Position.MIDDLE);
        } else {
            Notification.show("Kérem töltsön ki minden mezőt!", 2000, Notification.Position.MIDDLE);
        }

    }

}
