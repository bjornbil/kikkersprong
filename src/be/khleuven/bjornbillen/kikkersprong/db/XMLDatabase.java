package be.khleuven.bjornbillen.kikkersprong.db;

import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class XMLDatabase {
	
	private Context context;
	static final String FTP_HOST = "http://r0258068.webontwerp.khleuven.be";
	static final String FTP_USER = "r0258068";
	static final String FTP_PASS = "Bejarn02";
	private BillDAO billcontroller;
	private MemberDAO membercontroller;
	private AttendanceDAO attendancecontroller;
	
	public XMLDatabase(Context context) {
		this.setContext(context);
		membercontroller = MemberDAO.getInstance(context);
		attendancecontroller = AttendanceDAO.getInstance(context);
		billcontroller = BillDAO.getInstance(context);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writetoXML() throws IllegalArgumentException,
			IllegalStateException, IOException {
		

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File("sdcard/kikkersprong.xml"));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(fos, "UTF-8");
		serializer.startDocument(null, Boolean.valueOf(true));
		serializer.setFeature(
				"http://xmlpull.org/v1/doc/features.html#indent-output", true);

		List<Member> members = getMemberController().getAllMembers();
		List<Attendance> attendances = getAttendanceController().getAllAttendances();
		List<Bill> bills = getBillController().getAllBills();
		serializer.startTag(null, "root");
		for (Member m : members) {
			serializer.startTag(null, "member");
			serializer.startTag(null, "id");
			serializer.text(Integer.toString(m.getId()));
			serializer.endTag(null, "id");
			serializer.startTag(null, "name");
			serializer.text(m.getFirstname() + " " + m.getLastname());
			serializer.endTag(null, "name");
			serializer.startTag(null, "birthday");
			serializer.text(m.getBirthdayString());
			serializer.endTag(null, "birthday");
			serializer.startTag(null, "present");
			serializer.text(Boolean.toString(m.isPresent()));
			serializer.endTag(null, "present");
			serializer.startTag(null, "imgurl");
			serializer.text(m.getImageurl());
			serializer.endTag(null, "imgurl");
			serializer.startTag(null, "checkin");
			serializer.text(m.getLastCheckinString());
			serializer.endTag(null, "checkin");
			serializer.endTag(null, "member");
		}

		for (Attendance a : attendances) {
			serializer.startTag(null, "attendance");
			serializer.startTag(null, "id");
			serializer.text(Integer.toString(a.getId()));
			serializer.endTag(null, "id");
			serializer.startTag(null, "memberid");
			serializer.text(Integer.toString(a.getMember().getId()));
			serializer.endTag(null, "memberid");
			serializer.startTag(null, "startdate");
			serializer.text(a.getStartdateString());
			serializer.endTag(null, "startdate");
			serializer.startTag(null, "enddate");
			serializer.text(a.getEnddateString());
			serializer.endTag(null, "enddate");
			serializer.endTag(null, "attendance");
		}

		for (Bill b : bills) {
			serializer.startTag(null, "bill");
			serializer.startTag(null, "id");
			serializer.text(Integer.toString(b.getId()));
			serializer.endTag(null, "id");
			serializer.startTag(null, "memberid");
			serializer.text(Integer.toString(b.getMember().getId()));
			serializer.endTag(null, "memberid");
			serializer.startTag(null, "amount");
			serializer.text(Double.toString(b.getAmount()));
			serializer.endTag(null, "amount");
			serializer.startTag(null, "paydate");
			serializer.text(b.getPaybeforeString());
			serializer.endTag(null, "paydate");
			serializer.startTag(null, "ispaid");
			serializer.text(Boolean.toString(b.isPaid()));
			serializer.endTag(null, "ispaid");
			serializer.endTag(null, "bill");
		}
		serializer.endTag(null, "root");

		serializer.endDocument();

		serializer.flush();

		fos.close();
		new AsyncTask(){
			@Override
			protected Object doInBackground(Object... params) {
				writeToFTP("r0258068", "Bejarn02");
				return null;
			}
		
		}.execute();
		
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public AttendanceDAO getAttendanceController(){
		return attendancecontroller;
	}
	public BillDAO getBillController(){
		return billcontroller;
	}
	public void writeToFTP(String userName, String pass) {  
        
        try {
            FTPSClient ftpClient = new FTPSClient("TLS",false);
            // Connect to host
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);
            KeyManager km = kmf.getKeyManagers()[0];

            
            ftpClient.setKeyManager(km);
            ftpClient.connect("r0258068.webontwerp.khleuven.be");
           
            int reply = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
            	
              // Login
              if (ftpClient.login(userName, pass)) {
            	
                // Set protection buffer size
                ftpClient.execPBSZ(0);
                // Set data channel protection to private
                ftpClient.execPROT("P");
                // Enter local passive mode
                ftpClient.enterLocalPassiveMode();

                // Store file on host
      	  InputStream is = new FileInputStream("sdcard/kikkersprong.xml");
      	  if (ftpClient.storeFile("kikkersprong.xml", is)) {
      	    is.close();
      	  
      	  } else {
      	    System.out.println("Could not store file");
      	  }
      	  // Logout
      	  ftpClient.logout();

              } else {
                System.out.println("FTP login failed");
              }

              // Disconnect
          	ftpClient.disconnect();

            } else {
              System.out.println("FTP connect to host failed");
            }
          } catch (IOException ioe) {
            System.out.println("FTP client received network error   " + ioe.toString());
          } catch (NoSuchAlgorithmException nsae) {
            System.out.println("FTP client could not use SSL algorithm");
          } catch (UnrecoverableKeyException e) {
			
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
        }
     
	public void readFromFTP(String userName, String pass){
		 try {
	            FTPSClient ftpClient = new FTPSClient("TLS",false);
	            // Connect to host
	            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	            kmf.init(null, null);
	            KeyManager km = kmf.getKeyManagers()[0];

	            
	            ftpClient.setKeyManager(km);
	            ftpClient.setDefaultTimeout(100000000);
	            
	            ftpClient.connect("r0258068.webontwerp.khleuven.be");
	            int reply = ftpClient.getReplyCode();
	            if (FTPReply.isPositiveCompletion(reply)) {

	              // Login
	              if (ftpClient.login(userName, pass)) {

	                // Set protection buffer size
	                ftpClient.execPBSZ(0);
	                // Set data channel protection to private
	                ftpClient.execPROT("P");
	                // Enter local passive mode
	                ftpClient.enterLocalPassiveMode();
	                
	                // Store file on host
	      	  OutputStream os = new FileOutputStream("sdcard/kikkersprong.xml");
	      	  if (ftpClient.retrieveFile("kikkersprong.xml", os)) {
	      		os.close();
	      	  } else {
	      	    System.out.println("Could not store file");
	      	  }
	      	  // Logout
	      	  ftpClient.logout();

	              } else {
	                System.out.println("FTP login failed");
	              }

	              // Disconnect
	          	ftpClient.disconnect();

	            } else {
	              System.out.println("FTP connect to host failed");
	            }
	          } catch (IOException ioe) {
	            System.out.println("FTP client received network error   " + ioe.toString());
	          } catch (NoSuchAlgorithmException nsae) {
	            System.out.println("FTP client could not use SSL algorithm");
	          } catch (UnrecoverableKeyException e) {
				
				e.printStackTrace();
			} catch (KeyStoreException e) {
				
				e.printStackTrace();
			}
	}
	public void parseBills() {
		try {
			File file = new File("sdcard/kikkersprong.xml");
			InputStream is = new FileInputStream(file.getPath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new InputSource(is));
			// doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("bill");

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				Element fstElmnt = (Element) node;

				int id = Integer.parseInt(fstElmnt.getChildNodes().item(1)
						.getTextContent());
				int memberid = Integer.parseInt(fstElmnt.getChildNodes()
						.item(3).getTextContent());
				double amount = Double.parseDouble(fstElmnt.getChildNodes()
						.item(5).getTextContent());
				Calendar paydate = Calendar.getInstance();
				paydate.set(
						Calendar.YEAR,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split("/")[0]));
				paydate.set(
						Calendar.MONTH,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split("/")[1])-1);
				paydate.set(
						Calendar.DATE,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split("/")[2]));
				boolean ispaid = false;
				if (fstElmnt.getChildNodes().item(9).getTextContent()
						.equals("true")) {
					ispaid = true;
				}

				Bill b = new Bill(id, amount,
						getMemberController().getMember(memberid), paydate, ispaid);
				if (getBillController().hasBill(b)) {
					getBillController().updateBill(b);
				} else {
					getBillController().addBill(b);
				}
			}
		} catch (Exception e) {
			Log.d("ERROR","Exception bill : " + e.getMessage());
		}
	}

	public void parseMembers() {
		try {
			File file = new File("sdcard/kikkersprong.xml");
			InputStream is = new FileInputStream(file.getPath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new InputSource(is));
			// doc.getDocumentElement().normalize();

			NodeList nl = doc.getElementsByTagName("member");

			for (int i = 0; i < nl.getLength(); i++) {
				Element fstElmnt = (Element) nl.item(i);

				int id = Integer.parseInt(fstElmnt.getChildNodes().item(1)
						.getTextContent());

				String firstname = fstElmnt.getChildNodes().item(3)
						.getTextContent().split(" ")[0];
				String lastname = fstElmnt.getChildNodes().item(3)
						.getTextContent().split(" ")[1];

				Calendar birthday = Calendar.getInstance();
				birthday.set(
						Calendar.YEAR,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split("/")[0]));
				birthday.set(
						Calendar.MONTH,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split("/")[1])-1);
				birthday.set(
						Calendar.DATE,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split("/")[2]));

				boolean ispresent = false;
				if (fstElmnt.getChildNodes().item(7).getTextContent()
						.equals("true")) {
					ispresent = true;
				}

				Calendar lastcheck = Calendar.getInstance();
				lastcheck.set(
						Calendar.YEAR,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[0].split("/")[0]));
				lastcheck.set(
						Calendar.MONTH,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[0].split("/")[1])-1);
				lastcheck.set(
						Calendar.DATE,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[0].split("/")[2]));
				lastcheck.set(
						Calendar.HOUR_OF_DAY,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[1].split(":")[0]));
				lastcheck.set(
						Calendar.MINUTE,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[1].split(":")[1]));
				lastcheck.set(
						Calendar.SECOND,
						Integer.parseInt(fstElmnt.getChildNodes().item(11)
								.getTextContent().split(" ")[1].split(":")[2]));
				String imgurl = fstElmnt.getChildNodes().item(9)
						.getTextContent();
				Member m = new Member(id, firstname, lastname, birthday,
						imgurl, ispresent, lastcheck);
				if (getMemberController().existMember(firstname, lastname)) {
					getMemberController().updateMember(m);
				} else {
					getMemberController().addMember(m);
				}

			}

		} catch (Exception e) {
			Log.d("ERROR","Exception member : " + e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadFromXML() throws IOException {
		new AsyncTask(){
			@Override
			protected Object doInBackground(Object... params) {
				readFromFTP("r0258068", "Bejarn02");
				return null;
			}
		
		}.execute();
		parseMembers();
		parseAttendances();
		parseBills();
		
		
	}

	

	private void parseAttendances() {
		try {
			File file = new File("sdcard/kikkersprong.xml");
			InputStream is = new FileInputStream(file.getPath());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(new InputSource(is));
			// doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("attendance");

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				Element fstElmnt = (Element) node;

				int id = Integer.parseInt(fstElmnt.getChildNodes().item(1)
						.getTextContent());

				int memberid = Integer.parseInt(fstElmnt.getChildNodes()
						.item(3).getTextContent());

				Calendar startdate = Calendar.getInstance();
				startdate.set(
						Calendar.YEAR,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[0].split("/")[0]));
				startdate.set(
						Calendar.MONTH,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[0].split("/")[1])-1);
				startdate.set(
						Calendar.DATE,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[0].split("/")[2]));
				startdate.set(
						Calendar.HOUR_OF_DAY,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[1].split(":")[0]));
				startdate.set(
						Calendar.MINUTE,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[1].split(":")[1]));
				startdate.set(
						Calendar.SECOND,
						Integer.parseInt(fstElmnt.getChildNodes().item(5)
								.getTextContent().split(" ")[1].split(":")[2]));
				Calendar enddate = Calendar.getInstance();
				enddate.set(
						Calendar.YEAR,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[0].split("/")[0]));
				enddate.set(
						Calendar.MONTH,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[0].split("/")[1])-1);
				enddate.set(
						Calendar.DATE,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[0].split("/")[2]));
				enddate.set(
						Calendar.HOUR_OF_DAY,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[1].split(":")[0]));
				enddate.set(
						Calendar.MINUTE,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[1].split(":")[1]));
				enddate.set(
						Calendar.SECOND,
						Integer.parseInt(fstElmnt.getChildNodes().item(7)
								.getTextContent().split(" ")[1].split(":")[2]));

				Attendance a = new Attendance(id,
						getMemberController().getMember(memberid), startdate,
						enddate);
				if (getAttendanceController().hasAttendance(a)) {
					getAttendanceController().updateAttendance(a);
				} else {
					getAttendanceController().addAttendance(a);
				}

			}

		} catch (Exception e) {
			Log.d("ERROR","Exception att : " + e.getMessage());
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
