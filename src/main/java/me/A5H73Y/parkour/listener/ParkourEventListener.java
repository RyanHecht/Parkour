package me.A5H73Y.parkour.listener;

import me.A5H73Y.parkour.course.CourseInfo;
import me.A5H73Y.parkour.event.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ParkourEventListener implements Listener {

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        dispatchCommand(CourseInfo.getOnDeathCommand(event.getCourseName()), event.getPlayer());
    }

    @EventHandler
    public void onPlayerAchieveCheckpointEvent(PlayerAchieveCheckpointEvent event) {
        dispatchCommand(CourseInfo.getOnAchieveCheckpointCommand(event.getCourseName(), event.getCheckpointIndex()), event.getPlayer());
    }

    @EventHandler
    public void onPlayerFinishCourseEvent(PlayerFinishCourseEvent event) {
        dispatchCommand(CourseInfo.getOnFinishCommand(event.getCourseName()), event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoinCourseEvent(PlayerJoinCourseEvent event) {
        dispatchCommand(CourseInfo.getOnJoinCommand(event.getCourseName()), event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeaveCourseEvent(PlayerLeaveCourseEvent event) {
        dispatchCommand(CourseInfo.getOnLeaveCommand(event.getCourseName()), event.getPlayer());
    }



    public void dispatchCommand(String command, Player player) {
        if (command != null) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%PLAYER%", player.getName()));
        }
    }
}
