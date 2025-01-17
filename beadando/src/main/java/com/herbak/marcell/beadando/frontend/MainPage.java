package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.Car;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("/")
@PageTitle("Main Page")
@AnonymousAllowed
public class MainPage extends AppLayout {

    UserService userService;

    @Autowired
        public MainPage(UserService userService) {
            this.userService = userService;

            NavigationBar navbar = new NavigationBar();
            VerticalLayout content = new VerticalLayout();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    String username = ((UserDetails) principal).getUsername();
                    int role = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")) ? 0 : authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")) ? 1 : 2;
                    content.add(new H2("Üdv újra " + userService.getUserFullName(username) + "!"));
                    if (role == 0) {
                        content.add(new H3("Vezetett óráinak száma: " + userService.getDrivingProfile(username).getHoursDriven() + " óra"));
                        content.add(new H3("Levezetett kilométerek: " + userService.getDrivingProfile(username).getDistanceDriven() + " km"));
                        Car car = userService.getDrivingProfile(username).getCar();
                        content.add(new H3("Autód: " + (car != null ? car.getCarName() : "Nincs autó kiválasztva")));
                    } else {

                    }

                }
            } else {
                content.add("Üdvözöljük a Bekre Pál Autósiskola oldalán!");
            }


            addToNavbar(navbar);
            setContent(content);
        }
}
