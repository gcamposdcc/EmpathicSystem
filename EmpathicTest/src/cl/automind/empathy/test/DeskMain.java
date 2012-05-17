package cl.automind.empathy.test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import cl.automind.empathy.data.IDataSource;
import cl.automind.empathy.data.IQueryOption;
import cl.automind.empathy.data.QueryCriterion;
import cl.automind.empathy.data.QueryOption;
import cl.automind.empathy.data.QueryOptions;
import cl.automind.empathy.data.sql.ComparisonType;
import cl.automind.empathy.data.sql.ISqlConnectionInfo;
import cl.automind.empathy.data.sql.ISqlConnector;
import cl.automind.empathy.data.sql.ISqlDataSource;
import cl.automind.empathy.data.sql.SqlNamedValuePair;
import cl.automind.empathy.data.sql.SqlPairs;
import cl.automind.empathy.fw.data.sql.DefaultSqlConnector;
import cl.automind.empathy.fw.data.sql.PropertySqlConnectionInfo;
import cl.automind.empathy.test.t00.Choice;
import cl.automind.empathy.test.t00.Score;
import cl.automind.empathy.test.t00.TestDataManager;
import cl.automind.empathy.testquery.EmpathyMetadata;
import cl.automind.empathy.testquery.EmpathySqlDataSource;
import cl.automind.empathy.testquery.SagdeMetadata;
import cl.automind.empathy.testquery.SagdeSqlDataSource;

public class DeskMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					DeskMain window = new DeskMain();
					window.frame.setVisible(true);
//					int threads = 10;
//					int cycles = 100000;
//					for (int thread_id = 0; thread_id < threads; thread_id++){
//						TestTask tt = new TestTask();
//						tt.execute(thread_id, cycles);
//					}
					Properties psql_props = new Properties();
					// equivalente a psql_props.setProperty("user", "carlos");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Username.toString(), "carlos");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Password.toString(), "123456");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Database.toString(), "sagde");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Hostname.toString(), "localhost");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Port.toString(), "5432");
					psql_props.setProperty(ISqlConnectionInfo.Fields.Driver.toString(), "postgresql");
					psql_props.setProperty(ISqlConnectionInfo.Fields.DriverClassname.toString(), "org.postgresql.Driver");
					// En v�as de quedar obsoleto
					PropertySqlConnectionInfo psql = new PropertySqlConnectionInfo(psql_props);

					final ISqlConnector psql_connector = new DefaultSqlConnector(psql);
					IDataSource<SagdeMetadata> meta = new SagdeSqlDataSource(new SagdeMetadata(), psql_connector);
					List<SagdeMetadata> sagdeMetadatas = ((ISqlDataSource<SagdeMetadata>) meta).executeNamedQuery("byIdEmpathy", SqlPairs.pair("idempathy",6));
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Begin");
					for (SagdeMetadata sagdeMetadata: sagdeMetadatas){
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:Begin");
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+sagdeMetadata);
						Collection<SqlNamedValuePair<?>> pairs = ((ISqlDataSource<SagdeMetadata>) meta).toPairs(sagdeMetadata);
						for (SqlNamedValuePair<?> pair: pairs){
							Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Pair::" + pair);
						}
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:End");
					}
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:End");
//					for (SagdeMetadata sagdeMetadata: sagdeMetadatas){
//						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Inserting::" + meta.insert(sagdeMetadata));
//					}

//					List<SagdeMetadata> metadatas = meta.select(QueryOption.All);
//					for (SagdeMetadata sagdeMetadata: metadatas){
//						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:Begin");
//						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(sagdeMetadata);
//						Collection<SqlNamedValuePair<?>> pairs = ((ISqlDataSource<SagdeMetadata>) meta).toPairs(sagdeMetadata);
//						for (SqlNamedValuePair<?> pair: pairs){
//							Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Pair::" + pair);
//						}
//						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:End");
//					}

					IDataSource<EmpathyMetadata> empathySource = new EmpathySqlDataSource(new EmpathyMetadata(), psql_connector);

					List<EmpathyMetadata> empathyMetadatas = empathySource.select(QueryOptions.ALL);

					for (EmpathyMetadata empathyMetadata: empathyMetadatas){
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:Begin");
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(""+empathyMetadata);
						Collection<SqlNamedValuePair<?>> pairs
							= ((ISqlDataSource<EmpathyMetadata>) empathySource).toPairs(empathyMetadata);
						for (SqlNamedValuePair<?> pair: pairs){
							Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Pair::" + pair);
						}
						Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("ShowList:Pair:End");
					}
					Date now = new Date(Calendar.getInstance().getTimeInMillis());
					EmpathyMetadata empathy = new EmpathyMetadata();
					empathy.setCreatedAt(now);
					Collection<EmpathyMetadata> inserts = new ArrayList<EmpathyMetadata>();
					for (int i = 0; i < 10; i++){
						EmpathyMetadata dummy = new EmpathyMetadata();
						dummy.setCreatedAt(now);
						dummy.setEvaluated(i%2 == 1);
						inserts.add(dummy);
					}
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Inserting:Empathy:" + empathySource.insert(inserts));
					Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("Inserting:Empathy:" + empathySource.insert(empathy));
					empathySource.delete(new QueryOption(IQueryOption.Type.Filter),
							new QueryCriterion<EmpathyMetadata>(null ,SqlPairs.pair("id", 33, ComparisonType.GreaterOrEqual)));
					EmpathyMetadata emp = new EmpathyMetadata();
					emp.setEvaluated(true);
					emp.setAnswered(true);
					emp.setLiked(true);
					emp.setCreatedAt(now);
					empathySource.update(emp, QueryOptions.ALL);
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
				AppContext.getInstance().getEmpathy().pushValue(TestDataManager.DS_SCORE, new Choice(1, 1, 'A'));
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
				Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info("NotImplemented");
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
