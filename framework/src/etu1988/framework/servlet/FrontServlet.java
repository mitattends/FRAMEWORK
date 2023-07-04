/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1988.framework.servlet;

import com.google.gson.Gson;
import etu1988.FileUpload;
import etu1988.ModelView;
import etu1988.framework.Mapping;
import etu1988.framework.myAnnotation.MethodAnnotation;
import etu1988.framework.myAnnotation.Scope;
import etu1988.framework.myAnnotation.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.servlet.GenericServlet;
import javax.servlet.http.Part;

/**
 *
 * @author mita
 */
public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> classeInstances;
    HashMap<String, Object> sessions; //pour contenir les variables de sessions contenues dans init
    Vector<FileUpload> fileUploads;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void setMappingUrl() {
        this.mappingUrls = new HashMap<>();
    }

    public HashMap<String, Mapping> getMappingUrl() {
        return mappingUrls;
    }

    public HashMap<String, Object> getClasseInstances() {
        return classeInstances;
    }

    public void setClasseInstances() {
        this.classeInstances = new HashMap<>();
    }

    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public void setSessions() {
        this.sessions = new HashMap<>();
    }

    public Vector<FileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads() {
        this.fileUploads = new Vector<>();
    }

    public String formatFilePath(File file) {
        String className = file.getAbsolutePath().replace(Thread.currentThread().getContextClassLoader().getResource(".").getFile(), "");
        className = className.replace(".class", "");
        className = className.replace("/", ".");
        return className;
    }

    public void checkMethodAnnotation(Class classToChecked) {
        for (Method method : classToChecked.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodAnnotation.class)) {
                String url = method.getAnnotation(MethodAnnotation.class).url();
                mappingUrls.put(url, new Mapping(classToChecked.getName(), method.getName()));
            }
        }
    }

    public void fillMappingUrl(File file) throws ClassNotFoundException {
        for (File fileUnderFile : file.listFiles()) {
            if (fileUnderFile.isFile() && fileUnderFile.getName().contains(".class")) {
                String className = formatFilePath(fileUnderFile);
                Class classTemp = Class.forName(className);
                checkMethodAnnotation(classTemp);
                checkSingleton(classTemp);
            } else if (fileUnderFile.isDirectory()) {
                fillMappingUrl(fileUnderFile);
            }
        }
    }

    public Mapping findMapping(HttpServletRequest req) {
        Mapping mappingUsed = new Mapping();
        String url = req.getServletPath().split("/")[1];
        mappingUsed = mappingUrls.get(url);
        return mappingUsed;
    }

    public void fillAttributes(HashMap<String, Object> hm, HttpServletRequest req) {
        for (Map.Entry<String, Object> entry : hm.entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public static String makeFirstCharUp(String mot) {
        String strCapitalized = mot.substring(0, 1).toUpperCase() + mot.substring(1);
        return strCapitalized;
    }
    

    public void useSet(Object object, HttpServletRequest req) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, ServletException {
//        Enumeration<String> attributeNames = req.getParameterNames();
        Field[]fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            String attributeName = field.getName();
//            try {
//                field = object.getClass().getDeclaredField(attributeName);
//            } catch (NoSuchFieldException e) {
//                continue;
//            }
            String attributeValue = req.getParameter(attributeName);
            String methodName = "set" + makeFirstCharUp(attributeName);
            Class fieldType = field.getType();
            Method setMethod = object.getClass().getDeclaredMethod(methodName, fieldType);
            if (field.getType().equals(int.class)) {
                int attribute = Integer.parseInt(attributeValue);
                setMethod.invoke(object, attribute);
            }
            if (field.getType().equals(double.class)) {
                double attribute = Double.parseDouble(attributeValue);
                setMethod.invoke(object, attribute);
            }
            if (field.getType().equals(float.class)) {
                float attribute = Float.parseFloat(attributeValue);
                setMethod.invoke(object, attribute);
            }
            if (field.getType().equals(String.class)) {
                String attribute = attributeValue;
                setMethod.invoke(object, attribute);
            }
            if (field.getType().equals(Date.class)) {
                Date attribute = Date.valueOf(attributeValue);
                setMethod.invoke(object, attribute);
            }
            if (field.getType().equals(FileUpload.class)) {
                System.out.println("huhuuuuu");
                getFileByInput(req, field);
//                setMethod.invoke(object, attributeValue);
            }
            int x = 0;
        }
    }

    /*
        prendre le nom de la fonction via l'url
        chercher le nom de la fonction dans la liste des fonctions du modele
        si il y un match 
            prendre les parametres de cette fonction 
            prendre les parametres du servletRequest
            si paramFonction[0].nom = paramServle[0].nom
                si paramFonction[0].type = paramServlet[0].type
                    paramsUsed.add(paramFonction[0])
            si paramFonction[1].nom = paramServle[0].nom
                si paramFonction[0].type = paramServlet[0].type
                    paramsUsed.add(paramFonction[0])
                    
     */
    public Object[] findArgsValues(Method method, HttpServletRequest req) {
        Parameter[] methodParameters = method.getParameters();
        Parameter[] parameters = methodParameters;
        Object[] values = new Object[methodParameters.length];
        for (int i = 0; i < values.length; i++) {
            if (parameters.length == 0) {
                return null;
            }

            String paramNameSimple = parameters[i].getName();

            if (parameters[i].getType().equals(int.class)) {
                values[i] = Integer.valueOf(req.getParameter(paramNameSimple));
            }
            if (parameters[i].getType().equals(double.class)) {
                values[i] = Double.valueOf(req.getParameter(paramNameSimple));
            }
            if (parameters[i].getType().equals(float.class)) {
                values[i] = Float.valueOf(req.getParameter(paramNameSimple));
            }
            if (parameters[i].getType().equals(String.class)) {
                values[i] = req.getParameter(paramNameSimple);
            }
            if (parameters[i].getType().equals(Date.class)) {
                values[i] = Date.valueOf(req.getParameter(paramNameSimple));
            }

            String paramNameArray = parameters[i].getName() + "[]";

            if (parameters[i].getType().getSimpleName().equals("int[]")) {
                int[] values_a = new int[req.getParameterValues(paramNameArray).length];
                for (int j = 0; j < values_a.length; j++) {
                    values_a[j] = Integer.parseInt(req.getParameterValues(paramNameArray)[j]);
                }
                values[i] = values_a;
            }
            if (parameters[i].getType().getSimpleName().equals("double[]")) {
                double[] values_a = new double[req.getParameterValues(paramNameArray).length];
                for (int j = 0; j < values_a.length; j++) {
                    values_a[j] = Double.parseDouble(req.getParameterValues(paramNameArray)[j]);
                }
                values[i] = values_a;
            }
            if (parameters[i].getType().getSimpleName().equals("float[]")) {
                float[] values_a = new float[req.getParameterValues(paramNameArray).length];
                for (int j = 0; j < values_a.length; j++) {
                    values_a[j] = Float.parseFloat(req.getParameterValues(paramNameArray)[j]);
                }
                values[i] = values_a;
            }
            if (parameters[i].getType().getSimpleName().equals("Date[]")) {
                Date[] values_a = new Date[req.getParameterValues(paramNameArray).length];
                for (int j = 0; j < values_a.length; j++) {
                    values_a[j] = Date.valueOf(req.getParameterValues(paramNameArray)[j]);
                }
                values[i] = values_a;
            }
            if (parameters[i].getType().getSimpleName().equals("String[]")) {
                values[i] = req.getParameterValues(paramNameArray);
            }
        }
        return values;
    }

    public boolean checkArgs(HttpServletRequest req, Method method) {
        for (Parameter p : method.getParameters()) {
            if (req.getParameter(p.getName() + "[]") == null && req.getParameter(p.getName()) == null) {
                return false;
            }
        }
        return true;
    }

    public Method findMethod(HttpServletRequest req, Object model) throws Exception {
        Method[] modelMethods = model.getClass().getDeclaredMethods();
        for (Method modelMethod : modelMethods) {
            String url = req.getServletPath().split("/")[1];
            if (modelMethod.isAnnotationPresent(MethodAnnotation.class) && mappingUrls.get(url).getMethod().equals(modelMethod.getName())) {
                //si la fonction appelée est une fonction sans argument
                if (checkArgs(req, modelMethod) && modelMethod.getParameterCount() != 0) {
                    return modelMethod;
                }

                if ((!Collections.list(req.getParameterNames()).isEmpty() || Collections.list(req.getParameterNames()).isEmpty()) && modelMethod.getParameterCount() == 0) {
                    return modelMethod;
                }
            }
        }
        throw new Exception("Methode introuvable");
    }

    public void checkFile(HttpServletRequest req) {

    }

    public void checkSingleton(Class classToChecked) {
        if (classToChecked.isAnnotationPresent(Singleton.class)) {
            classeInstances.put(classToChecked.getName(), null);
        }
    }

    public Object AddInClassInstances(Class classToChecked) throws InstantiationException, IllegalAccessException {
        if (classeInstances.containsKey(classToChecked.getName())) {
            if (classeInstances.get(classToChecked.getName()) == null) {
                classeInstances.replace(classToChecked.getName(), classToChecked.newInstance());
            }
            return classeInstances.get(classToChecked.getName());
        }
        return classToChecked.newInstance();
    }

    public void fillSessions(HttpServletRequest req, HashMap<String, Object> sessionsFromDataObject) {
        for (Map.Entry<String, Object> entry : sessionsFromDataObject.entrySet()) {
            if (sessions.containsValue(entry.getKey())) {
                req.getSession().setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    public void checkAuthorisation(Method m, HttpServletRequest req) throws Exception {
        if (m.isAnnotationPresent(Scope.class)) {
            int hierarchieForMethod = m.getAnnotation(Scope.class).hierarchie();
            String sessionProfilName = (String) sessions.get("sessionValue");
            // check si la fonction a besoin d'un identifiant
            if (req.getSession().getAttribute(sessionProfilName) == null) {
                throw new Exception("cette fonction requiert une connexion");
            }
            // fin du checking
            int userProfilHierarchie = (int) req.getSession().getAttribute(sessionProfilName);
            if (hierarchieForMethod > userProfilHierarchie) {
                String exceptionMessage = "Cette méthode ne peut etre appellée par vous ";
                String exceptionExplanation = "vous : " + userProfilHierarchie + " --- la fonction requiert : " + hierarchieForMethod;
                throw new Exception(exceptionMessage + "------" + exceptionExplanation);
            }
        }
    }

    // ----- sprint 13 -------
    public String changeToJson(ModelView mv) {
        String sessionToJson = "test";
        Gson gson = new Gson();
        sessionToJson = gson.toJson(mv.getData());
        return sessionToJson;
    }
    // ---- fin sprint 13 ----

    // ---- sprint 14 ----
    public String changeToJson(Object ob) {
        String objectToJson = "test";
        Gson gson = new Gson();
        objectToJson = gson.toJson(ob);
        return objectToJson;
    }

    public void setModelView(ModelView modelView, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
            sprint 6
            remplissage des datas de modelView     
         */
        if (modelView.getData() != null) {
            /*
                sprint 13
                changement des datas en Json
             */
            if (modelView.getIsJson()) {
                System.out.println(changeToJson(modelView));
            }
            /*
                sprint 6
                remplissage des attribute de la servlet
             */
            fillAttributes(modelView.getData(), req);
        }
        /*
            sprint 11
            remplissage des sessions de modelView
         */
        if (!modelView.getSessions().isEmpty()) {
            fillSessions(req, modelView.getSessions());
        }
        /*
            sprint 15
            partie 1 :
            => supprimer la session actuelle = l'invalider
         */
        if (modelView.getInvalidateSession() == true) {
            removeSession(req);
        }
        /*
            sprint 15
            partie 2 :
            => supprimer des variables de sessions choisies
         */
        if (modelView.getInvalidateSession() == false && !modelView.getSessionsToDelete().isEmpty()) {
            removeSession(req, modelView.getSessionsToDelete());
        }
        //  renvoie vers la vue avec les donnees ou non
        req.getRequestDispatcher(modelView.getView()).forward(req, resp);
    }

    /*
        sprint 15
        partie 1 : supprimer toutes les sessions
     */
    public void removeSession(HttpServletRequest req) {
        req.getSession().invalidate();
    }

    /*
        sprint 15
        partie 2 : supprimer les sessions choisies
     */
    public void removeSession(HttpServletRequest req, List<String> sessionsToDelete) {
        for (String sessionToDelete : sessionsToDelete) {
            req.removeAttribute(sessionToDelete);
        }
    }

    /*
        sprint 9
        prendre un fichier uploade
     */
    public void getFileByInput(HttpServletRequest req, Field field) throws IOException, ServletException {
        Part part = null;
        part = req.getPart(field.getName());
        if (part != null) {
            InputStream inputStream = part.getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();
            inputStream.close();
            String path = part.getSubmittedFileName();
            FileUpload fu = new FileUpload(path, fileBytes);
            System.out.println(Arrays.toString(fu.getBytes()) + "===" + fu.getName());
            fileUploads.add(fu);
        }
    }


    /*
        Fonction a appeler pour le demarrage 
        de la servlet
     */
    @Override
    public void init() throws ServletException {
        setMappingUrl();
        setClasseInstances();
        setSessions();
        setFileUploads();
        try {
            fillMappingUrl(new File(Thread.currentThread().getContextClassLoader().getResource(".").getPath()));
            /*
                getIniParameter et getIniParameterNames ne peuvent etre accessibles que dans init
             */
            // ------------- remplissage de session ------------- 
            Enumeration initParamaterNames = getInitParameterNames();
            while (initParamaterNames.hasMoreElements()) {
                String initParamaterName = (String) initParamaterNames.nextElement();
                String initParameterValue = (String) getInitParameter(initParamaterName);
                sessions.put(initParamaterName, initParameterValue);
            }
            // ------------- fin de remplissage de sessions ----------
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeAction(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (!req.getServletPath().equals("/")) {
            /*
                voir si l'input est un fichier
                sprint 9
             */
            Mapping mappingUsed = findMapping(req);
            String objectName = mappingUsed.getClassName();
            Class classCalled = null;
            Object classCalledInstance = null;
            try {
                classCalled = Class.forName(objectName);
                classCalledInstance = AddInClassInstances(classCalled);
                /*
                    set tous les champs de la classe
                 */
                useSet(classCalledInstance, req);
                /*
                    check pour la fonction trouvee
                 */
                Method methodCalled = findMethod(req, classCalledInstance);
                checkAuthorisation(methodCalled, req);
                /*
                    fin du checking
                 */
                Object[] argsValues = findArgsValues(methodCalled, req);

                /*
                    pour les fonctions qui retournent un modelView
                 */
                if (methodCalled.getReturnType().equals(ModelView.class)) {
                    ModelView modelView = new ModelView();
                    if (argsValues.length == 0) {
                        modelView = (ModelView) methodCalled.invoke(classCalledInstance);
                    } else {
                        modelView = (ModelView) methodCalled.invoke(classCalledInstance, argsValues);
                    }
                    setModelView(modelView, req, resp);
                } /*
                    pour les fonctions qui ne retournent pas de ModelView
                 */ else {
                    Object methodReturn = new Object();
                    if (argsValues.length == 0) {
                        methodReturn = methodCalled.invoke(classCalledInstance);
                    } else {
                        methodReturn = methodCalled.invoke(classCalledInstance, argsValues);
                    }
                    PrintWriter out = resp.getWriter();
                    out.print("<h2>" + changeToJson(methodReturn) + "</h2>");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
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

}
