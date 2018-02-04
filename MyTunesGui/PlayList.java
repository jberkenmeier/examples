

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This Playlist class allows you to create a new ArrayList of songs in order to use as
 * your "playlist". Includes an assortment of methods for which to play/stop/add/remove songs
 * as well as getter and setter methods to retrieve or update song/playlist information.
 * @author Josh Berkenmeier
 *
 */
public class PlayList implements MyTunesPlayListInterface{
	
	private String name;
	private Song playing;
	private ArrayList<Song> songList;
	
	/**
	 * constructor: sets up a new ArrayList of songs to be used as a playlist.
	 * @param name The desired name of your playlist
	 */
	public PlayList(String name)
	{
	this.name = name;
	playing = null;
	songList = new ArrayList<Song>();
	}
	
	/**
	 * constructor: scans the passed in file, and creates songs accordingly. Adds them
	 * to the playlist
	 * @param file The file of songs you want in your playlist.
	 */
	public PlayList(File file)
	{
	try {
	playing = null;
	songList = new ArrayList<Song>();
	Scanner scan = new Scanner(file);
	this.name = scan.nextLine().trim();
	while(scan.hasNextLine()) {
	String title = scan.nextLine().trim();
	String artist = scan.nextLine().trim();
	String playtime = scan.nextLine().trim();
	String songPath = scan.nextLine().trim();

	int colon = playtime.indexOf(':');
	int minutes = Integer.parseInt(playtime.substring(0, colon));
	int seconds = Integer.parseInt(playtime.substring(colon+1));
	int playtimesecs = (minutes * 60) + seconds;

	Song song = new Song(title, artist, playtimesecs, songPath);
	this.songList.add(song);
	}
	scan.close();
	} catch (FileNotFoundException e) {
	System.err.println("Failed to load playlist. " + e.getMessage());
	}
	}
	
	/**
	 * Returns the name of this playlist.
	 * @return The name.
	 */
	public String getName()
	{
	return name;
	}
	
	/**
	 * Returns the song that is currently playing.
	 * @return The song that is currently playing.
	 */
	public Song getPlaying()
	{
	return playing;
	}
	
	/**
	 * Returns the songlist. This is not a copy, so be careful!
	 * @return A reference to the songlist.
	 */
	public ArrayList<Song> getSongList()//returns songList
	{
	return songList;
	}
	
	/**
	 * Sets the name of this playlist.
	 * @param newName The name.
	 */
	public void setName(String newName)//sets name for playlist
	{
	name = newName;
	}
	
	/**
	 * Adds the given song to the end of this playlist.
	 * @param addSong The song to add.
	 */
	public void addSong(Song addSong)//creates addSong method, adds argument(song) to songList
	{
	songList.add(addSong);
	}
	
	/**
	 * Removes the song with the given index from this playlist, or null
	 * if the index is invalid.
	 * @param i The index of the song to remove.
	 * @return The song at index or null if none exists.
	 */
	public Song removeSong(int i)//accepts index int to remove song, if input value out of range
	{                                //returns null, removes song from songList, returns removed song
	Song tempSong = null;
	if (i<0 || i>=songList.size())
	{
	return null;	
	}
	else
	{
	tempSong = songList.get(i);
	songList.remove(i);
	return tempSong;
	}
	}
	
	/**
	 * Returns the number of songs in this playlist.
	 * @return The number of songs.
	 */
	public int getNumSongs()//returns number of songs in songList
	{
	return songList.size();
	}
	
	/**
	 * Returns the total playtime of all the songs in this playlist.
	 * @return The total play time in seconds.
	 */
	public int getTotalPlayTime()
	{
	int playTime = 0;
	for (int i=0; i<songList.size(); i++)
	{
	playTime += songList.get(i).getPlayTime();
	}
	return playTime;
	}
	
	/**
	 * Returns the song with the given index from this playlist, or null
	 * if the index is invalid.
	 * @param index The index of the song to retrieve.
	 * @return The song at index or null if none exists.
	 */
	public Song getSong(int i)
	{
	Song tempSong;
	
	if(i>=0 && i<songList.size())
	{
	tempSong = songList.get(i);
	return tempSong;
	}
	else
	{
	return null;	
	}
	}
	
	/**
	 * Plays the song at the specified index. 
	 * @param index The index of the song to play.
	 */
	public void playSong(int i)
	{
	playing = null;
	if(i>=0 && i<=songList.size())
	{
	songList.get(i).play();
	playing = songList.get(i);	
	}
	else
	{
	
	}
	}
	
