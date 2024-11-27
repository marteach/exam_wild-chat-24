package communication;

import controllers.MainWindowController;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the UDP protocol which may lead to
 * messages being lost.
 *
 * @author Thomas Ejnefjäll
 * @author Martin Goblirsch
 */
public class UDPChatCommunicator implements Runnable {

    private final int PORT = 6789;
    private static final int BUFFER_SIZE = 4096;
    private static final String RECEIVER_ADDRESS = "::1";
    private DatagramSocket _socket = null;
    private boolean _listening = true;

    private MainWindowController _chat = null;

    /**
     * Create a chat communicator that communicates over UDP.
     *
     * @param chat The UI that want to receive incoming messages.
     */
    public UDPChatCommunicator(MainWindowController chat) {
        _chat = chat;
        /*
         * force java to use IPv4 so we do not get a problem when using IPv4 multicast
         * address
         */
        //Changed to DatagramSocket since IPv6 now is common enogh to be used. 
        //IPv4 is also hard to force in mac in java 21+ it seems.
        //System.setProperty("java.net.preferIPv4Stack", "true");
    }

    /**
     * Send the chat message to all clients.
     *
     * @param sender  Name of the sender.
     * @param message Text message to send.
     * @throws IOException If there is an IO error.
     */
    public void sendChat(String sender, String message) throws Exception {

        String msg = sender + ": " + message;

        try {
            DatagramSocket socket = new DatagramSocket();

            byte[] buffer = msg.getBytes();
            InetAddress receiver_address = InetAddress.getByName(RECEIVER_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiver_address, PORT);

            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            _chat.error(e);
        }
    }

    /**
     * Start to listen for messages from other clients.
     */
    public void startListen() {
        new Thread(this).start();
    }

    /**
     * Listen for messages from other clients.
     *
     * @throws Exception If there is an IO error.
     */
    private void listenForMessages() throws Exception {
        try {
            _socket = new DatagramSocket(PORT);

            byte[] buffer = new byte[BUFFER_SIZE];

            while (_listening) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                _socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());

                //Send the message to the chat controller
                _chat.receiveMessage(msg);
            }

        } catch (IOException e) {
            _chat.error(e);
        }
    }


    /**
     * Stop listen, we leave the group..
     * 
     * @throws Exception
     */
    public void stopListen() throws Exception {
        _listening = false;

        try {
            _socket.close();
        } catch (Exception e) {
            _chat.error(e);
        }
    }

    @Override
    public void run() {
        try {
            this.listenForMessages();
        } catch (Exception e) {
            _chat.error(e);
        }
    }
}
