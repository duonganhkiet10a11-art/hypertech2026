<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<br><br><br>

<h2>QUẢN LÝ SẢN PHẨM</h2>

<!-- ===== SEARCH + ADD ===== -->
<div style="display:flex; justify-content:space-between; margin-bottom:15px;">

    <form action="MainController" method="get">

        <input type="hidden" name="action" value="searchProductByAd"/>

        <input type="text" name="keywords"
               value="${param.keywords}"
               placeholder="Tìm sản phẩm...">

        <select name="category">
            <option value="">Tất cả</option>
            <option value="1" ${param.category eq '1' ? 'selected' : ''}>Laptop</option>
            <option value="2" ${param.category eq '2' ? 'selected' : ''}>Màn Hình</option>
            <option value="3" ${param.category eq '3' ? 'selected' : ''}>Bàn Phím</option>
            <option value="4" ${param.category eq '4' ? 'selected' : ''}>Mouse</option>
        </select>

        <button type="submit">Tìm</button>
    </form>

    <a href="updateProduct.jsp">
        <button type="button">+ Thêm</button>
    </a>
</div>

<!-- ===== TABLE ===== -->
<c:if test="${not empty list}">

    <table border="1" width="100%" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Tên</th>

            <c:if test="${isLaptop}">            
                <th>CPU</th>
                <th>GPU</th>
                <th>SSD</th>
                <th>Màn hình</th>
                <th>Hz</th>
                </c:if>

            <th>RAM</th>
            <th>Giá</th>
            <th>Status</th>
            <th>Hành động</th>
        </tr>

        <c:forEach var="p" items="${list}">
            <tr>
                <td>${p.id}</td>
                <td>${p.name}</td>

                <c:if test="${isLaptop}">                 
                    <td>${p.cpu}</td>
                    <td>${p.gpu}</td>
                    <td>${p.ssd}</td>
                    <td>${p.screen}</td>
                    <td>${p.refresh_rate}</td>
                </c:if>

                <td>${p.ram}</td>
                <td>${p.new_price}</td>
                <td>${p.status}</td>
                <td>
                    <form action="MainController" method="get">
                        <input type="hidden" name="action" value="updateProduct" />
                        <input type="hidden" name="id" value="${p.id}" />
                        <input type="hidden" name="keyword" value="${param.keyword}" />

                        <button type="submit">Update</button>
                    </form>

                    <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit" name="action" value="deleteProduct">
                            Delete
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>

    </table>

</c:if>


<!-- ===== SCRIPT ===== -->
<script>
    function openForm() {
        document.getElementById("productForm").style.display = "block";
    }

    function editProduct(id, name, cpu, gpu, ram, ssd, old_price, new_price, image) {
        openForm();

        document.getElementById("id").value = id;
        document.getElementById("name").value = name;
        document.getElementById("cpu").value = cpu;
        document.getElementById("gpu").value = gpu;
        document.getElementById("ram").value = ram;
        document.getElementById("ssd").value = ssd;
        document.getElementById("old_price").value = old_price;
        document.getElementById("new_price").value = new_price;
        document.getElementById("image_url").value = image;
    }
</script>