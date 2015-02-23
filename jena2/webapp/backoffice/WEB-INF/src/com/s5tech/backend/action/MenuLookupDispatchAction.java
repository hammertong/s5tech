package com.s5tech.backend.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import com.s5tech.backend.form.MenuForm;

public class MenuLookupDispatchAction extends LookupDispatchAction {
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Map getKeyMethodMap() {
		
		HashMap<String, String> map = new HashMap<String, String>();		
		
		//
		// default menu
		//
		map.put("menu.productassociation", "productassociation");		
		map.put("menu.deleteassociation", "deleteassociation");		
		map.put("menu.changeprice", "changeprice");		
		map.put("menu.alerts", "alerts");
		map.put("menu.dailyreports", "dailyreports");
		map.put("menu.details", "details");		
		map.put("menu.sticker", "sticker");
		
		//
		// network diagnostics menu
		//
		map.put ("menu.netdiag.system", "system");
		map.put ("menu.netdiag.hubs", "hubs");
		map.put ("menu.netdiag.coordinators", "coordinators");
		map.put ("menu.netdiag.queues", "queues");
		map.put ("menu.netdiag.commands", "commands");
		map.put ("menu.netdiag.esls", "esls");
		map.put ("menu.netdiag.eslcommands", "eslcommands");
		map.put ("menu.netdiag.threads", "threads");
		map.put ("menu.netdiag.config", "config");
		map.put ("menu.netdiag.firmware", "firmware");
		map.put ("menu.netdiag.graphics", "graphics");
		
		return map;
	}
	
	
	//
	// default menu 
	//
		
	public ActionForward productassociation (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Association"); 
	}
	
	public ActionForward deleteassociation (ActionMapping mapping,	ActionForm form, 
				HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Disassociation"); 
	}
	
	public ActionForward changeprice (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		return mapping.findForward("Pricer"); 
	}
	
	public ActionForward details (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Product"); 
	}
	
	public ActionForward dailyreports (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		return mapping.findForward("Daily");  		
	}	
	
	public ActionForward alerts (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Alerts");  
	}
	
	public ActionForward sticker (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Sticker"); 
	}
	
	//
	// network diagnostics menu
	//
	
	public ActionForward system (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("System"); 
	}
	
	public ActionForward hubs (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Hubs"); 
	}
	
	public ActionForward coordinators (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Coordinators"); 
	}
	
	public ActionForward queues (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Queues"); 
	}
	
	public ActionForward commands (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Commands"); 
	}
	
	public ActionForward esls (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Esls"); 
	}
	
	public ActionForward eslcommands (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("EslCommands"); 
	}
	
	public ActionForward threads (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Threads"); 
	}
	
	public ActionForward config (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Config"); 
	}

	public ActionForward firmware (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Firmware"); 
	}
	
	public ActionForward graphics(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		return mapping.findForward("Graphics"); 
	}
	
	
	
}
