package com.academy.telesens.model;

import model.Button;
import model.Component;
import model.Label;
import model.VisualComponent;

public class HomeTask1 {
    public static void main(String[] args) {

        VisualComponent vComp = new VisualComponent() {
            @Override
            public void draw() {
                System.out.println("vComp anonymous");
            }
        };

        VisualComponent anonymuousLambda = () -> System.out.println("Anonymous Lambda");

        VisualComponent[] vComponents = new VisualComponent[]{
                new Button(),
                new Label(),
                new Component(),
                new VisualComponent() {
                    @Override
                    public void draw() {
                        System.out.println("Анонимный Компонент");
                    }
                },
                () -> System.out.println("Lambda Component")
        };

        for (VisualComponent element : vComponents) {
            element.draw();
        }
    }
}
