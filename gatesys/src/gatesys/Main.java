package gatesys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;



public class Main {

	SerialPort activePort;
	SerialPort[] ports = SerialPort.getCommPorts();
	static File[] oldListRoot = File.listRoots();

	private String dataToSend, lastPort, id_pos = "2";
	public static OutputStream outputStream;
	private static boolean rs;

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
					if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
						 int size = event.getSerialPort().bytesAvailable();
		                    byte[] buffer = new byte[size];
		                    event.getSerialPort().readBytes(buffer, size);
		                    var dataBuffer = "";
		                    for(byte b:buffer) {
		                 	   dataBuffer += (char)b;
		                 	   if(dataBuffer.contains("#")){
		                 		   if(dataBuffer.contains("*IN1ON#")){
			             			 visitorIn("*IN1ON#");
			             			 System.out.print("VISITOR 1\n");
			             			 break;
		                           }else if(dataBuffer.contains("*IN2ON#")){
		                        	 System.out.print("VISITOR 2\n");
									 visitorIn("*IN2ON#");
									 break;
		                           }else if(dataBuffer.contains("*IN3ON#")){
		                        	 System.out.print("VISITOR 3\n");
									 visitorIn("*IN3ON#");
									 break;
		                           }else if(dataBuffer.contains("*IN4ON#")){
		                        	 System.out.print("VISITOR 4\n");
									 visitorIn("*IN4ON#");
									 break;
		                           }if(dataBuffer.contains("*PINGOK#")){
		                        	   System.out.print("PINGOK\n");
		                        	   break;
		                           }
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
             System.out.print("Connection Success!\n");
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
					String rfid = comnd.nextLine();
					CommandExecute(rfid);
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
		JsonObject jsonObject = gson.fromJson(api.PosListening(id_pos), JsonObject.class);
		JsonObject pos = gson.fromJson(jsonObject.get("pos").toString(), JsonObject.class);
		select_port = pos.get("select_port").toString().replace("\"", "");
		lastPort = select_port;
		try {
			setPort(Integer.parseInt(select_port));
		} catch (Exception e) {
			reconnectingInterface();
		}
	}

	public void openPort() {
		if(lastPort == null) {
			selectPort();
		}else {
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

	private void CommandExecute(String cmd) {
		if(cmd.contains("exit")){
			activePort.removeDataListener();
			activePort.closePort();
			System.exit(0);
		}else if(cmd.contains("open")){
			try {
				openGate();
			} catch (Exception e) {
				System.out.println("Something went wrong. #158 " + e);
				reconnectingInterface();
			}
		}else if(cmd.contains("clear")){
			try {
				clearConsole();
			} catch (Exception e) {
				System.out.println("Something went wrong. #169 " + e);
				System.exit(0);
			}
		} else if(cmd.contains("ping")){
			try {
				pingInterface();
			} catch (Exception e) {
				System.out.println("Something went wrong. #220 " + e);
				System.exit(0);
			}
		} else {
			readCard(cmd);
		}
	}

	public void readCard(String rfid){
		Api api = new Api();
		var computerName = "VISITOR-PC";
		JsonObject jsonObject = new Gson().fromJson(api.CardIn(rfid, id_pos, computerName), JsonObject.class);
        var st = jsonObject.get("status").getAsBoolean();
        if(st){
        	try {
				openGate();
			} catch (Exception e) {
				System.out.println("Something went wrong. #176");
				reconnectingInterface();
			}
        }else{
        	System.out.print(rfid + " tidak terdaftar\n");
        }
	}

	private void visitorIn(String trigger_button) {

		Api api = new Api();
		Gson gson = new Gson();

		String getPrinterName, text_title, text_caption, text_footer, text_end, generate_code, cluster, visitor_id, timeStamp;
		JsonObject jsonObject = gson.fromJson(api.MasterListening("*IN1ON#"), JsonObject.class);
		JsonObject master_visitor = gson.fromJson(jsonObject.get("master_visitor").toString(), JsonObject.class);

		text_title = "VISITOR TICKETING";
		text_caption = "Taman Kopo Indah Bandung";
		text_footer = "SELAMAT DATANG";
		text_end = "GUNAKAN TIKET UNTUK KELUAR";

		getPrinterName = master_visitor.get("printer_name").toString().replace("\"", "");
		generate_code = jsonObject.get("generate_code").toString().replace("\"", "");
		timeStamp = jsonObject.get("current_date").toString().replace("\"", "");
		cluster = master_visitor.get("keterangan").toString().replace("\"", "");
		visitor_id = master_visitor.get("visitor_id").toString().replace("\"", "");
		try {
			PrintServices printr = new PrintServices();
			printr.Printing(getPrinterName, cluster, generate_code, text_title, text_caption, text_footer, text_end, timeStamp);
			api.VisitorIn(visitor_id, generate_code);
			
			try {
				openGate();
			} catch (Exception e) {
				reconnectingInterface();
			}
		} catch (Exception e) {
			System.out.print("Print failure " + e);
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
	
	public void reconnectingInterface() {
		if(activePort != null) {
			activePort.flushIOBuffers();
			activePort.removeDataListener();
			activePort.closePort();
			activePort = null;
			lastPort = null;
		}
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

	public void clearConsole() {
		try {
			final String os = System.getProperty("os.name");
			if(os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	
	public void Pinging() {
    	Runnable helloRunnable = new Runnable() {
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
