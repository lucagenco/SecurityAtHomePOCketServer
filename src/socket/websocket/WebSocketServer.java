package socket.websocket;
import java.io.IOException;
import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {
	@Inject
    private SessionManager sessionManager;
	
	@OnOpen
    public void open(Session session) {
    	sessionManager.addSession(session);
    }
	
	@OnClose
    public void close(Session session) {
    	sessionManager.removeSession(session);
    }
	
	@OnError
    public void onError(Throwable error) {
    	System.out.println(error.getMessage());
    }
	
	@OnMessage
    public void handleMessage(Session session, String message) throws IOException {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();
			
			if ("initSession".equals(jsonMessage.getString("action"))) {
				sessionManager.initSession(session, jsonMessage);
			}
			
			else if ("sendMessageToType".equals(jsonMessage.getString("action"))) {
				sessionManager.sendMessageToType(session, jsonMessage);
			}
			
			else if ("broadcast".equals(jsonMessage.getString("action"))) {
				sessionManager.sendMessageToAll(session, jsonMessage);
			}
		}
	}
}
