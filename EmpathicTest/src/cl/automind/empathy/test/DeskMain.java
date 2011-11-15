package cl.automind.empathy.test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import cl.automind.empathy.fw.data.sql.AbstractSqlConnector;
import cl.automind.empathy.fw.data.sql.AbstractSqlDataSource;
import cl.automind.empathy.fwdesk.data.sql.HsqldbDataSource;
import cl.automind.empathy.test.t00.Score;
import cl.automind.empathy.test.t00.TestDataManager;
import cl.automind.empathy.testquery.TestSqlConnectionInfo;

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
					TestSqlConnectionInfo tsci = new TestSqlConnectionInfo();
					AbstractSqlConnector asc = new AbstractSqlConnector(tsci) {

						@Override
						public Statement getStatement(String statement) {
							// TODO Auto-generated method stub
							return null;
						}

					};
					System.out.println(asc.getDriverString());
					System.out.println(asc.getUrlString());
					System.out.println(asc.getFullConnectionString());

					AbstractSqlDataSource<Score> ds = new HsqldbDataSource<Score>(new Score(0,0));


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
