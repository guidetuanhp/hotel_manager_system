package upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.Cloudinary;

public class FileUpload {

	public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "husttung");
        config.put("api_key", "815691425451157");
        config.put("api_secret", "tV_WLzenfX-C8xvnK5c6A6Uob0M");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
	
	public String upload(String path) {
		Cloudinary cloudinaryConfig = cloudinaryConfig();
		File file = new File(path);
		Map uploadResult;
		Map uploadParams = new HashMap<String, String>();
		uploadParams.put("resource_type", "auto");
		try {
			uploadResult = cloudinaryConfig.uploader().upload(file, uploadParams);
			return uploadResult.get("url").toString();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		return null;
	}
	
}
