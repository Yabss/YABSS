package redirect;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.io.*;

import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

public class redirect implements SipListener, Runnable{
	private SipStack sipStack;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipProvider sipProvider;
	private String proxyHost="localhost";
	public redirect() throws ObjectInUseException, UnknownHostException{
		SipFactory sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist");
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException e1) {
			properties.setProperty("javax.sip.STACK_NAME","TextClient");
			properties.setProperty("javax.sip.IP_ADDRESS","127.0.0.1");
			try {
				properties.store(new FileOutputStream("config.properties"), null);
			}catch (IOException e) {
				System.out.println("Can't save properties in file ./config.properties");
			}
			System.out.println("Was setting default properties and save in file ./config.properties");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InetAddress inetAddress;
		inetAddress = InetAddress.getByName(proxyHost);
		try{
		sipStack=sipFactory.createSipStack(properties);
		headerFactory = sipFactory.createHeaderFactory();
		addressFactory = sipFactory.createAddressFactory();
		messageFactory = sipFactory.createMessageFactory();
        ListeningPoint udp,tcp,tls;
		tcp = sipStack.createListeningPoint(inetAddress.getHostAddress(), 5060, "TCP");
		udp = sipStack.createListeningPoint(inetAddress.getHostAddress(), 5060, "UDP");
		tls = sipStack.createListeningPoint(inetAddress.getHostAddress(), 5061, "TLS");
        sipProvider = sipStack.createSipProvider(tcp);
        sipProvider.addSipListener(this);
        sipProvider = sipStack.createSipProvider(udp);
        sipProvider.addSipListener(this);
        sipProvider = sipStack.createSipProvider(tls);
        sipProvider.addSipListener(this);
		} catch(Exception e) { 
			e.printStackTrace(); 
		} 
	}
    public void run() {  
    	System.out.println("bla bla");
    }
	@Override
	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void processRequest(RequestEvent arg0) {
		// TODO Auto-generated method stub
		Request req = arg0.getRequest();
		String method = req.getMethod();
		if( ! method.equals("MESSAGE")) {
			//bad request type.
			System.out.println("Not SIP message received");
			return;	
		}
		System.out.println("SIP message received");
	}
	@Override
	public void processResponse(ResponseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
