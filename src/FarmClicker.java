
package src;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
	public class FarmClicker {
		private JFrame frame;
		private JPanel makingPanel;
		private JPanel infoPanel;
		private JLabel moneyLabel;
		private JMenuBar menuBar;
		private JMenu fileMenu;
		private JMenuItem save;
		private JMenuItem load; //PROGRESS BAR LABELS
		private static FarmClicker game;
		private ClickerClass wheat;
		private ClickerClass rice;
		private ClickerClass barley;
		private ClickerClass cocoa;
		private ClickerClass bamboo;
		private ClickerClass ginseng;
		private ClickerClass mushroom;
		private ClickerClass thyme;
		private ClickerClass ginger;
		private ClickerClass tree;
		private ArrayList<ClickerClass> makers;
		private boolean newThread = false;
		private JLabel prestigeLabel;
		private JButton prestigeButton;
		private int numPrestiges = 1;
		private long money = 5;
		public static void main(String[] args) {
			game = new FarmClicker();
			game.launch();
		}
		public void launch() {
			frame = new JFrame();
			makingPanel = new JPanel();
			makingPanel.setLayout(new BoxLayout(makingPanel, BoxLayout.Y_AXIS));
			infoPanel = new JPanel();
			moneyLabel = new JLabel("Money: " + money);
			moneyLabel.setFont(new Font("Courier New", Font.BOLD, 18));
			prestigeButton = new JButton("Upgrade! ($300000)");
			prestigeButton.addActionListener(new PrestigeListener());
			prestigeLabel = new JLabel("Number of upgrades: " + (numPrestiges - 1));

			menuBar = new JMenuBar();
			fileMenu = new JMenu("File");
			save = new JMenuItem("Save");
			save.addActionListener(new SaveListener());
			load = new JMenuItem("Load");
			load.addActionListener(new LoadListener());
			fileMenu.add(save);
			fileMenu.add(load);
			menuBar.add(fileMenu);

			wheat = new ClickerClass(1,3,"Wheat", 1);//2
			rice = new ClickerClass(10, 25, "Rice", 3);//5
			barley = new ClickerClass(50, 100, "Barley", 5);//10
			cocoa = new ClickerClass(80, 200, "Cocoa", 10);//12
			bamboo = new ClickerClass(175, 400, "Bamboo", 15);//15
			ginseng = new ClickerClass(300, 700, "Ginseng", 20);//20
			mushroom = new ClickerClass(500, 1100, "Mushroom", 24); //25
			ginger = new ClickerClass(760, 1600, "Ginger", 30); //28
			thyme = new ClickerClass(1080, 2200, "Thyme", 35); //32
			tree = new ClickerClass(1560, 3000, "Tree", 40); //36

			makers = new ArrayList<ClickerClass>();
			makers.add(wheat);
			makers.add(rice);
			makers.add(barley);
			makers.add(cocoa);
			makers.add(bamboo);
			makers.add(ginseng);
			makers.add(mushroom);
			makers.add(ginger);
			makers.add(thyme);
			makers.add(tree);

			makingPanel.add(wheat.panel);
			makingPanel.add(rice.panel);
			makingPanel.add(barley.panel);
			makingPanel.add(cocoa.panel);
			makingPanel.add(bamboo.panel);
			makingPanel.add(ginseng.panel);
			makingPanel.add(mushroom.panel);
			makingPanel.add(ginger.panel);
			makingPanel.add(thyme.panel);
			makingPanel.add(tree.panel);

			infoPanel.add(moneyLabel);
			infoPanel.add(prestigeButton);
			infoPanel.add(prestigeLabel);

			frame.setLayout(new BorderLayout());
			frame.setJMenuBar(menuBar);
			frame.getContentPane().add(infoPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(makingPanel, BorderLayout.EAST);
			frame.pack();
			frame.setTitle("Farm!");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		private void refreshMoney() {
			moneyLabel.setText("Money: " + money);
		}
		private class PrestigeListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (money >= 300000 * Math.pow(1.5, numPrestiges - 1)) {
					money -= 300000 * Math.pow(1.5, numPrestiges - 1);
					game.refreshMoney();
					numPrestiges++;
					prestigeLabel.setText("Number of prestiges: " + (numPrestiges-1));
					prestigeButton.setText("Upgrade! ($" + 300000 * Math.pow(1.5, numPrestiges - 1) + ")");

					System.out.println(numPrestiges);
					for (ClickerClass c : makers) {
						if (c.workerNum > 0){
							c.bar.setString("$" + c.profit * c.workerNum * numPrestiges);
							c.aw = c.getAutoWorker();
							c.aw.execute();
						} else {
							c.bar.setString("$" + c.profit * numPrestiges);
						}
					}
				}
				frame.revalidate();
			}

		}
		private class LoadListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser ch = new JFileChooser();
				ch.showOpenDialog(frame);
				try {
					BufferedReader r = new BufferedReader(new FileReader(ch.getSelectedFile()));
					String[] values = r.readLine().split(" ");
					money = Long.parseLong(values[0]);
					wheat.workerNum = Integer.parseInt(values[1]);
					rice.workerNum = Integer.parseInt(values[2]);
					barley.workerNum = Integer.parseInt(values[3]);
					cocoa.workerNum = Integer.parseInt(values[4]);
					bamboo.workerNum = Integer.parseInt(values[5]);
					ginseng.workerNum = Integer.parseInt(values[6]);
					mushroom.workerNum = Integer.parseInt(values[7]);
					ginger.workerNum = Integer.parseInt(values[8]);
					thyme.workerNum = Integer.parseInt(values[9]);
					tree.workerNum = Integer.parseInt(values[10]);
					numPrestiges = Integer.parseInt(values[11]);
					prestigeLabel.setText("Number of upgrades: " + (numPrestiges - 1));
					prestigeButton.setText("Upgrade! ($" + 300000 * Math.pow(1.5, numPrestiges - 1) + ")");
					game.refreshMoney();
					for (ClickerClass c : makers) {
						c.workerLabel.setText("Number of farmers: " + c.workerNum);
						c.workerButton.setText("Add Farmer($" + Math.ceil(c.profit * 35 * Math.pow(1.2, c.workerNum)) + ")");
						c.bar.setString("$" + c.profit * numPrestiges);
						if (c.workerNum > 0) {
							c.bar.setString("$" + c.profit * c.workerNum * numPrestiges);
							c.aw = c.getAutoWorker();
							c.aw.execute();
						}
					}

					r.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		private class SaveListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				long[] values = new long[12]; //money, workervalues in order, prestiges;
				values[0] = money;
				values[1] = wheat.workerNum;
				values[2] = rice.workerNum;
				values[3] = barley.workerNum;
				values[4] = cocoa.workerNum;
				values[5] = bamboo.workerNum;
				values[6] = ginseng.workerNum;
				values[7] = mushroom.workerNum;
				values[8] = ginger.workerNum;
				values[9] = thyme.workerNum;
				values[10] = tree.workerNum;
				values[11] = numPrestiges;
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(frame);
				try {
					PrintWriter p = new PrintWriter(new FileWriter(fc.getSelectedFile()));
					for (long l : values) {
						p.write(String.valueOf(l));
						p.write(" ");
					}
					p.println();
					p.close();
					JOptionPane.showMessageDialog(frame, "Successfully saved");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Save failed");
				}
			}

		}

		private class ClickerClass {
			JPanel panel;
			AutoWorker aw;
			JButton button;
			JButton workerButton;
			JLabel label;
			JProgressBar bar;
			int value = 1;
			int cost;
			int profit;
			String name;
			int time;
			int workerNum;
			JLabel workerLabel;
			public ClickerClass(int c, int p, String n, int t) {
				this.cost = c;
				this.time = t;
				this.profit = p;
				this.name = n;
				this.workerNum = 0;
				this.workerLabel = new JLabel("Number of farmers: " + workerNum);
				this.panel = new JPanel();
				this.button = new JButton("Plant");
				this.workerButton = new JButton("Add Farmer($" + profit * 35 + ")");
				this.label = new JLabel("<html>"+name+"<br>"+"Cost: "+"$"+cost+"</html>");
				this.bar = new JProgressBar(0,10);
				this.bar.setStringPainted(true);
				this.bar.setString("$" + profit);
				button.addActionListener(new Listener());
				workerButton.addActionListener(new WorkerListener());
				panel.add(label);
				panel.add(button);
				panel.add(bar);
				panel.add(workerButton);
				panel.add(workerLabel);

			}
			private class Worker extends SwingWorker<Object, Object> {

				@Override
				protected Object doInBackground() throws Exception {

					for (int i = 0; i < 10; i++) {


						bar.setValue(value);
						Thread.sleep(time * 100);
						value += 1;

					}
					return null;
				}
				@Override
				protected void done() {
					Toolkit.getDefaultToolkit().beep();
					value = 1;
					bar.setValue(0);
					button.setEnabled(true);
					money += profit * numPrestiges;
					game.refreshMoney();
				}
			}
			private class Listener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (money > (cost - 1)) {
						money -= cost;
						game.refreshMoney();
						button.setEnabled(false);
						Worker w = new Worker();
						w.execute();
					}
				}

			}
			private class WorkerListener implements ActionListener {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (money >= Math.ceil(profit * 35 * Math.pow(1.2, workerNum))) {
						money -= Math.ceil(profit * 35 * Math.pow(1.2, workerNum));
						workerNum++;
						workerLabel.setText("Number of farmers: " + workerNum);
						workerButton.setText("Add Farmer($" + Math.ceil(profit * 35 * Math.pow(1.2, workerNum)) + ")");
						if (aw == null) {
							aw = new AutoWorker();
							aw.execute();
						}
						bar.setString("$" + profit * workerNum * numPrestiges);
					}
					frame.revalidate();
					frame.pack();
				}

			}
			private AutoWorker getAutoWorker() {
				return new AutoWorker();
			}
			private class AutoWorker extends SwingWorker<Object, Object> {

				@Override
				protected Object doInBackground() throws Exception {
					button.setEnabled(false);
					while (!newThread) {
						for (int i = 0; i < 10; i++) {


							bar.setValue(value);
							Thread.sleep(time * 100);
							value += 1;

						}
						Toolkit.getDefaultToolkit().beep();
						value = 1;
						bar.setValue(0);
						money += profit * workerNum * numPrestiges;
						game.refreshMoney();
						if (button.isEnabled())
							button.setEnabled(false);
					}
					return null;
				}

			}

		}
	}

