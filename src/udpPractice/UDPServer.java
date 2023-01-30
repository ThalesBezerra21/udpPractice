package udpPractice;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.io.IOException;

public class UDPServer{
	/**
	 * @param args
	 */
	public static void main(String[] args)  throws IOException{
		
		DatagramSocket aSocket = new DatagramSocket(6789);
		
		try {
			byte[] buffer = new byte[1024];

			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);//recebendo o que é transmitido por outros processos

				new Thread(() -> {
					System.out.println("Address: " + request.getAddress());//endereço do processo transmissor
					System.out.println("Port: " + request.getPort());//endereço da porta do processo transmissor

					String input = new String(request.getData(), 0, request.getLength());
					System.out.println("Data: " + input);

					String[] inputs = new String(input).split(" ");
					int num1 = Integer.parseInt(inputs[1]);
					int num2 = Integer.parseInt(inputs[2]);
					int result = 0;

					switch (inputs[0]) {
					case "+":
						result = num1 + num2;
						break;
					case "*":
						result = num1 * num2;
						break;
					case "/":
						result = num1 / num2;
						break;
					default:
						result = 0;
					}

					byte[] output = Integer.toString(result).getBytes();
					DatagramPacket reply = new DatagramPacket(output, output.length, request.getAddress(), request.getPort());//construindo-se a mensagem de resposta
					try {
						aSocket.send(reply);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//enviando-se para o processo cujo endereço e porta estão indicados no objeto request
				}).start();

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{

			if(aSocket!=null){
				aSocket.close();
			}
		} //fechando o try
	}//fechando a main
}//fechando a classe
