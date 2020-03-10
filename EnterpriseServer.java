package prüfung_24_11_2016;
import java.sql.Connection;
import java.util.*;

public class EnterpriseServer {
	private static EnterpriseServer server;
	private List<Ticket> tickets;
	private List<Connection> connections;
	
	private EnterpriseServer(){
		this.connections = new Vector<Connection>();
		this.tickets = new Vector<Ticket>();
	}
	
	public Ticket createTicket(List<Connection> connections, PaymentCard card, Discout discount) throws PaymentException{
		float price = 0.0;
		Ticket t;
		for(Connection c: connections){
			price += this.getPrice( ( (FerryConnection)c ).getDistnace(), discount);
		}
		t = new Ticket(this.generateTicketCode(), price, discount );
		for(Connection c: connections){
			t.addConnection( (FerryConnection)c );
		}
		if(BankServer.checkCreditCard((CreditCard)card) || BankServer.checkATMCard((ATMCard)card) ){
			return 	t;		
		}else throw new PaymentException();
	}
	
	public boolean checkTicket(String origin, String dest, String ticketcode)throws TicketException{
		for(Ticket t: this.tickets){
			if(ticketcode.equals(t.getTicketCode())){
				if( t.isExpired() ){
					return false;
				}
				for(Connection c: t.getConnections() ){
					if((FerryConnection)c.checkConnection(origin, dest)){
						if(t.devalueConnection(ticketcode, c)){
							return true;
						}else{
							return false;
						}
					}
				}
				throw new TicketException();
			}			
		}
		throw new TicketException();
	}
	
	private String generateTicketCode(){
		
	}
	
	public List<Connection> getConnections(String origin, String dest) throws NoConnectionException{
		List<Connection> results = new Vector<Connection>();
		if(!origin.isEmpty()){
			if(dest.isEmpty()){
				for(Connection c : this.connections){
					if(origin.equals(c.getOrigin())) results.add(c);
				}
			}else{
				for(Connection c : this.connections){
					if(origin.equals(c.getOrigin()) && dest.equals(c.getDestination())) results.add(c);
				}		
			}
		}
		if(results.isEmpty()) throw new NoConnectionException();
		else return results;
	}
	
	public static EnterpriseServer getInstance(){
		if(server == null ) server = new EnterpriseServer();
		return server;
	}
	
	public float getPrice(int distance, Discount discount){
		
	}
}
