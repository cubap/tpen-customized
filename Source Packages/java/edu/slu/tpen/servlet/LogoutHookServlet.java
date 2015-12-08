/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.slu.tpen.servlet;

import edu.slu.tpen.servlet.util.EncryptUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hanyan
 */
@WebServlet(name = "LogoutHookServlet", urlPatterns = {"/logoutHook"})
public class LogoutHookServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String text = request.getQueryString();
            EncryptUtil mcrypt = new EncryptUtil();
            String decrypted = URLDecoder.decode(new String( mcrypt.decrypt( text ) ), "UTF-8");
            String[] params = decrypted.split("&");
            Map<String, Object> vals = new HashMap();
            for(int i = 0; i < params.length; i++){
                String param = params[i];
                if(param.contains("=")){
                    String[] nameVal = param.split("=");
                    if(nameVal.length == 2){
                        String prop = nameVal[0];
                        String val = nameVal[1];
                        vals.put(prop, val);
                    }
                }
            }
            HttpSession session = request.getSession();
            if(null != session.getAttribute("UID")){
                session.removeAttribute("UID");
            }
            if(null != session.getAttribute("role")){
                session.removeAttribute("role");
            }
            response.sendRedirect((String)vals.get("redirect_uri"));
        } catch (Exception ex) {
            Logger.getLogger(LogoutHookServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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