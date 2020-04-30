import java.sql.*;
import java.awt.*;
import java.awt.event.*; //user clicking buttons
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;

// import Pickup.java;

//import swing1.s1.ListenForButton;

/*
 aggregation query
 select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id group by waste_type.name;
 */

import java.awt.Dimension;
import java.awt.Toolkit;


public class waste extends JFrame{
  
  public static void main(String[] aargs){
    new waste();
  }
// All Jthings for insertion
  private JButton submit, insert_C, insert_S, insert_W, show_C, show_S, show_W;
  private JComboBox waste_type;     
  private JComboBox company;    
  private JComboBox site;
  private JComboBox weightType;
  private JComboBox whichTable;
  private JSpinner day;
  private JSpinner year;
  private JLabel displayLabel; 
  private JTextField tf1, tf2, tf3, tf4;
  private ListenForButton lfb; 
  private JTextField idbox;
  private JButton delete;
  
//Instance data for insertion
  private String date;
  private String dayValue;
  private String monthValue;
  private String yearValue;
  private int companyId;
  private int siteId;
  private int wasteTypeId;
  private double weight;
  
  
//All Jthings for queries
  //all data
  private JComboBox wt_sort;
  private JButton allPickups;
  private JButton dataByMonth; 
  //specific data
  private JComboBox sortBy_pickup;
  private JComboBox sortBy_aggro;
  private JComboBox upOrDown;
  private JButton generate_pickup_table;
  private JButton generate_aggregation_table; 
  private JSpinner end_day;
  private JSpinner start_day; 
  private JButton dmon;
  private JComboBox group; 
  private JComboBox sortIdDate;
  
  
//instance data queries
  private String sdate;
  private String edate;
  private String sday;
  private String smonth;
  private String syear;
  private String eday;
  private String emonth;
  private String eyear;
  private boolean ascdesc;
  
  
//buttons for entity insertion
  private JButton c_insert;
  private JButton s_insert;
  private JButton w_insert; 
  
  private Font font;
  private Font bigFont;
  
  private static final String DB_URL = "jdbc:sqlite:dataWaste.db";
 
  
//CONSTRUCTOR:
  public waste(){
// setting window parameters 
    Toolkit tk = Toolkit.getDefaultToolkit();      
    Dimension dim = tk.getScreenSize(); 
    this.setSize(1200, 1000);
    this.setLocationRelativeTo(null);        
    this.setTitle("WasteManager");
    font = new Font("Times New Roman", Font.PLAIN, 20);
    bigFont = new Font("Times New Roman", Font.BOLD, 30); 
    JPanel p = new JPanel(new GridLayout(0,2)); 
    p.setBackground(Color.lightGray);
      
    // ********************************************** INSERT/DELETE PORTION ************************************************
    JLabel insert_portion = new JLabel("Insert/Delete Data:");
    insert_portion.setFont(bigFont); 
    p.add(insert_portion);
    p.add(new JLabel()); 
    
    //weight input label
    JLabel weight_label = new JLabel("Enter the pickup weight in this text box");
    weight_label.setFont(font); 
    p.add(weight_label);
    //weight input textbox
    tf1 = new JTextField("",20);
    tf1.setToolTipText("put in the weight");
    tf1.setFont(font); 
    p.add(tf1);
    
    //weight_types label
    JLabel weight_types_label = new JLabel("Specify the weight in pounds or tons");
    weight_types_label.setFont(font); 
    p.add(weight_types_label);
    //weight tpyes combo box
    String[] weight_types = {"tons", "lbs"};
    this.weightType = new JComboBox(weight_types);
    weightType.setFont(font); 
    p.add(weightType);
    
    try(Connection umbreon = DriverManager.getConnection(DB_URL)) { 
      
//generate new companies
      
      PreparedStatement num = umbreon.prepareStatement( "select count(*) from company" );
      
      ResultSet i = num.executeQuery();
      int numberOfCompanies = i.getInt("count(*)"); 
      
      String[] companies = new String[numberOfCompanies];
      
      PreparedStatement getNames = umbreon.prepareStatement("select name from company order by id" );
      
      ResultSet names = getNames.executeQuery();
      String currentName;
      int count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        companies[count] = currentName;
        count++;
      }
//label for company combo box
      JLabel company_label = new JLabel("Select the company responsible for this pickup");
      company_label.setFont(font); 
      p.add(company_label);
//initialize company combobox
      this.company  = new JComboBox(companies);           
      company.setSize(40, 40);
      company.setFont(font); 
      p.add(this.company);
      
//generate new sites
      num = umbreon.prepareStatement("select count(*) from site");
      
      i = num.executeQuery();
      int numberOfSites = i.getInt("count(*)"); 
      
      String[] sites = new String[numberOfSites];
      
      PreparedStatement getSites = umbreon.prepareStatement("select name from site order by id");
      
      names = getSites.executeQuery();
      count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        sites[count] = currentName;
        count++;
      }
      
//label for sites combobox
      JLabel site_label = new JLabel("Select the site where this pickup occured");
      site_label.setFont(font); 
      p.add(site_label);
//initialize sites combobox
      this.site  = new JComboBox(sites);
      site.setSize(40, 40);
      site.setFont(font); 
      p.add(this.site);
      
      
//generate new waste types
      num = umbreon.prepareStatement("select count(*) from waste_type");
      i = num.executeQuery();
      int numberOfWastes = i.getInt("count(*)"); 
      
