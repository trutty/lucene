package org.apache.lucene;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;

import java.awt.Color;

import javax.swing.JToolBar;

import java.awt.Component;

import javax.swing.Box;

import java.awt.GridLayout;

import javax.swing.JSeparator;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class GUI extends JFrame {

	private static final String FIELD_ALL = "All";
	private SearchInterface search;
	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtTotalNo;
	private JComboBox<String> comboBoxFields;
	private JSplitPane splitPane;
	private JList<String> listResults;
	private JPanel panel;
	private JTextField txtPage;
	private JTextField txtResultsPerPage;
	private JButton btnNewButton;
	private JSeparator separator;
	private JPanel emptyPane;
	private JLabel lblTotalNo;
	private JLabel lblField;
	private JPanel panel_1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI(new SearchInterface() {

						@Override
						public SearchResult search(String query,
								int startResult, int numberOfResults,
								Fieldname fieldname) {
							return null;
						}

						@Override
						public SearchResult search(String query,
								int startResult, int numberOfResults) {
							return null;
						}
					});
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI(SearchInterface search) {
		this.search = search;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(550, 500);
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
		setTitle("Lucene Humen Gene Search");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// DocumentPanel d = new DocumentPanel(new GeneDocument("blub", "blub",
		// "blub", "blub", "blub", "blub", 1));
		// contentPane.add(d, new GridBagConstraints(0, 0, 1, 1, 0, 0,
		// GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
		// new Insets(0, 0, 0, 0), 0, 0));
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0 };
		contentPane.setLayout(gbl_contentPane);

		txtSearch = new JTextField();
		txtSearch.setToolTipText("Search query");
		GridBagConstraints gbc_txtSearch = new GridBagConstraints();
		gbc_txtSearch.gridwidth = 3;
		gbc_txtSearch.ipady = 7;
		gbc_txtSearch.weightx = 1.0;
		gbc_txtSearch.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearch.anchor = GridBagConstraints.NORTHWEST;
		gbc_txtSearch.gridx = 0;
		gbc_txtSearch.gridy = 0;
		contentPane.add(txtSearch, gbc_txtSearch);
		txtSearch.setColumns(10);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startSearch();
			}
		});
		btnSearch.setToolTipText("Execute search");
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearch.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSearch.gridx = 3;
		gbc_btnSearch.gridy = 0;
		contentPane.add(btnSearch, gbc_btnSearch);

		Vector<String> comboItems = new Vector<String>();
		comboItems.add(FIELD_ALL);
		for (Fieldname f : Fieldname.values()) {
			comboItems.add(f.name());
		}
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(5, 0, 8, 0);
		gbc_panel_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_1.gridwidth = 4;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
		
		lblTotalNo = new JLabel("Max. number of results");
		panel_1.add(lblTotalNo);
		
				txtTotalNo = new JTextField();
				txtTotalNo.setHorizontalAlignment(SwingConstants.RIGHT);
				panel_1.add(txtTotalNo);
				txtTotalNo.setText("20");
				txtTotalNo.setColumns(5);
		
		lblField = new JLabel("Field");
		panel_1.add(lblField);
		comboBoxFields = new JComboBox<String>(comboItems);
		panel_1.add(comboBoxFields);

		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_splitPane.gridwidth = 4;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 2;
		contentPane.add(splitPane, gbc_splitPane);

		panel = new JPanel();
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 1, 26, 26, 73, 1, 0 };
		gbl_panel.rowHeights = new int[] { 25, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		listResults = new JList<String>();
		GridBagConstraints gbc_listResults = new GridBagConstraints();
		gbc_listResults.fill = GridBagConstraints.BOTH;
		gbc_listResults.gridwidth = 3;
		gbc_listResults.anchor = GridBagConstraints.NORTHWEST;
		gbc_listResults.gridx = 0;
		gbc_listResults.gridy = 2;
		panel.add(listResults, gbc_listResults);

		txtPage = new JTextField();
		txtPage.setToolTipText("Page");
		GridBagConstraints gbc_txtPage = new GridBagConstraints();
		gbc_txtPage.ipady = 7;
		gbc_txtPage.anchor = GridBagConstraints.WEST;
		gbc_txtPage.insets = new Insets(0, 0, 0, 5);
		gbc_txtPage.gridx = 0;
		gbc_txtPage.gridy = 0;
		panel.add(txtPage, gbc_txtPage);
		txtPage.setText("1");
		txtPage.setColumns(2);

		txtResultsPerPage = new JTextField();
		txtResultsPerPage.setToolTipText("Results Per Page");
		GridBagConstraints gbc_txtResultsPerPage = new GridBagConstraints();
		gbc_txtResultsPerPage.ipady = 7;
		gbc_txtResultsPerPage.anchor = GridBagConstraints.WEST;
		gbc_txtResultsPerPage.insets = new Insets(0, 0, 0, 5);
		gbc_txtResultsPerPage.gridx = 1;
		gbc_txtResultsPerPage.gridy = 0;
		panel.add(txtResultsPerPage, gbc_txtResultsPerPage);
		txtResultsPerPage.setText("15");
		txtResultsPerPage.setColumns(2);

		btnNewButton = new JButton("Apply");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		panel.add(btnNewButton, gbc_btnNewButton);

		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.weightx = 1.0;
		gbc_separator.insets = new Insets(3, 0, 3, 0);
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 3;
		gbc_separator.anchor = GridBagConstraints.NORTHWEST;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		panel.add(separator, gbc_separator);

		emptyPane = new JPanel();
		splitPane.setRightComponent(emptyPane);
	}

	private void startSearch() {
		String query = txtSearch.getText();
		int numberOfResults = Integer.parseInt(txtTotalNo.getText());
		int fieldIndex = comboBoxFields.getSelectedIndex();

		SearchResult result;
		if (fieldIndex == 0) {
			// all
			result = search.search(query, 1, numberOfResults);
		} else {
			Fieldname field = Fieldname.values()[fieldIndex - 1];
			result = search.search(query, 1, numberOfResults, field);
		}

		updateSearchResults(result);
	}

	private void updateSearchResults(SearchResult result) {
		if (result == null) {
			JOptionPane.showMessageDialog(this, "Something went wrong",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		List<DocumentPanel> docPanels = new ArrayList<DocumentPanel>();
		List<String> titles = new Vector<String>();
		for (GeneDocument geneDoc : result.getDocuments()) {
			titles.add(geneDoc.getTitle());
			docPanels.add(new DocumentPanel(geneDoc));
		}
		// TODO sort list?
		int page = Integer.parseInt(txtPage.getText());
		int resultsPerPage = Integer.parseInt(txtResultsPerPage.getText());
		int startIndex = (page - 1) * resultsPerPage + 1;
		int endIndex = startIndex + resultsPerPage;
		titles = titles.subList(startIndex, endIndex);
		listResults.setListData(new Vector<String>(titles));
	}

	class DocumentPanel extends JPanel {

		private DocumentPanel(GeneDocument content) {
			super();
			this.setLayout(new GridBagLayout());

			JLabel lblSource = new JLabel("Source:");
			GridBagConstraints gbc_lblSource = new GridBagConstraints();
			gbc_lblSource.insets = new Insets(0, 0, 5, 5);
			gbc_lblSource.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblSource.gridx = 0;
			gbc_lblSource.gridy = 0;
			this.add(lblSource, gbc_lblSource);

			JLabel lblSourceContent = new JLabel(content.getSource());
			GridBagConstraints gbc_lblSourceContent = new GridBagConstraints();
			gbc_lblSourceContent.insets = new Insets(0, 0, 5, 0);
			gbc_lblSourceContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblSourceContent.weightx = 1.0;
			gbc_lblSourceContent.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblSourceContent.gridx = 1;
			gbc_lblSourceContent.gridy = 0;
			this.add(lblSourceContent, gbc_lblSourceContent);

			JLabel lblTitle = new JLabel("Title:");
			GridBagConstraints gbc_lblTitle = new GridBagConstraints();
			gbc_lblTitle.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
			gbc_lblTitle.gridx = 0;
			gbc_lblTitle.gridy = 1;
			this.add(lblTitle, gbc_lblTitle);

			JLabel lblTitleContent = new JLabel(content.getTitle());
			GridBagConstraints gbc_lblTitleContent = new GridBagConstraints();
			gbc_lblTitleContent.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblTitleContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblTitleContent.insets = new Insets(0, 0, 5, 0);
			gbc_lblTitleContent.gridx = 1;
			gbc_lblTitleContent.gridy = 1;
			this.add(lblTitleContent, gbc_lblTitleContent);

			JLabel lblAuthor = new JLabel("Author:");
			GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
			gbc_lblAuthor.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_lblAuthor.gridx = 0;
			gbc_lblAuthor.gridy = 2;
			this.add(lblAuthor, gbc_lblAuthor);

			JLabel lblAuthorContent = new JLabel(content.getAuthor());
			GridBagConstraints gbc_lblAuthorContent = new GridBagConstraints();
			gbc_lblAuthorContent.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblAuthorContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblAuthorContent.insets = new Insets(0, 0, 5, 0);
			gbc_lblAuthorContent.gridx = 1;
			gbc_lblAuthorContent.gridy = 2;
			this.add(lblAuthorContent, gbc_lblAuthorContent);

			JLabel lblOrigin1 = new JLabel("Origin 1:");
			GridBagConstraints gbc_lblOrigin1 = new GridBagConstraints();
			gbc_lblOrigin1.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblOrigin1.insets = new Insets(0, 0, 5, 5);
			gbc_lblOrigin1.gridx = 0;
			gbc_lblOrigin1.gridy = 3;
			this.add(lblOrigin1, gbc_lblOrigin1);

			JLabel lblOrigin1Content = new JLabel(content.getOrigin1());
			GridBagConstraints gbc_lblOrigin1Content = new GridBagConstraints();
			gbc_lblOrigin1Content.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblOrigin1Content.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblOrigin1Content.insets = new Insets(0, 0, 5, 0);
			gbc_lblOrigin1Content.gridx = 1;
			gbc_lblOrigin1Content.gridy = 3;
			this.add(lblOrigin1Content, gbc_lblOrigin1Content);

			JLabel lblOrigin2 = new JLabel("Origin 2:");
			GridBagConstraints gbc_lblOrigin2 = new GridBagConstraints();
			gbc_lblOrigin2.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblOrigin2.insets = new Insets(0, 0, 5, 5);
			gbc_lblOrigin2.gridx = 0;
			gbc_lblOrigin2.gridy = 4;
			this.add(lblOrigin2, gbc_lblOrigin2);

			JLabel lblOrigin2Content = new JLabel(content.getOrigin2());
			GridBagConstraints gbc_lblOrigin2Content = new GridBagConstraints();
			gbc_lblOrigin2Content.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblOrigin2Content.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblOrigin2Content.insets = new Insets(0, 0, 5, 0);
			gbc_lblOrigin2Content.gridx = 1;
			gbc_lblOrigin2Content.gridy = 4;
			this.add(lblOrigin2Content, gbc_lblOrigin2Content);

			JLabel lblContent = new JLabel("Content:");
			GridBagConstraints gbc_lblContent = new GridBagConstraints();
			gbc_lblContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblContent.insets = new Insets(0, 0, 5, 5);
			gbc_lblContent.gridx = 0;
			gbc_lblContent.gridy = 5;
			this.add(lblContent, gbc_lblContent);

			JLabel lblContentContent = new JLabel(content.getContent());
			GridBagConstraints gbc_lblContentContent = new GridBagConstraints();
			gbc_lblContentContent.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblContentContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblContentContent.insets = new Insets(0, 0, 5, 0);
			gbc_lblContentContent.gridx = 1;
			gbc_lblContentContent.gridy = 5;
			this.add(lblContentContent, gbc_lblContentContent);

			JLabel lblPmid = new JLabel("PMID:");
			GridBagConstraints gbc_lblPmid = new GridBagConstraints();
			gbc_lblPmid.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblPmid.insets = new Insets(0, 0, 5, 5);
			gbc_lblPmid.gridx = 0;
			gbc_lblPmid.gridy = 6;
			this.add(lblPmid, gbc_lblPmid);

			JLabel lblPmidContent = new JLabel(content.getPMID() + "");
			GridBagConstraints gbc_lblPmidContent = new GridBagConstraints();
			gbc_lblPmidContent.fill = GridBagConstraints.HORIZONTAL;
			gbc_lblPmidContent.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblPmidContent.insets = new Insets(0, 0, 5, 0);
			gbc_lblPmidContent.gridx = 1;
			gbc_lblPmidContent.gridy = 6;
			this.add(lblPmidContent, gbc_lblPmidContent);
		}
	}

}
