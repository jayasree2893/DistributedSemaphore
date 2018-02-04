package onlinelibrary;

import java.net.InetAddress;
import java.net.UnknownHostException;

import dissem.Coordinator;
import dissem.DisSemHelper;

public class Setup {
	public static void main(String [] args) throws UnknownHostException
	{
		if (args.length != 4) {
			System.out
				.println("usage: java Setup helperCount coordinatorPort helperPortMin helperIp");
		
			System.exit(1);
		}
		final int helperCount=Integer.parseInt(args[0]);
		final int coorPort=Integer.parseInt(args[1]);
		final int minHelperPort=Integer.parseInt(args[2]);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Coordinator coordinator = new Coordinator(coorPort, helperCount);
			}
		}, "coordinator").start();
		String coip = InetAddress.getByName(args[3]).getHostAddress();
		System.out.println(coip);
		for (int i = 0; i < helperCount; i++) {
			DisSemHelper helper = new DisSemHelper(coip, coorPort, i,
					minHelperPort + i);
			new Thread(helper, "helper" + i).start();
		}
		System.out.printf("Initial Library setup complete,upto %d can login to system",helperCount);
	}
}
