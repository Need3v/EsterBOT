package me.caua.endercraft.esterbot.tickets;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

public class TicketTranscript {
    // working
    public void getTranscript(TextChannel ch) {
        List<Message> mensagens = ch.getHistory().getRetrievedHistory();
        StringBuilder sb = new StringBuilder();
        for (Message message: mensagens) {
            sb.append(
                    message.getTimeCreated().getDayOfMonth()
                    +"/"+message.getTimeCreated().getMonthValue()
                    +"/"+message.getTimeCreated().getYear()
                    + " - "
                    + message.getAuthor().getName()
                    + "(" +message.getAuthor().getId() + ")"
                    + ": " + message.getContentRaw()+"\n\n"
            );
        }
        System.out.println(sb);
    }

}
