<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<title>Ajax 실시간 회원제 채팅 서비스</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		function registerCheckFunction(){
			var userID = $('#userID').val();
			$.ajax({
				type: 'POST',
				url: './UserRegisterCheckServlet',
				data: {userID : userID},
				success: function(result){
					if(result == 1){
						console.log("가입가능한 아이디")
						$('#checkMessage').html('사용할수 있는 아이디입니다.');
						$('#checkType').attr('class', 'modal-content panel-success');
					}else{
						
						$('#checkMessage').html('사용할수 없는 아이디입니다.');
						$('#checkType').attr('class', 'modal-content panel-warning');
					}
					$('#checkModal').modal("show");
				}
			});
		}
		
		
		function passwordCheckFunction(){
			var userPw1 = $('#userPw1').val();
			var userPw2 = $('#userPw2').val();
			if(userPw1 !=userPw2 ){
				$('#passwordCheckMessage').html('비밀번호가 서로 일치하지 않습니다.');
			}else{
				$('#passwordCheckMessage').html('');
			}
		}
		
		
	</script>

</head>
<body>
	<%
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		
		if(userID !=null){
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "현재 로그인이 되어 있는 상태입니다");
			response.sendRedirect("index.jsp");
			return;
		}
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" 
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" 
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">실시간 회원제 채팅 서비스</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="index.jsp">메인</a></li>
				<li><a href="find.jsp">친구찾기</a></li>
				<li><a href="box.jsp">메세지함<span id="unread" class="label label-info"></span></a></li>
				<li><a href="boardView.jsp">자유게시판</a></li>
			</ul>
			<% 
				if(userID == null) { 
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					   data-toggle="dropdown" role="button" aria-haspopup="true"
					   aria-expanded="false">접속하기 <span class="caret"></span>
					</a>
					<ul class="dropdown-menu"> <!-- 수정된 부분 -->
					    <li><a href="login.jsp">로그인</a></li>
					    <li class="active"><a href="join.jsp">회원가입</a></li>
				    </ul>
				</li>
			</ul>
			<%
				}
			%>
		</div>
	</nav>
	
	<div class="container">
		<form action="./userRegister" method="post">
			<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3"><h4>회원 등록 양식</h4></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 110px;"><h5>아이디</h5></td>
						<td><input class="form-control" type="text" id="userID" name="userID" maxlength="20" placeholder="아이디를 입력하세요"></td>
						<td style="width: 110px;"><button type ="button" class="btn btn-primary" onclick="registerCheckFunction()">중복체크</button></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>비밀번호</h5></td>
						<td colspan="2"><input onkeyup="passwordCheckFunction();"  class="form-control" type="password" id="userPw1" name="userPw1" maxlength="20" placeholder="비밀번호를 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>비밀번호</h5></td>
						<td colspan="2"><input onkeyup="passwordCheckFunction();"  class="form-control" type="password" id="userPw2" name="userPw2" maxlength="20" placeholder="비밀번호 확인을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>이름</h5></td>
						<td colspan="2"><input class="form-control" type="text" id="userName" name="userName" maxlength="20" placeholder="이름을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>나이</h5></td>
						<td colspan="2"><input class="form-control" type="number" id="userAge" name="userAge" maxlength="20" placeholder="나이을 입력하세요"></td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>성별</h5></td>
						<td colspan="2">
							<div class="form-group" style="text-align: center; margin: 0 auto;">
								<div class="btn-group" data-toggle="buttons">
									<label class="btn btn-primary active">
										<input type="radio" name="userGender" autocapitalize="off" value="남자" checked="checked">남자
									</label>
									<label class="btn btn-primary">
										<input type="radio" name="userGender" autocapitalize="off" value="여자">여자
									</label>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td style="width: 110px;"><h5>이메일</h5></td>
						<td colspan="2"><input class="form-control" type="email" id="userEmail" name="userEmail" maxlength="20" placeholder="이메일을 입력하세요"></td>
					</tr>
					<tr>
						<td style="text-align: left;" colspan="3">
						    <h5 style="color: red;" id="passwordCheckMessage"></h5>
						    <input class="btn btn-primary pull-right" type="submit" value="등록">
						</td>					
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<%
		String messageContent = null;
		if(session.getAttribute("messageContent") !=null){
			messageContent = (String)session.getAttribute("messageContent");
		}
		String messageType = null;
		if(session.getAttribute("messageType") !=null){
			messageType = (String)session.getAttribute("messageType");
		}
		if(messageContent != null){
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="vertical-alignment-helper">
	        <div class="modal-dialog vertical-align-center">
	            <div class="modal-content 
	            	<%
					    if (messageType != null && messageType.equals("오류 메시지")) {
					        out.println("panel-warning");
					    } else {
					        out.println("panel-success");
					    }
					%>">
            		<div class="modal-header panel-heading">
            			<button type="button" class="close" data-dismiss="modal">
            				<span aria-hidden="true">&times;</span>
            				<span class="sr-only">Close</span>
            			</button>
            			<h4 class="modal-title">
            				<%= messageType %>
            			</h4>
            		</div>
            		<div class="modal-body">
            			<%= messageContent %>
            		</div>
            		<div class="modal-footer">
            			<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
            		</div>
      			</div>
	        </div>
	    </div>
	</div>
	<script type="text/javascript">
		$('#messageModal').modal("show");
	</script>
	<%
		session.removeAttribute("messageContent");
		session.removeAttribute("messageType");
		}
	%>
	<div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-hidden="true">
	    <div class="vertical-alignment-helper">
	        <div class="modal-dialog vertical-align-center">
	            <div id="checkType" class="modal-content panel-info">
            		<div class="modal-header panel-heading">
            			<button type="button" class="close" data-dismiss="modal">
            				<span aria-hidden="true">&times;</span>
            				<span class="sr-only">Close</span>
            			</button>
            			<h4 class="modal-title">
            				확인 메세지
            			</h4>
            		</div>
            		<div id="checkMessage" class="modal-body">
            		</div>
            		<div class="modal-footer">
					    <button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
      			</div>
	        </div>
	    </div>
	</div>
</body>
</html>
