package commands;

import database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ForceSave extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
	if ("?ForceSave".equals(event.getMessage().getContentDisplay())
		&& event.getMember().getIdLong() == 281713730046394369l) {
	    event.getChannel().sendMessage("Saved").queue();
	    Database.database().forceSave();
	}
    }
}