package com.herbak.marcell.beadando.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@PageTitle("Main Page")
@AnonymousAllowed
public class MainPage extends AppLayout {
        public MainPage() {
            NavigationBar navbar = new NavigationBar();
            VerticalLayout content = new VerticalLayout();
            content.add("Welcome to the driving school management system!");
            addToNavbar(navbar);
            setContent(content);
        }
}
