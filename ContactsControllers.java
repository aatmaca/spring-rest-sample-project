package iris.contacts;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
maxFileSize = 1024 * 1024 * 10, // 10 MB
maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ContactsControllers {

	public static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private ContactsDAO contactsDAO;

	@Autowired
	private ContactFormValidator validator;

	@Autowired
	private ServletContext servletContext;

	// public void init() {
	// org.apache.derby.drda.NetworkServerControl server = null;
	// try {
	// server = new
	// org.apache.derby.drda.NetworkServerControl(java.net.InetAddress.getByName("0.0.0.0"),
	// 1527);
	// } catch (UnknownHostException ex) {
	// ex.printStackTrace();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// try {
	// server.start(null);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping("/searchContacts")
	public ModelAndView searchContacts(@RequestParam(required = false, defaultValue = "") String ad) {
		ModelAndView mav = new ModelAndView("showContacts");
		List<Contact> contacts = contactsDAO.searchContacts(ad.trim());
		mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);
		return mav;
	}

	@RequestMapping("/randevular")
	public ModelAndView randevular(@RequestParam(required = false, defaultValue = "") String basTarih,
			@RequestParam(required = false, defaultValue = "") String bitTarih,
			@RequestParam(required = false, defaultValue = "") String randevuTarihSorgulama) {

		ModelAndView mav = new ModelAndView("showContacts");
		mav.addObject("randevuListeSayfasi", Boolean.TRUE);

		if (randevuTarihSorgulama.isEmpty()) {
			
			Date today = new Date();
			int year = today.getYear();
			int month = today.getMonth();
			int lastDayOfMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
			
			Date baslangic = new Date(year, month, 1);
			Date bitis = new Date(year, month, lastDayOfMonth);
			
			List<Contact> contacts = contactsDAO.randevular(baslangic, bitis);
			mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);
			
			mav.addObject("baslangicStr", df.format(baslangic));
			mav.addObject("bitisStr", df.format(bitis));
			return mav;
		}
		
		// Tarih ile sorgulama yapýlýyor...		
		if (basTarih.isEmpty() || bitTarih.isEmpty()) {
			mav.addObject("ikiTarihBirdenGirilmeli", Boolean.TRUE);
		} else {
			try {
				Date baslangic = df.parse(basTarih);
				Date bitis = df.parse(bitTarih);

				List<Contact> contacts = contactsDAO.randevular(baslangic, bitis);
				mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);

			} catch (ParseException e) {
				mav.addObject("tarihlerFormataUygunDegil", Boolean.TRUE);
			}
		}

		mav.addObject("baslangicStr", basTarih);
		mav.addObject("bitisStr", bitTarih);
		return mav;
	}

	@RequestMapping("/viewAllContacts")
	public ModelAndView getAllContacts() {
		ModelAndView mav = new ModelAndView("showContacts");
		List<Contact> contacts = contactsDAO.getAllContacts();
		mav.addObject("SEARCH_CONTACTS_RESULTS_KEY", contacts);
		return mav;
	}

	@RequestMapping(value = "/saveContact", method = RequestMethod.GET)
	public ModelAndView newuserForm() {
		ModelAndView mav = new ModelAndView("newContact");
		mav.getModelMap().put("newContact", new Contact());
		return mav;
	}

	@RequestMapping(value = "/saveContact", method = RequestMethod.POST)
	public String create(@ModelAttribute("newContact") Contact contact, BindingResult result, SessionStatus status) {
		validator.validate(contact, result);
		if (result.hasErrors()) {
			return "newContact";
		}
		contactsDAO.save(contact);
		status.setComplete();
		return "redirect:viewAllContacts.do";
	}

	@RequestMapping(value = "/updateContact", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id") Integer id, @RequestParam("updateSuccessful") Integer updateSuccessful,
			@RequestParam("dosyaYuklemeOk") Integer dosyaYuklemeOk) {

		ModelAndView mav = new ModelAndView("editContact");
		Contact contact = contactsDAO.getById(id);
		mav.addObject("editContact", contact);
		mav.addObject("uploadedFile", new UploadedFile());
		mav.addObject("fileList", getFileList(id));
		if (updateSuccessful.intValue() == 1) {
			mav.addObject("guncellemeBasarili", updateSuccessful);
		}
		if (dosyaYuklemeOk.intValue() == 1) {
			mav.addObject("dosyaYuklendi", dosyaYuklemeOk);
		}
		if (dosyaYuklemeOk.intValue() == -1) {
			mav.addObject("dosyaSilindi", dosyaYuklemeOk);
		}
		return mav;
	}

	@RequestMapping(value = "/updateContact", method = RequestMethod.POST)
	public String update(@ModelAttribute("editContact") Contact contact, BindingResult result, SessionStatus status) {
		validator.validate(contact, result);
		if (result.hasErrors()) {
			return "editContact";
		}
		contactsDAO.update(contact);
		status.setComplete();

		return "redirect:updateContact.do?updateSuccessful=1&dosyaYuklemeOk=0&id=" + contact.getId();
	}

	@RequestMapping("/deleteContact")
	public ModelAndView delete(@RequestParam("id") Integer id) throws IOException {
		ModelAndView mav = new ModelAndView("redirect:viewAllContacts.do");
		contactsDAO.delete(id);
		deleteDir(new File(getFolderPath(id)));
		return mav;
	}

	@RequestMapping("/deletePhoto")
	public String deletePhoto(@RequestParam("id") Integer id, @RequestParam("photoName") String photoName) throws IOException {
		new File(getFolderPath(id) + File.separator + photoName).delete();
		return "redirect:updateContact.do?updateSuccessful=0&dosyaYuklemeOk=-1&id=" + id;
	}

	@RequestMapping(value = "/dosyaYukle", method = RequestMethod.POST)
	public ModelAndView dosyaYukle(@ModelAttribute("uploadedFile") UploadedFile uploadedFile, BindingResult result, SessionStatus status)
			throws Exception {

		MultipartFile multipartFile = uploadedFile.getFile();
		Integer id = uploadedFile.getId();

		try {
			validateImage(uploadedFile.getFile());
			saveImage(multipartFile, id);
			return edit(id, 0, 1);
		} catch (RuntimeException re) {
			result.reject(re.getMessage());
			return edit(id, 0, 0);
		}
	}

	private List<String> getFileList(Integer id) {

		List<String> fileNames = new ArrayList<String>();

		File directory = new File(getFolderPath(id));
		if (directory.exists() == false) {
			return fileNames;
		}

		for (File f : directory.listFiles()) {
			fileNames.add(f.getName());
		}
		return fileNames;
	}

	private void saveImage(MultipartFile multipartFile, Integer id) throws Exception {
		String fileName = getFolderPath(id) + File.separator + multipartFile.getOriginalFilename();
		File file = new File(fileName);

		if (file.exists()) {

			String fileNameBase = null;
			String fileExtension = null;

			int lastDotIndex = fileName.lastIndexOf(".");
			if (lastDotIndex == -1) {
				fileNameBase = fileName;
				fileExtension = "";
			} else {
				fileNameBase = fileName.substring(0, lastDotIndex);
				fileExtension = fileName.substring(lastDotIndex);
			}

			int counter = 1;
			do {
				file = new File((fileNameBase + "_" + counter++) + fileExtension);
			} while (file.exists());
		}

		FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
		System.out.println("Go to the location:  " + file.toString() + " on your computer and verify that the image has been stored.");
	}

	private String getFolderPath(Integer id) {
		return servletContext.getRealPath("/") + File.separator + "images" + File.separator + id;
	}

	private void validateImage(MultipartFile image) {
		if (image.isEmpty()) {
			// Exception açýklmasýndan messages.properties içindeki açýklamaya
			// gidecek
			throw new RuntimeException("dosyaSecilmedi");
		}
	}

	public static boolean deleteDir(java.io.File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new java.io.File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}
}