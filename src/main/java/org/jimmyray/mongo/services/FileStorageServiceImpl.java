package org.jimmyray.mongo.services;

import java.io.InputStream;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * Implementation of File Storage Service to store/retrieve files in GridFS
 * MongoDB store.
 * 
 * @author jimmyray
 * @version 1.0
 */
public class FileStorageServiceImpl implements FileStorageService {
	private final GridFS gridFs;

	public FileStorageServiceImpl(DB db) {
		gridFs = new GridFS(db);
	}

	public String save(InputStream inputStream, final String contentType,
			final String fileName, Map<String, String> metadata) {

		return this.save(inputStream, contentType, fileName, metadata, false);
	}

	@Override
	public String save(InputStream inputStream, final String contentType,
			final String fileName, Map<String, String> metadata,
			final boolean replace) {

		if (null != fileName) {
			this.delete(fileName);
		}

		GridFSInputFile file = this.gridFs.createFile(inputStream, fileName,
				true);
		file.setContentType(contentType);
		if (null != metadata && metadata.size() > 0) {
			file.setMetaData(new BasicDBObject(metadata));
		}
		
		return this.save(file);
	}

	@Override
	public String save(GridFSInputFile file) {
		file.save();
		return file.getId().toString();
	}

	@Override
	public GridFSDBFile get(final String id) {
		return this.gridFs.findOne(new ObjectId(id));
	}

	@Override
	public GridFSDBFile getByFileName(final String fileName) {
		return this.gridFs.findOne(fileName);
	}

	@Override
	public void delete(final String fileName) {
		GridFSDBFile file = this.getByFileName(fileName);
		if (file != null) {
			this.gridFs.remove(file);
		}
	}

	@Override
	public DBCursor getListByMap(final Map<String, String> map) {
		DBObject dbo = new BasicDBObject(map);
		return this.gridFs.getFileList(dbo);
	}
}
