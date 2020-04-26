<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
	
	<div class="container">
		<table class="table table-striped">
			<caption>Your tasks are</caption>
			<thead>
				<tr>
					<th>Description</th>
					<th>Target Date</th>
					<th>Is it Done?</th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${tasks}" var="task">
					<tr>
						<td>${task.desc}</td>
						<td><fmt:formatDate value="${task.targetDate}" pattern="dd/MM/yyyy"/></td>
						<td>${task.done}</td>
						<td><a type="button" class="btn btn-success"
							href="./update-task?id=${task.id}">Update</a></td>
						<td><a type="button" class="btn btn-warning"
							href="./delete-task?id=${task.id}">Delete</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div>
			<a class="button" href="./add-task">Add a task</a>
		</div>
	</div>
<%@ include file="common/footer.jspf" %>