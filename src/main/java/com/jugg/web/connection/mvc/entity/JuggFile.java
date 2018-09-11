package com.jugg.web.connection.mvc.entity;

/**
 * 
 * @author JingWangZou
 *
 */
public class JuggFile {
	
	private String _id; 
	private String file_name;
	private String file_content;
	private String file_type;
	private String owner;
	
	public JuggFile() {}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_content() {
		return file_content;
	}

	public void setFile_content(String file_content) {
		this.file_content = file_content;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "File [_id=" + _id + ", file_name=" + file_name + ", file_content=" + file_content + ", file_type="
				+ file_type + ", owner=" + owner + "]";
	}
	
	
	
	
	

}
