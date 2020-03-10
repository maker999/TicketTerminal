package prüfung_24_11_2016;
import java.sql.Connection;
import java.util.*;

public class TicketTerminal {
	private String terminalLocation;
	private UserInterface ui;
	private EnterpriseServer enterpriseServer;
	public TicketTerminal(String location){
		this.terminalLocation = location;
		this.ui = ui.getInstance();
		this.enterpriseServer = EnterpriseServer.getInstance();
	}
	public static void main(String[] args){
		
	}
	
	public void run(PaymentCard card){
		String orig = this.terminalLocation;
		//String dest = null;
		List<Connection> conns;
		Ticket t;
		while(1){
			try{
				conns = enterpriseServer.getConnections(orig, null);
			}catch(NoConnectionException nce){
				ui.display(UIMeassages.NO_CONNECTION);
			}
			Connection c = ui.selectConnection(conns);
			if( c == null ){
				break;
			}
			orig = ((FerryConnection)c).getDestination();
			conns.add(c);
		}
		
		Discount d = ui.getDiscount();
		ui.display(UIMessages.GET_CONFIRMATION);
		if(ui.getConfirmation()){
			try{
				t = enterpriseServer.createTicket(conns, card, d);
			}catch(PaymentException pe){
				ui.display(UIMessages.PAYMENT_FAILED);
			}
			ui.display(UIMessages.CONFIRM_BOOKING);
		}
	}
	public void checkTicketSegment(String ticketcode, String destination) {
		try{
			if( enterpriseServer.checkTicket(this.terminalLocation, destination, ticketcode) )
				ui.display(UIMessages.TICKET_DEVALUED);
			else ui.display(UIMessages.TICKET_INVALID);
		}catch(TicketException e){
			ui.display(UIMessages.NO_TICKET);
		}		
	}
	
}
