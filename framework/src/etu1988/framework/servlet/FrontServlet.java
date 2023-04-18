/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1988.framework.servlet;

import etu1988.ModelView;
import etu1988.framework.Mapping;
import etu1988.framework.myAnnotation.MethodAnnotation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mita
 */
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    public void setMappingUrl(HashMap<String, Mapping> mappingUrl) {
        this.mappingUrls = mappingUrl;
    }

    public HashMap<String, Mapping> getMappingUrl() {
        return mappingUrls;
    }
    
    public String formatFilePath(File file){
        String className =  file.getAbsolutePath().replace(Thread.currentThread().getContextClassLoader().getResource(".").getFile(), "");
        className = className.replace(".class", "");
        className = className.replace("/", ".");
        return className;
    }
    
    public void checkMethodAnnotation(Class classToChecked){
        for (Method method : classToChecked.getDeclaredMethods()) {
            if(method.isAnnotationPresent(MethodAnnotation.class)){
                String url = method.getAnnotation(MethodAnnotation.class).url();
                mappingUrls.put(url, new Mapping(classToChecked.getName(), method.getName()));
            }
        }
    }
    
    public void fillMappingUrl(File file) throws ClassNotFoundException{
        for(File fileUnderFile : file.listFiles()){
            if(fileUnderFile.isFile() && fileUnderFile.getName().contains(".class")){
                String className = formatFilePath(fileUnderFile);
                Class classTemp = Class.forName(className);
                checkMethodAnnotation(classTemp);
            }
            else if(fileUnderFile.isDirectory()) {
                fillMappingUrl(fileUnderFile);
            }
        }
    }
    
    public void redirectView(HttpServletRequest req, HttpServletResponse resp) {
        if(!req.getServletPath().equals("/")){    
            String url=req.getServletPath().split("/")[1];
            Mapping mappingUsed=mappingUrls.get(url);
            String objectName=mappingUsed.getClassName();
            String methodName=mappingUsed.getMethod();
            Class<?>classCalled=null;
            Object classCalledInstance=null;
            System.out.println(objectName);
            try {
                classCalled=Class.forName(objectName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                classCalledInstance=classCalled.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                Object obj =  classCalledInstance.getClass().getMethod(methodName, null).invoke(classCalledInstance, null);
                ModelView modelView = (ModelView)obj;
                try {  
                    req.getRequestDispatcher(modelView.getView()).forward(req, resp);
                } catch (ServletException | IOException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    @Override
    public void init() throws ServletException {
        mappingUrls = new HashMap<>();
        try {
            fillMappingUrl(new File(Thread.currentThread().getContextClassLoader().getResource(".").getPath()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        redirectView(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
