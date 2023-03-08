package parksys;

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
	Api api = new Api();
    public void Printing(String printerName, String TicketID, String BarcodeID, String kendaraan, String text_title, String text_caption, String text_footer, String text_end, String tanggal, String waktu) {


        // get the printer service by name passed on command line...
        //this call is slow, try to use it only once and reuse the PrintService variable.



        PrintService printService = PrinterOutputStream.getPrintServiceByName(printerName);
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));

            Style title = new Style().setFontSize(Style.FontSize._2, Style.FontSize._2).setJustification(EscPosConst.Justification.Center).setBold(true);

            Style center = new Style().setJustification(EscPosConst.Justification.Center);
            Style StyleKategori = new Style(escpos.getStyle()).setFontSize(Style.FontSize._2, Style.FontSize._2);

            escpos.writeLF(title, text_title);
            escpos.writeLF(center, text_caption);
            escpos.write("   No Ticket : " + TicketID).writeLF(StyleKategori,"       "+ kendaraan);
            escpos.writeLF("   Tanggal   : " + tanggal);
            escpos.writeLF("   Waktu     : " + waktu);

            BarCode barcode = new BarCode();
            barcode.setSystem(BarCode.BarCodeSystem.CODE128);
            barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode).setJustification(EscPosConst.Justification.Center);
            barcode.setBarCodeSize(3, 150);
            escpos.write(barcode, "{B"+"*"+BarcodeID+"*");
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

     public static void main(String[] args) {
        if (args.length != 1) {
////            System.out.println("Usage: java -jar xyz.jar (\"printer name\")");
            System.out.println("Printer list to use:");
            String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
            for (String printServiceName : printServicesNames) {
                System.out.println(printServiceName);
            }
//
////            System.exit(0);
        }
//        PrintServices obj = new PrintServices();
//        obj.Printing("EPSON TM-T82X Receipt", "927321", "549282514262", "MOBIL", "TITLE", "CAPTION", "FOOTER", "END", "10 July 2022", "15:23:23");

    }
}