      String[] waste_types = new String[numberOfWastes];
      PreparedStatement getWastes = umbreon.prepareStatement("select name from waste_type order by id");
      
      names = getWastes.executeQuery();
      count = 0;
      while(names.next()) {
        currentName = names.getString("name");
        waste_types[count] = currentName;
        count++;
      }
//waste_types_label
      JLabel wt_label = new JLabel("Select the type of waste that was picked up");
      wt_label.setFont(font); 
      p.add(wt_label);
//initialize waste_types combo box
      this.waste_type = new JComboBox(waste_types);
      waste_type.setFont(font); 
      p.add(this.waste_type);
      
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
    
//date picker label
    JLabel date1_label = new JLabel("Input the date of this pickup");
    date1_label.setFont(font); 
    p.add(date1_label);
//date picker for data insertion
    Date todaysDate = new Date();
    year = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(year, "MM/dd/yy");
    year.setEditor(dateEditor);
    date = year.getValue().toString();
    year.setFont(font); 
    p.add(year);
    
    //submit button label
    JLabel submit_label = new JLabel("Click this button to submit a single pickup entry");
    submit_label.setFont(font); 
    p.add(submit_label);
    
    //submit button
    submit = new JButton("Insert Pickup");
    submit.setToolTipText("Adds a new row to the pickup table with parameters in text box and dropdown menus. Configure these correctly before clicking.");
    lfb = new ListenForButton();
    submit.addActionListener(lfb);
    submit.setFont(font); 
    p.add(submit);
    
    p.add(new JLabel()); //seperation line
    p.add(new JLabel()); 
          
    //id text box label
    JLabel idtb = new JLabel("Specify the id of the pickup you want to delete");
    idtb.setFont(font);
    p.add(idtb);
          
    //id box
    this.idbox = new JTextField();
    idbox.setFont(font);
    p.add(idbox);
          
    //delete button label
    JLabel del_but = new JLabel("Click to delete the pickup of the specified ID");;
    del_but.setFont(font);
    p.add(del_but);
    //delete button
    this.delete = new JButton("Delete Pickup");
    delete.setToolTipText("Deletes the pickup of the specified ID. Cannot be undone, but can be re-added");
    delete.addActionListener(lfb);
    delete.setFont(font);
    p.add(delete);
    
    
//*********************************** Query (non-specific) PORTION **************************************
    p.add(new JLabel());   // new line to seperate portion on the main interface
    p.add(new JLabel());
    
    JLabel all_portion = new JLabel("View All Data:");
    all_portion.setFont(bigFont); 
    p.add(all_portion);
    p.add(new JLabel()); 
    
    //label for waste type grouping option
    JLabel wt_grouping = new JLabel("Would you like to group all data by waste type?");
    wt_grouping.setFont(font); 
    p.add(wt_grouping);
    //waste type grouping combo box
    String[] yesNo = {"Yes", "No"};
    wt_sort = new JComboBox(yesNo);
    wt_sort.setFont(font);
    p.add(wt_sort);
    
    //label for display months table
    JLabel displayM = new JLabel("Click to display total weight of waste collected monthly");
    displayM.setFont(font); 
    p.add(displayM);
    //display months button
    dataByMonth = new JButton("Generate Monthly Report");
    dataByMonth.setToolTipText("Generate a table that shows total weight of waste collected grouped by month and optionally by waste type");
    dataByMonth.addActionListener(lfb);
    dataByMonth.setFont(font); 
    p.add(dataByMonth);
    
    
    //label for sort by id or date
    JLabel idDate = new JLabel("Sort Pickups by Date?");
    idDate.setFont(font);
    p.add(idDate);
    // comboBox for sort by id or date
    String[] bibble = {"Yes", "No"};
    sortIdDate = new JComboBox(bibble);
    sortIdDate.setFont(font);
    p.add(sortIdDate); 
    
    
    // label for all pickups table
    JLabel displayP = new JLabel("Click to display all pickups in the database");
    displayP.setFont(font); 
    p.add(displayP);
    //display all pickups button
    allPickups = new JButton("Show All Pickups"); 
    allPickups.setToolTipText("Generates a table that shows all pickups logged in the database");
    allPickups.addActionListener(lfb);
    allPickups.setFont(font);
    p.add(allPickups); 
    
    //
    

//*********************************** QUERY (specific) PORTION ******************************************
    p.add(new JLabel());   // new line to seperate portion on the main interface
    p.add(new JLabel());
    
