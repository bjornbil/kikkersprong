package be.khleuven.bjornbillen.kikkersprong.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
 
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
  private void createPDF (List<Attendance> attendances, Member m, Bill b){
 
  Document doc = new Document();
  PdfWriter docWriter = null;
  this.member = member;
  this.attendances = attendances;
  this.bill = bill;
  initializeFonts();
 
  try {
   String path = "docs/" + b.getPaybeforeString() + "_" + m.getFirstname() + "_" + m.getLastname() + ".pdf";
   docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));
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
    
   for(int i=0; i < 100; i++ ){
    if(beginPage){
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
   printPageNumber(cb);
 
  }
  catch (DocumentException dex)
  {
   dex.printStackTrace();
  }
  catch (Exception ex)
  {
   ex.printStackTrace();
  }
  finally
  {
   if (doc != null)
   {
    doc.close();
   }
   if (docWriter != null)
   {
    docWriter.close();
   }
  }
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
   cb.moveTo(150,50);
   cb.lineTo(150,650);
   cb.moveTo(430,50);
   cb.lineTo(430,650);
   cb.moveTo(500,50);
   cb.lineTo(500,650);
   cb.stroke();
 
   // Invoice Detail box Text Headings 
   createHeadings(cb,22,633,"#");
   createHeadings(cb,52,633,"Datum");
   createHeadings(cb,152,633,"Startuur");
   createHeadings(cb,432,633,"Einduur");
   createHeadings(cb,502,633,"Prijs");
 
   //add the images
   Image companyLogo = Image.getInstance("images/kikkersprong_logo");
   companyLogo.setAbsolutePosition(25,700);
   companyLogo.scalePercent(25);
   doc.add(companyLogo);
 
  }
 
  catch (DocumentException dex){
   dex.printStackTrace();
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
   createHeadings(cb,200,690,"België");
    
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
 
   createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
   createContent(cb,52,y, "ITEM" + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
   createContent(cb,152,y, "Product Description - SIZE " + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
    
   double price = Double.valueOf(df.format(Math.random() * 10));
   double extPrice = price * (index+1) ;
   createContent(cb,498,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
   createContent(cb,568,y, df.format(extPrice),PdfContentByte.ALIGN_RIGHT);
    
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
 
}