/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import control.AdvisoryHandler;
import control.PlayerHandler;
import control.SQLiteConnectionHandler;
import control.TimedActivePlayersHandler;
import control.TimedActiveTeamsHandler;
import control.TimedAdvisoryReleaserHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.AdvisoriesGetter;
import model.Advisory;
import model.AnnouncementsGetter;
import model.AnswersGetter;
import model.PlayersGetter;
import model.Protocol;
import model.ScoreSystem;
import model.ScoresGetter;
import model.Server;
import model.ServiceMonitor;

/**
 *
 * @author Dragon
 */
public class FXMLDocumentController implements Initializable 
{
    private final String logname = "HackHunt Log";
    private final Logger log = Logger.getLogger(logname);
    private Level INFO = Level.INFO;
    private Level DEBUG = Level.FINE; 
    private Level WARNING = Level.WARNING; 
    private final String propertiesURL = "properties.properties";
    private final Properties properties = new Properties();    
    private final TimedAdvisoryReleaserHandler timedAdvisoryReleaserHandler = new TimedAdvisoryReleaserHandler();
    private final TimedActiveTeamsHandler timedActiveTeamsHandler = new TimedActiveTeamsHandler();
    private final TimedActivePlayersHandler timedActivePlayersHandler = new TimedActivePlayersHandler();      
    private boolean sendingAdvisories = false;
    private long delay = 0;
    private long interval = 1;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private PlayerHandler playerHandler = new PlayerHandler();
    private AdvisoryHandler advisoryHandler = new AdvisoryHandler();
    @FXML private Hyperlink start_sending_hyperlink,reset_advisories_hyperlink,terminate_sessions_hyperlink,remove_all_answers_hyperlink,reset_all_scores_hyperlink,remove_all_announcements_hyperlink,delete_all_players_hyperlink,delete_all_scores_hyperlink,reset_all_services_hyperlink,remove_all_services_hyperlink,remove_all_exploits_and_patches_hyperlink,edit_players_hyperlink,delete_selected_players_hyperlink,save_players_hyperlink,terminate_players_session_hyperlink,edit_advisory_hyperlink,save_advisory_hyperlink, new_advisory_hyperlink,scores_adjust_hyperlink,make_announcements_hyperlink;
    @FXML private TextField status_textfield,players_edit_name_textfield,players_edit_team_textfield,players_edit_password_textfield,advisory_id,advisory_title,advisory_difficulty,advisory_service,advisory_uri,advisory_answer,advisory_vulnpart,scores_prestige_textfield,scores_credit_textfield;
    @FXML private TextArea advisory_description,advisory_challenge,advisory_hint,advisory_second_hint,announcements_textarea,old_announcements_textarea;
    @FXML private TableView<PlayersListRowItem> players_tableview;
    @FXML private TableView<AdvisoryListRowItem> advisories_tableview;
    @FXML private TableView<AnswerListRowItem> answers_tableview;
    @FXML private TableView<ScoreListRowItem> scores_tableview;
    @FXML private TableColumn players_name, players_team, players_password,players_active,advisories_id,advisories_title,advisories_released,advisories_answered,answers_username,answers_advisoryid,answers_answer,answers_correct,scores_username,scores_prestige,scores_credit;
    @FXML private ImageView  advisories_button,scores_button,announcements_button,answers_button,services_button,exploits_button, players_button;
    @FXML private AnchorPane advisories_ap, answers_ap,scores_ap,announcements_ap,services_ap,exploits_ap, spacer_ap, menu_ap, players_ap;
    @FXML private Label scores_username_label;
    private String selectedUsername = "";
    private String selectedAdvisory = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {      
        initProperties();
        configureLogLevels();
        Server server = Server.getInstance(new Protocol(),Integer.parseInt(properties.getProperty("ServerPort")),properties.getProperty("ServerIP"));
        Thread serverThread = new Thread(server);
        serverThread.setDaemon(true);
        server.startServer();
        serverThread.start(); //TODO make it so the server can be started after the GUI loads
        Thread serviceMonitorThread = new Thread(ServiceMonitor.getInstance());
        serviceMonitorThread.start();  

        //players table setup
        players_name.setCellValueFactory(new PropertyValueFactory("Username"));
        players_team.setCellValueFactory(new PropertyValueFactory("Teamname"));
        players_password.setCellValueFactory(new PropertyValueFactory("Password"));
        players_active.setCellValueFactory(new PropertyValueFactory("Active"));
        
        //advisories table setup
        advisories_id.setCellValueFactory(new PropertyValueFactory("ID"));
        advisories_title.setCellValueFactory(new PropertyValueFactory("Title"));
        advisories_released.setCellValueFactory(new PropertyValueFactory("Released"));
        advisories_answered.setCellValueFactory(new PropertyValueFactory("Answered"));

        //answers table setup
        answers_username.setCellValueFactory(new PropertyValueFactory("Username"));
        answers_advisoryid.setCellValueFactory(new PropertyValueFactory("AdvisoryID"));
        answers_answer.setCellValueFactory(new PropertyValueFactory("Answer"));
        answers_correct.setCellValueFactory(new PropertyValueFactory("Correct"));
        
        //scores table setup
        scores_username.setCellValueFactory(new PropertyValueFactory("Username"));
        scores_prestige.setCellValueFactory(new PropertyValueFactory("Prestige"));
        scores_credit.setCellValueFactory(new PropertyValueFactory("Credit"));
        
        start_sending_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent e) 
            {
                startStopSendingAdvisories();
            }
        });
        
        reset_advisories_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                resetAdvisories();
            }
        });
        
        terminate_sessions_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                terminateAllSessions();
            }
        });
        
        remove_all_answers_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                removeAllAnswers();
            }
        });
        
        reset_all_scores_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                resetAllScores();
            }
        });

        remove_all_announcements_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                removeAllAnnouncements();
            }
        });
        
        delete_all_players_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                deleteAllPlayers();
            }
        });
        
        delete_all_scores_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                deleteAllFromScores();
            }
        });
        
        reset_all_services_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                resetAllServices();
            }
        });
        
        remove_all_services_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                deleteAllFromServices();
            }
        });
        
        remove_all_exploits_and_patches_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                removeAllExploitsAndPatches();
            }
        });
                
        terminate_players_session_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(players_edit_name_textfield.getText().length() < 1)
                {
                    //do nothing, no user selected
                }
                else
                {
                    terminateSession(players_edit_name_textfield.getText());
                    players_edit_name_textfield.setText("");
                    players_edit_password_textfield.setText("");
                    players_edit_team_textfield.setText("");
                }                
            }
        });
        
        delete_selected_players_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(players_edit_name_textfield.getText().length() < 1)
                {
                    //do nothing, no user selected
                }
                else
                {
                    terminateSession(players_edit_name_textfield.getText());                
                    deleteSelectedUser();
                    players_edit_name_textfield.setText("");
                    players_edit_password_textfield.setText("");
                    players_edit_team_textfield.setText("");
                    selectedUsername  ="";
                }
            }
        });
              
        edit_players_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(players_edit_name_textfield.getText().length() < 1)
                {
                    status_textfield.setText("Please select a user from the table first");
                }
                else
                {
                    selectedUsername = players_edit_name_textfield.getText();
                    players_edit_name_textfield.setEditable(true);
                    players_edit_password_textfield.setEditable(true);
                    players_edit_team_textfield.setEditable(true);
                    status_textfield.setText("You can now edit the user details");
                }
            }
        });
        
        save_players_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(players_edit_name_textfield.getText().length() < 1)
                {
                    status_textfield.setText("Please select a user from the table first");
                }
                else
                {
                    saveChangesToUser();
                    players_edit_name_textfield.setEditable(false);
                    players_edit_name_textfield.setText("");
                    players_edit_password_textfield.setEditable(false);
                    players_edit_password_textfield.setText("");
                    players_edit_team_textfield.setEditable(false);
                    players_edit_team_textfield.setText("");
                    selectedUsername  ="";
                }
            }
        });
             
        edit_advisory_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {
                if(advisory_id.getText().length() < 1)
                {
                    status_textfield.setText("Please select an advisory from the table first");
                }
                else
                {
                    if(edit_advisory_hyperlink.getText().equalsIgnoreCase("edit"))
                    {
                        edit_advisory_hyperlink.setText("Cancel");
                        selectedAdvisory = advisory_id.getText();
                        advisory_title.setEditable(true);
                        advisory_difficulty.setEditable(true);
                        advisory_service.setEditable(true);
                        advisory_description.setEditable(true);
                        advisory_challenge.setEditable(true);
                        advisory_uri.setEditable(true);
                        advisory_hint.setEditable(true);
                        advisory_second_hint.setEditable(true);
                        advisory_answer.setEditable(true);
                        advisory_vulnpart.setEditable(true);
                        status_textfield.setText("You can now edit the advisory details");
                    }
                    else if(edit_advisory_hyperlink.getText().equalsIgnoreCase("cancel"))
                    {
                        edit_advisory_hyperlink.setText("Edit");
                        selectedAdvisory = advisory_id.getText();
                        advisory_title.setEditable(false);
                        advisory_difficulty.setEditable(false);
                        advisory_service.setEditable(false);
                        advisory_description.setEditable(false);
                        advisory_challenge.setEditable(false);
                        advisory_uri.setEditable(false);
                        advisory_hint.setEditable(false);
                        advisory_second_hint.setEditable(false);
                        advisory_answer.setEditable(false);
                        advisory_vulnpart.setEditable(false);
                        populateAdvisory(Integer.parseInt(selectedAdvisory));
                        status_textfield.setText("You can no longer edit the advisory details");
                    }
                    
                }
            }
        });
        
        save_advisory_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                if(advisory_id.getText().length() < 1)
                {                    
                    status_textfield.setText(advisoryHandler.addAdvisory(advisory_title.getText(), advisory_difficulty.getText(), advisory_service.getText(), advisory_description.getText(), advisory_challenge.getText(), advisory_uri.getText(), advisory_hint.getText(), advisory_second_hint.getText(), advisory_answer.getText(), advisory_vulnpart.getText()));
                }
                else
                {
                    String returnValue = advisoryHandler.updateAdvisory(advisory_id.getText(), advisory_title.getText(), advisory_difficulty.getText(), advisory_service.getText(), advisory_description.getText(), advisory_challenge.getText(), advisory_uri.getText(), advisory_hint.getText(), advisory_second_hint.getText(), advisory_answer.getText(), advisory_vulnpart.getText());
                    if(returnValue.contains("SUCCESS: "))
                    {
                        status_textfield.setText(returnValue);
                        edit_advisory_hyperlink.setText("Edit");
                        selectedAdvisory = advisory_id.getText();
                        advisory_title.setEditable(false);
                        advisory_difficulty.setEditable(false);
                        advisory_service.setEditable(false);
                        advisory_description.setEditable(false);
                        advisory_challenge.setEditable(false);
                        advisory_uri.setEditable(false);
                        advisory_hint.setEditable(false);
                        advisory_second_hint.setEditable(false);
                        advisory_answer.setEditable(false);
                        advisory_vulnpart.setEditable(false);
                        populateAdvisory(Integer.parseInt(selectedAdvisory));
                    }
                    else
                    {
                        status_textfield.setText(returnValue);
                    }
                }
            }
        });
        
        new_advisory_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {
                advisory_id.setText("");
                edit_advisory_hyperlink.setText("Edit");
                selectedAdvisory = "";
                advisory_title.setEditable(true);
                advisory_title.setText("");
                advisory_difficulty.setEditable(true);
                advisory_difficulty.setText("");
                advisory_service.setEditable(true);
                advisory_service.setText("");
                advisory_description.setEditable(true);
                advisory_description.setText("");
                advisory_challenge.setEditable(true);
                advisory_challenge.setText("");
                advisory_uri.setEditable(true);
                advisory_uri.setText("");
                advisory_hint.setEditable(true);
                advisory_hint.setText("");
                advisory_second_hint.setEditable(true);
                advisory_second_hint.setText("");
                advisory_answer.setEditable(true);
                advisory_answer.setText("");
                advisory_vulnpart.setEditable(true);
                advisory_vulnpart.setText("");
                status_textfield.setText("You can now add the advisory details. Click save when done.");
            }
        });
        
        scores_adjust_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {
                if(scores_adjust_hyperlink.getText().equalsIgnoreCase("Adjust"))
                {
                    scores_adjust_hyperlink.setText("Save");
                    scores_credit_textfield.setEditable(true);
                    scores_prestige_textfield.setEditable(true);
                }
                else
                {
                    try
                    {
                        int credits = Integer.parseInt(scores_credit_textfield.getText());
                        int prestige = Integer.parseInt(scores_prestige_textfield.getText());
                        String username = scores_username_label.getText();
                        ScoreSystem scoreHandler = new ScoreSystem();
                        scoreHandler.setCredit(credits, username);
                        scoreHandler.setPrestige(prestige, username);
                        status_textfield.setText(username + "'s score was successfully updated");
                        scores_adjust_hyperlink.setText("Adjust");
                        scores_username_label.setText("");
                        scores_credit_textfield.setText("");
                        scores_prestige_textfield.setText("");
                        scores_credit_textfield.setEditable(false);
                        scores_prestige_textfield.setEditable(false);
                    }
                    catch(Exception e)
                    {
                        status_textfield.setText("ERROR: Please set a valid number");
                    }
                }
            }
        });
        
        make_announcements_hyperlink.setOnAction(new EventHandler<ActionEvent>() 
        {            
            @Override
            public void handle(ActionEvent event) 
            {
                SQLiteConnectionHandler sql = new SQLiteConnectionHandler();
                sql.addAnnouncement(announcements_textarea.getText());
                announcements_textarea.setText("");
            }
        });
        
        startGetters();
        startingSetup();
    }   
    
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
        else if(event.getTarget() == answers_button)
        {
            hideAllBut(answers_ap);
        }
        else if(event.getTarget() == scores_button)
        {
            hideAllBut(scores_ap);
        }
        else if(event.getTarget() == announcements_button)
        {
            hideAllBut(announcements_ap);
        }
        else if(event.getTarget() == services_button)
        {
            hideAllBut(services_ap);
        }
        else if(event.getTarget() == exploits_button)
        {
            hideAllBut(exploits_ap);
        }
        else if(event.getTarget() == players_button)
        {
            hideAllBut(players_ap);
        }
    }
    
    /**
     * Sets all AnchorPanes to not visible, except the specified pane and the spacer pane
     * @param pane the pane to display
     */
    private void hideAllBut(AnchorPane pane)
    {
        advisories_ap.setVisible(false);
        answers_ap.setVisible(false);
        scores_ap.setVisible(false);
        announcements_ap.setVisible(false);
        services_ap.setVisible(false);
        exploits_ap.setVisible(false);  
        players_ap.setVisible(false);
        pane.setVisible(true);
        spacer_ap.setVisible(true);
        status_textfield.setVisible(true);
    }
    
    /**
     * the display of panels to use when launching the application
     */
    private void startingSetup()
    {
        advisories_ap.setVisible(false);
        answers_ap.setVisible(false);
        scores_ap.setVisible(false);
        announcements_ap.setVisible(false);
        services_ap.setVisible(false);
        exploits_ap.setVisible(false); 
        players_ap.setVisible(false);
        spacer_ap.setVisible(false);
        menu_ap.setVisible(true);
        status_textfield.setVisible(false);
    }        
    
    public void startStopSendingAdvisories()
    {
        if(sendingAdvisories)
        {
            timedAdvisoryReleaserHandler.stopSendingAdvisories();
            start_sending_hyperlink.setText("Start sending advisories");            
            sendingAdvisories = false;
            status_textfield.setText("No longer sending advisories");
        }
        else
        {
            timedAdvisoryReleaserHandler.startSendingAdvisories();
            start_sending_hyperlink.setText("Stop sending advisories");        
            sendingAdvisories = true;
            status_textfield.setText("Now sending advisories");
        }        
    }
    
    public void deleteSelectedUser()
    {
        status_textfield.setText(players_edit_name_textfield.getText() + " has been deleted");
        playerHandler.deleteUser(players_edit_name_textfield.getText(), players_edit_password_textfield.getText());                    
    }
    
    public void resetAdvisories()
    {
        timedAdvisoryReleaserHandler.resetAdvisories();
        status_textfield.setText("Advisories reset");
    }
    
    public void terminateAllSessions()
    {
        playerHandler.terminateAllSessions();
        status_textfield.setText("All sessions terminated");
    }
    
    public void terminateSession(String username)
    {
        status_textfield.setText(username +"'s session terminated");
        playerHandler.terminateUsersSession(username);
    }
    
    public void removeAllAnswers()
    {
        playerHandler.removeAllAnswers();
        status_textfield.setText("All answers removed");
    }
    
    public void resetAllScores()
    {
        status_textfield.setText("All scores have been reset");
        playerHandler.resetAllScores();        
    }
    
    public void removeAllAnnouncements()
    {
        playerHandler.removeAllAnnouncements();
        status_textfield.setText("All announcements removed");
    }
    
    public void deleteAllPlayers()
    {
        status_textfield.setText("All players have been deleted");
        playerHandler.deleteAllPlayers();
    }
    
    public void deleteAllFromScores()
    {
        status_textfield.setText("All scores have been removed");
        playerHandler.deleteAllFromScores();
    }
    
    public void resetAllServices()
    {
        status_textfield.setText("All services have been reset");
        playerHandler.resetAllServices();
    }
    
    public void deleteAllFromServices()
    {
        status_textfield.setText("All services have been removed");
        playerHandler.removeAllServices();
    }
    
    public void removeAllExploitsAndPatches()
    {
        playerHandler.removeAllVulnerabilities();
        status_textfield.setText("All exploits and patches removed");
    }
    
    public void pickPlayer()
    {
        if(players_tableview.getSelectionModel().getSelectedItem() != null)
        {
            players_edit_name_textfield.setText(players_tableview.getSelectionModel().getSelectedItem().getUsername());
            players_edit_team_textfield.setText(players_tableview.getSelectionModel().getSelectedItem().getTeamname());
            players_edit_password_textfield.setText(players_tableview.getSelectionModel().getSelectedItem().getPassword());
            status_textfield.setText(players_tableview.getSelectionModel().getSelectedItem().getUsername() + " selected");
        }
        else
        {
            //do nothing, they didn't click right
        }
    }
    
    public void pickAdvisory()
    {
        if(advisories_tableview.getSelectionModel().getSelectedItem() != null)
        {
            populateAdvisory(Integer.parseInt(advisories_tableview.getSelectionModel().getSelectedItem().getID()));            
            status_textfield.setText("Advisory " +advisories_tableview.getSelectionModel().getSelectedItem().getID() + " selected");
        }
        else
        {
            //do nothing, they didn't click right
        }
    }
    
    public void populatePlayersTable(ArrayList<String[]> entries)
    {
        players_tableview.getItems().clear();
        for(String[] entry : entries)
        {
            String user = entry[0];
            String team = entry[1];            
            String password = entry[2];
            String active = entry[3];
            PlayersListRowItem i = new PlayersListRowItem(user, team, password, active);
            players_tableview.getItems().add(i);        
        }   
    }
    
    public void populateAdvisoriesTable(ArrayList<String[]> entries)
    {
        advisories_tableview.getItems().clear();
        for(String[] entry : entries)
        {
            String id = entry[0];
            String title = entry[1];            
            String released = entry[2];
            String answered = entry[3];
            AdvisoryListRowItem i = new AdvisoryListRowItem(id, title, released, answered);
            advisories_tableview.getItems().add(i);        
        }   
    }
        
    public void saveChangesToUser()
    {        
        status_textfield.setText(playerHandler.updateUser(selectedUsername, players_edit_name_textfield.getText(), players_edit_team_textfield.getText(), players_edit_password_textfield.getText()));        
    }
    
    private void startGetters()
    {
        PlayersGetter playersGetter = new PlayersGetter(this);
        playersGetter.configureForTable(players_tableview);
        ScheduledFuture playersSchedule = scheduler.scheduleAtFixedRate(playersGetter, delay, interval, TimeUnit.SECONDS);
        
        AdvisoriesGetter advisoriesGetter = new AdvisoriesGetter(this);
        advisoriesGetter.configureForTable(players_tableview);
        ScheduledFuture advisoriesSchedule = scheduler.scheduleAtFixedRate(advisoriesGetter, delay, interval, TimeUnit.SECONDS);
        
        AnswersGetter answersGetter = new AnswersGetter(this);
        answersGetter.configureForTable(answers_tableview);
        ScheduledFuture answersSchedule = scheduler.scheduleAtFixedRate(answersGetter, delay, interval, TimeUnit.SECONDS);
        
        ScoresGetter scoresGetter = new ScoresGetter(this);
        scoresGetter.configureForTable(scores_tableview);
        ScheduledFuture scoresSchedule = scheduler.scheduleAtFixedRate(scoresGetter, delay, interval, TimeUnit.SECONDS);
        
        AnnouncementsGetter announcementsGetter = new AnnouncementsGetter(this);
        announcementsGetter.configureForTable(old_announcements_textarea);
        ScheduledFuture announcementsSchedule = scheduler.scheduleAtFixedRate(announcementsGetter, delay, interval, TimeUnit.SECONDS);
    }
    
    public void exit()
    {
        //TODO stop all the things
        System.exit(0);
    }
    
   /**
     * configures the LogLevel based on the properties file
     */
    private void configureLogLevels()
    {        
        String level = properties.getProperty("LogLevel");
        
        switch(level)
        {
            case "INFO":    INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.FINE;
                            break;
            case "DEBUG":   INFO = Level.INFO;
                            DEBUG = Level.INFO; 
                            WARNING = Level.INFO;
                            break;
            case "WARNING": INFO = Level.FINE; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.INFO;    
                            break;
            default:        INFO = Level.INFO; 
                            DEBUG = Level.FINE; 
                            WARNING = Level.WARNING;
                            break;
        }
    }
    
    /**
     * loads the properties from the file
     */
    private void initProperties()
    {
        try
        {
            properties.load(new FileInputStream(propertiesURL));
        }
        catch (IOException ex)
        {
            log.log(DEBUG,""+ex);
        }        
    }
    
    private void populateAdvisory(int id)
    {
        Advisory advisory = advisoryHandler.getAdvisory(id);
        advisory_id.setText(""+advisory.getID());
        advisory_title.setText(advisory.getTitle());
        advisory_difficulty.setText(advisory.getDifficulty());
        advisory_service.setText(advisory.getAffectedService());
        advisory_description.setText(advisory.getDescription());
        advisory_challenge.setText(advisory.getChallenge());
        advisory_uri.setText(advisory.getChallengeURI());
        advisory_hint.setText(advisory.getHint());
        advisory_second_hint.setText(advisory.getSecondHint());
        advisory_answer.setText(advisory.getCorrectAnswer());
        advisory_vulnpart.setText(advisory.getPartOfVuln());
    }
    
    public void populateAnswersTable(ArrayList<String[]> entries)
    {
        answers_tableview.getItems().clear();
        for(String[] entry : entries)
        {
            String username = entry[0];
            String id = entry[1];            
            String answer = entry[2];
            String correct= entry[3];
            AnswerListRowItem i = new AnswerListRowItem(username, id, answer, correct);
            answers_tableview.getItems().add(i);        
        }   
    }
    
    public void populateScoresTable(ArrayList<String[]> entries)
    {
        scores_tableview.getItems().clear();
        for(String[] entry : entries)
        {
            String username = entry[0];
            String prestige = entry[1];            
            String credit = entry[2];
            ScoreListRowItem i = new ScoreListRowItem(username, prestige, credit);
            scores_tableview.getItems().add(i);        
        }   
    }
    
    public void pickScores()
    {
        if(scores_tableview.getSelectionModel().getSelectedItem() != null)
        {
            scores_username_label.setText(scores_tableview.getSelectionModel().getSelectedItem().getUsername());            
            status_textfield.setText(scores_tableview.getSelectionModel().getSelectedItem().getUsername() + " selected");
            scores_prestige_textfield.setText(scores_tableview.getSelectionModel().getSelectedItem().getPrestige());
            scores_credit_textfield.setText(scores_tableview.getSelectionModel().getSelectedItem().getCredit());
        }
        else
        {
            //do nothing, they didn't click right
        }        
    }
    
    public void populateAnnouncements(String entries)
    {
        old_announcements_textarea.setText(entries);
    }
}
