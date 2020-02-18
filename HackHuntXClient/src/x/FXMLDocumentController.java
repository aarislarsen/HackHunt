/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x;

import control.AdvisoryHandler;
import control.ExploitHandler;
import control.LoginHandler;
import control.PlayerHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.ActivePlayersGetter;
import model.AdvisoriesGetter;
import model.AnnouncementsGetter;
import model.ExploitStatusGetter;
import model.HackHuntClass;
import model.RankingsGetter;
import model.ScoreGetter;
import model.ServicesGetter;
import model.VulnerabilitiesGetter;
import static x.X.token;

/**
 *
 * @author Dragon
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private AnchorPane  menu_ap,    advisories_ap,      advisory_ap,                services_ap,        exploits_ap,        attack_ap,      score_ap,       activeplayers_ap,       rankings_ap,        anouncements_ap,        spacer_ap,    login_ap,   create_ap;
    @FXML private ImageView               advisories_button,  advisory_details_button,    services_button,    exploits_button,    attack_button,  score_button,   active_players_button,  rankings_button,    anouncements_button,    exit_button,              second_tip_button, attack_rescan,attack_attack,attack_attack_green;  
    @FXML private TableView<AdvisoryListRowItem> advisories_tableview;
    @FXML private TableColumn ID,Title,Difficulty,CVE,Status,Username,Teamname,Rank,Rank_Teamname,Prestige,service_team,service_crypto,service_file,service_storage,service_communication,service_web,attack_unpatched_vulns_column,attack_vulnerable_members_column;
    @FXML private TableView<ExploitsListRowItem> exploits_tableview;
    @FXML private TableView<ActivePlayersListRowItem> activeplayers_tableview;
    @FXML private TableView<RankingListRowItem> rankings_tableview;
    @FXML private TableView<ServicesRowItem> services_tableview;
    @FXML private TableView<SingleStringRowItem> attack_unpatched_vulns,attack_vulnerable_members;
    @FXML private Hyperlink loginRegisterLink, login_link, create_link;
    @FXML private Label score_team_credit,score_team_prestige,score_individual_credit,score_individual_prestige,advisory_id,advisory_affected_service,advisory_difficulty,advisory_title,exploits_cve,attack_target_team,attack_target_service,attack_target_vuln,attack_target_member;
    @FXML private Button submit_button;
    @FXML private TextField answerField, login_username, login_password, create_username, create_password, create_teamname,attack_statusfield;
    @FXML private TextArea announcements_textarea,advisory_description,advisory_challenge,advisory_tip,advisory_second_tip;
    @FXML private MenuButton exploits_generate_button;
    private long delay = 0;
    private long interval = 1;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private AdvisoriesGetter advisoriesGetter;
    private int currentAdvisory;
    private String currentCVE;
    private String selectedTeam = "";
    private String selectedService = "";
    private String selectedMember = "";
    private String selectedExploit = "";
    private String loggedinuser = "";
    
            
    /**
     * Handles mouse events from clickable ImageViews
     * @param event the click event
     */
    @FXML
    private void handleImageViewClickAction(MouseEvent event) 
    {
        if(event.getTarget() == advisories_button)
        {
            hideAllBut(advisories_ap);
        }
        else if(event.getTarget() == advisory_details_button)
        {
            hideAllBut(advisory_ap);
        }
        else if(event.getTarget() == services_button)
        {
            hideAllBut(services_ap);
        }
        else if(event.getTarget() == exploits_button)
        {
            hideAllBut(exploits_ap);
        }
        else if(event.getTarget() == attack_button)
        {
            hideAllBut(attack_ap);
        }
        else if(event.getTarget() == score_button)
        {
            hideAllBut(score_ap);
        }
        else if(event.getTarget() == active_players_button)
        {
            hideAllBut(activeplayers_ap);
        }
        else if(event.getTarget() == rankings_button)
        {
            hideAllBut(rankings_ap);
        }
        else if(event.getTarget() == anouncements_button)
        {
            hideAllBut(anouncements_ap);
        }
        else if(event.getTarget() == exit_button)
        {
            doLogout();
        }        
        else if(event.getTarget() == second_tip_button)
        {
            getSecondHint();
        }  
    }
    
    /**
     * Sets all AnchorPanes to not visible, except the specified pane and the spacer pane
     * @param pane the pane to display
     */
    private void hideAllBut(AnchorPane pane)
    {
        advisories_ap.setVisible(false);
        advisory_ap.setVisible(false);
        services_ap.setVisible(false);
        exploits_ap.setVisible(false);
        attack_ap.setVisible(false);
        score_ap.setVisible(false);
        activeplayers_ap.setVisible(false);
        rankings_ap.setVisible(false);
        anouncements_ap.setVisible(false); 
        create_ap.setVisible(false);
        pane.setVisible(true);
        spacer_ap.setVisible(true);
    }
    
    /**
     * the display of panels to use when launching the application
     */
    private void startingSetup()
    {
        advisories_ap.setVisible(false);
        advisory_ap.setVisible(false);
        services_ap.setVisible(false);
        exploits_ap.setVisible(false);
        attack_ap.setVisible(false);
        score_ap.setVisible(false);
        activeplayers_ap.setVisible(false);
        rankings_ap.setVisible(false);
        anouncements_ap.setVisible(false);        
        spacer_ap.setVisible(false);
        menu_ap.setVisible(false);
        create_ap.setVisible(false);
        login_ap.setVisible(true);
    }
    
    /**
     * updates the UI with the user's current score
     * @param individualCredit
     * @param individualPrestige
     * @param teamCredit
     * @param teamPrestige 
     */
    public void displayScore(int individualCredit, int individualPrestige, int teamCredit, int teamPrestige)
    {
        score_individual_credit.setText(""+individualCredit);
        score_individual_prestige.setText(""+individualPrestige);
        score_team_credit.setText(""+teamCredit);
        score_team_prestige.setText(""+teamPrestige);
    }

    private void doLogin()
    {
        LoginHandler loginHandler = new LoginHandler();
        String returnmessage = loginHandler.login(login_username.getText(), login_password.getText().toCharArray());
        String code = returnmessage.split(":")[0];
        if(code.equalsIgnoreCase("SUCCESS"))
        {            
            token = returnmessage.split(":")[1].split(";")[0].trim();
            String message = returnmessage.split(";")[1];
            if(token.length() > 0)
            {
                login_ap.setVisible(false);
                menu_ap.setVisible(true);
                loggedinuser = login_username.getText();
                startGetters();
            }
            else
            {
                //TODO show error, this should never occour
            }
        }
        else
        {
            login_username.setText("login failed: " + returnmessage);
            login_password.setText("");
        }        
    }
    
    private void doLogout()
    {
        PlayerHandler playerHandler = new PlayerHandler();
        String returnmessage = playerHandler.logoutPlayer(token);
        String code = returnmessage.split(":")[0];
        System.out.println(code);
        System.exit(0);
    }
    
    private void createPlayer()
    {
        PlayerHandler playerHandler = new PlayerHandler();
        String response = playerHandler.addPlayer(create_username.getText(), create_password.getText().toCharArray(), create_teamname.getText());
        if(response.split(":")[0].equalsIgnoreCase("SUCCESS"))
        {
            startingSetup();
            loginRegisterLink.setText("User created, please login");
            loginRegisterLink.setVisited(false);
        }
        else
        {
            create_link.setText(response.split(": ")[1] + ". Try again and click here");
            create_link.setVisited(false);
        }        
    }
    
    private void startGetters()
    {
        AnnouncementsGetter announcementsGetter = new AnnouncementsGetter(token);  
        announcementsGetter.configureForTextArea(announcements_textarea);
        ScheduledFuture announcementSchedule = scheduler.scheduleAtFixedRate(announcementsGetter, delay, interval, TimeUnit.SECONDS);
        
        ActivePlayersGetter activePlayersGetter = new ActivePlayersGetter(token,this);
        activePlayersGetter.configureForTable(activeplayers_tableview);
        ScheduledFuture activePlayersSchedule = scheduler.scheduleAtFixedRate(activePlayersGetter, delay, interval, TimeUnit.SECONDS);
        
        RankingsGetter rankingsGetter = new RankingsGetter(token,this);
        rankingsGetter.configureForTable(rankings_tableview);
        ScheduledFuture rankingsSchedule = scheduler.scheduleAtFixedRate(rankingsGetter, delay, interval, TimeUnit.SECONDS);
        
        ScoreGetter scoreGetter = new ScoreGetter(token,this);
        scoreGetter.configureForLabels(score_individual_prestige, score_team_prestige);
        ScheduledFuture scoreSchedule = scheduler.scheduleAtFixedRate(scoreGetter, delay, interval, TimeUnit.SECONDS);
        
        advisoriesGetter = new AdvisoriesGetter(token,this);
        advisoriesGetter.configureForTable(advisories_tableview);
        ScheduledFuture advisoriesSchedule = scheduler.scheduleAtFixedRate(advisoriesGetter, delay, interval, TimeUnit.SECONDS);
        
        ExploitStatusGetter statusGetter = new ExploitStatusGetter(token,this);
        statusGetter.configureForTable(exploits_tableview);        
        ScheduledFuture statusSchedule = scheduler.scheduleAtFixedRate(statusGetter, delay, interval, TimeUnit.SECONDS);
        
        ServicesGetter servicesGetter = new ServicesGetter(token,this);
        ScheduledFuture servicesSchedule = scheduler.scheduleAtFixedRate(servicesGetter, delay, interval, TimeUnit.SECONDS);
    }
    
    public void getSecondHint()
    {
        AdvisoryHandler advisoryHandler = new AdvisoryHandler();        
        String secondHint = advisoryHandler.getSecondHint(token, ""+currentAdvisory);
        advisory_second_tip.setText(secondHint);
    }
    
    public void populateAdvisoriesTable(String[] entries)
    {          
        HackHuntClass hhc = new HackHuntClass();
        advisories_tableview.getItems().clear();
        for(String s : entries)
        {                    
            String[] fields = hhc.decode(s).split(";");
            AdvisoryListRowItem i = new AdvisoryListRowItem(hhc.decode(fields[0]),hhc.decode(fields[1]),hhc.decode(fields[2]));
            advisories_tableview.getItems().add(i);
        }
    }
    
    public void populateAdvisory()
    {
        if(advisories_tableview.getSelectionModel().getSelectedItem() != null)
        {
            currentAdvisory = Integer.parseInt(advisories_tableview.getSelectionModel().getSelectedItem().getID());
            String text = advisoriesGetter.getCurrentAdvisory(currentAdvisory);
            String[] lines = text.split("##\r\n");

            advisory_id.setText("ID: "+lines[0]);
            advisory_title.setText("Title: "+lines[1]);
            advisory_difficulty.setText("Difficulty: " +lines[2]);;
            advisory_affected_service.setText("Affected service: "+lines[3]);
            advisory_description.setText(lines[4]+"\r\n\r\n");
            advisory_description.appendText(lines[5]+"\r\n\r\n"+lines[6]);
            advisory_description.appendText("\r\n\r\n"+lines[7]);
            advisory_second_tip.setText("");
            answerField.setText("");
            advisory_description.home();
            hideAllBut(advisory_ap);
        }
        else
        {
            //do nothing, they didn't click right
        }
    }
    
    public void pickCVE()
    {
        if(exploits_tableview.getSelectionModel().getSelectedItem() != null)
        {
            currentCVE = exploits_tableview.getSelectionModel().getSelectedItem().getCVE();
            exploits_cve.setText("Advisory: " + currentCVE);
        }
        else
        {
            //do nothing, they didn't click right
        }
    }
    
    public void answerAdvisory()
    {
        AdvisoryHandler advisoryHandler = new AdvisoryHandler();
        String answer = advisoryHandler.answerAdvisory(token, ""+currentAdvisory,answerField.getText());
        System.out.println(answer);
        System.out.println(answerField.getText());
        if(answer.contains("SUCCESS: "))
        {
            answerField.setText("Correct!");
        }
        else
        {
            answerField.setText("Sorry, that is incorrect!");
        }
    }
    
    public void populateExploitsTable(String[] entries)
    {
        exploits_tableview.getItems().clear();        
        for(String s : entries)
        {
            ExploitsListRowItem i = new ExploitsListRowItem(s.split(",")[0], s.split(",")[2]+"/"+s.split(",")[1]);
            exploits_tableview.getItems().add(i);
        }
        
    }
    
    public void populateActivePlayersTable(String[] entries)
    {
        activeplayers_tableview.getItems().clear();
        for(String entry : entries)
        {
            String user = entry.split(",")[0];
            String team = entry.split(",")[1];
            //System.out.println(entry);
            ActivePlayersListRowItem i = new ActivePlayersListRowItem(user, team);
            activeplayers_tableview.getItems().add(i);        
        }   
    }
    
    public void populateRankingsTable(String[] entries)
    {
        rankings_tableview.getItems().clear();
        for(int i = 0 ; i < entries.length ; i++)
        {
            int place = i+1;
            String user = entries[i].split(",")[1];
            String prestige = entries[i].split(",")[0];
            RankingListRowItem r = new RankingListRowItem(""+place, user,prestige);
            rankings_tableview.getItems().add(r);
        }
    }    
    
    public void populateServicesTable(String[] entries)
    {
        
        services_tableview.getItems().clear();
        for(String s : entries)
        {
            String[] entry = s.split(",");
            
            String crypto = entry[3];
            String file = entry[1];
            String storage = entry[5];
            String communication = entry[2];
            String web = entry[4];
            
            if(crypto.equalsIgnoreCase("1")){crypto = "Enabled";} else {crypto = "Disabled";}
            if(file.equalsIgnoreCase("1")){file = "Enabled";} else {file = "Disabled";}
            if(storage.equalsIgnoreCase("1")){storage = "Enabled";} else {storage = "Disabled";}
            if(communication.equalsIgnoreCase("1")){communication = "Enabled";} else {communication = "Disabled";}
            if(web.equalsIgnoreCase("1")){web = "Enabled";} else {web = "Disabled";}
            ServicesRowItem i = new ServicesRowItem(entry[0], crypto, file, storage, communication, web);
            services_tableview.getItems().add(i);
        }        
    }
    
    public void populateUnpatchedVulnsTable(String[] vulns)
    {
        attack_unpatched_vulns.getItems().clear();
        for(String s : vulns)
        {
            SingleStringRowItem i = new SingleStringRowItem(s);
            attack_unpatched_vulns.getItems().add(i);
        }
    }
    
    public void createPatchOrExploit(boolean patchorexploit)
    {
        if(currentCVE != null)
        {
            ExploitHandler exploitHandler = new ExploitHandler();
            if(patchorexploit)
            {
                //patch
                String response = exploitHandler.generatePatch(token, currentCVE);
                exploits_cve.setText(response);
            }
            else
            {
                //exploit
                String response = exploitHandler.generateExploit(token, currentCVE);
                exploits_cve.setText(response);
            }
            
            
        }
        else
        {
            //do nothing, they didn't pick anything
        }
        
    }
    
    public void populateAttack()
    {
        selectedExploit = "";
        selectedMember  ="";
        selectedService = "";
        selectedTeam = "";
        attack_statusfield.setText("");
        attack_target_member.setText("");
        attack_target_service.setText("");
        attack_target_team.setText("");
        attack_target_vuln.setText("");
        attack_unpatched_vulns.getItems().clear();
        attack_vulnerable_members.getItems().clear();
        attack_attack.setVisible(true);
        attack_attack_green.setVisible(false);
                
        if(services_tableview.getSelectionModel().getSelectedItem() != null)
        {
            String team = services_tableview.getSelectionModel().getSelectedItem().getTeam();
            selectedTeam = team;
            int column = services_tableview.getSelectionModel().getSelectedCells().get(0).getColumn();
            String service = "";
            switch(column)
            {
                case 1:
                    service = "Cryptoservice";
                    selectedService = service;
                    attack_target_team.setText(team);
                    attack_target_service.setText(service);
                    hideAllBut(attack_ap);
                    break;
                case 2:
                    service = "Fileservice";
                    selectedService = service;
                    attack_target_team.setText(team);
                    attack_target_service.setText(service);
                    hideAllBut(attack_ap);
                    break;
                case 3:
                    service = "Storageservice";
                    selectedService = service;
                    attack_target_team.setText(team);
                    attack_target_service.setText(service);
                    hideAllBut(attack_ap);
                    break;
                case 4:
                    service = "Communicationservice";
                    selectedService = service;
                    attack_target_team.setText(team);
                    attack_target_service.setText(service);
                    hideAllBut(attack_ap);
                    break;
                case 5:
                    service = "Webservice";
                    selectedService = service;
                    attack_target_team.setText(team);
                    attack_target_service.setText(service);
                    hideAllBut(attack_ap);
                    break;                    
            }
            
            String[] vulns = getUnpatchedVulns(team, service);
            populateUnpatchedVulnsTable(vulns);            
        }   
    }                
    
    public String[] getUnpatchedVulns(String teamname, String servicename)
    {
        VulnerabilitiesGetter getter = new VulnerabilitiesGetter(token, teamname,servicename); //TODO this should really be in a handler class
        String[] vulns = getter.getVulnerabilities().split(",");
        return vulns;
    }
    
