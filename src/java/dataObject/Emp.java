/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataObject;

import etu1988.framework.myAnnotation.MethodAnnotation;
import java.net.URL;

/**
 *
 * @author mita
 */
public class Emp {
    String nom;

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
    
    @MethodAnnotation(url = "coucoou")
    public void callMe(){
        
    }
}
