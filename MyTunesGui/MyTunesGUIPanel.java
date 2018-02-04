import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import java.io.File;
import javax.swing.Timer;
/**
 * This class puts together a "My Tunes" GUI panel, in order
 * to be able to play music files. It is complete with buttons 
 * that allow you to play and manipulate song position in the list
 * as well as adding and removing songs. The panel also has a "Music Square"
 * which is an assortment of buttons with songs from the list, which play
 * the corresponding song when pressed.
 * 
 * @author Josh Berkenmeier
 *
 */
public class MyTunesGUIPanel extends JPanel
{
	private JLabel playlistName;
	private JLabel totalSongsAndPlaytime;
	private JLabel nowPlaying;
	private JButton upButton;
	private JButton downButton;
	private JButton addButton;
	private JButton removeButton;
	private JButton playButton;
	private JButton stopButton;
	private JButton nextButton;
	private JButton prevButton;
	private JList<Song> songList;
	private PlayList playList;
	private Song[][] songSquare;
	private JButton[][] songSquareButtons;
	private Timer timer;
	private JPanel rightPanel;
	//private JPanel leftPanel;
	private boolean check = true;
	final double MAX_PLAYS = 50;
	
	
	
	/**
	 * This builds all of the necessary panels and buttons,
	 * adds listeners to the buttons so as to perform actions
	 * when clicked, initializes a timer, loads the given
	 * playlist.txt text file full of song info into a JList
	 * and places everything into the main panel
	 */
	public MyTunesGUIPanel()
	{
		setLayout(new BorderLayout());
		
		//creates leftpanel, set layout
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		//label for song currently playing, adds to LP
		JPanel nowPlayingPanel = new JPanel();
		nowPlaying = new JLabel("Nothing Playing");
		nowPlayingPanel.add(nowPlaying);
		leftPanel.add(nowPlayingPanel, BorderLayout.NORTH);
		
		//buttons for moving song up/down + listeners, adds to temp panel, add that to LP
		JPanel UpDownButtonsPanel = new JPanel();
		UpDownButtonsPanel.setLayout(new BoxLayout(UpDownButtonsPanel, BoxLayout.X_AXIS));
		JPanel tempUpDownPanel = new JPanel();
		tempUpDownPanel.setLayout(new BoxLayout(tempUpDownPanel, BoxLayout.Y_AXIS));
		upButton = new JButton("MOVE UP");
		downButton = new JButton("MOVE DOWN");
		tempUpDownPanel.add(upButton);
		tempUpDownPanel.add(downButton);
		UpDownButtonsPanel.add(tempUpDownPanel);
		leftPanel.add(UpDownButtonsPanel, BorderLayout.WEST);
		upButton.addActionListener(new MoveButtonActionListener());
		downButton.addActionListener(new MoveButtonActionListener());
		
		//buttons for add/remove song + listeners, add them to temp panel, add that to LP
		JPanel AddRemoveButtonsPanel = new JPanel();
		AddRemoveButtonsPanel.setLayout(new BoxLayout(AddRemoveButtonsPanel, BoxLayout.X_AXIS));
		JPanel tempAddRemovePanel = new JPanel();
		tempAddRemovePanel.setLayout(new BoxLayout(tempAddRemovePanel, BoxLayout.Y_AXIS));
		addButton = new JButton("ADD");
		removeButton = new JButton("REMOVE");
		tempAddRemovePanel.add(addButton);
		tempAddRemovePanel.add(removeButton);
		AddRemoveButtonsPanel.add(tempAddRemovePanel);
		leftPanel.add(AddRemoveButtonsPanel, BorderLayout.EAST);
		addButton.addActionListener(new AddSongButtonActionListener());
		removeButton.addActionListener(new RemoveSongButtonActionListener());
		
		//buttons for play/prev/pause + listeners, add to temp panel, add to play panel
		//add that to LP, and initially set up timer set repeat to false.
		JPanel PlayButtonsPanel = new JPanel();
		PlayButtonsPanel.setLayout(new BoxLayout(PlayButtonsPanel, BoxLayout.Y_AXIS));
		JPanel tempPlayButtonsPanel = new JPanel();
		tempPlayButtonsPanel.setLayout(new BoxLayout(tempPlayButtonsPanel, BoxLayout.X_AXIS));
		playButton = new JButton("PLAY");
		nextButton = new JButton("NEXT");
		prevButton = new JButton("PREV");
		tempPlayButtonsPanel.add(prevButton);
		tempPlayButtonsPanel.add(playButton);
		tempPlayButtonsPanel.add(nextButton);
		PlayButtonsPanel.add(tempPlayButtonsPanel);
		leftPanel.add(PlayButtonsPanel, BorderLayout.SOUTH);
		playButton.addActionListener(new PlaySongButtonActionListener());
		nextButton.addActionListener(new PlaySongButtonActionListener());
		prevButton.addActionListener(new PlaySongButtonActionListener());
		timer = new Timer(0, new TimerListener());
		timer.setRepeats(false);
		
		//creates new file from given playlist.txt, loads that song info 
		//into a arrayList called playList, creates new JList + listener, 
		//and adds the playList to it, sets font of text
		File file = new File("playlist.txt");
		playList = new PlayList(file);
		songList = new JList<Song>();
		songList.setListData(playList.getSongArray());
		songList.setFont(new Font(Font.MONOSPACED, Font.BOLD,12));
		songList.addListSelectionListener(new SongListListener());
		
		//creates scroll pane, adds the JList full of songs to it
		//add the scroll pane to the LP
		JScrollPane scrollPane = new JScrollPane(songList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		//create top panel, label for name of playlist and total songs/playtime
		//adds label to temp panel, and temp panel to TP
		JPanel topPanel = new JPanel();
		JPanel tempTop = new JPanel();
		tempTop.setLayout(new BoxLayout(tempTop, BoxLayout.Y_AXIS));
		playlistName = new JLabel(playList.getName());
		totalSongsAndPlaytime = new JLabel("Total Songs:" + playList.getNumSongs() + "~" + "Total Playtime:" + (playList.getTotalPlayTime()/60) + " mins");
		tempTop.add(playlistName);
		tempTop.add(totalSongsAndPlaytime);
		topPanel.add(tempTop);
		
		//add TP and LP as well as a configured music square to the main panel
		this.add(topPanel, BorderLayout.NORTH);
		this.add(leftPanel, BorderLayout.WEST);
		configureMusicSquare();
		
	}
	
	/**
	 * Listener for the song list, shows the title of song and name of author
	 * of whatever song is selected.
	 * @author Josh
	 *
	 */
	private class SongListListener implements ListSelectionListener
	{

		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			if(songList.getSelectedValue() == null)
			{
				nowPlaying.setText("Nothing Playing");
			}
			else
			{
				nowPlaying.setText(songList.getSelectedValue().getTitle() + " by: " + songList.getSelectedValue().getArtist());
			}
			
		}
	}
	
