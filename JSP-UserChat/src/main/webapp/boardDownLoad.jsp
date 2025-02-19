<%@ page import="java.io.FileNotFoundException"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.text.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="board.BoardDTO"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Ajax 실시간 회원제 채팅 서비스</title>
</head>
<body>
	<%
		request.setCharacterEncoding("UTF-8");
		String boardID = request.getParameter("boardID");
		
 		if(boardID == null || boardID.equals("")){
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근 할수 없습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
 		
 		String root = request.getSession().getServletContext().getRealPath("/");
 		String savePath = root + "upload";
 		String fileName = "";
 		String realFile = ""; 
 		BoardDAO boardDAO = new BoardDAO();
 		fileName = boardDAO.getFile(boardID);
 		realFile = boardDAO.getRealFile(boardID);
 		
 		if(realFile == null || realFile.equals("")){
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근 할수 없습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
 		
 		InputStream in = null;
 		OutputStream os = null;
 		File file = null;
 		boolean skip = false;
 		String client = "";
 		
 		try{
 			try{
 				file = new File(savePath, realFile);
 				in = new FileInputStream(file);
 			}catch (FileNotFoundException e){
 				skip = true;
 			}
 			
 			client = request.getHeader("User-Agent");
 			response.reset();
 			response.setContentType("application/octet-stream");
 			response.setHeader("Content-Disposition", "JSP Generated Data");
 			
 			if(!skip){
 				
 				if (client.indexOf("MSIE") != -1) {
 					response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("KSC5601"), "ISO8859_1"));
 				} else {
 				    fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
 				    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
 				    response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
 				}
				response.setHeader("Content-Length", "" + file.length()); 
 				os = response.getOutputStream();
 				byte b[] = new byte[(int)file.length()];
 				int leng = 0;
 				
 				while((leng = in.read(b)) > 0){
 					os.write(b, 0, leng);
 				}
 			} else {
 				response.setContentType("text/html; charset=UTF-8");
 				out.println("<script>alert('파일을 찾을 수 없습니다.');history.back();</script>");
 			}
 			in.close();
 			os.close();
 		}catch (Exception e){
 			e.printStackTrace();
 		}
	%>
</body>
</html>
