/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataObject;

import etu1988.ModelView;
import etu1988.framework.FileUpload;
import java.util.Arrays;

/**
 *
 * @author mitantsoa
 */
public class Parent {
    String[]childsName = new String[3];
    FileUpload familyPicture;

    public String[] getChildsName() {
        return childsName;
    }

    public void setChildsName(String[] childsName) {
        this.childsName = childsName;
    }

    public FileUpload getFamilyPicture() {
        return familyPicture;
    }

    public void setFamilyPicture(FileUpload familyPicture) {
        this.familyPicture = familyPicture;
    }
    
    public Parent(){}
    
    public Parent(String[]childsName, FileUpload familyPicture){
        setChildsName(childsName);
        setFamilyPicture(familyPicture);
    }
    
    public ModelView showSprint9(){
        ModelView mv = new ModelView();
        for (String childName : childsName) {
            System.out.println("childName = "+childName);
        }
        System.out.println("fileName  = "+familyPicture.getFileName());
        System.out.println("bytes = "+Arrays.toString(familyPicture.getFileBytes()));
        mv.addItem("other.jsp", null);
        return mv;
    }
    
    
}
