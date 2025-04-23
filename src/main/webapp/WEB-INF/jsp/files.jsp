<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Select Files</title>
    <style>
        /* Optional styling for better appearance */
        select {
            width: 300px;
            height: 150px;
            padding: 5px;
            margin: 10px 0;
        }
        .input-field {
            margin: 10px 0;
        }
        label {
            display: inline-block;
            width: 150px;
        }
    </style>
</head>
<body>
    <h1>Search Word in Files</h1>
    <!-- Form to load files from directory (uses GET) -->
    <form method="get" action="files">
        <div class="input-field">
            <label for="directory">Directory Path:</label>
            <input type="text" id="directory" name="directory" 
                   value="/files" required placeholder="Enter directory path">
            <input type="submit" value="Load Files">
        </div>
    </form>
    <form method="post" action="files">
        <div class="input-field">
            <label for="word">Word to Search:</label>
            <input type="text" id="word" name="word" required placeholder="Enter search word">
        </div>
        
        <h2>Files in Directory:</h2>
        <select name="selectedFiles" multiple>
            <c:forEach items="${files}" var="file">
                <option value="${file}">${file}</option>
            </c:forEach>
        </select>
        <br>
        <input type="submit" value="Search in Selected Files">
        <p>(Hold Ctrl/Cmd to select multiple files)</p>
    </form>
</body>
</html>