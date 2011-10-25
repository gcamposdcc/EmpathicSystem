package cl.automind.empathy.test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import cl.automind.connectivity.IHttpClient;
import cl.automind.desk.connectivity.DeskHttpClient;
import cl.automind.empathy.query.EmpathicObject;
import cl.automind.empathy.test.t00.Score;
import cl.automind.empathy.test.t00.TestDataManager;

public class DeskMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeskMain window = new DeskMain();
					window.frame.setVisible(true);

//					EmpathicObject object00 = new EmpathicObject();
//					object00.set(EmpathicObject.Id, 1);
//
//					EmpathicObject object01 = new EmpathicObject();
//					object01.set(EmpathicObject.Id, object00.get(EmpathicObject.Id));
//
//					int K = 1000;
//					int LOOPS = 100*K*K;
//					int cache00 = 0;
//					int cache01 = 0;
//					long time = System.currentTimeMillis();
//					long result = 0;
//					for (int i = 0; i < LOOPS; i++) {
//						cache00 = i%2 == 0? object00.getId() : object01.getId();
//						cache01 = i%3 == 0? object01.getId() : object00.getId();
//					}
//					result = System.currentTimeMillis() - time;
//					System.out.println("Loops = " + LOOPS + ";; Time = "+result+";;");
//					time = System.currentTimeMillis();
//					for (int i = 0; i < LOOPS; i++) {
//						cache00 = i%2 == 0? object00.get(EmpathicObject.Id) : object01.get(EmpathicObject.Id);
//						cache01 = i%3 == 0? object01.get(EmpathicObject.Id) : object00.get(EmpathicObject.Id);
//					}
//					result = System.currentTimeMillis() - time;
//					System.out.println("Loops = " + LOOPS + ";; Time = "+result+";;");
//
//					System.out.println(object00.compareTo(object01));

					IHttpClient http = new DeskHttpClient();
//					http.setUrl("http://web.simt.cl/simtweb/buscarAction.do");
//					http.addRequestParameter("ingresar_paradero", "pe375");
//					http.addRequestParameter("d", "busquedaParadero");
					http.setUrl("http://localhost:3000/empathy");
					http.addRequestParameter("value", "2");
					System.out.println(http.getRequestString());
					http.sendRequest();
					System.out.println(http.getResponse());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeskMain() {
		initialize();
	}
	private static int turn = 0;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "var00", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppContext.getInstance().getEmpathy().triggerEmpathy();
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
					.addComponent(btnUpdate)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnUpdate)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
					.addContainerGap())
		);

		JButton btnIncrease = new JButton("increase");
		btnIncrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppContext.getInstance().getEmpathy().pushValue(TestDataManager.DS_SCORE, new Score(turn++, 1));
			}
		});

		JButton btnDecrease = new JButton("decrease");
		btnDecrease.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppContext.getInstance().getEmpathy().pushValue(TestDataManager.DS_SCORE, new Score(turn++, 0));
			}
		});

		JButton btnReset = new JButton("reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("NotImplemented");
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnIncrease, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addComponent(btnDecrease, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addComponent(btnReset, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(btnIncrease)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDecrease)
					.addPreferredGap(ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
					.addComponent(btnReset)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
	}
}