	/**
	 * Listener for the play/next/prev buttons. Checks what button was pressed
	 * and plays the correct song accordingly complete with setting timer with
	 * the right playtime and timer start/stop.
	 * @author Josh
	 *
	 */
	private class PlaySongButtonActionListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) {
				//check if playbutton clicked
				if (e.getSource() == playButton)
				{	
					//check if no song playing, if so continue to other checks
					if (check == true)
					{
						//check if no song selected, update label text
						if(songList.getSelectedValue() == null)
						{
							nowPlaying.setText("Nothing Playing");
						}
						//when passes checks, stop existing timer, create new timer for
						//selected song, start song and timer, update button/label text
						//swith song check to false
						else
						{
							timer.stop();
							Song song = new Song();
							song = songList.getSelectedValue();
							playButton.setText("STOP");
							nowPlaying.setText(song.getTitle() + " by: "  + song.getArtist());
							int totalMils = song.getPlayTime() * 1000;
							timer.setInitialDelay(totalMils);
							timer.start();
							playList.playSong(song);
							check = false;
								
						}
					}
					//check if song is playing. if so, stop timer
					//and song, set song check to true, update button text
					else if (check == false)
					{
						playButton.setText("PLAY");
						timer.stop();
						playList.getPlaying().stop();
						check = true;
					}
				}
				
				//check if prev button clicked
				if (e.getSource() == prevButton)
				{
					//check if no song selected, update label text
					if(songList.getSelectedValue()== null)
					{
						nowPlaying.setText("Nothing Playing");
					}
					//set up like playbutton except plays the prev song in list
					//from the one currently selected, update button/label text
					else
					{
						timer.stop();
						Song song = new Song();
						playButton.setText("STOP");
						int i = songList.getSelectedIndex();
						songList.setSelectedIndex(i-1);
						song = songList.getSelectedValue();
						nowPlaying.setText(song.getTitle() + " by: "  + song.getArtist());
						int totalMils = song.getPlayTime() * 1000;
						timer.setInitialDelay(totalMils);
						timer.start();
						playList.playSong(song);
						check = false;
					}
					
				}
				
				//check if next button clicked
				if (e.getSource() == nextButton)
				{
					//check if no song selected, update label text
					if(songList.getSelectedValue()== null)
					{
						nowPlaying.setText("Nothing Playing");
					}
					
					//set up like playbutton except plays the next song in the list
					//from the one currently selected, update button/label text
					else
					{
						timer.stop();
						Song song = new Song();
						playButton.setText("STOP");
						int i = songList.getSelectedIndex();
						songList.setSelectedIndex(i+1);
						song = songList.getSelectedValue();
						nowPlaying.setText(song.getTitle() + " by: "  + song.getArtist());
						int totalMils = song.getPlayTime() * 1000;
						timer.setInitialDelay(totalMils);
						timer.start();
						playList.playSong(song);
						check = false;
						
					}

				}
			//updates music square
			configureMusicSquare();
		}
		
	}
	/**
	 * Timer listener, stops the timer/song when fired, as well
	 * as some housekeeping with labels.
	 * @author Josh
	 *
	 */
	private class TimerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			timer.stop();
			playList.getPlaying().stop();
			nowPlaying.setText("Nothing Playing");
			playButton.setText("PLAY");
			check = true;
			configureMusicSquare();
		}
		
	}
	/**
	 * Listener for the add song button. When triggered creates multiple
	 * text fields in a panel so user can input song data in order to add
	 * song to the playlist.
	 * @author Josh
	 *
	 */
	private class AddSongButtonActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//create add song panel
			JPanel formInputPanel = new JPanel();
			formInputPanel.setLayout(new BoxLayout(formInputPanel, BoxLayout.Y_AXIS));
			
			//create text fields
			JTextField titleField = new JTextField(20);
			JTextField artistField = new JTextField(20);
			JTextField playtimeField = new JTextField(20);
			JTextField filepathField = new JTextField(20);
			
			//add text fields to panel
			formInputPanel.add(new JLabel("Title: "));
			formInputPanel.add(titleField);
			formInputPanel.add(new JLabel("Artist: "));
			formInputPanel.add(artistField);
			formInputPanel.add(new JLabel("MM:SS"));
			formInputPanel.add(playtimeField);
			formInputPanel.add(new JLabel("FilePath: "));
			formInputPanel.add(filepathField);
			
			//set up message, and joptionpane options
			int result = JOptionPane.showConfirmDialog(null, formInputPanel, "Add Song",
	    			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			//if ok is pressed, takes all input song info and creates/adds new song
			//to the playlist.
			if (result == JOptionPane.OK_OPTION)
			{
				String title = titleField.getText();
				String artist = artistField.getText();
				String playtime = playtimeField.getText();
				String filepath = filepathField.getText();
				int playtimesecs = 0;
				try
				{
					int colon = playtime.indexOf(':');
					if(colon < 0)
					{
						JOptionPane.showMessageDialog(null, "Input correct Playtime format (MM:SS)");
						return;
					}
					int minutes = Integer.parseInt(playtime.substring(0, colon));
					int seconds = Integer.parseInt(playtime.substring(colon+1));
					playtimesecs = (minutes * 60) + seconds;

				
					
				}
				catch (NumberFormatException  ex)
				{
					JOptionPane.showMessageDialog(null, "Input correct Playtime format (MM:SS)");
				}
				//creating song from input info, adding song to playlist/jlist
				//update label text, update music square with new info
				Song song = new Song(title, artist, playtimesecs, filepath);
				playList.addSong(song);
				totalSongsAndPlaytime.setText("Total Songs:" + playList.getNumSongs() + "~" + "Total Playtime:" + (playList.getTotalPlayTime()/60) + " mins");
				songList.setListData(playList.getSongArray());
				configureMusicSquare();
				
			}
		}
	}
	
	/**
	 * Listener for the remove song button. When triggered pops open a JOptionPane
	 * that gives option to remove song or cancel the action. Removes selected song 
	 * from the playlist if yes button is pushed.
	 * @author Josh
	 *
	 */
	private class RemoveSongButtonActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//set up option pane for song removel
			int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove song?", "Remove Song",
	    			JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				
				//if user presses yes to remove, song is removed from playlist
				//update label text, update music square
				if (result == JOptionPane.YES_OPTION)
				{
					playList.removeSong(songList.getSelectedIndex());
					totalSongsAndPlaytime.setText("Total Songs:" + playList.getNumSongs() + "~" + "Total Playtime:" + (playList.getTotalPlayTime()/60) + " mins");
					songList.setListData(playList.getSongArray());
					configureMusicSquare();
				}
		}
		
	}
	
	/**
	 * Listener for the move up/down buttons. When pressed the buttons will allow
	 * to move song in the playlist up or down in the order. Wraps if necessary.
	 * @author Josh
	 *
	 */
	private class MoveButtonActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
				//check if upbutton clicked
				if(e.getSource() == upButton)
				{
					//check if no song selected
					if (songList.getSelectedValue() == null)
					{
						JOptionPane.showMessageDialog(null, "No Song Selected");
					}
					//pass checks, moves selected song up in the playlist/jlist. 
					//wraps if needed, update music square with new song position
					else
					{
						int i = songList.getSelectedIndex();
						int newPos = playList.moveUp(i);
						songList.setListData(playList.getSongArray());
						songList.setSelectedIndex(newPos);
						configureMusicSquare();
					}
					
					
				}
				
				//checks if downbutton is clicked
				if(e.getSource() == downButton)
				{
					//check if no song selected
					if (songList.getSelectedValue() == null)
					{
						JOptionPane.showMessageDialog(null, "No Song Selected");
					}
					
					//pass checks, moves selected song down in the playlist/jlist
					//wraps if needed, updates music square with new song position
					else
					{
						int i = songList.getSelectedIndex();
						int newPos = playList.moveDown(i);
						songList.setListData(playList.getSongArray());
						songList.setSelectedIndex(newPos);
						configureMusicSquare();
					}
				}
		}

	}
	
	/**
	 * Listener for the music square. When a button is pushed, corresponding
	 * song plays.
	 * @author Josh
	 *
	 */
	private class MusicSquareListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			//nested for loop to iterate through buttons in order to determine 
			//which button was clicked
			JButton source = (JButton) arg0.getSource();
			for(int i=0; i < songSquareButtons.length; i++)
			{
				for(int j=0; j < songSquareButtons[i].length; j++)
				{
					if(songSquareButtons[i][j] == source)
					{
						//check if no songs in square, update label text
						if(songSquare[i][j] == null)
						{
							nowPlaying.setText("Nothing Playing");
						}
						
						else
						{
							//check if no song playing. plays song of corresponding
							//button that was clicked. update button/label text.
							if(playList.getPlaying() == null)
							{
								playButton.setText("STOP");
								nowPlaying.setText(songSquare[i][j].getTitle() + " by: "  + songSquare[i][j].getArtist());
								int totalMils = songSquare[i][j].getPlayTime() * 1000;
								timer.setInitialDelay(totalMils);
								timer.start();
								playList.playSong(songSquare[i][j]);
								check = false;
							}
							//if song playing, stop existing timer. then plays song of
							//corresponding button that was clicked. update button/label text.
							else
							{
								timer.stop();
								playButton.setText("STOP");
								nowPlaying.setText(songSquare[i][j].getTitle() + " by: "  + songSquare[i][j].getArtist());
								int totalMils = songSquare[i][j].getPlayTime() * 1000;
								timer.setInitialDelay(totalMils);
								timer.start();
								playList.playSong(songSquare[i][j]);
								check = false;	
							}
						}
						//update music square with current data
						configureMusicSquare();
					}
				}
			}
		}
	}
	
	/**
	 * Method that sets up music square. Populates song into the square, updating its
	 * size(amount of buttons) according to size of playlist(fills in empty space at 
	 * the end with songs from beginning of list as needed). Everytime method is called
	 * it will remove the existing panel and replace with an updated square. Includes
	 * method that changes color of the button according to the number of plays the 
	 * corresponding song has.
	 */
	private void configureMusicSquare()
	{
		//checks if panel already exists, and removes it
		if (rightPanel != null)
		{
			this.remove(rightPanel);
		}
		
		//set up dimensions of square according to playlist size
		//creates right panel for music square
		int dimensions;
		dimensions = (int) Math.ceil(Math.sqrt(playList.getNumSongs()));
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(dimensions, dimensions));
		rightPanel.setSize(200, 200);
		
		//load playlist into two dim array of songs
		songSquare = playList.getMusicSquare();
		
		//creates new two dim array for songsquarebuttons
		songSquareButtons = new JButton[(int) dimensions][(int) dimensions];
		
		//nested for loop to create the necessary amount of buttons to populate
		//music square, adds listeners to each button, as well as title of corresponding
		//song in the playlist. uses getHeatMapColor() method in order to set the 
		//color of each button according to the amount of plays that song has. adds these
		//buttons to the music square panel
		for(int i=0; i < dimensions; i++)
		{
			for(int j=0; j < dimensions; j++)
			{
				JButton squareButton = new JButton();
				squareButton.addActionListener(new MusicSquareListener());
				
				squareButton.setText(songSquare[i][j].getTitle());
				squareButton.setBackground(getHeatMapColor(songSquare[i][j].getPlayCount()));
				rightPanel.add(squareButton);
				songSquareButtons[i][j]= squareButton;
			}
		
		}
		//add music square(right panel) to the main panel
		this.add(rightPanel, BorderLayout.CENTER);
		this.revalidate();
	}
	
	/**
	 * Given method. Updates color according to number of plays.
	 * @param plays
	 * @return new Color
	 */
	private Color getHeatMapColor(int plays)
     {
          double minPlays = 0, maxPlays = MAX_PLAYS;    // upper/lower bounds
          double value = (plays - minPlays) / (maxPlays - minPlays); // normalize play count

          // The range of colors our heat map will pass through. This can be modified if you
          // want a different color scheme.
          Color[] colors = { Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED };
          int index1, index2; // Our color will lie between these two colors.
          float dist = 0;     // Distance between "index1" and "index2" where our value is.

          if (value <= 0) {
               index1 = index2 = 0;
          } else if (value >= 1) {
               index1 = index2 = colors.length - 1;
          } else {
               value = value * (colors.length - 1);
               index1 = (int) Math.floor(value); // Our desired color will be after this index.
               index2 = index1 + 1;              // ... and before this index (inclusive).
               dist = (float) value - index1; // Distance between the two indexes (0-1).
          }

          int r = (int)((colors[index2].getRed() - colors[index1].getRed()) * dist)
                    + colors[index1].getRed();
          int g = (int)((colors[index2].getGreen() - colors[index1].getGreen()) * dist)
                    + colors[index1].getGreen();
          int b = (int)((colors[index2].getBlue() - colors[index1].getBlue()) * dist)
                    + colors[index1].getBlue();

          return new Color(r, g, b);
     }
	
	
	
}
