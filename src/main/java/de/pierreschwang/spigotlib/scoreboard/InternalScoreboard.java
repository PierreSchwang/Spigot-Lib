package de.pierreschwang.spigotlib.scoreboard;

import de.pierreschwang.spigotlib.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalScoreboard<T extends User> {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final AbstractScoreboard handler;
    private final T user;
    private int sidebarTeamCounter = 0;
    private final Map<Integer, String> currentSidebar = new HashMap<>();

    public InternalScoreboard(AbstractScoreboard handler, T user) {
        this.handler = handler;
        this.user = user;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(user.getPlayer().getName(), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        user.getPlayer().setScoreboard(scoreboard);
        refreshSidebar();
    }

    public void refresh() {
        refresh(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public void refresh(Player... players) {
        for (Player player : players) {
            if (Bukkit.getPlayer(player.getUniqueId()) == null) {
                if (scoreboard.getTeam(player.getName()) != null)
                    scoreboard.getTeam(player.getName()).unregister();
            }
            User pUser = handler.getPlugin().getUser(player);
            Team team = getOrCreate(player.getName(), handler.getPlugin().getPlayerRenderer().getPrefix().apply(pUser),
                handler.getPlugin().getPlayerRenderer().getSuffix().apply(pUser));
            if(!team.hasEntry(player.getName()))
                team.addEntry(player.getName());
        }
        String title = handler.getPlugin().getPlayerRenderer().getSidebarTitle().apply(user);
        if (title != null)
            objective.setDisplayName(title);
    }

    public void refreshSidebar() {
        scoreboard.getEntries().forEach(scoreboard::resetScores);
        String[] lines = handler.getPlugin().getPlayerRenderer().getLines().apply(user);
        int score = lines.length;
        for (String s : lines) {
            rerenderScore(score, s);
            score--;
        }
    }

    public Team getOrCreate(String name, String prefix, String suffix) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }
        if (prefix != null)
            team.setPrefix(prefix);
        if (suffix != null)
            team.setSuffix(suffix);
        return team;
    }

    private InternalScoreboard<T> setScore(int score, String value) {
        this.objective.getScore(value).setScore(score);
        this.currentSidebar.put(score, value);
        return this;
    }

    public InternalScoreboard<T> removeScore(int score) {
        String entry = this.currentSidebar.get(score);
        if (entry != null) {
            this.scoreboard.resetScores(entry);
            if(scoreboard.getTeam(entry) != null)
                scoreboard.getTeam(entry).unregister();
        }
        return this;
    }

    private void rerenderScore(int score, String line) {
        Team team = createSidebarTeam(line);
        setScore(score, team.getDisplayName());
    }

    private Team createSidebarTeam(String label) {
        Team team = scoreboard.registerNewTeam("Sidebar-" + sidebarTeamCounter++);
        if(label.length() < 17) {
            team.setDisplayName(label);
        } else if(label.length() < 33) {
            team.setPrefix(label.substring(0, 16));
            team.setDisplayName(label.substring(16));
        } else if (label.length() < 49) {
            team.setPrefix(label.substring(0, 16));
            team.setDisplayName(label.substring(16, 32));
            team.setSuffix(label.substring(32));
        } else {
            throw new IllegalArgumentException("The line must be shorter than 49 chars!");
        }
        team.addEntry(team.getDisplayName());
        return team;
    }

    Objective getObjective() {
        return objective;
    }

}
