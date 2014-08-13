package be.khleuven.bjornbillen.kikkersprong.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
 
public class BillsToPDF {
 
 private BaseFont bfBold;
 private BaseFont bf;
 private int pageNumber = 0;
 private Member member = null;
 private List<Attendance> attendances = null;
 private Bill bill = null;
  private double uurprijs = 3.5;
  private double optelsom = 0;
 @SuppressWarnings("unused")
public String createPDF (List<Attendance> attendances, Member member, Bill bill, double uurprijs){
 
  Document doc = new Document();
  PdfWriter docWriter = null;
  this.uurprijs = uurprijs;
  this.member = member;
  this.setAttendances(attendances);
  this.bill = bill;
  String result = "";
  String path = null;
  initializeFonts();
 
  try {
   path = "sdcard/" + bill.getPaybefore().get(Calendar.YEAR) + "-" + (bill.getPaybefore().get(Calendar.MONTH)+1) + "-" + bill.getPaybefore().get(Calendar.DATE) + "_" + member.getFirstname() + "_" + member.getLastname() + ".pdf";
   docWriter = PdfWriter.getInstance(doc , new FileOutputStream(new File(path)));
   doc.addAuthor("Kikkersprong");
   doc.addCreationDate();
   doc.addProducer();
   doc.addCreator("Kikkersprong BVBA");
   doc.addTitle("Maandelijkse afrekening");
   doc.setPageSize(PageSize.LETTER);
 
   doc.open();
   PdfContentByte cb = docWriter.getDirectContent();
    
   boolean beginPage = true;
   int y = 0;
    int j = 0;
   for(int i=0;i<attendances.size();i++ ){
    if(beginPage){
    	j = i;
     beginPage = false;
     generateLayout(doc, cb); 
     generateHeader(doc, cb);
     y = 615; 
    }
    generateDetail(doc, cb, i, y);
    y = y - 15;
    if(y < 50){
     printPageNumber(cb);
     doc.newPage();
     beginPage = true;
    }
   }
   y = y - 5;
   cb.moveTo(20,y);
   cb.lineTo(570, y);
   cb.stroke();
   y = y-15;
   createContent(cb,48,y,"Totaal",PdfContentByte.ALIGN_RIGHT);
   createContent(cb,52,y,"",PdfContentByte.ALIGN_LEFT);
   createContent(cb,152,y,"",PdfContentByte.ALIGN_LEFT);
   double price = optelsom;
   int totalduration = 0;
   for (Attendance a : attendances){
	   totalduration += a.getDuration();
   }
   DecimalFormat df = new DecimalFormat("0.00");
   String pricestring = Double.toString(price);
   createContent(cb,498,y, df.format(totalduration),PdfContentByte.ALIGN_RIGHT);
   createContent(cb,568,y, pricestring,PdfContentByte.ALIGN_RIGHT);
  }
  catch (DocumentException dex)
  {
	  result = dex.getMessage();
   dex.printStackTrace();
  }
  catch (Exception ex)
  {
	  result = ex.getMessage();
   ex.printStackTrace();
  }
  finally
  {
   if (doc != null)
   {
    doc.close();
    result = path;
   }
   if (docWriter != null)
   {
    docWriter.close();
   }
  }
  return result;
 }
 
 private void generateLayout(Document doc, PdfContentByte cb)  {
 
  try {
 
   cb.setLineWidth(1f);
 
   // Invoice Header box layout
   cb.rectangle(420,700,150,60);
   cb.moveTo(420,720);
   cb.lineTo(570,720);
   cb.moveTo(420,740);
   cb.lineTo(570,740);
   cb.moveTo(480,700);
   cb.lineTo(480,760);
   cb.stroke();
 
   // Invoice Header box Text Headings 
   createHeadings(cb,422,743,"ID Kind");
   createHeadings(cb,422,723,"Naam Kind");
   createHeadings(cb,422,703,"Datum");
 
   // Invoice Detail box layout 
   cb.rectangle(20,50,550,600);
   cb.moveTo(20,630);
   cb.lineTo(570,630);
   cb.moveTo(50,50);
   cb.lineTo(50,650);
   cb.moveTo(250,50);
   cb.lineTo(250,650);
   cb.moveTo(430,50);
   cb.lineTo(430,650);
   cb.moveTo(500,50);
   cb.lineTo(500,650);
   cb.stroke();
 
   // Invoice Detail box Text Headings 
   createHeadings(cb,22,633,"#");
   createHeadings(cb,52,633,"Startuur");
   createHeadings(cb,256,633,"Einduur");
   createHeadings(cb,432,633,"#uur");
   createHeadings(cb,502,633,"Prijs");

 
  }
 
  catch (Exception ex){
   ex.printStackTrace();
  }
 
 }
  
 private void generateHeader(Document doc, PdfContentByte cb)  {
 
  try {
 
   createHeadings(cb,200,750,"Kikkersprong");
   createHeadings(cb,200,735,"Bjorn Billen");
   createHeadings(cb,200,720,"Brusselsestraat 145");
   createHeadings(cb,200,705,"3000 Leuven");
   createHeadings(cb,200,690,"Belgium");
    
   createHeadings(cb,482,743,""+member.getId());
   createHeadings(cb,482,723,member.getFirstname() + " " + member.getLastname());
   createHeadings(cb,482,703,bill.getPaybeforeString());
 
  }
 
  catch (Exception ex){
   ex.printStackTrace();
  }
 
 }
  
 private void generateDetail(Document doc, PdfContentByte cb, int index, int y)  {
  DecimalFormat df = new DecimalFormat("0.00");
   
  try {
	  Attendance a = attendances.get(index);
   createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
   createContent(cb,52,y,a.getStartdateString(),PdfContentByte.ALIGN_LEFT);
   createContent(cb,256,y,a.getEnddateString(),PdfContentByte.ALIGN_LEFT);
   double price = a.getDuration() * uurprijs;
   optelsom += price;
   String pricestring = Double.toString(price);
   createContent(cb,498,y, df.format(a.getDuration()),PdfContentByte.ALIGN_RIGHT);
   createContent(cb,568,y, pricestring,PdfContentByte.ALIGN_RIGHT);
    
  }
 
  catch (Exception ex){
   ex.printStackTrace();
  }
 
 }
 
 private void createHeadings(PdfContentByte cb, float x, float y, String text){
 
 
  cb.beginText();
  cb.setFontAndSize(bfBold, 8);
  cb.setTextMatrix(x,y);
  cb.showText(text.trim());
  cb.endText(); 
 
 }
  
 private void printPageNumber(PdfContentByte cb){
 
 
  cb.beginText();
  cb.setFontAndSize(bfBold, 8);
  cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
  cb.endText(); 
   
  pageNumber++;
   
 }
  
 private void createContent(PdfContentByte cb, float x, float y, String text, int align){
 
 
  cb.beginText();
  cb.setFontAndSize(bf, 8);
  cb.showTextAligned(align, text.trim(), x , y, 0);
  cb.endText(); 
 
 }
 
 private void initializeFonts(){
 
 
  try {
   bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
   bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
 
  } catch (DocumentException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
 
 
 }

public List<Attendance> getAttendances() {
	return attendances;
}

public void setAttendances(List<Attendance> attendances) {
	this.attendances = attendances;
}



}
