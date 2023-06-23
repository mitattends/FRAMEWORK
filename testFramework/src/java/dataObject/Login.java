/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataObject;

import etu1988.ModelView;

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
    
    public ModelView login(){
        ModelView mv = new ModelView();
        if(userName.equals("mitantsoa") && password.equals("123")){
            
        }
        
            
        
    }
        
        return mv;
    }
    
    
}
