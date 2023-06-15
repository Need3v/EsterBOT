package me.caua.endercraft.esterbot.status;

import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import me.caua.endercraft.esterbot.EsterBOT;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

import static me.caua.endercraft.esterbot.EsterBOT.dotenv;

public class StatusEmbed {



    public static EmbedBuilder getEmbed() {
        JsonObject geralData = StatusChecker.ping(dotenv.get("GERAL_IP"), Integer.valueOf(dotenv.get("GERAL_PORT")));
        JsonObject lobbyData = StatusChecker.ping(dotenv.get("LOBBY_IP"), Integer.valueOf(dotenv.get("LOBBY_PORT")));
        JsonObject survivalData = StatusChecker.ping(dotenv.get("SURVIVAL_IP"), Integer.valueOf(dotenv.get("SURVIVAL_PORT")));
        Boolean isBedrockOnline = StatusChecker.pingBedrockServer(dotenv.get("BEDROCK_ADDRESS").toString(), Integer.valueOf(dotenv.get("BEDROCK_PORT")));
        JsonObject ddosData = StatusChecker.ping(dotenv.get("DDOS_ADDRESS"), null);
        //Boolean isSiteOnline = StatusChecker.pingWebsite(dotenv.get("SITE_ADDRESS").toString());

        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(":satellite: **Central de Monitoramento**")
                .setDescription("Olá jogador! <:ZeroTwoGreeting:884744365711454238> \n" +
                        "Tendo problemas para se conectar? Confira abaixo o status do nosso servidor!\n" +
                        "\n" +
                        "<a:jumpingblock:876537116304236605> **Servidores Minecraft:**\n")
                .setColor(Color.GREEN)
                .setFooter("EnderCraft - Todos os direitos reservados.", "https://i.imgur.com/hAGHv6R.png")
                .setTimestamp(LocalDateTime.now().plusHours(3))
                .addField("",
                        "> <:minecraft_beacon:1104573758213193808>  **Proxy (Principal):**\n"+
                        "> **Status:** " + (Boolean.parseBoolean((geralData.get("online").toString())) ? "Online :green_heart:" : "Offline :broken_heart:") + "\n"+
                                "> **Jogadores:** " + (Boolean.parseBoolean((geralData.get("online").toString())) ? (geralData.get("players").toString()+"/"+geralData.get("maxplayers").toString()) : "Indisponível") + "\n",
                        true)
                .addField("",
                        "> <:minecraft_world:1104573698184314900> **Lobby:**\n"+
                        "> **Status:** " + (Boolean.parseBoolean((lobbyData.get("online").toString())) ? "Online :green_heart:" : "Offline :broken_heart:") +"\n"+
                                "> **Jogadores:** " + (Boolean.parseBoolean((lobbyData.get("online").toString())) ? (lobbyData.get("players").toString()+"/"+lobbyData.get("maxplayers").toString()) : "Indisponível"),
                        true)
                .addField("",
                        "> <:emerald:1105917900407197708> **Survival:**\n"+
                        "> **Status:** " + (Boolean.parseBoolean((survivalData.get("online").toString())) ? "Online :green_heart:" : "Offline :broken_heart:") +"\n"+
                                "> **Jogadores:** " + (Boolean.parseBoolean((survivalData.get("online").toString())) ? (survivalData.get("players").toString()+"/"+survivalData.get("maxplayers").toString()) : "Indisponível")
                        ,true)
                .addField("",
                        "<:minecraft_slime:876536490132389938> **Outros:**\n"+
                                //"> **Site:** " + ((isSiteOnline) ? "Online :green_heart:" : "Offline :broken_heart:") +"\n" +
                                "**Bedrock:** " + ((isBedrockOnline) ? "Online :green_heart:" : "Offline :broken_heart:") +"\n" +
                                "**Proteção DDoS:** " + ((Boolean.parseBoolean((ddosData.get("online").toString()))) ? "Online :green_heart:" : "Offline :broken_heart:") +
                        "\n\n" +
                                ":notepad_spiral: **Legenda:**\n" +
                                ":green_heart: Disponível.\n" +
                                ":broken_heart: Instável.\n" +
                                "\n" +
                                "Monitoramento da nossa máquina: [CLIQUE AQUI](https://status.endercraft.com.br)\n\n"+
                                "<:reload:1105916107354812456>  *Atualizado a cada 5 minutos!*\n\n",
                        false);
                //.setImage("https://api.loohpjames.com/serverbanner.png?ip=endercraft.com.br&width=1224?");

        if (
                Boolean.parseBoolean((geralData.get("online").toString()))
                && Boolean.parseBoolean((lobbyData.get("online").toString()))
                && Boolean.parseBoolean((survivalData.get("online").toString()))
                && Boolean.parseBoolean((ddosData.get("online").toString()))
                && isBedrockOnline

        ) {
            eb.setColor(Color.GREEN);
        } else {
            eb.setColor(Color.orange);
        }
        return eb;
    }

}
