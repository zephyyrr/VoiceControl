package swe.kbk.VoiceControl;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class DBHandeler {

	private static final String DBFILE = "objects.db";
	private static final String FILESEP = System.getProperty("file.separator");
	private static final String PLUGINFOLDER = "plugins" + FILESEP + "VoiceControl" + FILESEP;
	private Connection conn;

	public DBHandeler(VoiceControl plugin) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		MakeSureFolderExists();
		String dbfile = PLUGINFOLDER + DBFILE;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbfile);
		} catch (SQLException e) {
			plugin.getLogger().severe(plugin + " SQLException opening connection to " + dbfile);
		}
	}

	private boolean MakeSureFolderExists() {
		File folder = new File(PLUGINFOLDER);
		if (!folder.isDirectory()) {
			return folder.mkdirs();
		}
		return true;
	}

	public void Close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Some error message perhaps...
			e.printStackTrace();
		}
	}

	public List<Switchable> search(String[] passwords) throws SQLException {
		LinkedList<Switchable> list = new LinkedList<Switchable>();
		if (conn == null) {
			return list;
		}
		PreparedStatement prep = conn
				.prepareStatement("select radius, x, y, z, world from switchable where password=(?);");
		for (String s : passwords) {
			prep.setString(1, scrub(s));
			ResultSet res = prep.executeQuery();

			while (res.next()) {
				Switchable sw = getSwitchable(res.getInt("radius"),
						res.getDouble("x"), res.getDouble("y"),
						res.getDouble("z"), res.getString("world"));
				if (sw != null) {
					list.add(sw);
				}
			}
			res.close();
		}

		return list;
	}

	public void add(String password, Switchable sw) throws SQLException {
		addSwitchable(password, sw);
	}

	/**
	 * @param password
	 * @param sw
	 * @param row
	 * @throws SQLException
	 */
	public void addSwitchable(String password, Switchable sw)
			throws SQLException {
		password = scrub(password);
		Location loc = sw.getLocation();
		PreparedStatement prepSwitch = conn
				.prepareStatement("insert into switchable values (?,?,?,?,?,?);");

		prepSwitch.setString(1, password);
		prepSwitch.setInt(2, (int) sw.getRadius());
		prepSwitch.setDouble(3, loc.getX());
		prepSwitch.setDouble(4, loc.getY());
		prepSwitch.setDouble(5, loc.getZ());
		prepSwitch.setString(6, loc.getWorld().getName());
		prepSwitch.executeUpdate();
	}

	public Switchable getSwitchable(int radius, double x, double y, double z,
			String world) {
		world = scrub(world);
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		Block block = loc.getBlock();

		switch (block.getType()) {
		case LEVER:
			return new LeverSwitch(block, radius);
		case STONE_BUTTON:
			return new ButtonSwitch(block, radius);
			// TODO Add more cases as they get implemented...
		default:
			return null;
		}
	}

	public void remove(String password, Location location) throws SQLException {
		password = scrub(password);
		PreparedStatement prepSwitch = conn
				.prepareStatement("delete from switchable where password=(?) AND x=(?) AND y=(?) AND z=(?) AND world=(?);");
		prepSwitch.setString(1, password);
		prepSwitch.setDouble(2, location.getX());
		prepSwitch.setDouble(3, location.getY());
		prepSwitch.setDouble(4, location.getZ());
		prepSwitch.setString(5, location.getWorld().getName());
		prepSwitch.executeUpdate();
	}

	public void remove(Location location) throws SQLException {
		PreparedStatement prepSwitch = conn
				.prepareStatement("delete from switchable where x=(?) AND y=(?) AND z=(?) AND world=(?);");
		prepSwitch.setDouble(1, location.getX());
		prepSwitch.setDouble(2, location.getY());
		prepSwitch.setDouble(3, location.getZ());
		prepSwitch.setString(4, location.getWorld().getName());
		prepSwitch.executeUpdate();
	}

	public void SetupDB() throws SQLException {
		Statement stat = conn.createStatement();
		stat.executeUpdate("create table if not exists switchable (password varchar(30), radius integer, x double, y double, z double, world varchar(30));");
	}

	public String scrub(String s) {
		return s.replaceAll("[\'|\"|;|/|\\|\n|\r|\0]", "");
	}
}
