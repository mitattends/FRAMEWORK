/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataObject;

import etu1988.ModelView;
import etu1988.framework.myAnnotation.MethodAnnotation;
import etu1988.framework.myAnnotation.Singleton;

/**
 *
 * @author mitantsoa
 */
@Singleton
public class Dept {
    int x = 1;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }
    
    @MethodAnnotation(url = "essai")
    public ModelView testSingleton(){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        System.out.println("x =" +x);
        setX(getX() + 1);
        return mv;
    }
}
