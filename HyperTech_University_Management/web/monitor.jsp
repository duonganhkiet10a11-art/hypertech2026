<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">
<title>Monitor Management</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/monitor.css">

</head>

<body>

<div class="product-container">

<h1>Quản Lý Màn Hình</h1>

<div class="product-form">

<form action="MonitorController" method="post">

<input type="hidden" name="id">

<div class="form-row">
<label>Description</label>
<input type="text" name="description">
</div>

<div class="form-row">
<label>Price</label>
<input type="number" name="price">
</div>

<div class="form-row">
<label>Image</label>
<input type="text" name="image">
</div>

<div class="form-buttons">

<button type="submit" name="action" value="add" class="add-btn">
Add
</button>

<button type="submit" name="action" value="update" class="update-btn">
Update
</button>

</div>

</form>

</div>

<div class="search-box">

<form action="MonitorController" method="get">

<input type="hidden" name="action" value="search">

<input type="text" name="keyword" placeholder="Search Monitor">

<button type="submit">Search</button>

</form>

</div>

<div class="product-table-box">

<table>

<thead>

<tr>
<th>ID</th>
<th>Description</th>
<th>Price</th>
<th>Image</th>
<th>Action</th>
</tr>

</thead>

<tbody>

<tr>

<td>1</td>
<td>Gaming Monitor 165Hz</td>
<td>4500000</td>

<td>
<img src="images/monitor.png" width="80">
</td>

<td>

<button class="edit-btn">Edit</button>
<button class="delete-btn">Delete</button>

</td>

</tr>

</tbody>

</table>

</div>

</div>

</body>
</html>