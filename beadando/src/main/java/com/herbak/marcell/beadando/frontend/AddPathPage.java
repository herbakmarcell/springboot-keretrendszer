package com.herbak.marcell.beadando.frontend;

import com.herbak.marcell.beadando.service.PathService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.configuration2.web.AppletConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("/add-path")
@PageTitle("Útvonal felvétele")
@PermitAll
public class AddPathPage extends AppLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean hasRoleTeacher = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPERVISOR"));

            if (!hasRoleTeacher) {
                event.forwardTo("/");
            }
        } else {
            event.forwardTo("/login");
        }
    }

    PathService pathService;

    TextField pathNameField;
    IntegerField pathLengthField;

    Button submitButton;
    @Autowired
    public AddPathPage(PathService pathService){
        this.pathService = pathService;

        NavigationBar navbar = new NavigationBar();
        addToNavbar(navbar);
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        pathNameField = new TextField("Útvonal neve");
        pathLengthField = new IntegerField("Útvonal hossza");
        pathLengthField.setMax(50);
        pathLengthField.setMin(1);

        submitButton = new Button("Felvétel");
        submitButton.addClickListener(buttonClickEvent -> addPath());

        content.add(pathNameField, pathLengthField, submitButton);
        setContent(content);
    }

    private void addPath(){
        if (pathNameField.isEmpty() || pathLengthField.isEmpty()){
            Notification.show("Kérem töltsön ki minden mezőt!", 3000, Notification.Position.MIDDLE);
            return;
        } else if (pathLengthField.getValue() < 1 || pathLengthField.getValue() > 50){
            Notification.show("Az útvonal hossza nem lehet negatív vagy 50 km-nél nagyobb!", 3000, Notification.Position.MIDDLE);
            return;
        }
        pathService.addPath(pathNameField.getValue(), pathLengthField.getValue());
        Notification.show("Útvonal sikeresen felvéve!", 3000, Notification.Position.MIDDLE);
        submitButton.getUI().ifPresent(ui -> ui.navigate("/"));
    }

}
