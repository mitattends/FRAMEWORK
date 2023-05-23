/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1988.framework;

import java.io.File;

/**
 *
 * @author mitantsoa
 */
public class FileUpload {
    String FileName;
    byte[] fileBytes;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    
    public FileUpload(String fileName, byte [] fileBytes){
        setFileName(fileName);
        setFileBytes(fileBytes);
    }
    
}
