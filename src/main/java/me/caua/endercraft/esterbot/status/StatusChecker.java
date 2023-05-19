package me.caua.endercraft.esterbot.status;

import br.com.azalim.mcserverping.MCPing;
import br.com.azalim.mcserverping.MCPingOptions;
import br.com.azalim.mcserverping.MCPingResponse;
import br.com.azalim.mcserverping.examples.MCPingExample;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static me.caua.endercraft.esterbot.EsterBOT.dotenv;
import static me.caua.endercraft.esterbot.EsterBOT.prefix;

public class StatusChecker extends ListenerAdapter {

    private String cargoEquipeId = dotenv.get("CARGO_EQUIPE_ID");

    public StatusChecker() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(updateStatus, 1, 5, TimeUnit.MINUTES);
    }

    Runnable updateStatus = new Runnable () {
        @Override
        public void run () {
            TextChannel statusChannel = EsterBOT.jda.getGuildById(Objects.requireNonNull(dotenv.get("GUILD_ID"))).getTextChannelById(Objects.requireNonNull(dotenv.get("CHANNEL_STATUS_ID")));
            if (statusChannel == null) return;
            statusChannel.getHistoryFromBeginning(1).queue(history ->
            {
                if (!history.isEmpty())
                {
                    Message firstMessage = history.getRetrievedHistory().get(0);
                    if (firstMessage.getAuthor().getId().equals(EsterBOT.jda.getSelfUser().getId())) {
                        //statusChannel.editMessageEmbedsById(firstMessage.getId(), ).queue();
                        firstMessage.editMessageEmbeds(StatusEmbed.getEmbed().build()).queue();
                    } else {
                        System.out.println("Erro, a primeira mensagem do canal de status não é do bot ester.");
                    }
                }
                else
                    statusChannel.sendMessageEmbeds(StatusEmbed.getEmbed().build()).queue();
            });
        }
    };

    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (!e.isFromGuild()) return;
        if (Objects.requireNonNull(e.getMessage().getAuthor()).isBot()) return;
        if (!Objects.requireNonNull(e.getMember()).getRoles().contains(e.getGuild().getRoleById(cargoEquipeId))) return;

        String[] args = e.getMessage().getContentRaw().split(" ");

        if (args[0].equalsIgnoreCase(prefix + "setupstatus")) {
            e.getMessage().getChannel().sendMessageEmbeds(StatusEmbed.getEmbed().build()).queue();
        }

    }

    public static JsonObject ping(String address, Integer port) {
        JsonObject response = new JsonObject();
        MCPingOptions options;

        if (port != null) {
            options = MCPingOptions.builder()
                    .hostname(address)
                    .port(port)
                    .build();
        } else {
             options = MCPingOptions.builder()
                    .hostname(address)
                    .build();
        }

        MCPingResponse reply;
        response.addProperty("host", options.getHostname());
        response.addProperty("port", options.getPort());

        try {
            reply = MCPing.getPing(options);
        } catch (IOException ex) {
            System.out.println(options.getHostname() + " is down or unreachable.");
            response.addProperty("online", false);
            return response;
        }

        response.addProperty("online", true);

        MCPingResponse.Players players = reply.getPlayers();
        response.addProperty("players", players.getOnline());
        response.addProperty("maxplayers", players.getMax());

        response.addProperty("ping", reply.getPing());

        return response;
    }

    public static boolean pingBedrockServer(String serverAddress, int serverPort) {
        try {
            String apiUrl = "https://api.mcsrvstat.us/bedrock/2/" + serverAddress + ":" + serverPort;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = in.readLine();
                in.close();
                // Parse JSON response
                // Verifique se o servidor está online analisando o JSON de resposta
                boolean isOnline = response.contains("\"online\":true");

                return isOnline;
            }
        } catch (IOException e) {
            // Erro ao conectar ou ler a resposta do servidor
            System.out.println("Erro ao conectar ao mcsrvstat.us");
        }

        return false;
    }

    public static boolean pingWebsite(String websiteUrl) {
        try {
            URL url = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }


}
