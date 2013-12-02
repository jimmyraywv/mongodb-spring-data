package org.jimmyray.mongo.framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Filer is a utility class to perform the heavy lifting of file IO.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class Filer {
	private static Logger log = LoggerFactory.getLogger(Filer.class);

	/**
	 * Reads a file from a path.
	 * 
	 * @param filePath
	 * @return
	 */
	public List<String> readFile(String filePath) {
		if (null == filePath) {
			throw new IllegalArgumentException(
					Properties.getString("filer.msg.illegalArgumentException")); //$NON-NLS-1$
		}

		File file = new File(filePath);

		return this.readFile(file);
	}

	/**
	 * Reads a file as a Spring classpath resource.
	 * 
	 * @param url
	 * @return
	 */
	public List<String> readFileAsResource(String url) {
		Resource resource = new ClassPathResource(url);
		File file = null;

		try {
			file = resource.getFile();
		} catch (IOException ioe) {
			log.error(ioe.getLocalizedMessage());
		}

		return this.readFile(file);
	}

	/**
	 * Reads a file.
	 * 
	 * @param file
	 * @return
	 */
	private List<String> readFile(File file) {
		List<String> lines = new ArrayList<String>();

		Scanner scanner = null;

		try {
			scanner = new Scanner(file, ApplicationConstants.DEFAULT_ENCODING);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lines.add(line.trim());
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		} finally {
			if (null != scanner) {
				scanner.close();
			}
		}

		return lines;
	}

	public void writeFile(String filePath, List<String> data)
			throws IOException {
		PrintWriter printWriter = null;

		try {
			FileWriter fileWriter = new FileWriter(filePath);
			printWriter = new PrintWriter(fileWriter);

			for (String line : data) {
				printWriter.println(line);
			}

		} finally {
			if (null != printWriter) {
				printWriter.close();
			}
		}
	}
}
