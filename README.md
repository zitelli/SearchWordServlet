## 💻 Search for words in different file extensions


<span style="color:darkblue;">Development of a Java Restful application to search for words in a list of pre-selected files.</span>

</br></br>

---

**Start application via Docker:**
- Install Tesseract OCR core:

			sudo apt-get install tesseract-ocr
		
			sudo apt-get install tesseract-ocr-eng tesseract-ocr-por
	
- docker build -t searchword .

- docker compose up -d

- docker compose down

---

## ⚙️ Endpoints

- <span style="color:darkblue;">POST request</span> 

<div style="margin-left: 40px">
Copy the files with compatible extensions into the /files folder and define the word to be searched for;
</div>
<br/>
<div style="margin-left: 40px">
<a href="http://localhost:8080/searchword/files">http://localhost:8080/searchword/files</a>
</div>


---

## 🛠 Technologies

The following technologies were used in the development of the project's Rest API:


<table>
<tr>
<td>- <strong>Java</strong></td><td><em>https://www.java.com</em></td>
</tr><tr>
<td>- <strong>Maven</strong></td><td><em>https://maven.apache.org</em></td>
</tr><tr>
<td><strong>- Tesseract OCR</strong></td><td><em>https://github.com/tesseract-ocr/tesseract</em>
 - API Docs: <em>https://tesseract-ocr.github.io</em></td>
</tr>
</table>

---

## 📝 Copyright

Project implemented and availabled by

<div style="margin-left: 40px; font-size: smaller;">
  <strong>Francisco Zitelli</strong> (<em>franziju@hotmail.com / fpzitellij@gmail.com</em>)
</div>


---