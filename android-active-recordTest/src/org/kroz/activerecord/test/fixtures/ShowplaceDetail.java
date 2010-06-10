package org.kroz.activerecord.test.fixtures;

import java.sql.Timestamp;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;

/**
 * Value object representing details of local media file associated with
 * showplace.  
 * @author Vladimir Kroz (vkroz)
 * 
 */
public class ShowplaceDetail extends ActiveRecordBase {

	public ShowplaceDetail() {
		
	}
	public ShowplaceDetail(Database db) {
		super(db);
	}

	public Timestamp created;
	public Timestamp modified;
	
	// Corresponding showplace ID (as appear in the table FILES_INFO) 
	public int showplaceId;
	// Textual information corresponding to audiofile 
	public String longDescription;
	// Unique file name
	public String fileName;
	// File path and file name on local storage. Example: "/sdcard/mobta/media/myfile.mp3"
	public String fullPath;
	// DateTime of file creation at local storage 
	public Timestamp fileCreateDate;
	// DateTime of last file update at local storage 
	public Timestamp fileUpdateDate;
	// Date of last file update on remote server
	public Timestamp serverUpdateDate;
	// File size in bytes
	public int size;
	// Status of the file on server (see server datamodel for valid values)
	public String fileStatus;

	public ShowplaceDetail duplicate() {
		ShowplaceDetail o = null;
		try {
			o = super.newEntity(ShowplaceDetail.class);
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} 
		o.copyFrom(this);
		return o;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[FileInfoLocalVO: ");
		sb.append("_id=").append(getID()).append("; ");
		sb.append("_createDate=").append(created).append("; ");
		sb.append("showplaceId=").append(showplaceId).append("; ");
		sb.append("fileName=").append(fileName).append("; ");
		sb.append("fullPath=").append(fullPath).append("; ");
		sb.append("fileCreateDate=").append(fileCreateDate).append("; ");
		sb.append("fileUpdateDate=").append(fileUpdateDate).append("; ");
		sb.append("serverUpdateDate=").append(serverUpdateDate).append("; ");
		sb.append("size=").append(size).append(";");
		sb.append("]");
		return sb.toString();
	}

}
