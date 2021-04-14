import javax.swing.JFrame;

public class tut20_2 {
	public static void main(String[] args ){
            tut20_2_1 client;
            client = new tut20_2_1("127.0.0.1");
            client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.runningU();
	}
}
