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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.GenericServlet;
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
    
    public Mapping findMapping(HttpServletRequest req){
        Mapping mappingUsed = new Mapping();
        String url = req.getServletPath().split("/")[1];
        mappingUsed = mappingUrls.get(url);
        return mappingUsed;
    }
    
    public void fillAttributes(HashMap<String,Object>hm, HttpServletRequest req){
        for (Map.Entry<String, Object> entry : hm.entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }
    }
    
    public void redirectView(HttpServletRequest req, HttpServletResponse resp) {
        if(!req.getServletPath().equals("/")){    
            Mapping mappingUsed = findMapping(req);
            String objectName=mappingUsed.getClassName();
            String methodName=mappingUsed.getMethod();
            Class<?>classCalled=null;
            Object classCalledInstance=null;
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
                    for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
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
    
    public static String makeFirstCharUp(String mot){
        String strCapitalized = mot.substring(0, 1).toUpperCase() + mot.substring(1);
        return strCapitalized;
    }
        
    public void useSet(Object object, HttpServletRequest req) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Enumeration<String> attributeNames = req.getParameterNames();
        while(attributeNames.hasMoreElements()){
            String attributeName = attributeNames.nextElement();
            String attributeValue = (String) req.getParameter(attributeName);
            Field field = object.getClass().getDeclaredField(attributeName); 
            if(field != null){
                Method setMethod = object.getClass().getDeclaredMethod("set"+makeFirstCharUp(field.getName()), field.getType());
                setMethod.invoke(object, attributeValue);
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
    
    public void executeAction(HttpServletRequest req, HttpServletResponse resp){
        if(!req.getServletPath().equals("/")){
            Mapping mappingUsed = findMapping(req);
            String objectName = mappingUsed.getClassName();
            String methodName = mappingUsed.getMethod();
            Class classCalled = null;
            Object classCalledInstance = null;
            try {
                classCalled = Class.forName(objectName);
                classCalledInstance = classCalled.newInstance(); 
                useSet(classCalledInstance, req); //get all the attributes and set them
                Method methodCalled = classCalledInstance.getClass().getDeclaredMethod(methodName);
                ModelView modelView = (ModelView) methodCalled.invoke(classCalledInstance);
                if(modelView.getData() != null) fillAttributes(modelView.getData(), req);
                req.getRequestDispatcher(modelView.getView()).forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter()
        executeAction(request, response);
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
