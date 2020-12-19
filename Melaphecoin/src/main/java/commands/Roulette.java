package commands;

import static database.MessageDeletor.deleteMessage;
import static main.MainClass.coin;
import static main.MainClass.tagMember;

import java.util.Random;

import database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Roulette extends ListenerAdapter {

    private Database database = Database.database();

    private static final String ROULETTER_BASE = "_ _:black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::green_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square::red_square::black_large_square:";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
	// ?r amount colour
	String msg = event.getMessage().getContentRaw();

	if (!msg.toLowerCase().startsWith("?roulette") && !msg.toLowerCase().startsWith("?rt"))
	    return;
	
	deleteMessage(event.getChannel().getIdLong(), event.getMessage().getIdLong());

	long memberId = event.getMember().getIdLong();
	String[] parts = msg.split(" ");

	try {

	    if (parts.length > 2) {
		int bet = Integer.parseInt(parts[1]);
		if (database.subtract(event.getMember().getIdLong(), bet) == -1) {
		    event.getChannel().sendMessage(tagMember(memberId) + " Insufficent funds").queue();
		} else {
		    int prize = roulette(event, parts[2], bet);
		    if (prize == -1)
			event.getChannel().sendMessage(tagMember(memberId) + " lost: **" + bet + " " + coin + "**")
				.queue();
		    else if (prize == -2) {
			event.getChannel().sendMessage(tagMember(memberId) + " Please enter a valid color").queue();
			database.add(event.getMember().getIdLong(), bet); //undo the payment
		    }
		    else {
			event.getChannel().sendMessage(tagMember(memberId) + " won: **" + (prize - bet) + coin + "**")
				.queue();
			database.add(memberId, prize);
		    }
		}
	    }
	} catch (Exception e) {
	    event.getChannel().sendMessage("Error proccessing the command").queue();
	}

    }

    public int roulette(MessageReceivedEvent event, String color, int bet) {
	if (!"green".equalsIgnoreCase(color) && !"g".equalsIgnoreCase(color) && !"black".equalsIgnoreCase(color)
		&& !"b".equalsIgnoreCase(color) && !"red".equalsIgnoreCase(color) && !"r".equalsIgnoreCase(color))
	    return -2;

	Random rand = new Random();

	int colorPos = rand.nextInt(29);

	String arrow = "_ _";
	if (colorPos > 13)
	    arrow += "  ";

	for (int i = 0; i < colorPos; i++) {
	    arrow += "      ";
	}
	arrow += ":arrow_down_small:";

	event.getChannel().sendMessage(arrow + "\n" + ROULETTER_BASE).queue();

	if (colorPos == 14)
	    return ("green".equalsIgnoreCase(color) || "g".equalsIgnoreCase(color)) ? bet * 14 : -1;
	else if (colorPos % 2 == 0)
	    return ("black".equalsIgnoreCase(color) || "b".equalsIgnoreCase(color)) ? bet * 2 : -1;
	else
	    return ("red".equalsIgnoreCase(color) || "r".equalsIgnoreCase(color)) ? bet * 2 : -1;
    }

}
