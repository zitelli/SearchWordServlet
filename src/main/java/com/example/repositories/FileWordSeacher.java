package com.example.repositories;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileWordSeacher {
	List<String> search(Path filePath, String word) throws IOException, Exception;
}
