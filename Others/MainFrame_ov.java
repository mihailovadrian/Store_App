package frames;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import application.*;
import frames.models.*;
import frames.objects.*;
import generic.table.*;
import objects.*;
import threads.*;
import tools.*;

public class MainFrame extends JFrame implements ActionListener, WindowListener
{
	// ---------------------------------------------------------------------------------------------
	// static members
	// ---------------------------------------------------------------------------------------------
	private static final long serialVersionUID = 783625522709532017L;

	// ---------------------------------------------------------------------------------------------
	// action commands
	// ---------------------------------------------------------------------------------------------
	private final static String CMD_SELECT_CONNECTION = "select_connection";
	private final static String CMD_VIEW_RECORDS_ALL = "view_records_all";
	private final static String CMD_VIEW_RECORDS_SELECT = "view_records_select";
	private final static String CMD_VIEW_RECORDS_LAST_10 = "view_records_last_10";
	private final static String CMD_VIEW_RECORDS_LAST_100 = "view_records_last_100";
	private final static String CMD_VIEW_RECORDS_LAST_1000 = "view_records_last_1000";
	private final static String CMD_VIEW_RECORDS_LAST_CUSTOM = "view_records_last_custom";
	private final static String CMD_SEARCH_DB_COLUMN = "search_db_column";
	private final static String CMD_SEARCH_BO_FIELD = "search_bo_field";
	private final static String CMD_VIEW_ENTITY = "view_entity";
	private final static String CMD_VIEW_INDEXES = "view_indexes";
	private final static String CMD_VIEW_INDEXES_ALL = "view_indexes_all";
	private final static String CMD_SQL_TO_CLEANUP_ENTITY = "sql_to_cleanup_entity";
	private final static String CMD_CLOSE_TAB = "close_tab";
	private final static String CMD_CLOSE = "close_application";
	private final static String CMD_ABOUT = "about";
	private final static String CMD_HELP = "help";

	// ---------------------------------------------------------------------------------------------
	// flags
	// ---------------------------------------------------------------------------------------------
	private boolean firstActivation = true;
	private boolean autoClose = false;

	// ---------------------------------------------------------------------------------------------
	// components
	// ---------------------------------------------------------------------------------------------
	private TableManager<Entity> tmEntities;
	private TableManager<EntityField> tmEntity;

	private JLabel lblStatusBar;

	private JTabbedPane tbpAll;

	private JPopupMenu pmEntities;
	private JPopupMenu pmTabbedPane;

	private Component cIndexes;
	private TableManager<IndexField> tmIndexes;

	private LookupsPanel pnlLookups;

	// ---------------------------------------------------------------------------------------------
	// objects
	// ---------------------------------------------------------------------------------------------
	private AppContext context;

	// ---------------------------------------------------------------------------------------------
	// methods
	// ---------------------------------------------------------------------------------------------
	public MainFrame()
	{
		super();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		initialize();
	}

