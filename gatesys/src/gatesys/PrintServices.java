package gatesys;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.print.PrintService;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.output.PrinterOutputStream;

public class PrintServices {

	    public void Printing(String printerName, String cluster, String generate_code, String text_title, String text_caption, String text_footer, String text_end, String timeStamp) {
	    	PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
	        EscPos escpos;
	        try {
	            escpos = new EscPos(new PrinterOutputStream(printService));

	            Style title = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2).setJustification(EscPosConst.Justification.Center).setBold(true);

	            Style center = new Style().setJustification(EscPosConst.Justification.Center);

	            escpos.writeLF(title, text_title);
	            escpos.writeLF(center, text_caption);
	            escpos.writeLF("   Visit     : " + cluster);
	            escpos.writeLF("   Waktu     : " + timeStamp);

	            BarCode barcode = new BarCode();
	            barcode.setSystem(BarCode.BarCodeSystem.CODE128);
	            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode).setJustification(EscPosConst.Justification.Center);
	            barcode.setBarCodeSize(3, 150);
	            escpos.write(barcode, "{B"+generate_code);
	            escpos.feed(1);
	            escpos.writeLF(center, text_footer);
	            escpos.writeLF(center, text_end);
	            escpos.feed(5);

	            escpos.cut(EscPos.CutMode.FULL);

	            escpos.close();

	        } catch (IOException ex) {
	            Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
	    
	    public void PrintTest() {
	    	String printerName = "epson";
	    	PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
	        EscPos escpos;
	        try {
	            escpos = new EscPos(new PrinterOutputStream(printService));

	            Style title = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2).setJustification(EscPosConst.Justification.Center).setBold(true);

	            Style center = new Style().setJustification(EscPosConst.Justification.Center);

	            escpos.writeLF(title, "TITLE");
	            escpos.writeLF(center, "CAPTION");
	            escpos.writeLF("   Visit     : ");
	            escpos.writeLF("   Waktu     : ");

	            BarCode barcode = new BarCode();
	            barcode.setSystem(BarCode.BarCodeSystem.CODE128);
	            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode).setJustification(EscPosConst.Justification.Center);
	            barcode.setBarCodeSize(3, 150);
	            escpos.write(barcode, "{B9123123123178");
	            escpos.feed(5);

	            escpos.cut(EscPos.CutMode.FULL);

	            escpos.close();

	        } catch (IOException ex) {
	            Logger.getLogger(PrintServices.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }

	     public static void main(String[] args) {
	        if (args.length != 1) {
	            System.out.println("Printer list to use:");
	            String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
	            for (String printServiceName : printServicesNames) {
	                System.out.println(printServiceName);
	            }
	        }
	        
//	        PrintServices ps = new PrintServices();
//	        ps.PrintTest();

	    }
}
