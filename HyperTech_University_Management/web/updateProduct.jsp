<%-- 
    Document   : updateProducts
    Created on : Mar 20, 2026, 2:34:59 PM
    Author     : hasot
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="MainController" method="post">

            <!-- phân biệt add / update -->
            <input type="hidden" name="action"
                   value="${product != null ? 'editProduct' : 'addProduct'}" />

            <!-- chỉ có khi update -->
            <c:if test="${product != null}">
                <input type="hidden" name="id" value="${product.id}" />
            </c:if>

            Name:
            <input type="text" name="name"
                   value="${product != null ? product.name : ''}" /><br/>

            Category:
            <select name="category_id">
                <option value="1"
                        ${product != null && product.category_id == 1 ? 'selected' : ''}>
                    Laptop
                </option>

                <option value="2"
                        ${product != null && product.category_id == 2 ? 'selected' : ''}>
                    Màn Hình
                </option>

                <option value="3"
                        ${product != null && product.category_id == 3 ? 'selected' : ''}>
                    Bàn Phím
                </option>
                <option value="3"
                        ${product != null && product.category_id == 3 ? 'selected' : ''}>
                    Chuột
                </option>
            </select><br/>
            CPU:
            <input type="text" name="cpu"
                   value="${product != null ? product.cpu : ''}" /><br/>

            GPU:
            <input type="text" name="gpu"
                   value="${product != null ? product.gpu : ''}" /><br/>

            RAM:
            <input type="text" name="ram"
                   value="${product != null ? product.ram : ''}" /><br/>

            SSD:
            <input type="text" name="ssd"
                   value="${product != null ? product.ssd : ''}" /><br/>

            Screen:
            <input type="text" name="screen"
                   value="${product != null ? product.screen : ''}" /><br/>

            Refresh Rate:
            <input type="text" name="refresh_rate"
                   value="${product != null ? product.refresh_rate : ''}" /><br/>

            Old Price:
            <input type="text" name="old_price"
                   value="${product != null ? product.old_price : ''}" /><br/>

            New Price:
            <input type="text" name="new_price"
                   value="${product != null ? product.new_price : ''}" /><br/>

            Stock:
            <input type="text" name="stock"
                   value="${product != null ? product.stock : ''}" /><br/>

            Description:
            <input type="text" name="description"
                   value="${product != null ? product.description : ''}" /><br/>

            Image:
            <input type="text" name="image"
                   value="${product != null ? product.image : ''}" /><br/>

            <button type="submit">
                ${product != null ? 'Update' : 'Add'}
            </button>

        </form>
    </body>
</html>
