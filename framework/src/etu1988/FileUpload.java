/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1988;

/**
 *
 * @author mita
 */
public class FileUpload {
    String name;
    byte[] bytes;

    public void setName(String name) {
        this.name = name;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
     public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    public FileUpload(String name, byte [] bytes){
        setName(name);
        setBytes(bytes);
    }

   
}
