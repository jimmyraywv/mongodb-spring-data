package org.jimmyray.mongo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jimmyray.mongo.framework.SpringBeanFactory;
import org.jimmyray.mongo.services.FileStorageService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFSFile;

public class FileServiceTest {
	private static Logger log = LoggerFactory.getLogger(FileServiceTest.class);

	@Before
	public void setUp() throws Exception {
		SpringBeanFactory.initContext();
	}

	@Test
	public void testFileList() {
		FileStorageService fileStorageService = (FileStorageService) SpringBeanFactory
				.getBean("fileStorageService");

		Map<String, String> map = new HashMap<String, String>();
		map.put("contentType", "application/pdf");

		DBCursor cursor = fileStorageService.getListByMap(map);
		assertNotNull("Cursor was null.", cursor);
		assertTrue("Cursor was empty.", cursor.hasNext());
		log.info("Query:  "+cursor.getQuery().toString());
		while (cursor.hasNext()) {
			GridFSFile file = (GridFSFile) cursor.next();
			log.info(file.getId() + "::" + file.getFilename());
			if (null != file.getMetaData()) {
				log.info(file.getMetaData().toString());
			}
		}
	}
}