    JLabel query_portion = new JLabel("View Specific Data:");
    query_portion.setFont(bigFont); 
    p.add(query_portion);
    p.add(new JLabel()); 
    
    
//pickup sort label
    JLabel pickup_sort_label = new JLabel("How do you want the pickups sorted? (Pickup)");
    pickup_sort_label.setFont(font); 
    p.add(pickup_sort_label);
//sort by comboBox for pickup table
    String[] pickupSort = {"id","date"}; 
    this.sortBy_pickup = new JComboBox(pickupSort); 
    sortBy_pickup.setFont(font); 
    p.add(sortBy_pickup); 
    
    
//ascdesc label
    JLabel ascdesc_label = new JLabel("Ascending or Descending order? (Aggregation and Pickup)");
    ascdesc_label.setFont(font); 
    p.add(ascdesc_label);
// combo box to descide ascending or descending order 
    String[] asc_desc = {"Ascending Order", "Descending Order"}; 
    this.upOrDown = new JComboBox(asc_desc); 
    upOrDown.setFont(font); 
    p.add(upOrDown); 
    
    
//day2 label
    JLabel day2_label = new JLabel("Select the start date to sort by (All Buttons)");
    day2_label.setFont(font); 
    p.add(day2_label);
//spinner for picking start day for query 
    start_day = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    start_day.setToolTipText("The first date to include in the sort, inclusive"); 
    JSpinner.DateEditor dateEditor1 = new JSpinner.DateEditor(start_day, "MM/dd/yy");
    start_day.setEditor(dateEditor1);
    date = year.getValue().toString();
    start_day.setFont(font); 
    p.add(start_day);
  
//day3 label
    JLabel day3_label = new JLabel("Select the end date to sort by (All Buttons)");
    day3_label.setFont(font); 
    p.add(day3_label);
//spinner for picking end day for query
    end_day = new JSpinner(new SpinnerDateModel(todaysDate, null, null,Calendar.DAY_OF_MONTH));
    end_day.setToolTipText("The last date to include in the sort, inclusive"); 
    JSpinner.DateEditor dateEditor2 = new JSpinner.DateEditor(end_day, "MM/dd/yy");
    end_day.setEditor(dateEditor2);
    date = year.getValue().toString();
    end_day.setFont(font); 
    p.add(end_day);

    
//pickup label
    JLabel pickup_label = new JLabel("Click to generate a table of pickups from start date to end date");
    pickup_label.setFont(font); 
    p.add(pickup_label);
//pickup table button
    this.generate_pickup_table = new JButton("Generate Pickup Table");
    this.generate_pickup_table.setToolTipText("Generates a table displaying all pickups between requested dates"); 
    this.generate_pickup_table.addActionListener(lfb); 
    generate_pickup_table.setFont(font); 
    p.add(generate_pickup_table);
    
//aggregation label
    JLabel aggro_label = new JLabel("Click to generate a table that displays aggregated data");
    aggro_label.setFont(font); 
    p.add(aggro_label);
//aggregation table button
    this.generate_aggregation_table = new JButton("Generate Aggregation Table");
    this.generate_aggregation_table.setToolTipText("Generates a table displaying aggregated data between given dates and ordered by a specified parameter"); 
    this.generate_aggregation_table.addActionListener(lfb); 
    generate_aggregation_table.setFont(font); 
    p.add(generate_aggregation_table);
    
    
//grouping label
    JLabel group_label = new JLabel("Group monthly data by waste type? (Monthly)");
    group_label.setFont(font); 
    p.add(group_label);
//grouping combo box
    this.group = new JComboBox(yesNo);
    group.setFont(font);
    p.add(group); 
    
//display months label
    JLabel mon_label = new JLabel("Click to generate a table of all selected data grouped by month");
    mon_label.setFont(font); 
    p.add(mon_label);
//display months table button
    this.dmon = new JButton("Generate Monthly Grouping");
    this.dmon.setToolTipText("Generates a table displaying data between given dates grouped by month and waste type"); 
    this.dmon.addActionListener(lfb); 
    dmon.setFont(font); 
    p.add(dmon);
    
    
//***************************************************************************** ENTITY INSERTION PORTION **********************************************************************//    
    p.add(new JLabel());  //adding new lines to seperate section on the main interface
    p.add(new JLabel());
    JLabel entity_portion = new JLabel("Review Fields:");
    entity_portion.setFont(bigFont); 
    p.add(entity_portion);
    p.add(new JLabel()); 
    
    //label for company button
    JLabel c_but = new JLabel("See table of companies, or add a new one");
    c_but.setFont(font);
    p.add(c_but); 
    //insert company button
    c_insert = new JButton("Company"); 
    c_insert.setToolTipText("Add a new company to the database. this is irreversable");
    c_insert.addActionListener(lfb);
    c_insert.setFont(font);
    p.add(c_insert);
    
    //label for site button
    JLabel s_but = new JLabel("See table of sites, or add a new one");
    s_but.setFont(font);
    p.add(s_but); 
    //insert site button
    s_insert = new JButton("Pickup Site"); 
    s_insert.setToolTipText("Add a new pickup site to the database. this is irreversable");
    s_insert.addActionListener(lfb);
    s_insert.setFont(font);
    p.add(s_insert);
    
