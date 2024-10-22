package chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		String listType = request.getParameter("listType");
		
		System.out.println("ChatListServlet doPost request, response start");
		System.out.println("ChatListServlet doPost fromID, toID, chatContent listType : " + URLDecoder.decode(fromID, "UTF-8") +" " +URLDecoder.decode(toID, "UTF-8") +" " + URLDecoder.decode(chatContent, "UTF-8")+" " + listType);
		if(fromID == null || fromID.equals("")
				 || toID == null || toID.equals("")
				 || listType == null || listType.equals("")
				) {
			
			response.getWriter().write("");
			
		}else if(listType.equals("ten")){
			response.getWriter().write(
					getTen(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8")));
			
		}else {
			try {
				HttpSession session =request.getSession();
				
				String decodingFromID = URLDecoder.decode(fromID, "UTF-8");
				
				if(!decodingFromID.equals((String)session.getAttribute("userID"))) {
					response.getWriter().write("");
					return;
				}
				response.getWriter().write(
						getID(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8"), listType));
				
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("");
			}
		}	
	}

	private String getTen(String fromID, String toID) {
		System.out.println("ChatListServlet getTen fromID, toID : " + fromID+", "+ toID);
		
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 100);
		
		if(chatList.size() == 0) {
			return "";
		}
		
		for (int i = 0; i < chatList.size(); i++) {
		    result.append("[{\"value\":\"" + chatList.get(i).getFromID() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getToID() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
		    if(i != chatList.size() - 1) result.append(",");
		}
		
		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
		chatDAO.readChat(fromID, toID);
		
		return result.toString();
	}

	
	private String getID(String fromID, String toID, String chatID) {
		System.out.println("ChatListServlet getID fromID, toID, chatID : " + fromID+", "+ toID+", "+ chatID);
		
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		
		if(chatList.size() == 0) {
			return "";
		}
		
		for (int i = 0; i < chatList.size(); i++) {
		    result.append("[{\"value\":\"" + chatList.get(i).getFromID() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getToID() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
		    if(i != chatList.size() - 1) result.append(",");
		}
		
		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
		chatDAO.readChat(fromID, toID);
		
		return result.toString();
	}

}
