/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataObject;

import etu1988.ModelView;
import etu1988.framework.myAnnotation.MethodAnnotation;

/**
 *
 * @author mitantsoa
 */
public class Login {
    String userName,password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @MethodAnnotation(url = "login")
    public ModelView login(){
        ModelView mv = new ModelView();
        mv.setView("formEmp.jsp");
        if(getUserName().equals("Mitantsoa") && getPassword().equals("123")){
            mv.addSessions("isConnected", true);
            mv.addSessions("profil","admin");
        }
        else {
            mv.addSessions("isConnected", true);
            mv.addSessions("profil", "user");
        }
        return mv; 
    }
    
    
}