    //label for waste type button
    JLabel w_but = new JLabel("See table of waste types, or add a new one");
    w_but.setFont(font);
    p.add(w_but); 
    //insert waste type button
    w_insert = new JButton("Waste Type"); 
    w_insert.setToolTipText("Add a new waste type to track into the database. this is irreversable");
    w_insert.addActionListener(lfb);
    w_insert.setFont(font);
    p.add(w_insert);
    
    
    
    
//Final window setting, adding panels
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.add(p);  
    this.setVisible(true);
    submit.requestFocus();
  }
  
  
  
  //******************************************************************************** TABLE METHODS ******************************************************************************
  /*
   Void methods below for making tables pop up in the window
   */
  
  //all pickups
  private JTable displayPickups(boolean orderByDate) {
    PickupTableModel tableModel = new PickupTableModel();
    

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      String orderBy = " ";
      if(orderByDate){orderBy = " order by date";}

      Statement s = conn.createStatement();
      ResultSet pResults = s.executeQuery("select Pickup.id, Pickup.weight, Pickup.date, Site.name as sname, Waste_Type.name as wname, Company.name as cname, smalldate from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id" + orderBy);
      while (pResults.next()) {
        int id = pResults.getInt(1);
        double weight = pResults.getDouble(2);
        String date = pResults.getString(3);
        String site = pResults.getString(4);
        String waste_type = pResults.getString(5);
        String company = pResults.getString(6);
        String smalldate = pResults.getString(7);
        
        Pickup p = new Pickup(id, weight, date, site, waste_type, company, smalldate);
        tableModel.addInstance(p);
      } 

        
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }

    JTable table = new JTable(tableModel);
    return table;
  }
  
  
  //pickups with start/end dates, ordered by either by date or by id, ascending or descending
  private JTable displayPickups(String startDate, String endDate, String order, Boolean descending) {
    PickupTableModel tableModel = new PickupTableModel();
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      String desc;
      if(descending){desc = new String(" DESC");}
      else {desc = new String(" ASC");}
      ResultSet pResults = s.executeQuery("select Pickup.id, Pickup.weight, Pickup.date, Site.name as sname, Waste_Type.name as wname, Company.name as cname, smalldate from Pickup join Company on Company.id = Pickup.company_id join Waste_Type on Waste_Type.id = Pickup.waste_type_id join Site on Site.id = Pickup.site_id where Pickup.date >= '" + startDate + "' and Pickup.date <= '" + endDate + "' order by " + order + desc);
      while (pResults.next()) {
        int id = pResults.getInt(1);
        double weight = pResults.getDouble(2);
        String date = pResults.getString(3);
        String site = pResults.getString(4);
        String waste_type = pResults.getString(5);
        String company = pResults.getString(6);
        String smalldate = pResults.getString(7);
        
        Pickup p = new Pickup(id, weight, date, site, waste_type, company, smalldate);
        // System.out.println(id + weight + date + site + waste_type + company);
        
        tableModel.addInstance(p);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
    JTable table = new JTable(tableModel);
    return table;
  }
  
  private JTable displayAggro() {

    AggroTableModel aggroModel = new AggroTableModel();

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      ResultSet aResults = s.executeQuery("select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id group by waste_type.name");
      while (aResults.next()) {
        String waste_type = aResults.getString(1);
        double sum = aResults.getDouble(2);
        double average = aResults.getDouble(3);
        
        Aggro a = new Aggro(waste_type, sum, average);
        // System.out.println("A" + a);
        aggroModel.addInstance(a);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }

    JTable table = new JTable(aggroModel);
    return table;
  }
  
  private JTable displayAggro(String startDate, String endDate, String order, Boolean descending) {

    AggroTableModel aggroModel = new AggroTableModel();

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      String desc;
      if(descending){desc = new String(" DESC");}
      else {desc = new String(" ASC");}
      ResultSet aResults = s.executeQuery("select waste_type.name as 'waste type', sum(weight) as sum, avg(weight) as average from pickup join waste_type on waste_type.id = pickup.waste_type_id where pickup.date >= '" + startDate + "' and pickup.date <= '" + endDate + "' group by waste_type.name order by " + order + desc);
      while (aResults.next()) {
        String waste_type = aResults.getString(1);
        double sum = aResults.getDouble(2);
        double average = aResults.getDouble(3);
        
        Aggro a = new Aggro(waste_type, sum, average);
        // System.out.println("A" + a);
        aggroModel.addInstance(a);
      } 
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }

    JTable table = new JTable(aggroModel);
    return table;

  }

  private JTable displayMonths(boolean splitByWasteType) {
    MonthTypeModel monthTypeModel;
    MonthModel monthModel;

    monthTypeModel = new MonthTypeModel();
    monthModel = new MonthModel();

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      Statement s = conn.createStatement();
      ResultSet mResults;
      if(splitByWasteType) {
        mResults = s.executeQuery("select smalldate, waste_type.name, sum(weight) from pickup join waste_type on waste_type_id = waste_type.id group by smalldate, waste_type_id order by smalldate");
      }
      else{
        mResults = s.executeQuery("select smalldate, sum(weight) from pickup group by smalldate order by smalldate");
      }
      
      while (mResults.next()) {

        if (splitByWasteType) {
          String smalldate = mResults.getString(1);
          String wt = mResults.getString(2);
          double weight = mResults.getDouble(3);
          MonthType mT = new MonthType(smalldate, wt, weight);
          monthTypeModel.addInstance(mT);
        }

        else{
          String smalldate = mResults.getString(1);
          double weight = mResults.getDouble(2);
          Month m = new Month(smalldate, weight);
          monthModel.addInstance(m);
        }
      }

      
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }

    JTable table;
    if(splitByWasteType) { table= new JTable(monthTypeModel);}
    else {table= new JTable(monthModel);}
    return table;
  }

  private JTable showCompanies() {
    CompanyModel cm = new CompanyModel();
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
      Statement statement = conn.createStatement();
      ResultSet cResults = statement.executeQuery("select * from company");

      while(cResults.next()) {
        int id = cResults.getInt(1);
        String name = cResults.getString(2);
        String address = cResults.getString(3);
        String description = cResults.getString(4);

        Company c = new Company(id, name, address, description);
        cm.addInstance(c);
      }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    JTable table = new JTable(cm);
    return table;
  }

  private JTable showSites() {
    SiteModel sm = new SiteModel();
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
      Statement statement = conn.createStatement();
      ResultSet cResults = statement.executeQuery("select * from site");
      
      while(cResults.next()){
        int id = cResults.getInt(1);
        String name = cResults.getString(2);
        String address = cResults.getString(3);

        Site s = new Site(id, name, address);
        sm.addInstance(s);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    JTable table = new JTable(sm);
    return table;
  }

  private JTable showWaste_Types() {
    Waste_TypeModel wm = new Waste_TypeModel();
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
      Statement statement = conn.createStatement();
      ResultSet cResults = statement.executeQuery("select * from waste_type");

      while(cResults.next()) {
        int id = cResults.getInt(1);
        String name = cResults.getString(2);

        Waste_Type w = new Waste_Type(id, name);
        wm.addInstance(w);
      }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    JTable table = new JTable(wm);
    return table;
  }

  private void addCompany(String name, String address, String description) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
        PreparedStatement statement = conn.prepareStatement(
            "insert into Company (name, address, description) VALUES (?, ?, ?)"
        );

        statement.setString(1, name);
        statement.setString(2, address);
        statement.setString(3, description);
        statement.execute();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

  private void addSite(String name, String address) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
        PreparedStatement statement = conn.prepareStatement(
            "insert into Site (name, address) VALUES ( ?, ?)"
        );

        statement.setString(1, name);
        statement.setString(2, address);
        statement.execute();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

  private void addWaste_Type(String name) {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
        PreparedStatement statement = conn.prepareStatement(
            "insert into Waste_Type(name) VALUES (?)"
        );

        statement.setString(1, name);
        statement.execute();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

private void deletePickup(int id) {
  try (Connection conn = DriverManager.getConnection(DB_URL)) {
        
    PreparedStatement statement = conn.prepareStatement(
        "delete from pickup where id = "+ id
    );

    statement.execute();

  } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Unable to connect to the database. :(", "Database error!", JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
  }
}


private JTable displayMonths(boolean splitByWasteType, String startDate, String endDate) {
  MonthTypeModel monthTypeModel;
  MonthModel monthModel;

  monthTypeModel = new MonthTypeModel();
  monthModel = new MonthModel();

  try (Connection conn = DriverManager.getConnection(DB_URL)) {
    Statement s = conn.createStatement();
    ResultSet mResults;
    if(splitByWasteType) {
      mResults = s.executeQuery("select smalldate, waste_type.name, sum(weight) from pickup join waste_type on waste_type_id = waste_type.id where pickup.date >= '" + startDate + "' and pickup.date <= '" + endDate + "' group by smalldate, waste_type_id order by smalldate");
    }
    else{
      mResults = s.executeQuery("select smalldate, sum(weight) from pickup where pickup.date >= '" + startDate + "' and pickup.date <= '" + endDate + "' group by smalldate order by smalldate");
    }
    
    while (mResults.next()) {

      if (splitByWasteType) {
        String smalldate = mResults.getString(1);
        String wt = mResults.getString(2);
        double weight = mResults.getDouble(3);
        MonthType mT = new MonthType(smalldate, wt, weight);
        monthTypeModel.addInstance(mT);
      }

      else{
        String smalldate = mResults.getString(1);
        double weight = mResults.getDouble(2);
        Month m = new Month(smalldate, weight);
        monthModel.addInstance(m);
      }
    }

    
  } catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
  }

  JTable table;
  if(splitByWasteType) { table= new JTable(monthTypeModel);}
  else {table= new JTable(monthModel);}
  return table;
}


  private String monthfinder(String tempMonth){
   String month_num = "";  
        if (tempMonth.equals("Jan")){month_num = "01";}
        if (tempMonth.equals("Feb")){month_num = "02";} 
        if (tempMonth.equals("Mar")){month_num = "03";}  
        if (tempMonth.equals("Apr")){month_num = "04";}  
        if (tempMonth.equals("May")){month_num = "05";}
        if (tempMonth.equals("Jun")){month_num = "06";}
        if (tempMonth.equals("Jul")){month_num = "07";}
        if (tempMonth.equals("Aug")){month_num = "08";}  
        if (tempMonth.equals("Sep")){month_num = "09";}  
        if (tempMonth.equals("Oct")){month_num = "10";}  
        if (tempMonth.equals("Nov")){month_num = "11";}
        if (tempMonth.equals("Dec")){month_num = "12";} 
        return month_num; 
  }
  
  //SEPARATE CLASS
  private class ListenForButton implements ActionListener{
    public void actionPerformed(ActionEvent e) {
      //submit button
      if (e.getSource() == submit) {
        try {
          weight = Double.parseDouble(tf1.getText());
        }
        catch (NumberFormatException excep) {
          JOptionPane.showMessageDialog(waste.this, "input weight as a number", "Invalid Weight", JOptionPane.ERROR_MESSAGE);
          //System.exit(0);
        }
        if (weightType.getSelectedItem().toString().equals("tons")){
          weight *= 2000;
        }
        
        date = year.getValue().toString();
        //what.setText(date);  
        //get date data as integers
        String input, month, day, year, tempMonth; 
        input = date; 
        tempMonth = input.substring(4, 7);
        day = input.substring(8, 10); 
        year = input.substring(24, 28); 
        month = monthfinder(tempMonth); 
        dayValue = day;
        monthValue = month;
        yearValue = year;
        
        
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
          //company
          String cname = company.getSelectedItem().toString();
          PreparedStatement statement = conn.prepareStatement(
            "select id from company where name = " + "'"+cname+"'");
          ResultSet results = statement.executeQuery();
          while(results.next()) {
            companyId = results.getInt("id");
          }
          //System.out.println("cid: " + companyId);
          
          
          
          //site
          String sname = site.getSelectedItem().toString();
          statement = conn.prepareStatement(
            "select id from site where name = " + "'"+sname+"'");
          results = statement.executeQuery();
          while(results.next()) {
            siteId = results.getInt("id");
          }
          //System.out.println("sid: " + siteId);
          
          
          //wastetype
          String wname = waste_type.getSelectedItem().toString();
          statement = conn.prepareStatement(
            "select id from waste_type where name = " + "'"+wname+"'");
          results = statement.executeQuery();
          while(results.next()) {
            wasteTypeId = results.getInt("id");
          }
          //System.out.println("wid: " + wasteTypeId);
          
          String doot = yearValue+"-"+monthValue+"-"+dayValue;
          String littledoot = yearValue+monthValue;
          // PreparedStatement eyedee = conn.prepareStatement(
          //   "select id from pickup order by id desc limit 1"  
          // );
       
          // ResultSet i = eyedee.executeQuery();
          
          // int id = i.getInt("id");   
          // id++;
          
          statement = conn.prepareStatement(
            "insert into pickup (weight, date, site_id, waste_type_id, company_id, smalldate) values (" + weight + ", " + "'" +doot+ "'" + ", " + siteId + ", " + wasteTypeId +", " + companyId + ", '" +littledoot+ "')"
          );
          
          statement.execute();
          System.out.println("done");
          
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "An unexpected error has occured","ERROR", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }   
      }
      
      //generate pickup table button
      else if (e.getSource() == generate_pickup_table) {
        //start day data
        sdate = start_day.getValue().toString();
        String input1, month1, day1, year1, tempMonth1; 
        input1 = sdate; 
        tempMonth1 = input1.substring(4, 7);
        day1 = input1.substring(8, 10); 
        year1 = input1.substring(24, 28); 
        month1 = monthfinder(tempMonth1); 
        sday = day1;
        smonth = month1;
        syear = year1;
        
        //end day data
        edate = end_day.getValue().toString();
        String input2, month2, day2, year2, tempMonth2; 
        input2 = edate; 
        tempMonth2 = input2.substring(4, 7);
        day2 = input2.substring(8, 10); 
        year2 = input2.substring(24, 28); 
        month2 = monthfinder(tempMonth2); 
        eday = day2;
        emonth = month2;
        eyear = year2;
        
        String start = syear + "-" + smonth + "-" + sday; 
        String end = eyear + "-" + emonth + "-" + eday ;
         
        String ascendingOrDescending = upOrDown.getSelectedItem().toString();
        if (ascendingOrDescending.equals("Ascending Order")){
            ascdesc = false; 
        }
        else {
            ascdesc = true;  
        }
        
        String howToOrder = sortBy_pickup.getSelectedItem().toString(); 
        howToOrder = "pickup." + howToOrder; 
        
        JDialog anything = new JDialog();
        anything.setTitle("Pickups From " + start + " to " + end);
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = displayPickups(start, end, howToOrder, ascdesc);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
      }
      
      //aggregation table button
      else if (e.getSource() == generate_aggregation_table) {
        
        sdate = start_day.getValue().toString();
        String input1, month1, day1, year1, tempMonth1; 
        input1 = sdate; 
        tempMonth1 = input1.substring(4, 7);
        day1 = input1.substring(8, 10); 
        year1 = input1.substring(24, 28); 
        month1 = monthfinder(tempMonth1); 
        sday = day1;
        smonth = month1;
        syear = year1;
        
        //end day data
        edate = end_day.getValue().toString();
        String input2, month2, day2, year2, tempMonth2; 
        input2 = edate; 
        tempMonth2 = input2.substring(4, 7);
        day2 = input2.substring(8, 10); 
        year2 = input2.substring(24, 28); 
        month2 = monthfinder(tempMonth2); 
        eday = day2;
        emonth = month2;
        eyear = year2;
        
        String start = syear + "-" + smonth + "-" + sday; 
        String end = eyear + "-" + emonth + "-" + eday ;
         
        String ascendingOrDescending = upOrDown.getSelectedItem().toString();
        if (ascendingOrDescending.equals("Ascending Order")){
            ascdesc = false; 
        }
        else {
            ascdesc = true;  
        }
        
        
        JDialog anything = new JDialog();
        anything.setTitle("Aggregated Data from " + start + " to " + end);
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = displayAggro(start, end, "sum", ascdesc);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
      }
      else if (e.getSource() == c_insert) {
        JDialog ccc = new JDialog();
        ccc.setTitle("Companies");
        ccc.setSize(600,300);
        ccc.setLocationRelativeTo(null);
        ccc.setModal(true);
        JPanel ppp = new JPanel(new GridLayout(0,2));
        ppp.setBackground(Color.lightGray);
        
        JLabel lab1, lab2, lab3, lab4, lab5, labnull1, labnull2;
        lab1 = new JLabel("Company name");
        lab2 = new JLabel("Company address");
        lab3 = new JLabel("Company description");
        lab4 = new JLabel("Insert Company");
        lab5 = new JLabel("Show companies");
        labnull1 = new JLabel();
        labnull2 = new JLabel();
        lab1.setFont(font);
        lab2.setFont(font);
        lab3.setFont(font);
        lab4.setFont(font);
        lab5.setFont(font);
        tf2 = new JTextField("", 10);
        tf3 = new JTextField("", 10);
        tf4 = new JTextField("", 10);
        tf2.setFont(font);
        tf3.setFont(font);
        tf4.setFont(font);
        ppp.add(lab1);
        ppp.add(tf2);
        ppp.add(lab2);
        ppp.add(tf3);
        ppp.add(lab3);
        ppp.add(tf4);
        ppp.add(lab4);
        insert_C = new JButton("insert");
        insert_C.addActionListener(lfb);
        insert_C.setFont(font);
        ppp.add(insert_C);
        ppp.add(labnull1);
        ppp.add(labnull2);
        ppp.add(lab5);
        show_C = new JButton("show");
        show_C.addActionListener(lfb);
        show_C.setFont(font);
        ppp.add(show_C);
        ccc.add(ppp);
        
        ccc.setVisible(true);
        
      }
      else if (e.getSource() == s_insert) {
        JDialog ccc = new JDialog();
        ccc.setTitle("Sites");
        ccc.setSize(600,300);
        ccc.setLocationRelativeTo(null);
        ccc.setModal(true);
        JPanel ppp = new JPanel(new GridLayout(0,2));
        ppp.setBackground(Color.lightGray);
        
        JLabel lab1, lab2, lab3, lab4, lab5, labnull1, labnull2;
        lab1 = new JLabel("Site name");
        lab2 = new JLabel("Site address");
        lab3 = new JLabel("Insert site");
        lab5 = new JLabel("Show sites");
        labnull1 = new JLabel();
        labnull2 = new JLabel();
        lab1.setFont(font);
        lab2.setFont(font);
        lab3.setFont(font);
        lab5.setFont(font);
        tf2 = new JTextField("", 10);
        tf3 = new JTextField("", 10);
        tf2.setFont(font);
        tf3.setFont(font);
        ppp.add(lab1);
        ppp.add(tf2);
        ppp.add(lab2);
        ppp.add(tf3);
        ppp.add(lab3);
        insert_S = new JButton("insert");
        insert_S.addActionListener(lfb);
        insert_S.setFont(font);
        ppp.add(insert_S);
        ppp.add(labnull1);
        ppp.add(labnull2);
        ppp.add(lab5);
        show_S = new JButton("show");
        show_S.addActionListener(lfb);
        show_S.setFont(font);
        ppp.add(show_S);
        ccc.add(ppp);
        
        ccc.setVisible(true);
        
      }
      else if (e.getSource() == w_insert) {
        JDialog ccc = new JDialog();
        ccc.setTitle("Waste Types");
        ccc.setSize(600,300);
        ccc.setLocationRelativeTo(null);
        ccc.setModal(true);
        JPanel ppp = new JPanel(new GridLayout(0,2));
        ppp.setBackground(Color.lightGray);
        
        JLabel lab1, lab2, lab3, lab4, lab5, labnull1, labnull2;
        lab1 = new JLabel("Waste type name");
        lab3 = new JLabel("Insert waste type");
        lab5 = new JLabel("Show waste type");
        labnull1 = new JLabel();
        labnull2 = new JLabel();
        lab1.setFont(font);
        lab3.setFont(font);
        lab5.setFont(font);
        tf2 = new JTextField("", 10);
        tf2.setFont(font);
        ppp.add(lab1);
        ppp.add(tf2);
        ppp.add(lab3);
        insert_W = new JButton("insert");
        insert_W.addActionListener(lfb);
        insert_W.setFont(font);
        ppp.add(insert_W);
        ppp.add(labnull1);
        ppp.add(labnull2);
        ppp.add(lab5);
        show_W = new JButton("show");
        show_W.addActionListener(lfb);
        show_W.setFont(font);
        ppp.add(show_W);
        ccc.add(ppp);
        
        ccc.setVisible(true);
        
        
      }
      else if (e.getSource() == insert_C){
        int ask = JOptionPane.showConfirmDialog(waste.this, "Are you sure?");
        if (ask == JOptionPane.YES_OPTION){
          JOptionPane.showMessageDialog(waste.this, "Company added! Please restart the program to see changes");
          //get tf2,3,4
          String name, desc, addr;
          name = tf2.getText();
          addr = tf3.getText(); 
          desc = tf4.getText(); 
          addCompany(name,addr,desc); 
          
        }
      }
      else if (e.getSource() == insert_S){
        int ask = JOptionPane.showConfirmDialog(waste.this, "Are you sure?");
        if (ask == JOptionPane.YES_OPTION){
          JOptionPane.showMessageDialog(waste.this, "Site Added! Please restart the program to see changes");
          //tf2,3
          String name, addr;
          name = tf2.getText();
          addr = tf3.getText(); 
          addSite(name,addr);
        }
      }
      else if (e.getSource() == insert_W){
        int ask = JOptionPane.showConfirmDialog(waste.this, "Are you sure?");
        if (ask == JOptionPane.YES_OPTION){
          JOptionPane.showMessageDialog(waste.this, "Waste Type Added! Please restart the program to see changes");
          String name = tf2.getText();
          addWaste_Type(name);
        }
      }
      
      else if (e.getSource() == show_C){
        JDialog anything = new JDialog();
        anything.setTitle("All Companies");
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = showCompanies();
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
      }
      
      else if (e.getSource() == show_S){
        JDialog anything = new JDialog();
        anything.setTitle("All Pickup Sites");
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = showSites();
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
      }
      
      else if (e.getSource() == show_W){
        JDialog anything = new JDialog();
        anything.setTitle("All Waste Types");
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = showWaste_Types();
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
      }
      
      else if (e.getSource() == allPickups){
        JDialog anything = new JDialog();
        anything.setTitle("All Pickups");
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        boolean sort = false; 
        String yesNo = sortIdDate.getSelectedItem().toString();
        if (yesNo.equals("Yes")){
         sort = true;
        }
 
        JTable table = displayPickups(sort);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
        
      }
      else if (e.getSource() == dataByMonth){
        JDialog anything = new JDialog();
        anything.setTitle("All Waste by Month");
        anything.setSize(1200, 1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        boolean sort = true;
        String sortString = wt_sort.getSelectedItem().toString();
        if (sortString.equals("Yes")){
          sort = true;
        }
        else{
          sort = false; 
            }
        
        JTable table = displayMonths(sort);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
        
      }
       else if (e.getSource() == dmon){
        
        sdate = start_day.getValue().toString();
        String input1, month1, day1, year1, tempMonth1; 
        input1 = sdate; 
        tempMonth1 = input1.substring(4, 7);
        day1 = input1.substring(8, 10); 
        year1 = input1.substring(24, 28); 
        month1 = monthfinder(tempMonth1); 
        sday = day1;
        smonth = month1;
        syear = year1;
        
        //end day data
        edate = end_day.getValue().toString();
        String input2, month2, day2, year2, tempMonth2; 
        input2 = edate; 
        tempMonth2 = input2.substring(4, 7);
        day2 = input2.substring(8, 10); 
        year2 = input2.substring(24, 28); 
        month2 = monthfinder(tempMonth2); 
        eday = day2;
        emonth = month2;
        eyear = year2;
        
        String start = syear + "-" + smonth + "-" + sday; 
        String end = eyear + "-" + emonth + "-" + eday ;
        
        boolean sort = true;
        String sortString = group.getSelectedItem().toString();
        if (sortString.equals("Yes")){
          sort = true;
        }
        else{
          sort = false; 
            }
 
        JDialog anything = new JDialog();
        anything.setTitle("Monthly Data from " + start + " to " + end);
        anything.setSize(1200,1000);
        anything.setModal(true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setBackground(Color.lightGray);
        
        JTable table = displayMonths(sort, start, end);
        pp.add(new JScrollPane(table));
        anything.add(pp);
        anything.setVisible(true);
         
       }
       else if (e.getSource() == delete){
         int aa = 0;
         try {
          aa = Integer.parseInt(idbox.getText());
        }
        catch (NumberFormatException excep) {
          JOptionPane.showMessageDialog(waste.this, "input id as an integer number", "Invalid ID", JOptionPane.ERROR_MESSAGE);
        }
         deletePickup(aa);
         
       }
       
    }
  }
