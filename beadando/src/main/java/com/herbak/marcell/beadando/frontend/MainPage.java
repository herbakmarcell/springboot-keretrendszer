package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.entity.Car;
import com.herbak.marcell.beadando.service.ExamService;
import com.herbak.marcell.beadando.service.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

    private UserService userService;
    private ExamService examService;

        @Autowired
        public MainPage(UserService userService, ExamService examService) {
            this.userService = userService;
            this.examService = examService;

            NavigationBar navbar = new NavigationBar();
            VerticalLayout content = new VerticalLayout();
            content.setAlignItems(FlexComponent.Alignment.CENTER);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    String username = ((UserDetails) principal).getUsername();
                    int role = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")) ? 0 : authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER")) ? 1 : 2;
                    content.add(new H2("Üdv " + userService.getUserFullName(username) + "!"));
                    if (role == 0) {
                        content.add(new H3("Vezetett óráinak száma: " + userService.getHoursDriven(username) + " óra"));
                        content.add(new H3("Levezetett kilométerek: " + userService.getDistanceDriven(username) + " km"));
                        Car car = userService.getDrivingProfile(username).getCar();
                        content.add(new H3("Autód: " + (car != null ? car.getCarName() : "Nincs autó kiválasztva")));

                        if (examService.isThereUpcomingExam(username)){
                            content.add(new H2("Közelgő vizsgád időpontja: " + examService.getUpcomingExam(username).getExamDate()));
                        } else {
                            content.add(new H2("Nincs közelgő vizsgád."));
                        }


                    } else {

                    }
                } else {
                    content.add(new H1("Üdvözöljük a Bekre Pál Autósiskola oldalán!"));
                }

            } else {
                content.add(new H1("Üdvözöljük a Bekre Pál Autósiskola oldalán!"));
            }


            addToNavbar(navbar);
            setContent(content);
        }
}
