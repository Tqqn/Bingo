package dev.tqqn.utils;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.team.GameTeam;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftChatMessage;
import org.bukkit.entity.Player;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 04/04/2026
 */
public class NMSUtils {

    public static void refreshTag(Player receiver) {
        final PlayerModel joiningPlayer = PlayerModel.from(receiver);
        final GameTeam joiningTeam = joiningPlayer.getTempPlayerData().getTeam();

        System.out.println("Refreshing tag for " + receiver.getName() + " with team " + joiningTeam.getData().teamType().getName() + " and prefix " + joiningTeam.getData().teamType().name());

        for (Player player : Bukkit.getOnlinePlayers()) {
            final PlayerModel playerModel = PlayerModel.from(player);
            final GameTeam gameTeam = playerModel.getTempPlayerData().getTeam();
            if (gameTeam == null) continue;

            setTeamNameTag(receiver, player, gameTeam.getData().teamType().getName(), gameTeam.getData().teamType().name(), "", "");
            System.out.println("1 Player " + player.getName() + " is now on team " + gameTeam.getData().teamType().getName() + "with prefix " + gameTeam.getData().teamType().name());
            if (player.equals(receiver)) continue;
            System.out.println("Refreshing tag for " + playerModel.getName() + " with team " + gameTeam.getData().teamType().getName() + " and prefix " + gameTeam.getData().teamType().name());
            setTeamNameTag(player, receiver, joiningTeam.getData().teamType().getName(), joiningTeam.getData().teamType().name(), "", "");
            System.out.println("2 Player " + receiver.getName() + " is now on team " + joiningTeam.getData().teamType().getName() + "with prefix " + joiningTeam.getData().teamType().name());
        }
    }

    private static void setTeamNameTag(Player receiver, Player subject, String teamName, String color, String prefix, String suffix) {

        Scoreboard scoreboard = ((CraftPlayer) receiver).getScoreboard().getHandle(); // Use existing scoreboard
        PlayerTeam playerTeam = scoreboard.getPlayerTeam(teamName);
        boolean created = false;

        if (playerTeam == null) {
            playerTeam = new PlayerTeam(scoreboard, teamName);
            created = true;
        }

        playerTeam.setColor(ChatFormatting.getByName(color)); // Set name color

        if (prefix != null) playerTeam.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix));
        playerTeam.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));

        if (created) scoreboard.addPlayerTeam(teamName); // Ensure the team is added to the main scoreboard

        // Create packets
        ClientboundSetPlayerTeamPacket createOrModifyTeam = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, !created);
        ClientboundSetPlayerTeamPacket addPlayerToTeam = ClientboundSetPlayerTeamPacket.createPlayerPacket(playerTeam, subject.getName(), ClientboundSetPlayerTeamPacket.Action.ADD);

        // Send packets in correct order
        sendPacket(receiver, createOrModifyTeam);
        sendPacket(receiver, addPlayerToTeam);

    }

    private static void sendPacket(Player player, Object packetObject) {
        if (player == null) return;

        Packet<?> packet = (Packet<?>) packetObject;
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

}
