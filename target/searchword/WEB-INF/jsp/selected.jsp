<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Found Files</title>
    <style>
        table { border-collapse: collapse; width: 1000px; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2>Files that contain the word "${word}" </h2>
    <table>
        <tr>
            <th>File</th>
            <th>Status</th>
        </tr>
        <c:forEach var="file" items="${files}">
            <tr>
                <td>${file.name}</td>
                <td>
                    <c:forEach var="status" items="${file.status}" varStatus="loop">
                        ${status}
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>