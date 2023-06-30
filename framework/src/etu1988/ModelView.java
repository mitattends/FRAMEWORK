/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1988;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author mitantsoa
 */
public class ModelView {
    String view ;
    HashMap<String, Object> data ;
    HashMap<String, Object> sessions;
    boolean isJson = false;
    boolean invalidateSession = false;
    List<String> sessionsToDelete;
    
    public HashMap<String, Object> getSessions() {
        return sessions;
    }

    public List<String> getSessionsToDelete() {
        return sessionsToDelete;
    }

    public void setSessionsToDelete(List<String> sessionsToDelete) {
        this.sessionsToDelete = sessionsToDelete;
    }

    public void setSessions(HashMap<String, Object> sessions) {
        this.sessions = sessions;
    }
    
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public boolean getIsJson() {
        return isJson;
    }

    public void setIsJson(boolean isJson) {
        this.isJson = isJson;
    }
    
    public ModelView(){
        data = new HashMap<>();
        sessions = new HashMap<>();
        sessionsToDelete = new ArrayList<>();
    }

    // sprint 15
    public boolean getInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }
    
    public void addItem(String key, Object value){
        data.put(key, value);
    }
    
    public void addSessions(String key, Object value){
        sessions.put(key, value);
    }
    
    public void 
}
