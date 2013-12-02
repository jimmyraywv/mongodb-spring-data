package org.jimmyray.mongo.services;

import java.io.InputStream;
import java.util.Map;

import com.mongodb.DBCursor;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * File Storage Service Interface
 * 
 * @author jimmyray
 * @version 1.0
 */
public interface FileStorageService {
	String save(InputStream inputStream, String contentType, String fileName,
			Map<String, String> metadata, boolean replace);

	String save(GridFSInputFile file);

	GridFSDBFile get(String id);

	GridFSDBFile getByFileName(String fileName);

	void delete(String fileName);

	DBCursor getListByMap(Map<String, String> map);
}