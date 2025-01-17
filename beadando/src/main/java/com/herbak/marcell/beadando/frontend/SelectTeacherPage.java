package com.herbak.marcell.beadando.frontend;


import com.herbak.marcell.beadando.service.CarService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

@Route("/select-teacher")
@PageTitle("Válassz oktatót")
@PermitAll
public class SelectTeacherPage extends AppLayout implements BeforeEnterObserver {

    UserService userService;
    CarService carService;

    ComboBox<String> teacherComboBox;
    ComboBox<String> carComboBox;
    Button submitButton;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleStudent = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"));

            if (!hasRoleStudent) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }

    @Autowired
    public SelectTeacherPage(UserService userService, CarService carService) {
        this.userService = userService;
        this.carService = carService;
        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);

        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        teacherComboBox = new ComboBox<>("Válassz oktatót:");
        teacherComboBox.setItems(userService.getAllTeachers());
        teacherComboBox.addValueChangeListener(event -> {
            if (carComboBox == null){
                carComboBox = new ComboBox<>("Válassz autót:");
                content.add(carComboBox);
            }
            resetCarComboBox();
        });
        submitButton = new Button("Kiválaszt");
        submitButton.addClickListener(e -> {
            if (teacherComboBox.getValue() == null || carComboBox.getValue() == null){
                return;
            }
            userService.addCarToStudent(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
                    teacherComboBox.getValue(), carComboBox.getValue());
            submitButton.getUI().ifPresent(ui -> ui.navigate("/teacher-profile"));
        });


        content.add(teacherComboBox, submitButton);
        setContent(content);
    }

    void resetCarComboBox(){
        if (teacherComboBox.getValue() == null){
            carComboBox.setItems();
            return;
        }
        carComboBox.setItems(carService.getCarsByFullName(teacherComboBox.getValue()));
    }
}
