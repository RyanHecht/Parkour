package me.A5H73Y.parkour.event;

import me.A5H73Y.parkour.course.Checkpoint;
import org.bukkit.entity.Player;

public class PlayerAchieveCheckpointEvent extends ParkourEvent {

    private Checkpoint checkpoint;
    private int checkpointIndex=-1;

    public PlayerAchieveCheckpointEvent(final Player player, final String courseName, final Checkpoint checkpoint) {
        super(player, courseName);
        this.checkpoint = checkpoint;
    }

    public PlayerAchieveCheckpointEvent(final Player player, final String courseName, final Checkpoint checkpoint, int checkpointIndex) {
        super(player, courseName);
        this.checkpoint = checkpoint;
        this.checkpointIndex = checkpointIndex;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public int getCheckpointIndex() {
        return checkpointIndex;
    }

}
