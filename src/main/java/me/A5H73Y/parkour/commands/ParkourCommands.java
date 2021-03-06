package me.A5H73Y.parkour.commands;

import me.A5H73Y.parkour.Parkour;
import me.A5H73Y.parkour.conversation.CreateParkourKitConversation;
import me.A5H73Y.parkour.conversation.EditParkourKitConversation;
import me.A5H73Y.parkour.course.CheckpointMethods;
import me.A5H73Y.parkour.course.CourseInfo;
import me.A5H73Y.parkour.course.CourseMethods;
import me.A5H73Y.parkour.course.LobbyMethods;
import me.A5H73Y.parkour.gui.ParkourCoursesInventory;
import me.A5H73Y.parkour.kit.ParkourKitInfo;
import me.A5H73Y.parkour.manager.ChallengeManager;
import me.A5H73Y.parkour.manager.QuietModeManager;
import me.A5H73Y.parkour.other.Help;
import me.A5H73Y.parkour.player.PlayerInfo;
import me.A5H73Y.parkour.player.PlayerMethods;
import me.A5H73Y.parkour.utilities.DatabaseMethods;
import me.A5H73Y.parkour.utilities.Static;
import me.A5H73Y.parkour.utilities.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ParkourCommands implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command,
                             final String label, final String[] args) {
        if (!command.getName().equalsIgnoreCase("parkour")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("For console commands, please use: paconsole (command)");
            return false;
        }

        Player player = (Player) sender;

        if (Parkour.getSettings().isPermissionsForCommands()
                && !Utils.hasPermission(player, "Parkour.Basic", "Commands")) {
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(Static.getParkourString() + "Plugin proudly created by " + ChatColor.AQUA + "A5H73Y");
            player.sendMessage(Utils.getTranslation("Help.Commands", false));
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (!Utils.validateArgs(player, args, 2)) {
                return false;

            } else if (!Parkour.getPlugin().getConfig().getBoolean("OnJoin.AllowViaCommand")) {
                return false;
            }

            CourseMethods.joinCourse(player, args[1]);

        } else if (args[0].equalsIgnoreCase("joinall")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "JoinAll")) {
                return false;
            }

            player.openInventory(new ParkourCoursesInventory().buildInventory(player, 1));

        } else if (args[0].equalsIgnoreCase("create")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Create")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseMethods.createCourse(args[1], player);

        } else if (args[0].equalsIgnoreCase("leave")) {
            PlayerMethods.playerLeave(player);

        } else if (args[0].equalsIgnoreCase("info")) {
            PlayerMethods.displayPlayerInfo(args, player);

        } else if (args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("course")) {
            if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseInfo.displayCourseInfo(args[1], player);

        } else if (args[0].equalsIgnoreCase("lobby")) {
            LobbyMethods.joinLobby(args, player);

        } else if (args[0].equalsIgnoreCase("setlobby")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            LobbyMethods.createLobby(args, player);

        } else if (args[0].equalsIgnoreCase("setcreator")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setCreator(args, player);

        } else if (args[0].equalsIgnoreCase("setondeathcommand")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3, Integer.MAX_VALUE)) {
                return false;
            }

            CourseMethods.setOnDeathCommand(args, player);

        } else if (args[0].equalsIgnoreCase("setonachievecheckpointcommand")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 4, Integer.MAX_VALUE)) {
                return false;
            }

            CourseMethods.setOnAchieveCheckpointCommand(args, player);

        } else if (args[0].equalsIgnoreCase("setonfinishcommand")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3, Integer.MAX_VALUE)) {
                return false;
            }

            CourseMethods.setOnFinishCommand(args, player);

        } else if (args[0].equalsIgnoreCase("setonjoincommand")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3, Integer.MAX_VALUE)) {
                return false;
            }

            CourseMethods.setOnJoinCommand(args, player);

        } else if (args[0].equalsIgnoreCase("setonleavecommand")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3, Integer.MAX_VALUE)) {
                return false;
            }

            CourseMethods.setOnLeaveCommand(args, player);

        }   else if (args[0].equalsIgnoreCase("checkpoint")) {
            if (!PlayerInfo.hasSelected(player)) {
                return false;

            } else if (!Utils.hasPermissionOrCourseOwnership(
                    player, "Parkour.Admin", "Course", PlayerInfo.getSelected(player))) {
                return false;
            }

            CheckpointMethods.createCheckpoint(args, player);

        } else if (args[0].equalsIgnoreCase("finish")) {
            CourseMethods.setCompletionStatus(args, player);

        } else if (args[0].equalsIgnoreCase("setstart")) {
            if (!PlayerInfo.hasSelected(player)) {
                return false;

            } else if (!Utils.hasPermissionOrCourseOwnership(
                    player, "Parkour.Admin", "Course", PlayerInfo.getSelected(player))) {
                return false;
            }

            CourseMethods.setStart(player);

        } else if (args[0].equalsIgnoreCase("setautostart")) {
            if (!Utils.hasPermission(player, "Parkour.Admin", "Course")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseMethods.setAutoStart(args, player);

        } else if (args[0].equalsIgnoreCase("prize")) {
            if (!Utils.hasPermission(player, "Parkour.Admin", "Prize")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseMethods.setPrize(args[1], player);

        } else if (args[0].equalsIgnoreCase("like")
                || args[0].equalsIgnoreCase("dislike")) {
            CourseMethods.rateCourse(args, player);

        } else if (args[0].equalsIgnoreCase("perms")) {
            PlayerMethods.displayPermissions(player);

        } else if (args[0].equalsIgnoreCase("kit")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Kit")) {
                return false;
            }

            PlayerMethods.givePlayerKit(args, player);

        } else if (args[0].equalsIgnoreCase("delete")) {
            if (!Utils.hasPermission(player, "Parkour.Admin", "Delete")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            Utils.deleteCommand(args, player);

        } else if (args[0].equalsIgnoreCase("select")
                || args[0].equalsIgnoreCase("edit")) {
            if (!Utils.validateArgs(player, args, 2)) {
                return false;

            } else if (!Utils.hasPermissionOrCourseOwnership(
                    player, "Parkour.Admin", "Course", args[1])) {
                return false;
            }

            CourseMethods.selectCourse(args, player);

        } else if (args[0].equalsIgnoreCase("done")
                || args[0].equalsIgnoreCase("deselect")
                || args[0].equalsIgnoreCase("stopselect")) {
            CourseMethods.deselectCourse(player);

        } else if (args[0].equalsIgnoreCase("tp")
                || args[0].equalsIgnoreCase("teleport")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "TP")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CheckpointMethods.teleportCheckpoint(args, player, false);

        } else if (args[0].equalsIgnoreCase("tpc")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "TPC")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CheckpointMethods.teleportCheckpoint(args, player, true);

        } else if (args[0].equalsIgnoreCase("link")) {
            if (!Utils.validateArgs(player, args, 3, 4)) {
                return false;

            } else if (!PlayerInfo.hasSelected(player)) {
                return false;

            } else if (!Utils.hasPermissionOrCourseOwnership(
                    player, "Parkour.Admin", "Course", PlayerInfo.getSelected(player))) {
                return false;
            }

            CourseMethods.linkCourse(args, player);

        } else if (args[0].equalsIgnoreCase("setminlevel")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setMinLevel(args, player);

        } else if (args[0].equalsIgnoreCase("setmaxdeath")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setMaxDeaths(args, player);

        } else if (args[0].equalsIgnoreCase("setmaxtime")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setMaxTime(args, player);

        } else if (args[0].equalsIgnoreCase("setjoinitem")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 4)) {
                return false;
            }

            CourseMethods.setJoinItem(args, player);

        } else if (args[0].equalsIgnoreCase("rewardonce")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseMethods.setRewardOnce(args, player);

        } else if (args[0].equalsIgnoreCase("rewardlevel")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setRewardParkourLevel(args, player);

        } else if (args[0].equalsIgnoreCase("rewardleveladd")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setRewardParkourLevelAddition(args, player);

        } else if (args[0].equalsIgnoreCase("rewardrank")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setRewardParkourRank(args, player);

        } else if (args[0].equalsIgnoreCase("rewarddelay")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setRewardDelay(args, player);

        } else if (args[0].equalsIgnoreCase("rewardparkoins")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            CourseMethods.setRewardParkoins(args, player);

        } else if (args[0].equalsIgnoreCase("quiet")) {
            QuietModeManager.getInstance().toggleQuietMode(player);

        } else if (args[0].equalsIgnoreCase("reset")) {
            if (!Utils.hasPermission(player, "Parkour.Admin", "Reset")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            Utils.resetCommand(args, player);

        } else if (args[0].equalsIgnoreCase("test")) {
            if (!Utils.hasPermission(player, "Parkour.Admin", "Testmode")) {
                return false;
            }

            PlayerMethods.toggleTestmode(args, player);

        } else if (args[0].equalsIgnoreCase("economy")
                || args[0].equalsIgnoreCase("econ")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2, 4)) {
                return false;
            }

            Help.displayEconomy(args, player);

        } else if (args[0].equalsIgnoreCase("invite")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Invite")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            PlayerMethods.invitePlayer(args, player);

        } else if (args[0].equalsIgnoreCase("setmode")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            CourseMethods.setCourseMode(args, player);

        } else if (args[0].equalsIgnoreCase("createparkourkit")
                || args[0].equalsIgnoreCase("createkit")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            new CreateParkourKitConversation(player).begin();

        } else if (args[0].equalsIgnoreCase("editparkourkit")
                || args[0].equalsIgnoreCase("editkit")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            new EditParkourKitConversation(player).begin();

        } else if (args[0].equalsIgnoreCase("linkkit")) {
            if (!Utils.validateArgs(player, args, 3)) {
                return false;

            } else if (!Utils.hasPermissionOrCourseOwnership(
                    player, "Parkour.Admin", "Course", args[1])) {
                return false;
            }

            CourseMethods.linkParkourKit(args, player);

        } else if (args[0].equalsIgnoreCase("listkit")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Kit")) {
                return false;
            }

            Utils.listParkourKit(args, player);

        } else if (args[0].equalsIgnoreCase("validatekit")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            ParkourKitInfo.validateParkourKit(args, player);

        } else if (args[0].equalsIgnoreCase("challenge")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Challenge")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3, 4)) {
                return false;
            }

            CourseMethods.challengePlayer(args, player);

        } else if (args[0].equalsIgnoreCase("list")) {
            CourseMethods.displayList(args, player);

        } else if (args[0].equalsIgnoreCase("help")) {
            Help.lookupCommandHelp(args, player);

        } else if (args[0].equalsIgnoreCase("leaderboard")
                || args[0].equalsIgnoreCase("leaderboards")) {
            if (!Utils.hasPermission(player, "Parkour.Basic", "Leaderboard")) {
                return false;
            }

            CourseMethods.getLeaderboards(args, player);

        } else if (args[0].equalsIgnoreCase("sql")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            Help.displaySQL(player);

        } else if (args[0].equalsIgnoreCase("recreate")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            player.sendMessage(Static.getParkourString() + "Recreating courses...");
            DatabaseMethods.recreateAllCourses();

        } else if (args[0].equalsIgnoreCase("whitelist")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 2)) {
                return false;
            }

            Utils.addWhitelistedCommand(args, player);

        } else if (args[0].equalsIgnoreCase("setlevel")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            PlayerMethods.setLevel(args, player);

        } else if (args[0].equalsIgnoreCase("setrank")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;

            } else if (!Utils.validateArgs(player, args, 3)) {
                return false;
            }

            PlayerMethods.setRank(args, player);

        } else if (args[0].equalsIgnoreCase("material")) {
            Utils.lookupMaterialInformation(args, player);

            //Other commands//
        } else if (args[0].equalsIgnoreCase("about")
                || args[0].equalsIgnoreCase("ver")
                || args[0].equalsIgnoreCase("version")) {
            player.sendMessage(Static.getParkourString() + "Server is running Parkour " + ChatColor.GRAY + Static.getVersion());
            player.sendMessage("This plugin was developed by " + ChatColor.GOLD + "A5H73Y");

        } else if (args[0].equalsIgnoreCase("contact")) {
            player.sendMessage(Static.getParkourString() + "For information or help please contact me:");
            player.sendMessage("Bukkit: " + ChatColor.AQUA + "A5H73Y");
            player.sendMessage("Spigot: " + ChatColor.AQUA + "A5H73Y");
            player.sendMessage("Parkour URL: " + ChatColor.AQUA + "http://dev.bukkit.org/projects/parkour/");
            player.sendMessage("Discord Server: " + ChatColor.AQUA + "https://discord.gg/Gc8RGYr");

        } else if (args[0].equalsIgnoreCase("request")
                || args[0].equalsIgnoreCase("bug")) {
            player.sendMessage(Static.getParkourString() + "To Request a feature or to Report a bug...");
            player.sendMessage("Click here: " + ChatColor.DARK_AQUA + "https://github.com/A5H73Y/Parkour/issues");

        } else if (args[0].equalsIgnoreCase("tutorial")) {
            player.sendMessage(Static.getParkourString() + "To follow the official Parkour tutorials...");
            player.sendMessage("Click here: " + ChatColor.DARK_AQUA + "https://a5h73y.github.io/Parkour/");

        } else if (args[0].equalsIgnoreCase("settings")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            Help.displaySettings(player);

        } else if (args[0].equalsIgnoreCase("cmds")) {
            Help.processCommandsInput(args, player);

        } else if (args[0].equalsIgnoreCase("accept")) {
            ChallengeManager.getInstance().acceptChallenge(player);

        } else if (args[0].equalsIgnoreCase("yes")
                || args[0].equalsIgnoreCase("no")) {
            player.sendMessage(Utils.getTranslation("Error.NoQuestion"));

        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!Utils.hasPermission(player, "Parkour.Admin")) {
                return false;
            }

            Utils.reloadConfig();
            player.sendMessage(Utils.getTranslation("Other.Reload"));
            Utils.logToFile(player.getName() + " reloaded the Parkour config");

        } else {
            player.sendMessage(Utils.getTranslation("Error.UnknownCommand"));
            player.sendMessage(Utils.getTranslation("Help.Commands", false));
        }
        return true;
    }
}
