/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parksys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author ridhw
 */

import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {

    SerialPort activePort;
    SerialPort[] ports = SerialPort.getCommPorts();
    static File[] oldListRoot = File.listRoots();

    private String dataToSend, lastPort;
    public static OutputStream outputStream;
    private static boolean rs;
    public static boolean ready = true;

    public void showAllPort() {
            int i = 0;
            for(SerialPort port : ports) {
                    System.out.print(i + ". " + port.getDescriptivePortName() + "");
                    System.out.println(port.getPortDescription());
                    i++;

            }
	}

    public void setPort(int portIndex) {
		activePort = ports[portIndex];
		activePort.flushIOBuffers();
		activePort.openPort();
		try {
			Serial_EventBaseReading(activePort);
            System.out.println(activePort.getPortDescription() + " port opened.");
        } catch (Exception e) {
                System.out.print(e);
                reconnectingInterface();
        }

	}

    public void Serial_EventBaseReading(SerialPort activePort) {

				if (activePort.openPort()) {
		               activePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
		               activePort.addDataListener(new SerialPortDataListener() {
		               @Override
		               public void serialEvent(SerialPortEvent event) {
		                       int size = event.getSerialPort().bytesAvailable();
		                       byte[] buffer = new byte[size];
		                       event.getSerialPort().readBytes(buffer, size);
		                       var dataBuffer = "";
		                       for(byte b:buffer) {
		                    	   dataBuffer += (char)b;
		                    	   if(dataBuffer.contains("#")){
		                    		  if(dataBuffer.contains("*IN1ON#")){
			                        	   if(ready) {
				                               parkirIn("roda_dua");
				                               System.out.print("RODA DUA\n");
				                               break;
			                        	   }
			                           }else if(dataBuffer.contains("*IN2ON#")){
			                        	   if(ready) {
			                        		   parkirIn("roda_empat");
			                        		   System.out.print("RODA EMPAT\n");
			                        		   break;
			                        	   }
			                           }else if(dataBuffer.contains("*IN3OFF#")){
			                        	   if(!ready) {
			                        		   try {
				                                   resetGate();
				                                   System.out.print("RESET\n");
				                                   break;
				                               } catch (InterruptedException ex) {
				                                   Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
				                                   reconnectingInterface();
				                               }
			                        	   }
			                           }if(dataBuffer.contains("*PINGOK#")){
			                        	   System.out.print("PINGOK\n");
			                        	   break;
			                           }
		                    	   }
		                       }
		               }


		               @Override
		               public int getListeningEvents() {
		                       return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
		                       }
		               });
		       }else {
		               System.out.print("Failed to open port #86,\nTrying re open \n");
		               reconnectingInterface();
		       }

    }

    public boolean checkConnection() {
        System.out.println("Checking connection....");
        Api api = new Api();
         if(api.CheckConnection()){
                 System.out.print("Connection Success to "+ api.mainUrl() +"!\n");
                 rs = true;
         }else {
                 rs = false;
                 System.out.print("Connection Failed!\n");
         }
         return rs;
    }

    public void start() {

        if(checkConnection()) {
                if(lastPort == null) {
                        selectPortFromDB();
                }
                try {
                	Pinging();
                    @SuppressWarnings("resource")
                    Scanner comnd = new Scanner(System.in);
                    while(true) {
                        String cmd = comnd.nextLine();
                        CommandExecute(cmd);
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong. #104");
                    start();
                }
        }else {
            int i = 5;
            while (i>0){
                System.out.println("Remaining: " + i + " seconds");
                try {
                  i--;
                  Thread.sleep(1000L);    // 1000L = 1000ms = 1 second
                  if(i == 0) {
                      System.out.println("Reconnecting... \n");
                      this.start();
                  }
                }
                catch (InterruptedException e) {
                     //I don't think you need to do anything for your particular problem
                        System.out.println("Something went wrong. #117");
                        System.exit(0);
                }
            }
        }

    }

    public void selectPort() {
		this.showAllPort();
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		System.out.print("Select Port? ");
		int p = reader.nextInt();
		lastPort = String.valueOf(p);
		setPort(p);

	}


    public void selectPortFromDB() {
		Api api = new Api();
		Gson gson = new Gson();
		String select_port;
		JsonObject jsonObject = gson.fromJson(api.MasterGate(), JsonObject.class);
		JsonObject gate = gson.fromJson(jsonObject.get("gate").toString(), JsonObject.class);
		select_port = gate.get("select_port").toString().replace("\"", "");
		lastPort = select_port;
		try {
			setPort(Integer.parseInt(select_port));
		} catch (Exception e) {
			reconnectingInterface();
		}
	}

    public void openPort() {
            if(lastPort == null) {
                selectPortFromDB();
            }else{
                setPort(Integer.parseInt(lastPort));
            }
	}

    public void closePort() {
            try {
                activePort.removeDataListener();
                activePort.closePort();
            } catch (Exception e) {
                System.out.println("Something went wrong. #146");
                System.exit(0);
            }

	}

    public void openGate() throws Exception{
    	dataToSend = "*OPEN1#";
    	System.out.print(dataToSend + "\n");
    	try {
    		outputStream = activePort.getOutputStream();
            outputStream.write(dataToSend.getBytes());
            Serial_EventBaseReading(activePort);
		} catch (Exception e) {
			reconnectingInterface();
		}

    }

    private void pingInterface() throws IOException{
        outputStream = activePort.getOutputStream();
        dataToSend = "*PING#";
        try {
        	outputStream.write(dataToSend.getBytes());
		} catch (Exception e) {
			reconnectingInterface();
		}
        Serial_EventBaseReading(activePort);
    }

    public void resetGate() throws InterruptedException{
    	clearConsole();
        TimeUnit.SECONDS.sleep(4);
        System.out.print("READY\n");
        ready = true;
    }

    public void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if(os.contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else {
                    Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
                System.out.print(e);
        }
    }

    private void parkirIn(String kategori){
        ready = false;
        Api api = new Api();
        PrintServices printr = new PrintServices();
        String getPrinterName, text_title, text_caption, text_footer, text_end, GetTanggal, GetWaktu, TicketID, BarcodeID, timeStamp;

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(api.MasterGate(), JsonObject.class);
		JsonObject gate = gson.fromJson(jsonObject.get("gate").toString(), JsonObject.class);

		getPrinterName = gate.get("nama_printer").toString().replace("\"", "");
        text_title =  gate.get("text_title").toString().replace("\"", "");
        text_caption = gate.get("text_caption").toString().replace("\"", "");
        text_footer = gate.get("text_footer").toString().replace("\"", "");
        text_end = gate.get("text_end").toString().replace("\"", "");

        GetTanggal = jsonObject.get("tanggal").toString().replace("\"", "");
        GetWaktu = jsonObject.get("waktu").toString().replace("\"", "");
        TicketID = jsonObject.get("ticket").toString().replace("\"", "");
        BarcodeID = jsonObject.get("barcode").toString().replace("\"", "");
        timeStamp = jsonObject.get("timeStamp").toString().replace("\"", "");

        switch (kategori) {
            case "roda_dua":
                    try {
                        printr.Printing(getPrinterName, TicketID, BarcodeID, "MOTOR", text_title, text_caption, text_footer, text_end, GetTanggal, GetWaktu);
                        api.parkirInWithoutPicture(kategori, TicketID, BarcodeID, timeStamp);
                        openGate();
                    } catch (Exception e) {
                        System.out.println("Something went wrong. #265 " + e);
                        reconnectingInterface();
                    }
                break;
            case "roda_empat":
                    try {
                    	printr.Printing(getPrinterName, TicketID, BarcodeID, "MOBIL", text_title, text_caption, text_footer, text_end, GetTanggal, GetWaktu);
                        api.parkirInWithoutPicture(kategori, TicketID, BarcodeID, timeStamp);
                        openGate();
                    } catch (Exception e) {
                        System.out.println("Something went wrong. #275 " + e);
                        reconnectingInterface();
                    }
                break;
            default:
                throw new AssertionError();
        }
    }

    private void CommandExecute(String cmd) {
        if(cmd.contains("exit")){
                activePort.removeDataListener();
                activePort.closePort();
                System.exit(0);
        }else if(cmd.contains("open")){
            try {
                openGate();
            } catch (Exception e) {
                System.out.println("Something went wrong. #297 " + e);
                reconnectingInterface();
            }
        }else if(cmd.contains("ping")){
            try {
                pingInterface();
            } catch (Exception e) {
                System.out.println("Something went wrong. #304 " + e);
                reconnectingInterface();
            }
        }else if(cmd.contains("clear")){
            try {
                clearConsole();
            } catch (Exception e) {
                System.out.println("Something went wrong. #311 " + e);
                System.exit(0);
            }
        }else {
            readCard(cmd);
        }
    }

   public void reconnectingInterface() {
		if(activePort != null) {
		 	try {
				resetGate();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			activePort.removeDataListener();
			activePort.closePort();
			activePort = null;
			lastPort = null;
			ready = true;
		}

//		this.start();
		runBatFile();
   }

   public void runBatFile() {
		Runtime runtime = Runtime.getRuntime();
		try {
		    Process p1 = runtime.exec("cmd /c start C:\\Users\\"+ System.getenv("USERNAME") +"\\Documents\\jar\\startapp.bat");
		    InputStream is = p1.getInputStream();
		    int i = 0;
		    while( (i = is.read() ) != -1) {
		        System.out.print((char)i);
		    }
		} catch(IOException ioException) {
		    System.out.println(ioException.getMessage() );
		}
	}

    public void readCard(String rfid){
    	if(ready) {
    		if(rfid == "open") {
    				try {
						openGate();
					} catch (Exception e) {
						// TODO: handle exception
						System.out.print(0);
					}
    		}else {
    			Api api = new Api();
    	        JsonObject jsonObject = new Gson().fromJson(api.readMember(rfid), JsonObject.class);
    	        var st = jsonObject.get("status").getAsBoolean();
    	        if(st){
    	            ready = false;
    	            Gson gson = new Gson();
    	    		JsonObject master = gson.fromJson(api.MasterGate(), JsonObject.class);

    	            String TicketID, BarcodeID, timeStamp;
    	            TicketID = master.get("ticket").toString().replace("\"", "");
    	            BarcodeID = master.get("barcode").toString().replace("\"", "");
    	            timeStamp = master.get("timeStamp").toString().replace("\"", "");
    	            api.MemberInWithoutPicture(rfid, "member", TicketID, BarcodeID, timeStamp);
    	            try {
    	                openGate();
    	            } catch (Exception ex) {
    	                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    	            }
    	        }else{
    	            System.out.println(rfid + " tidak ditemukan");
    	        }	
    		}
    	}
    }

    public void Pinging() {
    	Runnable helloRunnable = new Runnable() {
    	    @Override
			public void run() {
    	    	 try {
					pingInterface();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	};

    	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    	executor.scheduleAtFixedRate(helloRunnable, 0, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args){
        Main main = new Main();
        main.start();
    }

}
