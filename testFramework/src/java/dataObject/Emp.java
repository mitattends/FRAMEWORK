/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dataObject;

import etu1988.ModelView;
import etu1988.framework.myAnnotation.MethodAnnotation;
import etu1988.framework.myAnnotation.Scope;
import java.util.HashMap;

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
    
    public Emp(String nom){
        setNom(nom);
    }
    
    public Emp(){}
    
    @Scope(profil = "user",hierarchie = 11)
    @MethodAnnotation(url = "appelMoi")
    public ModelView callMe(){
        ModelView mv = new ModelView();
        mv.setView("testView.jsp");
        Emp[]emps = {new Emp("Jean"), new Emp("Jeanne")};
        mv.addItem("empList", emps);
        System.out.println("vous etes un utilisateur normal");
        return mv;
    }
    
    @Scope(profil = "",hierarchie = 1)
    @MethodAnnotation(url = "callMe")
    public ModelView callMe2(){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        System.out.println("vous etes un visiteur");
        return mv;
    }
    
    @Scope(profil = "admin",hierarchie = 21)
    @MethodAnnotation(url = "empSave")
    public ModelView save(){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        System.out.println("vous etes admin");
        return mv;
    }
    
    @MethodAnnotation(url = "empSave")
    public ModelView save(int id){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        System.out.println("id "+id);
        return mv;
    }
    
    @MethodAnnotation(url = "empSave")
    public ModelView save(String nom){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        System.out.println("nom+++"+nom);
        return mv;
    }
    
    @MethodAnnotation(url = "showArray")
    public ModelView show(int[]noms){
        ModelView mv = new ModelView();
        mv.setView("formArray.jsp");
        for(int nom : noms){
            System.out.println(nom);
        }
        return mv;
    }
    
    
    
}