	private void initialize( )
	{
		context = new AppContext();

		context.connection = null;

		context.database = null;
		context.server = null;

		context.lookups = new LookupSet();
		context.entities = new EntitySet();
		context.indexes = new IndexSet();

		context.frame = this;

		// ----------------------------------------------------------------------------------------
		// create the menu bar
		createMenuBar();

		// ----------------------------------------------------------------------------------------
		tbpAll = new JTabbedPane();

		tbpAll.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged( ChangeEvent e )
			{
				boolean isset = false;

				if ((!isset) && (tbpAll.getSelectedIndex() == 0))
				{
					populateStatusBar(tmEntities);
					isset = true;
				}

				if ((!isset) && (tbpAll.getSelectedIndex() == 1))
				{
					populateStatusBar(pnlLookups.getTmLookups());
					isset = true;
				}

				if ((!isset) && (tbpAll.getSelectedComponent() != null))
				{
					if (tbpAll.getSelectedComponent() instanceof JScrollPane)
					{
						JScrollPane sp = (JScrollPane) tbpAll.getSelectedComponent();
						if ((sp.getComponentCount() >= 1) && (sp.getComponent(0) instanceof JViewport))
						{
							JViewport vp = (JViewport) sp.getComponent(0);
							if ((vp != null) && (vp.getComponentCount() == 1) && (vp.getComponent(0) instanceof JTable))
							{
								populateStatusBar(vp.getComponent(0));
								isset = true;
							}
						}
					}
				}

				if (!isset)
				{
					lblStatusBar.setText("");
				}
			}
		});

		tmEntities = new TableManager<>(new EntitySetModel(context.entities), this);
		tmEntities.setPopupMenu(null, true);
		tmEntity = new TableManager<>(new EntityModel(), this);
		tmEntity.setPopupMenu(null, true);
		tmEntities.addChild(tmEntity);

		// ----------------------------------------------------------------------------------------
		lblStatusBar = new JLabel();

		// ----------------------------------------------------------------------------------------
		// create the panel with business objects
		GridPanel pnlEntities = new GridPanel(0, 0, 0, 0);

		pnlEntities.addSizing(0, 0, 0.0, 0.0, 1, 1, 5, 5);
		pnlEntities.addObject(1, 1, 0.0, 1.0, 1, 1, tmEntities.getScrollPane());
		pnlEntities.addSizing(2, 1, 0.0, 0.0, 1, 1, 5, 5);
		pnlEntities.addObject(3, 1, 1.0, 1.0, 1, 1, tmEntity.getScrollPane());
		pnlEntities.addSizing(1, 2, 0.0, 0.0, 1, 1, 410, 5);
		pnlEntities.addSizing(4, 2, 0.0, 0.0, 1, 1, 5, 5);

		tbpAll.addTab("Entities", pnlEntities);

		// ----------------------------------------------------------------------------------------
		pnlLookups = new LookupsPanel(context, this);
		tbpAll.addTab("Lookups", pnlLookups);

		GridPanel pnlGeneral = new GridPanel(0, 0, 0, 0);

		pnlGeneral.addObject(0, 0, 1.0, 1.0, 3, 1, tbpAll);
		pnlGeneral.addSizing(0, 1, 0.0, 0.0, 1, 1, 5, 20);
		pnlGeneral.addObject(1, 1, 1.0, 0.0, 1, 1, lblStatusBar);
		pnlGeneral.addSizing(2, 1, 0.0, 0.0, 1, 1, 5, 20);

		// ----------------------------------------------------------------------------------------
		createEntitiesPopupMenu();

		// ----------------------------------------------------------------------------------------
		JMenuItem item;
		pmTabbedPane = new JPopupMenu();

		item = new JMenuItem("Close");
		item.addActionListener(this);
		item.setActionCommand(CMD_CLOSE_TAB);
		pmTabbedPane.add(item);

		// ----------------------------------------------------------------------------------------
		cIndexes = null;
		tmIndexes = null;

		// ----------------------------------------------------------------------------------------
		setContentPane(pnlGeneral);

		// ----------------------------------------------------------------------------------------
		setAppTitle();
	}

	private void setAppTitle( )
	{
		String title = AppConstants.appName;

		if (context.server != null)
		{
			title = title + " - " + context.server.getUrl();
		}
		else
		{
			title = title + " - " + "NO server";
		}

		if (context.database != null)
		{
			title = title + " - " + context.database.getUrl() + "/" + context.database.getDatabase();
		}
		else
		{
			title = title + " - " + "NO database";
		}

		setTitle(title);
	}

	void createEntitiesPopupMenu( )
	{
		JMenuItem item;
		pmEntities = new JPopupMenu();

		item = new JMenuItem("View records - ALL");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_ALL);
		pmEntities.add(item);

		item = new JMenuItem("View records - select");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_SELECT);
		pmEntities.add(item);

		JMenu recordsLastMenu = new JMenu("View records - last ...");

		item = new JMenuItem("10 records");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_LAST_10);
		recordsLastMenu.add(item);

		item = new JMenuItem("100 records");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_LAST_100);
		recordsLastMenu.add(item);

		item = new JMenuItem("1000 records");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_LAST_1000);
		recordsLastMenu.add(item);

		item = new JMenuItem("X records");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_RECORDS_LAST_CUSTOM);
		recordsLastMenu.add(item);

		pmEntities.add(recordsLastMenu);

		pmEntities.addSeparator();

		item = new JMenuItem("View entity");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_ENTITY);
		pmEntities.add(item);

		pmEntities.addSeparator();

		item = new JMenuItem("View indexes - table");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_INDEXES);
		pmEntities.add(item);

		item = new JMenuItem("View indexes - ALL");
		item.addActionListener(this);
		item.setActionCommand(CMD_VIEW_INDEXES_ALL);
		pmEntities.add(item);

		pmEntities.addSeparator();

		item = new JMenuItem("Search BO field");
		item.addActionListener(this);
		item.setActionCommand(CMD_SEARCH_BO_FIELD);
		pmEntities.add(item);

		item = new JMenuItem("Search DB column");
		item.addActionListener(this);
		item.setActionCommand(CMD_SEARCH_DB_COLUMN);
		pmEntities.add(item);

		pmEntities.addSeparator();

		item = new JMenuItem("SQL to cleanup entity");
		item.addActionListener(this);
		item.setActionCommand(CMD_SQL_TO_CLEANUP_ENTITY);
		pmEntities.add(item);

		tmEntities.setPopupMenu(pmEntities, true);
	}

	void createMenuBar( )
	{
		JMenuBar menubar = new JMenuBar();

		JMenu menu;

		// -----------------------------------------------------------------------------------------
		menu = new JMenu("Connections");
		menu.setMnemonic(KeyEvent.VK_C);
		menubar.add(menu);

		menu.add(GUITools.createMenuItem(this, "Select connection", 0, CMD_SELECT_CONNECTION));

		// -----------------------------------------------------------------------------------------
		//menu = new JMenu("Entities");
		//menu.setMnemonic(KeyEvent.VK_S);
		//menubar.add(menu);

		//menu.add(GUITools.createMenuItem(this, "View lookups", 0, CMD_VIEW_LOOKUPS));

		// -----------------------------------------------------------------------------------------
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menubar.add(menu);

		menu.add(GUITools.createMenuItem(this, "Help", KeyEvent.VK_H, CMD_HELP));
		menu.add(GUITools.createMenuItem(this, "About", KeyEvent.VK_B, CMD_ABOUT));

		// -----------------------------------------------------------------------------------------
		setJMenuBar(menubar);
	}

	public void close( )
	{
		System.exit(0);
	}

	@Override
	public void windowActivated( WindowEvent e )
	{
		if (firstActivation)
		{
			firstActivation = false;

			selectConnection();
		}
	}

	@Override
	public void windowDeactivated( WindowEvent e )
	{
	}

	@Override
	public void windowOpened( WindowEvent e )
	{
	}

	@Override
	public void windowClosed( WindowEvent e )
	{
	}

	@Override
	public void windowIconified( WindowEvent e )
	{
	}

	@Override
	public void windowDeiconified( WindowEvent e )
	{
	}

	@Override
	public void windowClosing( WindowEvent e )
	{
		if (!autoClose)
		{
			int reply = JOptionPane.showConfirmDialog(this, "Are you sure that you want to close the application?", "Warning", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
			{
				close();
			}
		}
		else
		{
			close();
		}
	}

	public void selectConnection( )
	{
		if (ViewConnections.select(null, this))
		{
			context.clear();

			AppSettings.setDefaultDatabase(ViewConnections.selectedDatabase);
			AppSettings.setDefaultServer(ViewConnections.selectedServer);

			AppSettings.writeToINIFile();
			AppSettings.readFromINIFile();

			context.database = ViewConnections.selectedDatabase;
			context.server = ViewConnections.selectedServer;

			context.connection = openDatabase(ViewConnections.selectedDatabase);

			setAppTitle();

			refreshEntities(ViewConnections.forceRefresh);
			refreshTables();

			context.entities.createInvisibleBoFields();
			context.entities.identifyBoParents();

			tmEntities.getModel().applyFilter();
			tmEntities.refreshGUI(0, null);

			pnlLookups.getTmLookups().getModel().applyFilter();
			pnlLookups.getTmLookups().refreshGUI(0, null);
		}
	}

	private Connection openDatabase( AppConnDataDatabase database )
	{
		Connection result = null;

		if (database != null)
		{
			result = ConnectionTools.openConnection(database.getUrl(), database.getDatabase(), database.getUsername(), database.getPassword());

			try
			{
				if ((result != null) && (!result.isClosed()))
				{
					System.out.println("Connected to the database \"" + database.getDatabase() + "\" !");
				}
				else
				{
					result = null;
					JOptionPane.showMessageDialog(this, "Cannot connect to the database \"" + database.getDatabase() + "\" !", "Database connection", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch (Exception e)
			{
				result = null;
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(this, "Cannot connect!", "Database connection", JOptionPane.ERROR_MESSAGE);
			}
		}

		return result;
	}

	public void refreshEntities( boolean force )
	{
		ProgressDialog.open(null, this, "Refresh business objects", new ReadEntityListThreadActivity(context, force));
		ProgressDialog.open(null, this, "Refresh business objects", new ReadEntitiesThreadActivity(context, force));
	}

	public void refreshTables( )
	{
		ProgressDialog.open(null, this, "Read lookups", new ReadLookupsThreadActivity(context));
		ProgressDialog.open(null, this, "Read database structure", new ReadStructureThreadActivity(context));
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		if (e.getSource() != null)
		{
			if (e.getSource() instanceof TableManager)
			{
				((TableManager<?>) e.getSource()).onActionPerformed(e);
			}
		}

		TableManager.onActionPerformedStatic(e);

		String command = e.getActionCommand();

		if (command.equals(LookupsPanel.SEARCH_INSTANCE_BY_ID))
		{
			pnlLookups.searchInstanceByID();
		}

		if (command.equals(LookupsPanel.SEARCH_INSTANCE_BY_NAME))
		{
			pnlLookups.searchInstanceByName();
		}

		if (command.equals(CMD_SELECT_CONNECTION))
		{
			selectConnection();
		}

		if (command.equals(CMD_CLOSE_TAB))
		{
			if (tbpAll.getSelectedIndex() > 0)
			{
				if ((cIndexes != null) && (tbpAll.getComponentAt(tbpAll.getSelectedIndex()) == cIndexes))
				{
					cIndexes = null;
					tmIndexes = null;
				}

				tbpAll.remove(tbpAll.getSelectedIndex());
			}
		}

		if (command.equals(CMD_VIEW_RECORDS_ALL))
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				if (entity.getDbName() != null)
				{
					RecordSet records = new RecordSet(entity);

					ProgressDialog.open(null, this, "Read records", new ReadRecordsThreadActivity(context.connection, records, false, context.preffix()));

					TableManager<Record> tm = new TableManager<>(new RecordSetModel(records, context.lookups), this);
					tm.setPopupMenu(new RecordsPopupMenu(this, tm, entity), true);
					tm.getModel().applyFilter();
					tm.setAllowAutoResizing(false);
					tm.resizeColumns();

					addTab((entity.getBoName() != null ? entity.getBoName() : entity.getDbName()) + " list", tm.getScrollPane());
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The selected entity is not linked with a table in the database!");
				}
			}
		}

		if (command.equals(CMD_VIEW_RECORDS_SELECT))
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				if (entity.getDbName() != null)
				{
					Record record = new Record(entity);

					if (ViewRecordValues.select(null, this, record))
					{
						RecordSet records = new RecordSet(entity);
						records.setSelection(record, 0, false);

						ProgressDialog.open(null, this, "Read records", new ReadRecordsThreadActivity(context.connection, records, false, context.preffix()));

						TableManager<Record> tm = new TableManager<>(new RecordSetModel(records, context.lookups), this);
						tm.setPopupMenu(new RecordsPopupMenu(this, tm, entity), true);
						tm.getModel().applyFilter();
						tm.setAllowAutoResizing(false);
						tm.resizeColumns();

						addTab((entity.getBoName() != null ? entity.getBoName() : entity.getDbName()) + " list", tm.getScrollPane());
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The selected entity is not linked with a table in the database!");
				}
			}
		}

		if (command.equals(CMD_VIEW_RECORDS_LAST_10))
		{
			viewRecordsLast(10);
		}
		if (command.equals(CMD_VIEW_RECORDS_LAST_100))
		{
			viewRecordsLast(100);
		}
		if (command.equals(CMD_VIEW_RECORDS_LAST_1000))
		{
			viewRecordsLast(1000);
		}
		if (command.equals(CMD_VIEW_RECORDS_LAST_CUSTOM))
		{
			String s = JOptionPane.showInputDialog(this, "Enter the maximum number of records", "");
			if (s != null)
			{
				int count = 0;
				try
				{
					count = Integer.parseInt(s);
					if (count > 0)
					{
						viewRecordsLast(count);
					}
					else
					{
						JOptionPane.showMessageDialog(this, "The maximum number of records must be a positive integer value!");
					}
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(this, "The maximum number of records must be a positive integer value!");
				}
			}
		}

		if (command.equals(CMD_VIEW_ENTITY))
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				addTab((entity.getBoName() != null ? entity.getBoName() : entity.getDbName()), new NodesTableManager(context, this, entity, null).getScrollPane());
			}
		}

		if (command.equals(CMD_VIEW_INDEXES))
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				if (entity.getDbName() != null)
				{
					viewIndexes(entity.getDbName());
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The current entity is not a table!");
				}
			}
		}

		if (command.equals(CMD_SQL_TO_CLEANUP_ENTITY))
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				if (entity.getDbName() != null)
				{
					generateSQLForCleanup(entity);
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The current entity is not a table!");
				}
			}
		}

		if (command.equals(CMD_VIEW_INDEXES_ALL))
		{
			viewIndexes(null);
		}

		if (command.equals(CMD_SEARCH_BO_FIELD))
		{
			String s = JOptionPane.showInputDialog(this, "Enter the BO field name", "");
			if (s != null)
			{
				s = s.trim().toLowerCase();
				if (s.length() > 0)
				{
					java.util.List<Entity> list = new java.util.ArrayList<Entity>();

					boolean added;
					for (int i = 0; i < context.entities.countBO(); i++)
					{
						added = false;
						for (int k = 0; (k < context.entities.getBoEntity(i).getBoFieldCount()) && (!added); k++)
						{
							if (context.entities.getBoEntity(i).getBoField(k).getBoName().toLowerCase().indexOf(s) >= 0)
							{
								added = list.add(context.entities.getBoEntity(i));
							}
						}
					}

					if (list.size() > 0)
					{
						tmEntities.getModel().clearFilter();
						tmEntities.getModel().clearSelection();

						for (int i = 0; i < list.size(); i++)
						{
							tmEntities.getModel().addToFilter(list.get(i));
						}

						tmEntities.getModel().fireTableDataChanged();
					}
					else
					{
						JOptionPane.showMessageDialog(this, "No business object having a field like \"" + s + "\" was found!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The searched name cannot be empty!");
				}
			}
		}

		if (command.equals(CMD_SEARCH_DB_COLUMN))
		{
			String s = JOptionPane.showInputDialog(this, "Enter the DB column name", "");
			if (s != null)
			{
				s = s.trim().toLowerCase();
				if (s.length() > 0)
				{
					java.util.List<Entity> list = new java.util.ArrayList<Entity>();

					boolean added;
					for (int i = 0; i < context.entities.countDB(); i++)
					{
						added = false;
						for (int k = 0; (k < context.entities.getDbEntity(i).getDbFieldCount()) && (!added); k++)
						{
							if (context.entities.getDbEntity(i).getDbField(k).getDbName().toLowerCase().indexOf(s) >= 0)
							{
								added = list.add(context.entities.getDbEntity(i));
							}
						}
					}

					if (list.size() > 0)
					{
						tmEntities.getModel().clearFilter();
						tmEntities.getModel().clearSelection();

						for (int i = 0; i < list.size(); i++)
						{
							tmEntities.getModel().addToFilter(list.get(i));
						}

						tmEntities.getModel().fireTableDataChanged();
					}
					else
					{
						JOptionPane.showMessageDialog(this, "No table having a column like \"" + s + "\" was found!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The searched name cannot be empty!");
				}
			}
		}

		if (command.equals(RecordsPopupMenu.CMD_VIEW_RECORD))
		{
			if (e.getSource() != null)
			{
				if (e.getSource() instanceof JMenuItem)
				{
					JMenuItem item = (JMenuItem) e.getSource();
					if (item.getParent() != null)
					{
						if (item.getParent() instanceof RecordsPopupMenu)
						{
							RecordsPopupMenu pmRecords = (RecordsPopupMenu) item.getParent();
							TableManager<Record> tmRecords = pmRecords.getTableManager();

							if (tmRecords != null)
							{
								Record record = tmRecords.getSelected();

								PrimaryKey pk = new PrimaryKey(record);

								addTab((record.getEntity().getBoName() != null ? record.getEntity().getBoName() : record.getEntity().getDbName()) + " " + pk.asText(false),
										new NodesTableManager(context, this, null, pk).getScrollPane());
							}
						}
					}
				}
			}
		}

		if (command.equals(RecordsPopupMenu.CMD_REFRESH_RECORDS))
		{
			if (e.getSource() != null)
			{
				if (e.getSource() instanceof JMenuItem)
				{
					JMenuItem item = (JMenuItem) e.getSource();
					if (item.getParent() != null)
					{
						if (item.getParent() instanceof RecordsPopupMenu)
						{
							RecordsPopupMenu pmRecords = (RecordsPopupMenu) item.getParent();
							TableManager<Record> tmRecords = pmRecords.getTableManager();

							if (tmRecords != null)
							{
								RecordSet records = ((RecordSetModel) tmRecords.getModel()).getRecords();

								ProgressDialog.open(null, this, "Refresh records", new ReadRecordsThreadActivity(context.connection, records, false, context.preffix()));

								tmRecords.getModel().applyFilter();
								tmRecords.getModel().fireTableDataChanged();
							}
						}
					}
				}
			}
		}

		if (command.equals(RecordsPopupMenu.CMD_UPDATE_RECORDS))
		{
			if (e.getSource() != null)
			{
				if (e.getSource() instanceof JMenuItem)
				{
					JMenuItem item = (JMenuItem) e.getSource();
					if (item.getParent() != null)
					{
						if (item.getParent() instanceof RecordsPopupMenu)
						{
							RecordsPopupMenu pmRecords = (RecordsPopupMenu) item.getParent();
							TableManager<Record> tmRecords = pmRecords.getTableManager();

							if (tmRecords != null)
							{
								RecordSet records = ((RecordSetModel) tmRecords.getModel()).getRecords();

								ProgressDialog.open(null, this, "Update records", new ReadRecordsThreadActivity(context.connection, records, true, context.preffix()));

								tmRecords.getModel().applyFilter();
								tmRecords.getModel().fireTableDataChanged();
							}
						}
					}
				}
			}
		}

		if (command.equals(RecordsPopupMenu.CMD_EDIT_FIELD))
		{
			if (e.getSource() != null)
			{
				if (e.getSource() instanceof JMenuItem)
				{
					JMenuItem item = (JMenuItem) e.getSource();
					if (item.getParent() != null)
					{
						if (item.getParent() instanceof RecordsPopupMenu)
						{
							RecordsPopupMenu pmRecords = (RecordsPopupMenu) item.getParent();
							TableManager<Record> tmRecords = pmRecords.getTableManager();

							if (tmRecords != null)
							{
								int columnIndex = tmRecords.getTable().getSelectedColumn();
								Record record = tmRecords.getSelected();
								if ((record != null) && (columnIndex >= 0))
								{
									if (!record.getEntity().getDbField(columnIndex).isDbPK())
									{
										boolean forAll = false;
										String pkFieldName = record.getEntity().getPkFields().get(0).getDbName();

										RecordSet records = new RecordSet(record.getEntity());
										Record selection = new Record(record.getEntity());
										records.setSelection(selection, 0, false);

										if (tmRecords.getModel().getRowCount() > 1)
										{
											String message = "Do you want to update ALL visible records?";

											int r = JOptionPane.showOptionDialog(this, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
													null);

											if (r == JOptionPane.YES_OPTION)
											{
												String values = "";
												for (int i = 0; i < tmRecords.getModel().getRowCount(); i++)
												{
													records.add(tmRecords.getModel().get(i));
													values = values + RecordField.VALUESEP + tmRecords.getModel().get(i).get(pkFieldName).ival();
												}

												selection.get(pkFieldName).sset(values);

												forAll = true;
											}
											else
											{
												records.add(record);
												selection.get(pkFieldName).sset("" + record.pk());
											}
										}
										else
										{
											records.add(record);
											selection.get(pkFieldName).sset("" + record.pk());
										}

										if (ObjectsTools.edit(context.connection, record, record.getFields().get(columnIndex), this, (forAll ? records : null)))
										{
											ProgressDialog.open(null, this, "Update records", new ReadRecordsThreadActivity(context.connection, records, true, context.preffix()));
											tmRecords.getModel().applyFilter();
											tmRecords.getModel().fireTableDataChanged();

											tmRecords.refreshGUI(columnIndex, record);
											GUITools.ensureCellIsVisible(tmRecords.getTable(), tmRecords.getTable().getSelectedRow(), columnIndex);
										}
									}
									else
									{
										JOptionPane.showMessageDialog(this, "Cannot edit a field that is used for primary key!");
									}
								}
								else
								{
									JOptionPane.showMessageDialog(this, "Please select a record!");
								}
							}
						}
					}
				}
			}
		}

		if (command.equals(TableManager.TABLE_DOUBLE_CLICK))
		{
			if (e.getSource() == tmEntity)
			{
				EntityField field = tmEntity.getSelected();
				if (field != null)
				{
					if (field.getBoName() != null)
					{
						Entity entity = context.entities.getBoEntity(field.getBoType());

						int row = tmEntities.getModel().getRow(entity);
						if (row < 0)
						{
							tmEntities.getModel().addToFilter(entity);
							tmEntities.refreshGUI(-1, entity);
						}
						else
						{
							tmEntities.refreshGUI(-1, entity);
						}

						if (field.isBoCollection())
						{
							entity = context.entities.getDbEntity(field.getBoCollectionTableName());

							row = tmEntities.getModel().getRow(entity);
							if (row < 0)
							{
								tmEntities.getModel().addToFilter(entity);
								tmEntities.refreshGUI(-1, entity);
							}
							else
							{
								tmEntities.refreshGUI(-1, entity);
							}
						}
					}
				}
			}
		}

		if (command.equals(CMD_ABOUT))
		{
			AboutDialog.open(null, this);
		}

		if (command.equals(CMD_HELP))
		{
			String s = "How to use the grids:\n";
			s = s + " - In any grid, pressing a letter on a column will filter the grid rows to those that contains that letter in the current column.\n";
			s = s + " - If already exists a filter on other column then both filter are applied.\n";
			s = s + " - Pressing the BACKSPACE the last letter from the filter will be deleted and the rows will be refiltered.\n";
			s = s + " - If the first filter character is \".\" then only the rows for which the value in the current column STARTS with the filter text will be kept.\n";
			s = s + " - Only the letters and numbers are accepted in the filters. The \".\" is accepted only as the first character in the filter.\n";
			s = s + " - The \"-\" character will remove the current filter (if present) and will keep only the rows that have an empty string on the current column.\n";
			s = s + " - Pressing the ESC key will remove all the filters from the current grid.\n";
			s = s + " - The filter on a column is shown in the column header right after the column name.\n";
			s = s + " - The rows can be sorted by the values in a column by clicking the column header. A second click will change the order (asc, desc).\n";
			s = s + " - Pressing ESC+SHIFT will cancel any sorting in the current grid.\n";
			s = s + "\n";
			s = s + "How to edit values in a table:\n";
			s = s + " - Enter the word \"null\" if you want to set the value as NULL in the database.\n";
			JOptionPane.showMessageDialog(this, s);
		}

		if (command.equals(TableManager.TABLE_DATA_CHANGED))
		{
			populateStatusBar(e.getSource());
		}

		if (command.equals(CMD_CLOSE))
		{
			autoClose = true;

			close();
		}
	}

	@SuppressWarnings("rawtypes")
	private void populateStatusBar( Object object )
	{
		boolean isset = false;

		if ((object != null) && (object instanceof TableManager))
		{
			TableManager tm = (TableManager) object;

			if ((!isset) && ((tm == tmEntities) || (tm == tmEntity)))
			{
				String s = "Total entities: " + tmEntities.getModel().getTotalRowCount();
				s = s + "    Visible entities: " + tmEntities.getModel().getRowCount();
				s = s + "                         ";
				s = s + "Total entity fields: " + tmEntity.getModel().getTotalRowCount();
				s = s + "    Visible entity fields: " + tmEntity.getModel().getRowCount();
				lblStatusBar.setText(s);

				isset = true;
			}

			if ((!isset) && ((tm == pnlLookups.getTmLookups()) || (tm == pnlLookups.getTmInstances())))
			{
				String s = "Total lookups: " + pnlLookups.getTmLookups().getModel().getTotalRowCount();
				s = s + "    Visible lookups: " + pnlLookups.getTmLookups().getModel().getRowCount();
				s = s + "                         ";
				s = s + "Total lookup instances: " + pnlLookups.getTmInstances().getModel().getTotalRowCount();
				s = s + "    Visible lookup instances: " + pnlLookups.getTmInstances().getModel().getRowCount();
				lblStatusBar.setText(s);

				isset = true;
			}

			if (!isset)
			{
				GenericModel model = ((TableManager) tm).getModel();
				if (model != null)
				{
					lblStatusBar.setText("Total: " + model.getTotalRowCount() + "    Visible: " + model.getRowCount());

					isset = true;
				}
			}
		}

		if ((object != null) && (object instanceof JTable))
		{
			JTable table = (JTable) object;

			TableModel model = table.getModel();
			if ((model != null) && (model instanceof GenericModel))
			{
				GenericModel gmodel = (GenericModel) model;
				lblStatusBar.setText("Total: " + gmodel.getTotalRowCount() + "    Visible: " + gmodel.getRowCount());

				isset = true;
			}
		}

		if (!isset)
		{
			lblStatusBar.setText("");
		}
	}

	public void viewRecordsLast( int count )
	{
		if (count > 0)
		{
			Entity entity = tmEntities.getSelected();
			if (entity != null)
			{
				if (entity.getDbName() != null)
				{
					RecordSet records = new RecordSet(entity);
					records.setSelection(null, count, true);

					ProgressDialog.open(null, this, "Read records", new ReadRecordsThreadActivity(context.connection, records, false, context.preffix()));

					TableManager<Record> tm = new TableManager<>(new RecordSetModel(records, context.lookups), this);
					tm.setPopupMenu(new RecordsPopupMenu(this, tm, entity), true);
					tm.getModel().applyFilter();
					tm.setAllowAutoResizing(false);
					tm.resizeColumns();

					addTab((entity.getBoName() != null ? entity.getBoName() : entity.getDbName()) + " list", tm.getScrollPane());
				}
				else
				{
					JOptionPane.showMessageDialog(this, "The selected entity is not linked with a table in the database!");
				}
			}
		}
	}

	public void viewIndexes( String tableName )
	{
		if (cIndexes == null)
		{
			TableManager<IndexField> tm = new TableManager<>(new IndexesModel(context, null), this);
			tm.getModel().applyFilter();
			tm.setAllowSorting(false);
			tm.setPopupMenu(null, true);

			addTab("DB indexes", tm.getScrollPane());

			cIndexes = tm.getScrollPane();
			tmIndexes = tm;
		}
		else
		{
			tbpAll.setSelectedComponent(cIndexes);
		}

		if ((tmIndexes != null) && (tableName != null))
		{
			tmIndexes.getModel().clearFilter();
			tmIndexes.getModel().applyFilter(0, "x." + tableName, (char) 0);

			for (int i = 0; i < tmIndexes.getModel().getColumnCount(); i++)
			{
				GUITools.setColumnTitle(tmIndexes.getTable(), i, tmIndexes.getModel().getColumnName(i));
			}
		}
	}

	public void generateSQLForCleanup( Entity entity )
	{
		if (entity != null)
		{
			String sqlClean = "";
			String sqlEnable = "";
			String sqlDisable = "";

			Entity other;

			for (int i = 0; i < context.entities.countDB(); i++)
			{
				other = context.entities.getDbEntity(i);
				if (other != entity)
				{
					for (int j = 0; j < other.getDbFieldCount(); j++)
					{
						if (other.getDbField(j).getDbParentTableName() != null)
						{
							if (other.getDbField(j).getDbParentTableName().equalsIgnoreCase(entity.getDbName()))
							{
								sqlClean = sqlClean + "update " + other.getDbName() + " set " + other.getDbField(j).getDbName() + "=null;" + AppConstants.EOL;
								sqlEnable = sqlEnable + "alter table " + other.getDbName() + " check constraint " + other.getDbField(j).getDbForeignKeyName() + ";"
										+ AppConstants.EOL;
								sqlDisable = sqlDisable + "alter table " + other.getDbName() + " nocheck constraint " + other.getDbField(j).getDbForeignKeyName() + ";"
										+ AppConstants.EOL;
							}
						}
					}
				}
			}

			String s = "";
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + "-- eliminate the child values for " + entity.getDbName() + AppConstants.EOL;
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + sqlClean;
			s = s + AppConstants.EOL;
			s = s + AppConstants.EOL;
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + "-- enable child foreign keys for " + entity.getDbName() + AppConstants.EOL;
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + sqlEnable;
			s = s + AppConstants.EOL;
			s = s + AppConstants.EOL;
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + "-- disable child foreign keys for " + entity.getDbName() + AppConstants.EOL;
			s = s + "-- -----------------------------------------------------------------------------------------------------------------" + AppConstants.EOL;
			s = s + sqlDisable;

			JTextArea ta = new JTextArea(s);
			JScrollPane sp = new JScrollPane(ta);

			addTab("SQLs for " + entity.getDbName(), sp);
		}
	}

	public void addTab( String title, Component component )
	{
		tbpAll.addTab(title, component);

		JLabel label = new JLabel(title);
		label.setComponentPopupMenu(pmTabbedPane);
		label.addMouseListener(new TabMouseAdapter(component));

		tbpAll.setTabComponentAt(tbpAll.getTabCount() - 1, label);
	}

	class TabMouseAdapter extends MouseAdapter
	{
		private Component component;

		public TabMouseAdapter( Component component )
		{
			super();
			this.component = component;
		}

		@Override
		public void mouseClicked( MouseEvent e )
		{
			tbpAll.setSelectedComponent(component);
		}
	}
}
