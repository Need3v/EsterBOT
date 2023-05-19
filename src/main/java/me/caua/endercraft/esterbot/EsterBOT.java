package me.caua.endercraft.esterbot;

import io.github.cdimascio.dotenv.Dotenv;
import me.caua.endercraft.esterbot.commands.*;
import me.caua.endercraft.esterbot.listeners.ReadyEvent;
import me.caua.endercraft.esterbot.status.StatusChecker;
import me.caua.endercraft.esterbot.tickets.TicketCmds;
import me.caua.endercraft.esterbot.tickets.TicketEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class EsterBOT {

    public static String prefix = "!";
    public static JDA jda;
    public static Dotenv dotenv;

    public static void main(String[] args) throws LoginException {
        dotenv = Dotenv.load();
        jda = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"))
                .setEnabledIntents(EnumSet.allOf(GatewayIntent.class)).build();
        jda.addEventListener(new PingCmd());
        jda.addEventListener(new SayCmd());
        jda.addEventListener(new SayeCmd());
        // jda.addEventListener(new RevisaoCmd());
        jda.addEventListener(new ReadyEvent());
        jda.addEventListener(new PixCmd());
        jda.addEventListener(new IPCmd());
        jda.addEventListener(new TicketCmds());
        jda.addEventListener(new TicketEvents());
        jda.addEventListener(new StatusChecker());
    }

}
