package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.DrivingProfile;
import com.herbak.marcell.beadando.service.CarService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;

@Route("/teacher-profile")
@PageTitle("Oktatód oldala")
@PermitAll
public class TeacherProfilePage extends AppLayout implements BeforeEnterObserver {
    UserService userService;
    CarService carService;

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
    public TeacherProfilePage(UserService userService, CarService carService) {
        this.userService = userService;
        this.carService = carService;

        NavigationBar navbar = new NavigationBar();
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        DrivingProfile drivingProfile = getDrivingProfile(username);
        if(drivingProfile.getCar() != null){
            content.add(new H2("Oktatód neve: " + userService.getUserFullName(drivingProfile.getCar().getTeacher().getUsername())));
            content.add(new H2("Autód: " + drivingProfile.getCar().getCarName()));
            content.add(new H2("Autód rendszáma: " + drivingProfile.getCar().getLicensePlate()));
            content.add(new H2("Váltó típusa: " + drivingProfile.getCar().getCarType()));

        } else {
            content.add(new H1("Nincs még oktatód!"));
            Button addTeacherButton = new Button("Oktató felvétele");
            addTeacherButton.addClickListener(e -> addTeacherButton.getUI().ifPresent(ui -> ui.navigate("/select-teacher")));
            content.add(addTeacherButton);
        }

        addToNavbar(navbar);
        setContent(content);
    }

    DrivingProfile getDrivingProfile(String username) {
        return userService.getDrivingProfile(username);
    }
}