	/**
	 * collects song info from songs in the playlist
	 * @return average playtime, shortest/longest song, total playtime
	 */
	public String getInfo()
	{
	DecimalFormat decFmt = new DecimalFormat("0.00");
	
	int tempPlayTime = 0, playTime = 0;
	double avgPlayTime = 0.0;
	int tempShort = Integer.MAX_VALUE;
	int tempLong = 0;
	Song shortSong = new Song(), tempShortSong = new Song(), longSong = new Song(), tempLongSong = new Song();
	String str1 = "";
	
	for(int i=0; i<songList.size(); i++)
	{
	tempPlayTime += songList.get(i).getPlayTime();
	}
	if(songList.size()> 0)
	{
	decFmt.format(avgPlayTime = (double)tempPlayTime/(double)songList.size());
	}
	
	for(int i=0; i<songList.size(); i++)
	{
	if(songList.get(i).getPlayTime() < tempShort)
	{
	tempShort = songList.get(i).getPlayTime();
	tempShortSong = songList.get(i);
	}
	shortSong = tempShortSong;
	}
	
	for(int i=0; i<songList.size(); i++)
	{
	if(songList.get(i).getPlayTime() > tempLong)
	{
	tempLong = songList.get(i).getPlayTime();
	tempLongSong = songList.get(i);
	}
	longSong = tempLongSong;
	}
	
	for (int i=0; i<songList.size(); i++)
	{
	playTime += songList.get(i).getPlayTime();
	}
	
	if (songList.size() > 0)
	{
	str1  = "The average play time is: " + decFmt.format(avgPlayTime) + " seconds \n";
	str1 += "The shortest song is: " + shortSong.toString() + "\n";
	str1 += "The longest song is: " + longSong.toString() + "\n";
	str1 += "Total play time: " + playTime;
	}
	else
	{
	str1 = "There are no songs.";
	}
	return str1;
	}
	
	/**
	 * modified toString method
	 * @return toString
	 */
	public String toString()
	{
	String str = "";
	if (songList.size() == 0)
	{
	return "------------------" + "\n" + name + "(" + songList.size() + " songs)" 
	+ "\n" + "------------------" + "\n" + "There are no songs." +"\n" + "------------------"; 
	
	}
	for (Song song: songList)
	{
	str += "(" + songList.indexOf(song) + ")" + song.toString() + "\n";
	}
	return "\n" + "------------------" + "\n" + name + "(" + songList.size() + "songs)" + "\n" +
	 	"------------------" + "\n" + str + "------------------";
	
	}
	
	/**
	 * Adds the given song to the end of this playlist.
	 * @param s The song to add.
	 */
	public void playSong(Song song) {

	if(playing != null)
	{
	playing.stop();
	}
	playing = song;	
	playing.play();

	}

	/**
	 * 
	 * Stops the currently playing song (if any) and sets playing song to null.
	 */
	public void stop() {
	playing.stop();
	playing = null;
	}

	/**
	 * Returns an array of all the songs in the playlist.
	 * @return An array of songs.
	 */
	public Song[] getSongArray() {
	

	Song[] copy = new Song[songList.size()];

	for(int i = 0; i < copy.length; i++){
	copy[i] = songList.get(i);
	}
	
	
	return copy;
	}

	/**
	 * Moves the song at the given index to the previous index in the list (index - 1). All other elements
	 * in the list will be shifted. If the index given is zero, it will wrap around and move the song to the
	 * end of the list.
	 * 
	 * @param index The index of the song to move.
	 * @return The new index of the song (after the move).  If a song at the given index does not exist,
	 * or could not be moved for some other reason, returns the original index.
	 */
	public int moveUp(int index) {
	int newIndex;
	Song temp = songList.remove(index);
	if (index == 0)
	{
	songList.add(temp);
	newIndex = songList.size()-1;

	}
	else
	{
	newIndex = index - 1;
	songList.add(newIndex, temp);
	}

	return newIndex;
	}

	/**
	 * Moves the song at the given index to the next index in the list (index + 1). All other elements
	 * in the list will be shifted. If the given index is the last song in the list, it will wrap around
	 * and move the song to the beginning of the list.
	 * @param index The index of the song to move.
	 * @return The new index of the song (after the move). If a song at the given index does not exist,
	 * or could not be moved for some other reason, returns the original index.
	 */
	public int moveDown(int index) {
	int newIndex;
	Song temp = songList.remove(index);
	if (index == songList.size())
	{
	songList.add(0, temp);
	newIndex = 0;
	}
	else
	{
	newIndex = index +1;
	songList.add(newIndex, temp);
	}

	return newIndex;
	}

	/**
	 * Returns a 2 dimensional musical square. The dimension of the square is calculated based on the number of
	 * songs in the playlist. If the number of songs in the list are not a square number, then the remaining slots
	 * are filled starting with the first song.
	 *
	 * <p>
	 * For example, if the playlist contains 7 songs, the generated array would contain songs in the following
	 * order.
	 * </p>
	 *
	 * <pre>
	 * song0 song1 song2
	 * song3 song4 song5
	 * song6 song0 song1
	 * </pre>
	 * @return - the 2 dimensional array of songs.
	 */
	public Song[][] getMusicSquare() {
	
	double dimensions = 0.0;
	
	dimensions = Math.ceil(Math.sqrt(songList.size()));
	
	Song[][] songsquare = new Song[(int) dimensions][(int) dimensions];
	
	for (int row = 0; row < dimensions; row++)
	for (int col = 0; col < dimensions; col++)
	{
	songsquare[row][col] = songList.get((int) ((dimensions * row + col)%(songList.size())));	
	
	}
	return songsquare;
	}
}