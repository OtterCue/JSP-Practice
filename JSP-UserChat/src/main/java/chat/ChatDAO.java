package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ChatDAO {
	DataSource ds;
	
	public ChatDAO () {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/UserChat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID) {
		ArrayList<ChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CHAT WHERE ((fromID = ? and toID = ?) or (fromID = ? and toID = ?)) and chatID> ? order by chatTime";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID));
			
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			
			while (rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType ="오전";
				
				if(chatTime >= 12) {
					timeType ="오후";
					chatTime -=12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + ":" + rs.getString("chatTime").substring(14, 16)+"");
	            chatList.add(chat); 
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return chatList; // 리스트 반환
	}//
	
	
	
	public ArrayList<ChatDTO> getBox(String userID) {
		ArrayList<ChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CHAT "
					+ "WHERE chatID IN (SELECT MAX(chatID) "
					+ "					FROM CHAT "
					+ "					WHERE toID = ? OR fromID =? GROUP BY fromID, toID )";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			pstmt.setString(2, userID);
			
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			
			while (rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType ="오전";
				
				if(chatTime >= 12) {
					timeType ="오후";
					chatTime -=12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + ":" + rs.getString("chatTime").substring(14, 16)+"");
				chatList.add(chat); 
				
			}
			
			////////////
			List<ChatDTO> filteredChatList = new ArrayList<>();

			for (int i = 0; i < chatList.size(); i++) {
			    ChatDTO x = chatList.get(i);	
			    boolean isDuplicate = false;

			    for (int j = 0; j < filteredChatList.size(); j++) {
			        ChatDTO y = filteredChatList.get(j);
			        
			        // 양방향 채팅 여부 확인
			        if (x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
			            isDuplicate = true;
			            
			            // 최신 채팅만 유지
			            if (x.getChatID() > y.getChatID()) {
			                filteredChatList.remove(j);
			                filteredChatList.add(x);  // 최신 채팅으로 교체
			            }
			            break;
			        }
			    }

			    if (!isDuplicate) {
			        filteredChatList.add(x);  // 중복되지 않은 경우 추가
			    }
			}

			// filteredChatList에 중복 제거된 결과가 담겨 있습니다.

			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return chatList; // 리스트 반환
	}//

	
	
	
	
	public ArrayList<ChatDTO> getChatListByRecent(String fromID, String toID, int number) {
		System.out.println("ChatDAO getChatListByRecent fromID : " + fromID);
		System.out.println("ChatDAO getChatListByRecent toID : " + toID);
		System.out.println("ChatDAO getChatListByRecent number : " + number);
		
		ArrayList<ChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CHAT "
		           + "WHERE ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) "
		           + "AND chatID > ("
		           + "    SELECT MAX(chatID) - ? FROM CHAT "
		           + "    WHERE (fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)"
		           + ") "
		           + "ORDER BY chatTime";

		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number);
			pstmt.setString(6, fromID);
			pstmt.setString(7, toID);
			pstmt.setString(8, toID);
			pstmt.setString(9, fromID);
			
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			
			while (rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType ="오전";
				
				if(chatTime >= 12) {
					timeType ="오후";
					chatTime -=12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + ":" + rs.getString("chatTime").substring(14, 16)+"");
	            chatList.add(chat); 
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return chatList;
	}
	
	
	public int submit(String fromID, String toID, String chatContent) {
		System.out.println("ChatDAO submit fromID : " + fromID);
		System.out.println("ChatDAO submit toID : " + toID);
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "INSERT INTO CHAT VALUES(NULL, ?,?,?, NOW(), 0)";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;  //디비 오류
	}
	
	public int readChat(String fromID, String toID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "UPDATE CHAT SET chatRead = 1 WHERE (fromID = ? AND toID = ?)";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, toID);
			pstmt.setString(2, fromID);
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;  //디비 오류
	}
	
	
	public int getAllUnreadChat(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(chatID) FROM CHAT WHERE toID = ? AND chatRead = 0";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("COUNT(chatID)");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;  //디비 오류
	}
	
	
	public int getUnreadChat(String fromID, String toID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(chatID) FROM CHAT WHERE fromID = ? and toID = ? AND chatRead = 0";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("COUNT(chatID)");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1;  //디비 오류
	}
	
	
}//
