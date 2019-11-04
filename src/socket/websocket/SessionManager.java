package socket.websocket;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

@ApplicationScoped
public class SessionManager {
	private final Set<Session> sessions = new HashSet<>();
	
	public void addSession(Session session) {
        sessions.add(session);
    }
	
	public void removeSession(Session session) {
        sessions.remove(session);
    }
	
	public void initSession(Session session, JsonObject message) throws IOException {
		boolean found = false;

		for (Session ses : sessions) {
            if(ses.getId() == session.getId()) {
            	ses.getUserProperties().put("token", message.getString("token"));
            	ses.getUserProperties().put("type", message.getString("type"));
            	found = true;

            	break;
            }
        }
		
		sendToSession(session, message);
	}
	
	public void sendMessageToType(Session sender, JsonObject message) {
		if(sender.getUserProperties().get("token").equals(null) || sender.getUserProperties().get("token").equals("")){
			return;
		}
    	for (Session session : sessions) {
    		if(session.getId() != sender.getId()) {
	    		if(session.getUserProperties().get("token").equals(sender.getUserProperties().get("token"))) {
	    			if(session.getUserProperties().get("type").equals(message.getString("type"))) {
	    				sendToSession(session, message);
	    			}
	    		}
    		}
        }
    }
	
	public void sendMessageToAll(Session sender, JsonObject message) throws IOException {
		for(Session session : sessions) {
			session.getBasicRemote().sendText(message.toString());
		}
	}
	
	private void sendToSession(Session session, JsonObject message) {
    	try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            System.out.println(ex.getMessage());
        }
    }
}
