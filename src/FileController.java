package iris.contacts;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file.htm")
public class FileController {

	@RequestMapping(method = RequestMethod.GET)
	public String getForm(Model model) {
		UploadedFile fileModel = new UploadedFile();
		model.addAttribute("file", fileModel);
		return "file";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fileUploaded(Model model, UploadedFile file,
			BindingResult result) {

		String returnVal = "successFile";
		if (result.hasErrors()) {
			returnVal = "file";
		} else {
			MultipartFile multipartFile = file.getFile();
		}
		return returnVal;
	}
}