//    public void isExploitAvailable(String vulnerability)
//    {
//        ExploitHandler handler = new ExploitHandler();
//        System.out.println(handler.getAvailableExploitsForService(token, "test",vulnerability));
//        
//    }
    
    public void selectVuln()
    {
        if(attack_unpatched_vulns.getSelectionModel().getSelectedItem() != null)
        {
            String vuln = attack_unpatched_vulns.getSelectionModel().getSelectedItem().getValue();
            selectedExploit = vuln;
            attack_target_vuln.setText(vuln);
            String vulnMembers = getVulnerableTeamMembers(vuln);
            populateVulnerableTeamMembers(vulnMembers);
            ExploitHandler handler = new ExploitHandler();
            if(handler.getAvailableExploitsForService(token, loggedinuser, selectedExploit).equalsIgnoreCase("SUCCESS: true"))
            {                    
                attack_attack.setVisible(false);
                attack_attack_green.setVisible(true);
            }
            else
            {
                attack_attack.setVisible(true);
                attack_attack_green.setVisible(false);           
            }       
        }
    }
    
    public void selectMember()
    {
        if(attack_vulnerable_members.getSelectionModel().getSelectedItem() != null)
        {
            selectedMember = attack_vulnerable_members.getSelectionModel().getSelectedItem().getValue();
            attack_target_member.setText(selectedMember);
        }
    }
    
    public void populateVulnerableTeamMembers(String vulnerableMembers)
    {
        attack_vulnerable_members.getItems().clear();
        String[] members = vulnerableMembers.split(";");
        for (String member : members) 
        {
            SingleStringRowItem i = new SingleStringRowItem(member);
            attack_vulnerable_members.getItems().add(i);
        }
        
    }
    
    public String getVulnerableTeamMembers(String vuln)
    {
        VulnerabilitiesGetter getter = new VulnerabilitiesGetter(token, selectedTeam, selectedService); //TODO this should really be in a handler class
        return getter.getVulnerableTeamMembers(vuln);
    }
    
    public void attack()
    {
        ExploitHandler handler = new ExploitHandler();
        String response = handler.attack(token, selectedExploit, selectedMember);
        attack_statusfield.setText(response);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //Advisories table setup
        ID.setCellValueFactory(new PropertyValueFactory("ID"));
        Title.setCellValueFactory(new PropertyValueFactory("Title"));
        Difficulty.setCellValueFactory(new PropertyValueFactory("Difficulty"));
        
        //Exploits table setup
        CVE.setCellValueFactory(new PropertyValueFactory("CVE"));
        Status.setCellValueFactory(new PropertyValueFactory("Status"));
        
        //Active players table setup
        Username.setCellValueFactory(new PropertyValueFactory("Username"));
        Teamname.setCellValueFactory(new PropertyValueFactory("Teamname"));
        
        //Rankings table setup
        Rank.setCellValueFactory(new PropertyValueFactory("Rank"));
        Rank_Teamname.setCellValueFactory(new PropertyValueFactory("Teamname"));
        Prestige.setCellValueFactory(new PropertyValueFactory("Prestige"));
        
        //Services table setup
        service_team.setCellValueFactory(new PropertyValueFactory("Team"));
        service_crypto.setCellValueFactory(new PropertyValueFactory("Crypto"));
        service_file.setCellValueFactory(new PropertyValueFactory("File"));
        service_storage.setCellValueFactory(new PropertyValueFactory("Storage"));
        service_communication.setCellValueFactory(new PropertyValueFactory("Communication"));
        service_web.setCellValueFactory(new PropertyValueFactory("Web"));
        
        //attack tables setup
        attack_unpatched_vulns_column.setCellValueFactory(new PropertyValueFactory("Value"));
        attack_vulnerable_members_column.setCellValueFactory(new PropertyValueFactory("Value"));
        
        //Setting up MenuButtons
        exploits_generate_button.getItems().get(0).setText("Exploit");
        exploits_generate_button.getItems().get(1).setText("Patch");
        
        //Setting tooltips for imageview buttons
        Tooltip.install(advisories_button, new Tooltip("Advisories"));
        Tooltip.install(advisory_details_button, new Tooltip("Advisory details"));
        Tooltip.install(services_button, new Tooltip("Your services"));
        Tooltip.install(exploits_button, new Tooltip("Exploits"));
        Tooltip.install(attack_button, new Tooltip("Attack"));
        Tooltip.install(score_button, new Tooltip("Score"));
        Tooltip.install(active_players_button, new Tooltip("Active players"));
        Tooltip.install(rankings_button, new Tooltip("Rankings"));
        Tooltip.install(anouncements_button, new Tooltip("Announcements"));
        Tooltip.install(exit_button, new Tooltip("Exit"));
        Tooltip.install(second_tip_button, new Tooltip("Get second hint"));
        
        //buttons and stuff. action listeners
        loginRegisterLink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                hideAllBut(create_ap);
                spacer_ap.setVisible(false);
            }
        });
        
        login_link.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                doLogin();
            }
        });

        login_password.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                doLogin();
            }
        });        
        
        create_link.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                createPlayer();
            }
        });        
        
        exploits_generate_button.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                createPatchOrExploit(false);
            }
        });
        
        exploits_generate_button.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                createPatchOrExploit(true);
            }
        });        
                
        startingSetup();

        
    }    
    
}
